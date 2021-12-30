package com.example.module_scanidbankcard.helper

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.example.module_scanidbankcard.util.FileCacheUtils
import com.example.module_scanidbankcard.util.HxCallBack
import com.example.module_scanidbankcard.util.ImageUtils
import exocr.engine.EngineManager
import exocr.exocrengine.EXIDCardResult
import exocr.idcard.IDCardManager
import java.io.File

class ExCardHelper: IDCardManager.IDCallBack {
    companion object {
        private val ID_CARD_FRONT = 1
        private val ID_CARD_BACK = 2
    }
    /**
     * 1表示正面
     * 2表示反面
     */
    private var selectType = 0
    private var idCardFrontUri: Uri? = null // 界面显示的身份证图片，正面
    private var idCardBackUri: Uri? = null // 界面显示的身份证图片，反面
    private var frontImgDate: String? = null
    private var backImgDate: String? = null

    var exCardCallback: ExCardCallback? = null

    /**
     * 初始化
     */
    fun init(activity: Activity){
        EngineManager.getInstance().initEngine(activity)
    }

    /**
     * 打开身份证扫描页面
     *
     * @param isFront 是否是身份证正面
     */
    fun pickIdPicture(isFront: Boolean, exCardCallback: ExCardCallback) {
        selectType = if (isFront) {
            1
        } else {
            2
        }

        this.exCardCallback = exCardCallback

        //初始化SDK
        EngineManager.getInstance().initEngine(getActivity())
        IDCardManager.getInstance().isDebug(true)
        IDCardManager.getInstance().setView(null)


        IDCardManager.getInstance().setShowLogo(false)
        IDCardManager.getInstance().setShowPhoto(true)
//        IDCardManager.getInstance().setFront(isFront())
//        IDCardManager.getInstance().setPackageName("com.example.module_scanidbankcard")
        IDCardManager.getInstance().setTipFrontRightText("请将身份证放在屏幕中央，人像面朝上")
        IDCardManager.getInstance().setTipFrontErrorText("请翻到人像面")
        IDCardManager.getInstance().setTipBackRightText("请将身份证放在屏幕中央，国徽面朝上")
        IDCardManager.getInstance().setTipBackErrorText("请翻到国徽面")
        //        IDCardManager.getInstance().recognize(this, this)

        IDCardManager.getInstance().setScanMode(IDCardManager.ID_IMAGEMODE_HIGH, 10)
        IDCardManager.getInstance().setAutoFlash(true)
        IDCardManager.getInstance().recognizeWithSide(this, getActivity(), isFront())

    }
    
    fun onDestroy(){
        EngineManager.getInstance().finishEngine()
    }
    
    private fun getActivity():Activity {
        return EngineManager.getInstance().activity
    }
    
    
    private fun isFront(): Boolean {
        if (selectType == ID_CARD_FRONT) return true
        return if (selectType == ID_CARD_BACK) false else false
    }
    
    override fun onRecSuccess(p0: Int, result: EXIDCardResult?) {
        if (result == null) {
            Toast.makeText(getActivity(), "无法识别", Toast.LENGTH_SHORT).show()
            return
        }
        if (isFront() && TextUtils.isEmpty(result.name) && !TextUtils.isEmpty(result.validdate)) {
            Toast.makeText(getActivity(), "请翻到人像面", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isFront() && TextUtils.isEmpty(result.validdate) && !TextUtils.isEmpty(result.name)) {
            Toast.makeText(getActivity(), "请翻到国徽面", Toast.LENGTH_SHORT).show()
            return
        }
        // 四周有遮挡
        if (IDCardManager.getInstance().scanMode == IDCardManager.ID_IMAGEMODE_HIGH) {
            if (result.isComplete == 1) {
                Toast.makeText(getActivity(), "检测到身份证正面图像四周有遮挡", Toast.LENGTH_SHORT).show()
            }
        }

        // 结果回调
        exCardCallback?.scanResult(isFront(), result)
        // Bitmap -> 本地图片 uri
        val tmpUri = Uri.fromFile(
            File(
                FileCacheUtils
                    .getCacheDirectory(getActivity()),
                "tmp_wish_idcard" + System.currentTimeMillis() + ".jpg"
            )
        )
        ImageUtils.saveBitmap(
            getActivity(),
            result.stdCardIm,
            tmpUri,
            onScannerResultCallback
        )

        if(isFront()){
            idCardFrontUri = tmpUri
        } else {
            idCardBackUri = tmpUri
        }
    }

    private val onScannerResultCallback: HxCallBack<Boolean?> =
        HxCallBack<Boolean?> { param ->
            if (param != null && param) {
                doScannerComplete()
            }
        }

    /**
     * 压缩图片后，界面显示
     */
    private fun doScannerComplete() {
        //本地图片 uri -> 缩略图
        ImageUtils.compressImage(
            getActivity(),
            if (isFront()) idCardFrontUri else idCardBackUri,
            480,
            720,
            100
        ) { param ->
            if (isFront()) {
                frontImgDate = ImageUtils.bitmapEncodeBase64(param)
            } else {
                backImgDate = ImageUtils.bitmapEncodeBase64(param)
            }
            if (frontImgDate != null && backImgDate != null) {
                // 假装通知更新
            }
        }
        // 结果回调
        val curUri = if (isFront()) {
            idCardFrontUri
        } else {
            idCardBackUri
        }
        exCardCallback?.idCardUri(isFront(), curUri)
//        if (isFront()) {
//            Glide.with(getActivity())
//                .load(idCardFrontUri)
//                .crossFade()
//                .into(idcard_front_iv)
//        } else {
//            Glide.with(getActivity())
//                .load(idCardBackUri)
//                .crossFade()
//                .into(idcard_back_iv)
//        }
//        showIdCardInfoView()
    }

    override fun onRecCanceled(p0: Int) {
//        Toast.makeText(getActivity(), "取消识别", Toast.LENGTH_SHORT).show()
    }

    override fun onRecFailed(p0: Int, p1: Bitmap?) {
        if (p0 == IDCardManager.IDCallBack.CARD_CODE_FAIL) {
            Toast.makeText(getActivity(), "识别失败", Toast.LENGTH_SHORT).show()
        } else if (p0 == IDCardManager.IDCallBack.CARD_CODE_TIMEOUT) {
            Toast.makeText(getActivity(), "识别超时", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCameraDenied() {
        Toast.makeText(getActivity(), "没有摄像头权限", Toast.LENGTH_SHORT).show()
    }
}

interface ExCardCallback {
    fun scanResult(isFront: Boolean, result: EXIDCardResult)
    fun idCardUri(isFront: Boolean, idCardUri: Uri?)
}
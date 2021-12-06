package com.example.module_scanidbankcard

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.libfundenter.videorecord.rxRequestPermissions
import com.example.module_scanidbankcard.util.FileCacheUtils
import com.example.module_scanidbankcard.util.HxCallBack
import com.example.module_scanidbankcard.util.ImageUtils
import exocr.engine.DataCallBack
import exocr.engine.EngineManager
import exocr.exocrengine.EXIDCardResult
import exocr.idcard.IDCardManager
import kotlinx.android.synthetic.main.activity_old_scan.*
import java.io.File

class IdCardScanActivity:AppCompatActivity(), DataCallBack {
    private val ID_CARD_FRONT = 1
    private val ID_CARD_BACK = 2

    /**
     * 1表示正面
     * 2表示反面
     */
    private var selectType = 0
    private var exidCardFrontResult: EXIDCardResult? = null
    private var exidCardBackResult: EXIDCardResult? = null
    private var frontImgDate: String? = null
    private var backImgDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_scan)
        EngineManager.getInstance().initEngine(this)
        initView()
    }

    private fun initView() {
        idcard_front_retry_tv.setOnClickListener {
            pickIdPicture(true)
        }
        idcard_back_retry_tv.setOnClickListener {
            pickIdPicture(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EngineManager.getInstance().finishEngine()
    }

    /**
     * 打开身份证扫描页面
     *
     * @param isFront 是否是身份证正面
     */
    private fun pickIdPicture(isFront: Boolean) {
        selectType = if (isFront) {
            1
        } else {
            2
        }

        rxRequestPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, describe = "相机、存储、录音") {
            startScannerActivityIndeed()
        }

    }

    private fun startScannerActivityIndeed() {
        IDCardManager.getInstance().setShowLogo(false)
        IDCardManager.getInstance().setShowPhoto(true)
        IDCardManager.getInstance().setFront(isFront())
        IDCardManager.getInstance().setPackageName("com.example.module_scanidbankcard")
        IDCardManager.getInstance().setTipFrontRightText("请将身份证放在屏幕中央，人像面朝上")
        IDCardManager.getInstance().setTipFrontErrorText("请翻到人像面")
        IDCardManager.getInstance().setTipBackRightText("请将身份证放在屏幕中央，国徽面朝上")
        IDCardManager.getInstance().setTipBackErrorText("请翻到国徽面")
        IDCardManager.getInstance().recognize(this, this)
    }

    private fun isFront(): Boolean {
        if (selectType == ID_CARD_FRONT) return true
        return if (selectType == ID_CARD_BACK) false else false
    }

    // =================================== DataCallBack
    private var idCardFrontUri: Uri? = null // 界面显示的身份证图片，正面
    private var idCardBackUri: Uri? = null //// 界面显示的身份证图片，反面

    private val onScannerResultCallback: HxCallBack<Boolean?> =
        HxCallBack<Boolean?> { param ->
            if (param != null && param) {
                doScannerComplete()
            }
        }

    override fun onCardDetected(isSuccess: Boolean) {
        val result = IDCardManager.getInstance().result
        if (result == null) {
            Toast.makeText(this, "无法识别", Toast.LENGTH_SHORT).show()
            return
        }
        if (isFront()) {
            exidCardFrontResult = result
            if (!TextUtils.isEmpty(result.photoUri)) {
                idCardFrontUri = Uri.parse(result.photoUri) 
            }else {
                idCardFrontUri = Uri.fromFile(
                    File(
                        FileCacheUtils
                            .getCacheDirectory(this),
                        "tmp_wish_idcard" + System.currentTimeMillis() + ".jpg"
                    )
                )
                ImageUtils.saveBitmap(
                    this,
                    result.stdCardIm,
                    idCardFrontUri,
                    onScannerResultCallback
                )
            }
        } else {
            exidCardBackResult = result
            if (!TextUtils.isEmpty(result.photoUri)) {
                idCardBackUri =
                    Uri.parse(result.photoUri)
            } else {
                idCardBackUri = Uri.fromFile(
                    File(
                        FileCacheUtils
                            .getCacheDirectory(this),
                        "tmp_wish_idcard" + System.currentTimeMillis() + ".jpg"
                    )
                )
                ImageUtils.saveBitmap(
                    this,
                    result.stdCardIm,
                    idCardBackUri,
                    onScannerResultCallback
                )
            }
        }

        if (this.isFinishing) return
        doScannerComplete()
        // toast 提示结果
        if (isSuccess) {
            if (isFront() && TextUtils.isEmpty(result.name) && !TextUtils.isEmpty(result.validdate)) {
                Toast.makeText(this, "请翻到人像面", Toast.LENGTH_SHORT).show()
                return
            }
            if (!isFront() && TextUtils.isEmpty(result.validdate) && !TextUtils.isEmpty(result.name)) {
                Toast.makeText(this, "请翻到国徽面", Toast.LENGTH_SHORT).show()
                return
            }
            // 四周有遮挡
            if (IDCardManager.getInstance().scanMode == IDCardManager.ID_IMAGEMODE_HIGH) {
                if (result.isComplete == 1) {
                    Toast.makeText(this, "检测到身份证正面图像四周有遮挡", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "无法识别", Toast.LENGTH_SHORT).show()
        }
        
    }

    /**
     * 压缩图片，界面显示
     */
    private fun doScannerComplete() {
        ImageUtils.compressImage(
            this,
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
        if (this.isFinishing) {
            return
        }
        if (isFront()) {
            Glide.with(this)
                .load(idCardFrontUri)
                .crossFade()
                .into(idcard_front_iv)
        } else {
            Glide.with(this)
                .load(idCardBackUri)
                .crossFade()
                .into(idcard_back_iv)
        }
        showIdCardInfoView()
    }

    override fun onCameraDenied() {
        Toast.makeText(this, "没有摄像头权限", Toast.LENGTH_SHORT).show()
    }

    /**
     * 界面的条件判断
     */
    private fun showIdCardInfoView() {
//        if (exidCardBackResult == null && exidCardFrontResult == null) {
//            upLoadTipsLl.setVisibility(View.VISIBLE)
//            frontRl.setVisibility(View.GONE)
//            idCardFrontIv.setVisibility(View.GONE)
//            backRl.setVisibility(View.GONE)
//            idCardBackIv.setVisibility(View.GONE)
//            resultTipsTv.setVisibility(View.GONE)
//        } else if (exidCardBackResult != null && exidCardFrontResult == null) {
//            upLoadTipsLl.setVisibility(View.GONE)
//            frontRl.setVisibility(View.GONE)
//            idCardFrontIv.setVisibility(View.GONE)
//            backRl.setVisibility(View.VISIBLE)
//            idCardBackIv.setVisibility(View.VISIBLE)
//            backRetryTv.setText("重拍国徽面")
//            resultTipsTv.setVisibility(View.VISIBLE)
//            setBackCardInfo()
//        } else if (exidCardBackResult == null && exidCardFrontResult != null) {
//            upLoadTipsLl.setVisibility(View.GONE)
//            frontRl.setVisibility(View.VISIBLE)
//            idCardFrontIv.setVisibility(View.VISIBLE)
//            frontRetryTv.setText("重拍人像面")
//            setFrontCardInfo()
//            backRl.setVisibility(View.GONE)
//            idCardBackIv.setVisibility(View.GONE)
//            resultTipsTv.setVisibility(View.VISIBLE)
//        } else if (exidCardBackResult != null && exidCardFrontResult != null) {
//            upLoadTipsLl.setVisibility(View.GONE)
//            frontRl.setVisibility(View.VISIBLE)
//            idCardFrontIv.setVisibility(View.VISIBLE)
//            frontRetryTv.setText("重拍人像面")
//            setFrontCardInfo()
//            backRl.setVisibility(View.VISIBLE)
//            idCardBackIv.setVisibility(View.VISIBLE)
//            backRetryTv.setText("重拍国徽面")
//            resultTipsTv.setVisibility(View.VISIBLE)
//            setBackCardInfo()
//        }
    }
}
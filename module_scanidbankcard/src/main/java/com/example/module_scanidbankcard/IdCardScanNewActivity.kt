package com.example.module_scanidbankcard

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libfundenter.videorecord.rxRequestPermissions
import exocr.carddom.DataCallBack
import exocr.carddom.DomCardManager
import exocr.carddom.ExStatus
import exocr.dom.CardInfo
import exocr.domEngine.EngineManager
import exocr.domEngine.EngineManager.cardType
import exocr.domUtils.LogUtils
import kotlinx.android.synthetic.main.activity_old_scan.idcard_back_iv
import kotlinx.android.synthetic.main.activity_old_scan.idcard_back_retry_tv
import kotlinx.android.synthetic.main.activity_old_scan.idcard_front_iv
import kotlinx.android.synthetic.main.activity_old_scan.idcard_front_retry_tv
import kotlinx.android.synthetic.main.activity_scan_new.*
import java.io.File
import java.io.FileOutputStream

class IdCardScanNewActivity : AppCompatActivity(), DataCallBack {
    private val ID_CARD_FRONT = 1
    private val ID_CARD_BACK = 2

    /**
     * 1表示正面
     * 2表示反面
     */
    private var selectType = 0
    private var frontImgDate: String? = null
    private var backImgDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_new)
//        EngineManager.getInstance().initEngine(this)
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

    override fun onResume() {
        super.onResume()
        val licPath = getExternalFilesDir("data")!!.absolutePath + File.separator + "dom-test.lic"
        val b: Boolean = copyAssetsFiles(this, "dom-test.lic", licPath)
        EngineManager.getInstance().applyForAuth(licPath, this)

        val sdkversion = """
            SDK版本号:${EngineManager.getInstance().sdkVersion}
            算法版本号:${EngineManager.getInstance().kernelVersion}
            算法版本类型:${EngineManager.getInstance().versionType}
            算法到期时间:${EngineManager.getInstance().validDate}
            """.trimIndent()
        txt_version.text = sdkversion
    }

    /**
     * 复制assets目录下所有文件及文件夹到指定路径
     *
     * @param context     context 上下文
     * @param mAssetsPath mAssetsPath Assets目录的相对路径
     * @param mSavePath   mSavePath 复制文件的保存路径
     * @return boolean
     */
    private fun copyAssetsFiles(context: Context, mAssetsPath: String, mSavePath: String): Boolean {
        val file = File(mSavePath)
        if (file.exists()) {
            return true
        }
        try {
            val b = file.parentFile.mkdirs()
            if (b) LogUtils.e("mkdir failed")
            val `is` = context.resources.assets.open(mAssetsPath)
            val fos = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var byteCount: Int
            // 循环从输入流读取字节
            while (`is`.read(buffer).also { byteCount = it } != -1) {
                // 将读取的输入流写入到输出流
                fos.write(buffer, 0, byteCount)
            }
            // 刷新缓冲区
            fos.flush()
            fos.close()
            `is`.close()
        } catch (e: Throwable) {
            LogUtils.e("Copy File Failed!")
            return false
        }
        return true
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
            ID_CARD_FRONT
        } else {
            ID_CARD_BACK
        }

        rxRequestPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            describe = "相机、存储、录音"
        ) {
            startScannerActivityIndeed()
        }

    }

    private fun startScannerActivityIndeed() {
//        IDCardManager.getInstance().setShowLogo(false)
//        IDCardManager.getInstance().setShowPhoto(true)
//        IDCardManager.getInstance().setFront(isFront())
//        IDCardManager.getInstance().setPackageName("com.example.module_scanidbankcard")
//        IDCardManager.getInstance().setTipFrontRightText("请将身份证放在屏幕中央，人像面朝上")
//        IDCardManager.getInstance().setTipFrontErrorText("请翻到人像面")
//        IDCardManager.getInstance().setTipBackRightText("请将身份证放在屏幕中央，国徽面朝上")
//        IDCardManager.getInstance().setTipBackErrorText("请翻到国徽面")
//        IDCardManager.getInstance().recognize(this, this)


        //设置严格切边，留白距离10
        DomCardManager.getInstance().setShowLogo(true)
        DomCardManager.getInstance().setAutoFlash(true)
        DomCardManager.getInstance().recognize(this, this, arrayOf<cardType>(getCardType()))

    }

    fun getCardType(): cardType {
        val myCardType = "2"
        val cardType: cardType
        cardType = when (myCardType) {
            "1" -> EngineManager.cardType.EXOCRCardTypeBANKCARD // 银行卡
            "2" -> EngineManager.cardType.EXOCRCardTypeIDCARD //身份证
            "3" -> EngineManager.cardType.EXOCRCardTypeGAT_RES_PERMIT
            "4" -> EngineManager.cardType.EXOCRCardTypeBUSINESS_LICENSE
            "5" -> EngineManager.cardType.EXOCRCardTypeHK_IDCARD
            "6" -> EngineManager.cardType.EXOCRCardTypeMO_IDCARD
            "7" -> EngineManager.cardType.EXOCRCardTypeGATJMLWNDTXZ
            "8" -> EngineManager.cardType.EXOCRCardTypeIDCARD_FOREIGN
            "9" -> EngineManager.cardType.EXOCRCardTypeTMP_IDCARD
            "10" -> EngineManager.cardType.EXOCRCardTypePASSPORT
            else -> throw IllegalStateException("Unexpected value: " + intent.getStringExtra("cardType"))
        }
        return cardType
    }


    private fun isFront(): Boolean {
        if (selectType == ID_CARD_FRONT) return true
        return if (selectType == ID_CARD_BACK) false else false
    }


    // =========================================================================================
    // =================================== DataCallBack callback
    // =========================================================================================

    /**
     * @param rawDatas :证件识别信息类
     */
    override fun onRecSuccess(exStatus: ExStatus?, rawDatas: MutableList<CardInfo>?) {
        if (exStatus == null || rawDatas == null) {
            return
        }
        val result: CardInfo = rawDatas.get(0)
        // pageType: 卡证正反面，1：国徽面；2：人像面。
        if (result.pageType == 1 && selectType == ID_CARD_FRONT) {
            Toast.makeText(this, "请扫描人像面", Toast.LENGTH_SHORT).show()
            return
        } else if (result.pageType != 1 && selectType == ID_CARD_BACK){
            Toast.makeText(this, "请扫描国徽面", Toast.LENGTH_SHORT).show()
            return
        }
        getImageView().setImageBitmap(result.cardImg)
    }

    private fun getImageView(): ImageView {
        if (selectType == ID_CARD_FRONT) return idcard_front_iv
        else return idcard_back_iv
    }

    override fun onRecParticularSuccess(p0: ExStatus?, p1: MutableList<Parcelable>?) {
    }

    override fun onRecCanceled(p0: ExStatus?) {
    }

    override fun onRecFailed(exStatus: ExStatus?, bitmap: Bitmap?) {
        if (bitmap != null) {
            getImageView().setImageBitmap(bitmap)
            Toast.makeText(this, "onRecFailed", Toast.LENGTH_SHORT).show()
        }
    }
}
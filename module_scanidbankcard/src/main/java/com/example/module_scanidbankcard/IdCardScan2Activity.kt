package com.example.module_scanidbankcard

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.libfundenter.videorecord.rxRequestPermissions
import com.example.module_scanidbankcard.helper.ExCardCallback
import com.example.module_scanidbankcard.helper.ExCardHelper
import com.example.module_scanidbankcard.util.FileCacheUtils
import com.example.module_scanidbankcard.util.HxCallBack
import com.example.module_scanidbankcard.util.ImageUtils
import exocr.exocrengine.EXIDCardResult
import exocr.idcard.IDCardManager
import kotlinx.android.synthetic.main.activity_old_scan.*
import java.io.File

class IdCardScan2Activity : AppCompatActivity() {
    private lateinit var exCardHelper: ExCardHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_scan)

        exCardHelper = ExCardHelper()
        exCardHelper.init(this)
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
        exCardHelper.onDestroy()
    }

    /**
     * 打开身份证扫描页面
     *
     * @param isFront 是否是身份证正面
     */
    private fun pickIdPicture(isFront: Boolean) {
        rxRequestPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            describe = "相机、存储、录音"
        ) {
            startScannerActivityIndeed(isFront)
        }
    }

    private fun startScannerActivityIndeed(isFront: Boolean) {
        exCardHelper.pickIdPicture(isFront, object : ExCardCallback {

            override fun scanResult(isFront: Boolean, result: EXIDCardResult) {
                if (isFront) {
                    // 正面
//            name_edt_id.setText(result.name);
//            sex_edt_id.setText(result.sex);
//            nation_edt_id.setText(result.nation);
//            birth_edt_id.setText(result.birth);
//            address_edt_id.setText(result.address);
//            num_edt_id.setText(result.cardnum);
                } else {
                    //反面
//            sign_edt_id.setText(result.office);
//            data_edt_id.setText(result.validdate);
                }
            }


            override fun idCardUri(isFront: Boolean, idCardUri: Uri?) {

                if (isFront) {
                    Glide.with(this@IdCardScan2Activity)
                        .load(idCardUri)
                        .crossFade()
                        .into(idcard_front_iv)
                } else {
                    Glide.with(this@IdCardScan2Activity)
                        .load(idCardUri)
                        .crossFade()
                        .into(idcard_back_iv)
                }
            }

        })
    }
}
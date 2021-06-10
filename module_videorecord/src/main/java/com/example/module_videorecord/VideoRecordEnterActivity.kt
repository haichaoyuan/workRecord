package com.example.module_videorecord

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.libfundenter.videorecord.rxRequestPermissions
import com.yxztb.liblivedetection.ui.videorecord.VideoRecordFragment
import kotlinx.android.synthetic.main.activity_video_record_enter.*

//@RuntimePermissions
class VideoRecordEnterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_record_enter)
        btn_video_record.setOnClickListener {
            requestCameraPerm()
        }

    }

    private fun requestCameraPerm() {
        rxRequestPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, describe = "相机、存储、录音") {
            startActivityForResult(Intent(this, VideoRecordActivity::class.java), 2)
        }
    }
}
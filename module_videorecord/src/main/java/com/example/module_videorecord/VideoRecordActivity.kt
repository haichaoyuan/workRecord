package com.example.module_videorecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yxztb.liblivedetection.ui.videorecord.VideoRecordFragment

class VideoRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_record)


        supportFragmentManager.beginTransaction()
            .replace(R.id.container_ll, VideoRecordFragment())
            .commitAllowingStateLoss()
    }
}
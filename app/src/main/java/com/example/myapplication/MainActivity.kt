package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.module_a.StickHeaderListViewActivity
import com.example.module_appbarlayout.AppbarLayoutEnterActivity
import com.example.module_autotextview.AutoTextViewActivity
import com.example.module_stickheaderscrollview.StickHeadScrollViewActivity
import com.example.module_videorecord.VideoRecordEnterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_appbarlayout.setOnClickListener {
            startActivity(Intent(this@MainActivity, AppbarLayoutEnterActivity::class.java))
        }
        btn_sticklistview.setOnClickListener {
            startActivity(Intent(this@MainActivity, StickHeaderListViewActivity::class.java))
        }
        btn_stickheaderscrollview.setOnClickListener {
            startActivity(Intent(this@MainActivity, StickHeadScrollViewActivity::class.java))
        }
        btn_video_record.setOnClickListener {
            startActivity(Intent(this@MainActivity, VideoRecordEnterActivity::class.java))
        }
        btn_auto_textview.setOnClickListener {
            startActivity(Intent(this@MainActivity, AutoTextViewActivity::class.java))
        }
    }
}
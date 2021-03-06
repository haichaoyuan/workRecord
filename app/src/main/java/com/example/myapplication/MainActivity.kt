package com.example.myapplication

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.module_a.StickHeaderListViewActivity
import com.example.module_appbarlayout.AppbarLayoutEnterActivity
import com.example.module_autotextview.AutoTextViewActivity
import com.example.module_homepage2.HomePage2Activity
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.viewmodel.AppMainEnterViewModel
import com.example.module_stickheaderscrollview.StickHeadScrollViewActivity
import com.example.module_videorecord.VideoRecordEnterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var menuAdapter: HomeMenu2Adapter
    private val model: AppMainEnterViewModel by viewModels()

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

        btn_test_weixin.setOnClickListener {
            toWeChatScan(this)
        }
        btn_test_weixin2.setOnClickListener {
            toWeChatScan(this)
        }
        //??????2.0
        btn_homepage2.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomePage2Activity::class.java))
        }
        //edittext
        btn_test_edittext.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomePage2Activity::class.java))
        }
    }

    companion object {
        fun toWeChatScan(context: Context) {
            val uri = Uri.parse("weixin://")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent);
        }

        fun toWeChat2(context: Context) {
            try {
                val intent = Intent(Intent.ACTION_MAIN)
                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                context.startActivity(intent);
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "??????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
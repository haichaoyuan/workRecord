package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_a.ModuleAMainActivity
import com.example.module_a.StickHeaderListViewActivity
import com.example.module_appbarlayout.MainAppBarLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_appbarlayout.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainAppBarLayoutActivity::class.java))
        }
        btn_sticklistview.setOnClickListener {
            startActivity(Intent(this@MainActivity, StickHeaderListViewActivity::class.java))
        }
    }
}
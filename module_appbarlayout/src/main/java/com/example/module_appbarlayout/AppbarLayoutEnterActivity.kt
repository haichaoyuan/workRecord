package com.example.module_appbarlayout

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_appbar_enter.*

class AppbarLayoutEnterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_appbar_enter)

        btn_1.setOnClickListener { startActivity(Intent(this@AppbarLayoutEnterActivity, AppbarLayoutSimpleActivity::class.java)) }
        btn_2.setOnClickListener { startActivity(Intent(this@AppbarLayoutEnterActivity, MainAppBarLayoutActivity::class.java)) }
        btn_3.setOnClickListener { startActivity(Intent(this@AppbarLayoutEnterActivity, HomePage2Activity::class.java)) }

    }
}
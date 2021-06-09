package com.example.module_a

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_module_a.*

class ModuleAMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_module_a)
        txt.setOnClickListener {
            startActivity(Intent(this@ModuleAMainActivity, StickHeaderListViewActivity::class.java))
        }
    }
}
package com.example.module_scanidbankcard

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import exocr.engine.EngineManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        btn_scan.setOnClickListener {
            startActivityForResult(Intent(this, IdCardScanActivity::class.java), 2)
        }
        btn_scan_new.setOnClickListener {
            startActivityForResult(Intent(this, IdCardScanNewActivity::class.java), 2)
        }
    }


}
package com.example.module_scanidbankcard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        var canUseDom = isCanUseDom()
        if(canUseDom){
            btn_scan.isEnabled = false
            btn_scan_new.isEnabled = true
        } else {
            btn_scan.isEnabled = true
            btn_scan_new.isEnabled = false
        }
    }

    fun isCanUseDom(): Boolean {
        val version = Build.VERSION.RELEASE.split("\\.".toRegex()).toTypedArray()[0].toInt()
        return version > 5 && getCPUAbi() == "arm64-v8a"
    }

    fun getCPUAbi(): String {
        val CPUAbi: String
        CPUAbi = try {
            val os_cpuabi = BufferedReader(
                InputStreamReader(
                    Runtime.getRuntime().exec("getprop ro.product.cpu.abi").inputStream
                )
            ).readLine()
            if (os_cpuabi.contains("x86")) {
                "x86"
            } else if (os_cpuabi.contains("armeabi-v7a")) {
                "armeabi-v7a"
            } else if (os_cpuabi.contains("arm64-v8a")) {
                "arm64-v8a"
            } else {
                "armeabi"
            }
        } catch (e: Exception) {
            "armeabi"
        }
        return CPUAbi
    }

    private fun initView() {
        btn_scan.setOnClickListener {
            startActivityForResult(Intent(this, IdCardScan2Activity::class.java), 2)
        }
        btn_scan_new.setOnClickListener {
            startActivityForResult(Intent(this, IdCardScanNewActivity::class.java), 2)
        }
    }


}
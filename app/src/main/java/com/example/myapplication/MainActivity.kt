package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_a.ModuleAMainActivity
import com.example.module_appbarlayout.MainAppBarLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            startActivity(Intent(this@MainActivity, ModuleAMainActivity::class.java))
        }
        button_appbar.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainAppBarLayoutActivity::class.java))
        }
    }
}
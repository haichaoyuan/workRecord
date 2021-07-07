package com.shhxzq.ztb.ui.home.ui.helper

import android.content.Intent
import android.os.Bundle
import com.example.module_homepage2.base.NormalTradeHomeFragment

/**
 * NormalTradeHomeFragment 的继承类
 */
open class HelpFragment : NormalTradeHomeFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }
}
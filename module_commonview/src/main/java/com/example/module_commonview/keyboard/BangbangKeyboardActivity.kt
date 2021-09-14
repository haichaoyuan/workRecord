package com.example.module_commonview.keyboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.bangcle.safekb.api.InputListener
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_bangbang_keyboard.*


class BangbangKeyboardActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bangbang_keyboard)

        findViewById<View>(R.id.trade_dialog_hide).setOnClickListener { finish() }
        txtPwd1.setInputListener(InputListener { encValue, txtValue, currLen, maxLen ->
            if (currLen == maxLen) {
                txtPwd1.hideKeyboard()
            }
        })

    }
}
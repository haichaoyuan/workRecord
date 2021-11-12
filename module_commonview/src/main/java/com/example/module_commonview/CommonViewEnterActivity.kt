package com.example.module_commonview

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.module_commonview.edittext.EditTextActivity
import com.example.module_commonview.edittext.VerificationCodeEditTextActivity
import com.example.module_commonview.edittext.VerificationCodeWithCursorActivity
import com.example.module_commonview.keyboard.BangbangKeyboardActivity
import com.example.module_commonview.keyboard.KeyboardActivity
import com.example.module_commonview.textview.AlignTextViewActivity
import com.example.module_commonview.wheelview.florent.FlorentWheelViewActivity
import com.example.module_commonview.wheelview.yuri.YuriWheelViewActivity
import kotlinx.android.synthetic.main.activity_common_view_enter.*

class CommonViewEnterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_common_view_enter)

        btn_edit_text.setOnClickListener {
            startActivity(Intent(this, EditTextActivity::class.java))
        }
        btn_align_text.setOnClickListener {
            startActivity(Intent(this, AlignTextViewActivity::class.java))
        }
        btn_verification_code.setOnClickListener {
            startActivity(Intent(this, VerificationCodeEditTextActivity::class.java))
        }
        btn_verification_code2.setOnClickListener {
            startActivity(Intent(this, VerificationCodeWithCursorActivity::class.java))
        }

        // 安全键盘
        btn_bangban_keyboard.setOnClickListener {
            startActivity(Intent(this, BangbangKeyboardActivity::class.java))
        }
        btn_keyboard.setOnClickListener {
            startActivity(Intent(this, KeyboardActivity::class.java))
        }

        // btn_wheelview
        btn_yuri_wheelview.setOnClickListener {
            startActivity(Intent(this, YuriWheelViewActivity::class.java))
        }
        btn_florent_wheelview.setOnClickListener {
            startActivity(Intent(this, FlorentWheelViewActivity::class.java))
        }
    }

}
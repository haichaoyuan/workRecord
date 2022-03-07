package com.example.module_commonview.edittext

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_verify_code_edit_text2.*

/**
 * 验证码 EditText
 * 带光标
 */
class VerificationCodeWithCursorActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code_edit_text2)

        verificationView.listener = {s,b ->
            Toast.makeText(this,"listener $s=$b", Toast.LENGTH_SHORT).show()
        }
        verificationView.finish = {s->
            Toast.makeText(this,"finish $s", Toast.LENGTH_SHORT).show()
        }
        verificationView2.listener = {s,b->
            println("$s,$b")
//            Toast.makeText(this,"$it",Toast.LENGTH_SHORT).show()
        }
        button.setOnClickListener {
            verificationView.clear()
        }
        button2.setOnClickListener {
            verificationView2.clear()
        }

    }
}
package com.example.module_commonview.edittext

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import com.example.module_commonview.edittext.vericationCode.VerificationCodeView

/**
 * 验证码 EditText
 * 不带光标
 */
class VerificationCodeEditTextActivity : FragmentActivity() {
    private var content: LinearLayout? = null
    private var icv: VerificationCodeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code_edit_text)
        content = findViewById(R.id.content) as LinearLayout
        icv = findViewById(R.id.icv) as VerificationCodeView?
        val codeView = VerificationCodeView(this)
        content!!.addView(codeView)

        icv!!.setInputCompleteListener(object : VerificationCodeView.InputCompleteListener {
            override fun inputComplete() {
                Log.i("icv_input", icv!!.getInputContent())
            }

            override fun deleteContent() {
                Log.i("icv_delete", icv!!.getInputContent())
            }
        })
        codeView.postDelayed(Runnable { codeView.setEtNumber(5) }, 5000)
        codeView.setInputCompleteListener(object : VerificationCodeView.InputCompleteListener {
            override fun inputComplete() {
                Log.i("icv_input", codeView.getInputContent())
            }

            override fun deleteContent() {
                Log.i("icv_delete", codeView.getInputContent())
            }
        })
    }

    fun onClick(view: View?) {
        icv!!.clearInputContent()
    }
}
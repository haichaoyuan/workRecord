package com.example.module_commonview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.module_commonview.edittext.EditTextActivity
import com.example.module_commonview.textview.AlignTextViewActivity
import kotlinx.android.synthetic.main.activity_common_view_enter.*

class CommonViewEnterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_view_enter)

        btn_edit_text.setOnClickListener {
            startActivity(Intent(this, EditTextActivity::class.java))
        }
        btn_align_text.setOnClickListener {
            startActivity(Intent(this, AlignTextViewActivity::class.java))
        }
    }

}
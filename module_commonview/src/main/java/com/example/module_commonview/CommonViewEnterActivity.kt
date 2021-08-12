package com.example.module_commonview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_common_view_enter.*

class CommonViewEnterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_view_enter)

        configNameEdit()
    }

    /**
     * 配置姓名输入框
     */
    private fun configNameEdit() {
        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 限制二：特殊字符限制
                NameEditLimitHelper.limitSpecialChar(nameEdit, s)
            }

            override fun afterTextChanged(s: Editable) {
                // 限制一：长度150字符
                NameEditLimitHelper.limitMaxLength(nameEdit, s)
            }
        })
    }
}
package com.example.module_commonview.edittext

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_edit_text.*

/**
 * EditText 限制输入字符
 */
class EditTextActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        configNameEdit()
    }

    /**
     * 配置姓名输入框
     */
    private fun configNameEdit() {
        nameEdit.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(NameEditLimitHelper.MAX_LENGTH))) //最大输入长度
        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // 限制二：特殊字符限制
                NameEditLimitHelper.limitSpecialChar(nameEdit, s)
            }

            override fun afterTextChanged(s: Editable) {
                // 限制一：长度150字符
//                NameEditLimitHelper.limitMaxLength(nameEdit, s)
            }
        })
    }
}
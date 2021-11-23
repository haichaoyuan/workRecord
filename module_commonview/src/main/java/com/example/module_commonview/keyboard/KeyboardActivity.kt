package com.example.module_commonview.keyboard

import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.Selection
import android.text.TextWatcher
import android.text.method.TransformationMethod
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.example.module_commonview.R
import com.example.module_commonview.keyboard.view.YxSafeSoftKeyboard
import kotlinx.android.synthetic.main.activity_keyboard.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 自定义安全键盘
 */
class KeyboardActivity : FragmentActivity() {
    lateinit var yxSafeSoftKeyboard: YxSafeSoftKeyboard
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        yxSafeSoftKeyboard = YxSafeSoftKeyboard(this)
        yxSafeSoftKeyboard.addEditViewListener(edit_phone)
        phoneAddSpace(edit_phone, null)
        // test
        yxSafeSoftKeyboard.addEditViewListener(edit_num, YxSafeSoftKeyboard.TYPE_NUM)
        yxSafeSoftKeyboard.addEditViewListener(edit_num2, YxSafeSoftKeyboard.TYPE_NUM)
        yxSafeSoftKeyboard.addEditViewListener(edit_dig, YxSafeSoftKeyboard.TYPE_DIG)
        yxSafeSoftKeyboard.addEditViewListener(edit_dig2, YxSafeSoftKeyboard.TYPE_DIG)
        yxSafeSoftKeyboard.addEditViewListener(edit_eng, YxSafeSoftKeyboard.TYPE_ENG)
        yxSafeSoftKeyboard.addEditViewListener(edit_eng2, YxSafeSoftKeyboard.TYPE_ENG)
    }

    /**
     * 手机号码加空格,同上方法
     *
     * @param mEditText
     */
    fun phoneAddSpace(mEditText: EditText, textWatcher: TextWatcher?) {
        mEditText.addTextChangedListener(object : TextWatcher {
            var beforeTextLength = 0
            var onTextLength = 0
            var isChanged = false
            var location = 0 // 记录光标的位置
            lateinit var tempChar: CharArray
            private val buffer = StringBuffer()
            var konggeNumberB = 0
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                beforeTextLength = s.length
                if (buffer.length > 0) {
                    buffer.delete(0, buffer.length)
                }
                konggeNumberB = 0
                for (i in 0 until s.length) {
                    if (s[i] == ' ') {
                        konggeNumberB++
                    }
                }
                textWatcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                onTextLength = s.length
                buffer.append(s.toString())
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false
                    return
                }
                isChanged = true
                textWatcher?.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable) {
                if (isChanged) {
                    location = mEditText.selectionEnd
                    var index = 0
                    while (index < buffer.length) {
                        if (buffer[index] == ' ') {
                            buffer.deleteCharAt(index)
                        } else {
                            index++
                        }
                    }
                    index = 0
                    var konggeNumberC = 0
                    while (index < buffer.length) {
                        if (index == 3 || index == 8) {
                            buffer.insert(index, ' ')
                            konggeNumberC++
                        }
                        index++
                    }
                    if (konggeNumberC > konggeNumberB) {
                        location += konggeNumberC - konggeNumberB
                    }
                    tempChar = CharArray(buffer.length)
                    buffer.getChars(0, buffer.length, tempChar, 0)
                    val str = buffer.toString()
                    if (location > str.length) {
                        location = str.length
                    } else if (location < 0) {
                        location = 0
                    }
                    mEditText.setText(str)
                    val etable = mEditText.text
                    Selection.setSelection(etable, location)
                    isChanged = false
                }
                textWatcher?.afterTextChanged(s)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        yxSafeSoftKeyboard.dismissMySofKeyBoard()
    }
}
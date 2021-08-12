package com.example.module_commonview

import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.EditText

/**
 * 姓名编辑框限制帮助类
 */
object NameEditLimitHelper {
    private val MAX_LENGTH = 120
    private val TAG = NameEditLimitHelper::class.java.simpleName


    /**
     *  限制一：长度150字符
     */
    fun limitMaxLength(nameEdit: EditText, s: Editable) {

        if (!TextUtils.isEmpty(s) && s.length > MAX_LENGTH) {
            //删除指定长度之后的数据
            s.delete(
                MAX_LENGTH, nameEdit.getSelectionEnd()
            )
        }
    }

    /**
     *  限制 二：特殊字符限制
     */
    fun limitSpecialChar(nameEdit: EditText, s: CharSequence) {
//        val time = System.currentTimeMillis()
        var tmp = s.toString()
        val chars = tmp.toCharArray()
        for (i in chars.indices) {
            if (!isAllowed(chars[i])) {
                tmp = tmp.replace(chars[i].toString(), "")
            }
        }
//        val time2 = System.currentTimeMillis()
//        print("长度：${s.length},时间：${time2- time}, content: $s")
        if (tmp != s.toString()) {
            nameEdit.setText(tmp)
            nameEdit.setSelection(nameEdit.length())
        }
    }

    private fun isAllowed(c: Char): Boolean {
        if (c in '0'..'9') return true
        if (c in 'a'..'z') return true
        if (c in 'A'..'Z') return true
        if ('—' == (c) || '·' == c || '（' == c || '）' == c || '“' == (c) || '”' == c) {
            return true
        }
        return isChineseByBlock(c)
    }

    private fun isChineseByBlock(c: Char): Boolean {
//        使用UnicodeBlock方法判断
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
    }

    private fun print(str: String) {
        Log.e(TAG, str)
    }

}
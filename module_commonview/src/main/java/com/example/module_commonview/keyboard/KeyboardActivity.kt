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
import kotlinx.android.synthetic.main.activity_keyboard.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 自定义安全键盘
 */
class KeyboardActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        edit_phone.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return configKeyboard(v, event)
            }
        })
        phoneAddSpace(edit_phone, null)
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

    /**
     * 配置键盘
     */
    private fun configKeyboard(v: View, event: MotionEvent): Boolean {
        val et = v as EditText
        if (event.getAction() == MotionEvent.ACTION_UP) {
            val inType: Int = et.getInputType()
            val method: TransformationMethod? = et.getTransformationMethod()
            et.setTransformationMethod(null)
            // 屏蔽软键盘
            hideSoftInputMethod(et)

            et.onTouchEvent(event)
            et.setInputType(inType)
            et.setTransformationMethod(method)
            et.setCursorVisible(true)

            if(view == null){
                displayMySofKeyBoard()
            } else {
                dismissMySofKeyBoard()
            }

            val locations = IntArray(2)
            et.getLocationOnScreen(locations)

            v.requestFocus()
            val text: String = et.getText().toString()
            if (text != null && text.length > 0) et.setSelection(text.length)
            return true
        }
        return false
    }

    var view: View? = null
    private fun displayMySofKeyBoard() {
        view = LayoutInflater.from(this).inflate(R.layout.keyboard_digital, null)
        val mParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT
        mParams.format = PixelFormat.TRANSLUCENT
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        mParams.flags =
            (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_SECURE)
        mParams.gravity = Gravity.BOTTOM or Gravity.LEFT
        // 解决显示到虚拟键盘问题
        mParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        windowManager.addView(view, mParams)

        view?.setOnTouchListener { v, event -> true }
        findViews(view!!)

    }

    private fun dismissMySofKeyBoard() {
        view?.run {
            windowManager.removeView(view)
            buttonDirectInput = null
            view = null
        }
    }

    override fun onPause() {
        super.onPause()
        dismissMySofKeyBoard()
    }

    // 隐藏系统键盘
    fun hideSoftInputMethod(ed: EditText) {
//        (Activity)mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val currentVersion = Build.VERSION.SDK_INT
        var methodName: String? = null
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus"
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus"
        }
        if (methodName == null) {
            ed.inputType = InputType.TYPE_NULL
        } else {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus: Method
            try {
                setShowSoftInputOnFocus =
                    cls.getMethod(methodName, Boolean::class.javaPrimitiveType)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(ed, false)
            } catch (e: NoSuchMethodException) {
                ed.inputType = InputType.TYPE_NULL
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    // 直接输入的按钮
    var buttonDirectInput: Array<Button?>? = null
    private fun findViews(view: View) {
        if (buttonDirectInput == null) {
            buttonDirectInput = arrayOfNulls(11)
            // 数字键盘
            // 0-9
            buttonDirectInput!![0] = view.findViewById(R.id.digital_num_0)
            buttonDirectInput!![1] = view.findViewById(R.id.digital_num_1)
            buttonDirectInput!![2] = view.findViewById(R.id.digital_num_2)
            buttonDirectInput!![3] = view.findViewById(R.id.digital_num_3)
            buttonDirectInput!![4] = view.findViewById(R.id.digital_num_4)
            buttonDirectInput!![5] = view.findViewById(R.id.digital_num_5)
            buttonDirectInput!![6] = view.findViewById(R.id.digital_num_6)
            buttonDirectInput!![7] = view.findViewById(R.id.digital_num_7)
            buttonDirectInput!![8] = view.findViewById(R.id.digital_num_8)
            buttonDirectInput!![9] = view.findViewById(R.id.digital_num_9)
            buttonDirectInput!![10] = view.findViewById(R.id.digital_num_point)
            var hideView = view?.findViewById<View>(R.id.digital_num_hide)
            hideView?.setOnClickListener {
                dismissMySofKeyBoard()
            }
        }

        buttonDirectInput?.forEach {
            it?.setOnClickListener(btnInputListener)
        }
    }

    private val btnInputListener = View.OnClickListener { v: View ->
        val s = (v as Button).text.toString()
        if (s != null) {
            directInput(s)
        }
    }

    val isStockSearch = true
    private fun directInput(inputStr: String) {
        var focusView: View? = this.getCurrentFocus()
        if (isStockSearch) {
//            focusView = stockEdit
        }
        if (focusView is EditText) {
            try { // 防止编辑框对输入的格式或者长度有限制时出现错误
                val et = focusView
                // 因为是选择的起始位置，可能start>end
                var start = et.selectionStart
                var end = et.selectionEnd
                if (start > end) {
                    val temp = start
                    start = end
                    end = temp
                }
                //input 为密码 数字 // 只允许数字
                if (et.inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER
                    || et.inputType == InputType.TYPE_CLASS_NUMBER
                ) {
                    val s = inputStr[0]
                    if (s.code < 48 || s.code > 57) {
                        return
                    }
                } else if (et.inputType == InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL) {
                    val s = inputStr[0]
                    if (s == '.') {
                        //包含点了，返回
                        if (et.text.toString().contains(".")) {
                            return
                        }
                    } else {
                        //非数字 非点
                        if (s.code < 48 || s.code > 57) {
                            return
                        }
                    }
                }
                val str = et.text.toString()
                val beginStr = str.substring(0, start)
                val endStr = str.substring(end)
                val midStr = inputStr
                val result = beginStr + inputStr + endStr
                val tmp = et.text.toString()
                et.setText(result)

                // 重新设置光标
//                et.setSelection(start + midStr.length());
                et.setSelection(et.text.toString().length)
//                if (isStockSearch && listView != null && listView.getTag() != null && listView.getTag() as Int == 1) {
//                    if (tmp.length != 0) return
//                    //对查股保护
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
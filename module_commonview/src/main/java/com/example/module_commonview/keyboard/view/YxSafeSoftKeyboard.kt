package com.example.module_commonview.keyboard.view

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.text.InputType
import android.text.method.TransformationMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.example.module_commonview.R
import kotlinx.android.synthetic.main.activity_keyboard.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

class YxSafeSoftKeyboard(val activity: Activity) {
    private var windowManager: WindowManager

    init {
        windowManager = activity.windowManager
    }

    private var layout: View? = null
    private var curEditView: View? = null
    private var numberView: View? = null
    private var englishView: View? = null
    private var digitalView: View? = null

    // 直接输入的按钮
    private var btnNumArray: Array<Button?>? = null // 数字按钮列表
    private var btnDigArray: Array<Button?>? = null // 英文按钮列表
    private var btnEngArray: Array<Button?>? = null // 纯数字按钮列表


    private var curKeyboardType: Int = -1 // 键盘类型

    @JvmOverloads
    fun addEditViewListener(editView: EditText, keyboardType: Int = TYPE_DIG) {
        editView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return configKeyboard(v, event, keyboardType)
            }
        })
        editView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus == true) {
                    //焦点存在
                    if (curEditView != v) {
                        curEditView = v
                    }
                }
            }
        })
        if (keyboardType == TYPE_DIG) {
            // 甬兴安全键盘，去除长按
            editView.setOnLongClickListener({ v: View? -> true })
        }
    }


    /**
     * 配置键盘
     */
    private fun configKeyboard(v: View, event: MotionEvent, keyboardType: Int): Boolean {
        val et = v as EditText
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 屏蔽软键盘
            hideSoftInputMethod(et)

            if (layout == null || curEditView == null || curEditView != et) {
                // 不存在，或者非同一个 EditView
                displayMySofKeyBoard(keyboardType)
                curEditView = et

                val locations = IntArray(2)
                et.getLocationOnScreen(locations)
                et.requestFocus()
                val text: String = et.getText().toString()
                if (text != null && text.length > 0) et.setSelection(text.length)
            } else {
                dismissMySofKeyBoard()
                et.clearFocus()
            }

            val inType: Int = et.getInputType()
            val method: TransformationMethod? = et.getTransformationMethod()
            et.setTransformationMethod(null)
            et.onTouchEvent(event)
            et.setInputType(inType)
            et.setTransformationMethod(method)
            et.setCursorVisible(true)
            return true
        }
        return false
    }


    /**
     * 显示键盘
     */
    private fun displayMySofKeyBoard(keyboardType: Int) {
        if (layout != null && curKeyboardType == keyboardType) {
            // 已经显示相同的键盘，略过
            return
        }
        if (layout != null) {
            dismissMySofKeyBoard()
        }
        layout = LayoutInflater.from(activity).inflate(R.layout.keyboard_normal_layout, null)
        curKeyboardType = keyboardType
        showSubKeyboard(layout, keyboardType)
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
        windowManager.addView(layout, mParams)

        layout?.run {
            setOnTouchListener { v, event -> true }
            findViews(this)
        }
    }

    /**
     * 根据不同的键盘类型，显示不同的键盘视图
     */
    private fun showSubKeyboard(view: View?, keyboardType: Int) {
        if (view == null) {
            return
        }
        numberView = view.findViewById(R.id.number)
        englishView = view.findViewById(R.id.english)
        digitalView = view.findViewById(R.id.digital)
        changeKeyboard(keyboardType)
    }

    private fun changeKeyboard(keyboardType: Int) {
        if (keyboardType == TYPE_DIG) {
            numberView?.setVisibility(View.GONE)
            englishView?.setVisibility(View.GONE)
            digitalView?.setVisibility(View.VISIBLE)
        } else if (keyboardType == TYPE_ENG) {
            numberView?.setVisibility(View.GONE)
            englishView?.setVisibility(View.VISIBLE)
            digitalView?.setVisibility(View.GONE)
        } else if (keyboardType == TYPE_NUM) {
            numberView?.setVisibility(View.VISIBLE)
            englishView?.setVisibility(View.GONE)
            digitalView?.setVisibility(View.GONE)
        }
    }

    /**
     * 隐藏键盘
     */
    fun dismissMySofKeyBoard() {
        layout?.run {
            windowManager.removeView(layout)
            btnNumArray = null
            btnDigArray = null
            btnEngArray = null
            curEditView = null
            layout = null
        }
    }

    /**
     * 隐藏系统键盘
     */
    private fun hideSoftInputMethod(ed: EditText) {
        val imm: InputMethodManager? =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(ed.getWindowToken(), 0)

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
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }


    private fun findViews(view: View) {
        when (curKeyboardType) {
            TYPE_DIG -> {
                findDigView(view)
            }
            TYPE_ENG -> {
                findEngView(view)
            }
            TYPE_NUM -> {
                findNumView(view)
            }
            else -> {
                findDigView(view)
            }
        }
    }

    private fun findNumView(view: View) {
        if (btnNumArray == null) {
            btnNumArray = arrayOfNulls(15)
            // 数字键盘
            // 0-9
            btnNumArray!![0] = view.findViewById(R.id.num_0)
            btnNumArray!![1] = view.findViewById(R.id.num_1)
            btnNumArray!![2] = view.findViewById(R.id.num_2)
            btnNumArray!![3] = view.findViewById(R.id.num_3)
            btnNumArray!![4] = view.findViewById(R.id.num_4)
            btnNumArray!![5] = view.findViewById(R.id.num_5)
            btnNumArray!![6] = view.findViewById(R.id.num_6)
            btnNumArray!![7] = view.findViewById(R.id.num_7)
            btnNumArray!![8] = view.findViewById(R.id.num_8)
            btnNumArray!![9] = view.findViewById(R.id.num_9)
            // 点
            btnNumArray!![10] = view.findViewById(R.id.num_point)
            // 600,000,002,300
            btnNumArray!![11] = view.findViewById(R.id.num_600)
            btnNumArray!![12] = view.findViewById(R.id.num_300)
            btnNumArray!![13] = view.findViewById(R.id.num_000)

            btnNumArray?.forEach {
                it?.setOnClickListener(btnInputListener)
            }
            // abc  num_abc
            var abcView = view.findViewById<View>(R.id.num_abc)
            abcView?.setOnClickListener {
                changeNumOrEng(it)
            }

            //num_hide num_del  num_enter
            var hideView = view.findViewById<View>(R.id.num_hide)
            hideView?.setOnClickListener {
                dismissMySofKeyBoard()
            }

            var delView = view.findViewById<View>(R.id.num_del)
            delView?.setOnClickListener(delClickListener)
            delView?.setOnLongClickListener(longPressDelListener)

            var enterView = view.findViewById<View>(R.id.num_enter)
            enterView?.setOnClickListener {
                dismissMySofKeyBoard()
            }
        }
    }

    private fun findEngView(view: View) {
        if (btnEngArray == null) {
            btnEngArray = arrayOfNulls(30)
            // a-z
            btnEngArray!![0] = view.findViewById(R.id.eng_a)
            btnEngArray!![1] = view.findViewById(R.id.eng_b)
            btnEngArray!![2] = view.findViewById(R.id.eng_c)
            btnEngArray!![3] = view.findViewById(R.id.eng_d)
            btnEngArray!![4] = view.findViewById(R.id.eng_e)
            btnEngArray!![5] = view.findViewById(R.id.eng_f)
            btnEngArray!![6] = view.findViewById(R.id.eng_g)
            btnEngArray!![7] = view.findViewById(R.id.eng_h)
            btnEngArray!![8] = view.findViewById(R.id.eng_i)
            btnEngArray!![9] = view.findViewById(R.id.eng_j)
            btnEngArray!![10] = view.findViewById(R.id.eng_k)
            btnEngArray!![11] = view.findViewById(R.id.eng_l)
            btnEngArray!![12] = view.findViewById(R.id.eng_m)
            btnEngArray!![13] = view.findViewById(R.id.eng_n)
            btnEngArray!![14] = view.findViewById(R.id.eng_o)
            btnEngArray!![15] = view.findViewById(R.id.eng_p)
            btnEngArray!![16] = view.findViewById(R.id.eng_q)
            btnEngArray!![17] = view.findViewById(R.id.eng_r)
            btnEngArray!![18] = view.findViewById(R.id.eng_s)
            btnEngArray!![19] = view.findViewById(R.id.eng_t)
            btnEngArray!![20] = view.findViewById(R.id.eng_u)
            btnEngArray!![21] = view.findViewById(R.id.eng_v)
            btnEngArray!![22] = view.findViewById(R.id.eng_w)
            btnEngArray!![23] = view.findViewById(R.id.eng_x)
            btnEngArray!![24] = view.findViewById(R.id.eng_y)
            btnEngArray!![25] = view.findViewById(R.id.eng_z)

            btnEngArray?.forEach {
                it?.setOnClickListener(btnInputListener)
            }

            //eng_del
            val delView = view.findViewById<View>(R.id.eng_del)
            delView?.setOnClickListener(delClickListener)
            delView?.setOnLongClickListener(longPressDelListener)

            // 空格
            val spaceView = view.findViewById<View>(R.id.eng_blank_space)
            spaceView?.setOnClickListener(blankSpaceInputListener)
            // 切换 123
            val abcView = view.findViewById<View>(R.id.eng_123)
            abcView?.setOnClickListener {
                changeNumOrEng(it)
            }
            // 大小写
            btnEngArray!![IndexCapLock] = view.findViewById(R.id.eng_changecase)
            btnEngArray!![IndexCapLock]?.setOnClickListener(daxiaoxieListener)

            val enterView = view.findViewById<View>(R.id.eng_enter)
            enterView?.setOnClickListener {
                dismissMySofKeyBoard()
            }
            val hideView = view.findViewById<View>(R.id.eng_exit)
            hideView?.setOnClickListener {
                dismissMySofKeyBoard()
            }
        }
    }


    private fun findDigView(view: View) {
        if (btnDigArray == null) {
            btnDigArray = arrayOfNulls(11)
            // 数字键盘
            // 0-9 + .
            btnDigArray!![0] = view.findViewById(R.id.digital_num_0)
            btnDigArray!![1] = view.findViewById(R.id.digital_num_1)
            btnDigArray!![2] = view.findViewById(R.id.digital_num_2)
            btnDigArray!![3] = view.findViewById(R.id.digital_num_3)
            btnDigArray!![4] = view.findViewById(R.id.digital_num_4)
            btnDigArray!![5] = view.findViewById(R.id.digital_num_5)
            btnDigArray!![6] = view.findViewById(R.id.digital_num_6)
            btnDigArray!![7] = view.findViewById(R.id.digital_num_7)
            btnDigArray!![8] = view.findViewById(R.id.digital_num_8)
            btnDigArray!![9] = view.findViewById(R.id.digital_num_9)
            btnDigArray!![10] = view.findViewById(R.id.digital_num_point)

            btnDigArray?.forEach {
                it?.setOnClickListener(btnInputListener)
            }

            // hide
            var hideView = view.findViewById<View>(R.id.digital_num_hide)
            hideView?.setOnClickListener {
                dismissMySofKeyBoard()
            }

            // del
            var delView = view.findViewById<View>(R.id.digital_num_del)
            delView?.setOnClickListener(delClickListener)
            delView?.setOnLongClickListener(longPressDelListener)

            var enterView = view.findViewById<View>(R.id.digital_num_enter)
            enterView?.setOnClickListener {
                dismissMySofKeyBoard()
            }
        }
    }


    /**
     * 数字、英文键盘切换
     */
    private fun changeNumOrEng(view: View) {
        if (view.getId() == R.id.eng_123) {
            // 数字 -> 英文
            curKeyboardType = TYPE_NUM
        } else if (view.getId() == R.id.num_abc) {
            curKeyboardType = TYPE_ENG
        }
        // 改变布局
        changeKeyboard(curKeyboardType)
        // 视图赋值
        layout?.run {
            setOnTouchListener { v, event -> true }
            findViews(this)
        }
    }

    /**
     * 文本输入
     */
    private val btnInputListener = View.OnClickListener { v: View ->
        val s = (v as Button).text.toString()
        if (s != null) {
            directInput(s)
        }
    }

    /**
     * 删除事件
     */
    private var delClickListener = View.OnClickListener {
        var focusView: View? = activity.getCurrentFocus()
        if (focusView != null && focusView is EditText) {
            val et = focusView
            val str = et.text.toString()
            var result = ""
            val strSbuffer = StringBuffer(str)
            var index = 0
            var mSpaceCount = 0
            while (index < strSbuffer.length) {
                if (strSbuffer[index] == ' ') {
                    mSpaceCount++
                    index++
                } else {
                    index++
                }
            }

            // 因为是选择的起始位置，可能start>end
            var start = et.selectionStart
            val indexBefore = et.selectionEnd - 2
            var end = if (indexBefore > 0 && mSpaceCount != 0 && strSbuffer[indexBefore] == ' ') {
                et.selectionEnd - 2
            } else {
                et.selectionEnd
            }
            if (start == end && start != 0) {
                result = str.substring(0, start - 1) + str.substring(end)
                et.setText(result)
                // 重新设置光标
                et.setSelection(start - 1)
                return@OnClickListener
            }
            if (start > end) {
                val temp = start
                start = end
                end = temp
            }
            // 如果编辑框中有文字选中，则删除选择的文字
            result = str.substring(0, start) + str.substring(end)
            et.setText(result)
            // 重新设置光标
            et.setSelection(start)
        }
    }

    /**
     * 长按删除键事件,长按删除按钮则清空编辑框
     */
    var longPressDelListener = View.OnLongClickListener {
        var focusView: View? = activity.getCurrentFocus()
        if (focusView != null && focusView is EditText) {
            focusView.setText("")
        }
        true
    }

    /**
     * 空格输入
     */
    private val blankSpaceInputListener = View.OnClickListener { directInput(" ") }

    private val IndexCapLock = 26

    /**
     * 大小写
     */
    private val daxiaoxieListener = View.OnClickListener {
        if (btnEngArray == null) {
            return@OnClickListener
        }
        var capLockView = btnEngArray!!.get(IndexCapLock)
        val tag: Any? = capLockView?.getTag()
        if (tag == null || tag == "小") {
            for (i in 0..25) {
                val tmpView = btnEngArray!!.get(i)
                tmpView?.setPadding(0, 0, 0, 0)
                val s: String = tmpView?.getText().toString()
                tmpView?.setText(s.toUpperCase(Locale.getDefault()))
            }
            capLockView?.setTag("大")
            capLockView?.setSelected(true)
        } else {
            for (i in 0..25) {
                val tmpView = btnEngArray!!.get(i)
                tmpView?.setPadding(0, 0, 0, 12)
                val s: String = tmpView?.getText().toString()
                tmpView?.setText(s.toLowerCase(Locale.getDefault()))
            }
            capLockView?.setTag("小")
            capLockView?.setSelected(false)
        }
    }

    private fun directInput(inputStr: String) {
        var focusView: View? = activity.getCurrentFocus()
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
                    if (s < 48.toChar() || s > 57.toChar()) {
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
                        if (s < 48.toChar() || s > 57.toChar()) {
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
                et.setSelection(et.text.toString().length)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val TYPE_NUM = 0 // 数字键盘
        const val TYPE_ENG = 1 // 英文键盘
        const val TYPE_DIG = 3 // 纯数字键盘
    }
}
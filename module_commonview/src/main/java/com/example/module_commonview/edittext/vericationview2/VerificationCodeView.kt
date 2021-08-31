package com.example.module_commonview.edittext.vericationview2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.example.module_commonview.R

/**
 *
 * Created by lilifeng on 2019/4/8
 * 自定义View - 手机验证码输入框（带光标）
 * https://github.com/limxing/Kotlin-VerificationView
 */
class VerificationCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), TextWatcher, View.OnKeyListener {

    private val density = resources.displayMetrics.density
    private var etTextSize = 18 * density
    private var etTextCount = 4
    private var etBackGround = 0
    private var etBackGroundColor = Color.BLACK
    private var etTextColor = Color.BLACK
    private var etCursorDrawable = 0
    private var etAutoShow = true //自动聚焦
    private var etWidth: Float
    private var etLineColor: Int
    private var wtWidthPercent: Float
    private var etLineHeight: Float

    /**
     * 读取属性
     */
    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.VerificationView)
        etTextSize = array.getDimension(R.styleable.VerificationView_vTextSize, 18 * density)
        etTextCount = array.getInteger(R.styleable.VerificationView_vTextCount, 4)
        etBackGround = array.getResourceId(R.styleable.VerificationView_vBackgroundResource, 0)
        etBackGroundColor =
            array.getColor(R.styleable.VerificationView_vBackgroundColor, Color.BLACK)
        etTextColor = array.getColor(R.styleable.VerificationView_vTextColor, Color.BLACK)
        etCursorDrawable = array.getResourceId(R.styleable.VerificationView_vCursorDrawable, 0)
        etAutoShow = array.getBoolean(R.styleable.VerificationView_vAutoShowInputBoard, true)
        etWidth = array.getDimension(R.styleable.VerificationView_vWidth, 0f)
        wtWidthPercent = array.getFloat(R.styleable.VerificationView_vWidthPercent, 1f)
        etLineColor = array.getColor(R.styleable.VerificationView_vLineColor, Color.BLACK)
        etLineHeight = array.getDimension(R.styleable.VerificationView_vLineHeight, 1f)
        array.recycle()
    }


    private var lastEditIsEmpty = true // 最好一个 EditView 是否为空
    private val paint = Paint() //划线
    private var sizeH = 0 //宽高
    private var sizeW = 0 //宽高

    /**
     * 填入 View
     */
    init {
        setWillNotDraw(false)
        for (i in 0 until etTextCount) {
            val et = buildEditView(i)
            addView(et)
        }
        val view = View(context)
        view.setOnClickListener {
            requestBgEditViewFocus()
        }
        addView(view)
    }

    /**
     * 构建单个 EditText
     */
    private fun buildEditView(i:Int): EditText {
        val et = EditText(context)
        et.gravity = Gravity.CENTER
        et.includeFontPadding = false
        if (etBackGroundColor != Color.BLACK)
            et.background = ColorDrawable(etBackGroundColor)
        et.includeFontPadding = false
        if (etBackGround != 0)
            et.setBackgroundResource(etBackGround)
        et.setEms(1)
        if (etCursorDrawable != 0) {
            // API 29 以上，使用 setTextCursorDrawable 方法；以下，使用反射改变 mCursorDrawableRes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                et.setTextCursorDrawable(etCursorDrawable)
            } else {
                try {
                    val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
                    f.isAccessible = true
                    f.set(et, etCursorDrawable)
                } catch (e: Throwable) {
                }
            }
        }
        et.inputType = InputType.TYPE_CLASS_NUMBER
        et.setTextColor(etTextColor)
        et.setTextSize(TypedValue.COMPLEX_UNIT_PX, etTextSize)
        et.addTextChangedListener(this)
        et.setOnKeyListener(this)
        et.tag = i
        et.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(1))
        return et
    }

    // =========================================================================================
    // =================================== OnKeyListener listener
    // =========================================================================================


    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_UP) {
            if (v?.tag == (etTextCount - 1)) {
                if (lastEditIsEmpty) {
                    backFocus()
                }
                lastEditIsEmpty = true
            } else {
                backFocus()
            }
        }
        return false
    }

    // =========================================================================================
    // =================================== TextWatcher listener
    // =========================================================================================

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        val lastETC = getChildAt(etTextCount - 1) as EditText
        lastEditIsEmpty = lastETC.text.isEmpty()
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        synchronized(this) {
            focus()
        }
    }

    // =========================================================================================
    // =================================== ViewGroup listener
    // =========================================================================================
    /**
     * 初始的显示输入框
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (etAutoShow)
            postDelayed({
                focus()
            }, 200)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        sizeW = MeasureSpec.getSize(widthMeasureSpec)
        sizeH = MeasureSpec.getSize(heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (wtWidthPercent != 1f) etWidth = wtWidthPercent * sizeW / 4
        val w = if (etWidth != 0f) etWidth.toInt() else sizeH //etText 宽度
        val middle = (sizeW - w * etTextCount) / (etTextCount - 1) // etText 之间的空间
        // 0~ 4 个 EditText
        for (i in 0 until etTextCount) {
            val et = getChildAt(i) as EditText
            val lp = getChildAt(0).layoutParams
            lp.width = w
            lp.height = sizeH
            getChildAt(i).layoutParams = lp

            val left = (middle + w) * i
            et.layout(left, 0, left + w, sizeH)
        }
        // 第5 个，就是 View
        getChildAt(etTextCount).layout(0, 0, sizeW, sizeH)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 画底部线，默认不用
        if (etWidth == 0f) return
        paint.isAntiAlias = true
        paint.color = etLineColor
        paint.strokeWidth = etLineHeight
        val w = if (etWidth != 0f) etWidth.toInt() else sizeH
        val middle = (sizeW - w * etTextCount) / (etTextCount - 1)
        for (i in 0 until etTextCount) {
            val left = (middle + w) * i
            canvas?.drawLine(
                left.toFloat(),
                sizeH - etLineHeight,
                (left + w).toFloat(),
                sizeH - etLineHeight,
                paint
            )
        }
    }
    // =========================================================================================
    // =================================== inner method
    // =========================================================================================

    /**
     * 点击底部 View
     */
    private fun requestBgEditViewFocus() {
        val lastETC = getChildAt(etTextCount - 1) as EditText
        if (lastETC.text.isNotEmpty()) {
            lastETC.isEnabled = true
            showInputPad(lastETC)
            lastETC.isCursorVisible = true
        } else
            focus()
    }

    /**
     * 展示输入键盘
     */
    private fun showInputPad(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            editText,
            0
        )
    }


    /**
     * 输入文本后，切换下一个 EditText
     */
    private fun focus() {
        val text = StringBuffer()
        for (i in 0 until etTextCount) {
            text.append((getChildAt(i) as EditText).text.toString())
        }
        listener?.invoke(text.toString(), text.length == etTextCount)
        // 找到第一个空的 editText，显示弹窗
        for (i in 0 until etTextCount) {
            val editText = getChildAt(i) as EditText
            (getChildAt(i) as EditText).isEnabled = true
            if (editText.text.isEmpty()) {
                showInputPad(editText)
                editText.isCursorVisible = true
                return
            }
        }
        // 每个 editText 都有值, editText 都设为不可用，取消焦点
        val et = getChildAt(etTextCount - 1) as EditText
        if (et.text.isNotEmpty()) {
            for (i in 0 until etTextCount) {
                (getChildAt(i) as EditText).isEnabled = false
            }
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                et.windowToken,
                0
            )
            et.isCursorVisible = false
            et.clearFocus()

            finish?.invoke(text.toString())
        }
    }

    /**
     * 焦点回退，反向找到第一个不为空的 editText，设为空
     */
    private fun backFocus() {
        for (i in etTextCount - 1 downTo 0) {
            val editText = getChildAt(i) as EditText
            if (editText.text.length == 1) {
                editText.setText("")
                return
            }
        }
    }

    /**
     * 最后一个完成回调
     */
    var finish: ((String) -> Unit)? = null

    var listener: ((String, Boolean) -> Unit)? = null

    fun clear() {
        for (j in 0 until etTextCount) {
            val et = (getChildAt(j) as EditText)
            et.removeTextChangedListener(this)
            et.setText("")
            et.addTextChangedListener(this)
        }
        focus()
    }
}
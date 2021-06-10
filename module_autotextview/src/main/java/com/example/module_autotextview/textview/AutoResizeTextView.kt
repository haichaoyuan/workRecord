package com.example.module_autotextview.textview

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import java.util.*

/**
 * AutoResizeTextView 实现类, 增加些测试代码，无时间作用，下次可以改继承为包含关系来实现
 */
class AutoResizeTextView : AbstractAutoResizeTextView2 {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    public override fun preSearchTextSize() {
        if (!DEBUG){
            return
        }
        curTime = System.currentTimeMillis()
    }

    private var maxTextSize = 0
    private var maxTextLength = 0
    private var curTime: Long = 0L

    public override fun postSearchTextSize(textSize: Int, start: Int, end: Int, availableSpace: RectF) {
        if (!DEBUG){
            return
        }
        val tmpTime = System.currentTimeMillis() - curTime
        val text = text
        val length = text.length
        var s = "postSearchTextSize=textsize:" + textSize + ",content:" + getText() +
                String.format(Locale.getDefault(), "=%s-%s-%s", start, end, availableSpace) + ",time:" + tmpTime
        // 计算最大文本和最长长度
        if (getTextSize() > maxTextSize) {
            maxTextSize = textSize
        }
        if (textSize == maxTextSize) {
            if (length > maxTextLength) {
                maxTextLength = length
            }
            Log.i(AbstractAutoResizeTextView2::class.java.simpleName, s)
        } else {
            //自动寻找出问题的地方，此时textSize 会变小,正常情况下，length 会长
            if (length < maxTextLength) {
                Log.e(AbstractAutoResizeTextView2::class.java.simpleName, "length:" + length + ",maxTextLength:" + maxTextLength)
                Log.e(AbstractAutoResizeTextView2::class.java.simpleName, s)
            } else {
                Log.i(AbstractAutoResizeTextView2::class.java.simpleName, s)
            }
        }
    }

    /**
     * 供列表里设置使用，防止多次调用 adjustTextSize
     */
    fun setAdjustText(resizeTextBuilder: AutoResizeTextBuilder) {
        resizeTextBuilder.apply {
            //setSingleLine
            singleLine?.let {
                super.setSingleLineImp(it, false)
            }
            //setTextSize
            if (textSize != 0F) {
                if (textSizeUnit != 0) {
                    super.setTextSizeImp(textSizeUnit, textSize, false)
                } else {
                    super.setTextSizeImp(textSize, false)
                }
            }
            //setMinTextSize
            super.setMinTextSizeImp(minTextSize, false)
        }
        //text
        super.setText(resizeTextBuilder.text)
//        super.adjustTextSize()
    }

    /**
     * textsize:37,content:汇添富中证主要消费ETF联接
     * textsize:41,content:汇添富消费行业混合
     *
     * textsize:33,content:易方达上证50指数A
     * textsize:40,content:汇添富外延增长主题股票A
     */
    fun testFont() {
        val start = 26
        val end = 42
        //宽高
        val availableSpace = RectF(0.0f, 0.0f, 475.0f, 57.0f)
        setText("易方达上证50指数A")
        var binarySearch = binarySearch(start, end, mSizeTester, availableSpace)
        System.out.println(binarySearch)
        Log.e("AutoResizeTextView", binarySearch.toString() + "")
    }

    companion object {
        const val DEBUG = false
    }

    class AutoResizeTextBuilder {
        var textSize: Float = 0F
        var textSizeUnit: Int = 0
        var singleLine: Boolean? = null
        var minTextSize: Float = 0F
        var text: String? = null

        fun setTextSize(unit: Int, size: Float) {
            textSize = size
            textSizeUnit = unit
        }
    }
}
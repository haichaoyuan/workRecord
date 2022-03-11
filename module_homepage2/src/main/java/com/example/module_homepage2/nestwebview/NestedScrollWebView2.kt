package com.example.module_homepage2.nestwebview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.webkit.JavascriptInterface
import android.widget.AbsListView
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.viewpager.widget.ViewPager

/**
 * 带嵌套滚动的 webview
 * 添加动态拦截高度功能
 */
class NestedScrollWebView2 : WrapFlingWebView2 {
    private var TAG = "NestedScrollWebView"


    private var isIntercept = false // 是否需拦截的状态
    private var interceptHeight = 0 // 需拦截的高度


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        addJavascriptInterface(this, "android")
    }

    // =================================== h5 的横向滚动

    @JavascriptInterface
    fun interceptTop(param: Boolean, height: Int) {
        isIntercept = param
        interceptHeight = (height * context.resources.displayMetrics.density).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onTouchEvent scrollX=" + event.getY() + ";rawY=" + event.rawY)
        val eventY = event.getY()
        if (isIntercept && eventY < interceptHeight) {
            // 拦截区域，直接让父类拦截事件
            val viewParent = findViewParentIfNeeds(this)
            when (event.getAction()) {
                MotionEvent.ACTION_MOVE -> {
                    viewParent?.requestDisallowInterceptTouchEvent(true)
                }
            }
            return super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private fun findViewParentIfNeeds(tag: View): ViewParent? {
        val parent = tag.parent
        if (parent == null) {
            return parent
        }
        return if (parent is ViewPager ||
                parent is AbsListView ||
                parent is ScrollView ||
                parent is HorizontalScrollView ||
                parent is GridView) {
            parent
        } else {
            if (parent is View) {
                findViewParentIfNeeds(parent as View)
            } else {
                parent
            }
        }
    }

    /**
     * 监测到WebView滑动到了边界
     */
    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        Log.e(TAG, "onOverScrolled scrollX=" + scrollX + ";scrollY=" + scrollY + ";clampedX=" + clampedX + ";clampedY=" + clampedY)
        if (isIntercept) {
            if (clampedX) {
                val viewParent = findViewParentIfNeeds(this)
                viewParent?.requestDisallowInterceptTouchEvent(false)
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }
}
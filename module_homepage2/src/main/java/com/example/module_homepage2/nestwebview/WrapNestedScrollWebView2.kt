package com.example.module_homepage2.nestwebview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.Nullable
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.example.lib_util.utils.UIUtil
import com.example.module_homepage2.nestwebview.ProgressWebView

/**
 * 从 NestedScrollWebView2 抽离出复杂的 NestScroll 逻辑
 * HzwSunshine/NestedWebViewRecyclerViewGroup
 */
open class WrapNestedScrollWebView2 : ProgressWebView, NestedScrollingChild2, NestedScrollingChild3 {
    private var TAG = "WrapNestedScrollWebView"

    private val mScrollConsumed = IntArray(2)
//    private var parent: NestedWebViewRecyclerViewGroup? = null
    private var childHelper: NestedScrollingChildHelper? = null
    private var velocityTracker: VelocityTracker? = null
    private var scroller: Scroller? = null
    private var isSelfFling = false
    private var hasFling = false
    private var density = 0f
    private var mWebViewContentHeight = 0
    private var mMaximumVelocity = 0
    private var maxScrollY = 0
    private var TouchSlop = 0
    private var firstY = 0
    private var lastY = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        childHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
        scroller = Scroller(getContext())
        val configuration = ViewConfiguration.get(getContext())
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
        density = resources.displayMetrics.density
        //TouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        TouchSlop = UIUtil.dip2px(context, 3f)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mWebViewContentHeight = 0
                lastY = event.rawY.toInt()
                firstY = lastY
                if (!scroller!!.isFinished) {
                    scroller!!.abortAnimation()
                }
                initOrResetVelocityTracker()
                isSelfFling = false
                hasFling = false
                maxScrollY = getWebViewContentHeight() - height
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                initVelocityTrackerIfNotExists()
                velocityTracker!!.addMovement(event)
                val y = event.rawY.toInt()
                val dy = y - lastY
                lastY = y
                if (!dispatchNestedPreScroll(0, -dy, mScrollConsumed, null)) {
                    scrollBy(0, -dy)
                }
                if (Math.abs(firstY - y) > TouchSlop) {
                    //屏蔽WebView本身的滑动，滑动事件自己处理
                    event.action = MotionEvent.ACTION_CANCEL
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (isParentResetScroll() && velocityTracker != null) {
                velocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val yVelocity = (-velocityTracker!!.yVelocity).toInt()
                recycleVelocityTracker()
                isSelfFling = true
                flingScroll(0, yVelocity)
            }
        }
        super.onTouchEvent(event)
        return true
    }

    override fun flingScroll(vx: Int, vy: Int) {
        var startY = getWebViewContentHeight() - height
        if (scrollY < startY) {
            startY = scrollY
        }
        scroller!!.fling(0, startY, 0, vy, 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        scroller!!.computeScrollOffset()
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleVelocityTracker()
        stopScroll()
        childHelper = null
        scroller = null
//        parent = null
    }

    override fun computeScroll() {
        if (scroller!!.computeScrollOffset()) {
            val currY = scroller!!.currY
            if (!isSelfFling) { //parent fling
                scrollTo(0, currY)
                invalidate()
                return
            }
            if (isWebViewCanScroll()) {
                scrollTo(0, currY)
                invalidate()
            }
            if (!hasFling && scroller!!.startY < currY && !canWebViewScrollDown()
                    && startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                    && !dispatchNestedPreFling(0f, scroller!!.currVelocity)) {
                //滑动到底部时，将fling传递给父控件和RecyclerView
                hasFling = true
                dispatchNestedFling(0f, scroller!!.currVelocity, false)
            }
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        var y = y
        if (isParentResetScroll()) {
            if (maxScrollY != 0 && y > maxScrollY) {
                y = maxScrollY
            }
            if (y < 0) {
                y = 0
            }
            super.scrollTo(x, y)

            //用于父控件不是嵌套控件时，绘制进度条，仅此而已
//            if (getParent() !is NestedWebViewRecyclerViewGroup) {
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
//                dispatchNestedScroll(1, 0, 0, 0, null)
//            }
        }
    }

    open fun stopScroll() {
        if (scroller != null && !scroller!!.isFinished) {
            scroller!!.abortAnimation()
        }
    }

    open fun scrollToBottom() {
        val y = computeVerticalScrollRange()
        super.scrollTo(0, y - height)
    }

    private fun initWebViewParent() {
        if (parent != null) {
            return
        }
        var parent: View = getParent() as View
//        while (parent != null) {
//            if (parent is NestedWebViewRecyclerViewGroup) {
//                this.parent = parent as NestedWebViewRecyclerViewGroup
//                break
//            } else {
//                parent = parent.getParent() as View
//            }
//        }
    }

    private fun isParentResetScroll(): Boolean {
        if (parent == null) {
            initWebViewParent()
        }
        return if (parent != null) {
            true
//            parent.getScrollY() === 0
        } else true
    }

    private fun initOrResetVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        } else {
            velocityTracker!!.clear()
        }
    }

    private fun initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker!!.recycle()
            velocityTracker = null
        }
    }

    private fun canWebViewScrollDown(): Boolean {
        val offset = scrollY
        val range = getWebViewContentHeight() - height
        return if (range == 0) {
            false
        } else offset < range - 3
    }

    private fun isWebViewCanScroll(): Boolean {
        val offset = scrollY
        val range = getWebViewContentHeight() - height
        return if (range == 0) {
            false
        } else offset > 0 || offset < range - 3
    }

    private fun getWebViewContentHeight(): Int {
        if (mWebViewContentHeight == 0) {
            mWebViewContentHeight = (contentHeight * density).toInt()
        }
        return mWebViewContentHeight
    }

    private fun getHelper(): NestedScrollingChildHelper {
        if (childHelper == null) {
            childHelper = NestedScrollingChildHelper(this)
        }
        return childHelper!!
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return getHelper().startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        getHelper().stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return getHelper().hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                      dyUnconsumed: Int, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return getHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, @Nullable consumed: IntArray?, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        getHelper().isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return getHelper().isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return getHelper().startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        getHelper().stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return getHelper().hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        getHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                      @Nullable offsetInWindow: IntArray?): Boolean {
        return getHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, @Nullable consumed: IntArray?, @Nullable offsetInWindow: IntArray?): Boolean {
        return getHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return getHelper().dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return getHelper().dispatchNestedPreFling(velocityX, velocityY)
    }
}
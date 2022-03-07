package com.example.module_homepage2.nestwebview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.example.module_homepage2.nestwebview.ProgressWebView

/**
 * 从 NestedScrollWebView2 抽离出复杂的 NestScroll 逻辑
 */
open class WrapNestedScrollWebView4 : ProgressWebView, NestedScrollingChild2, NestedScrollingChild3 {
    private var TAG = "WrapNestedScrollWebView"

    private var mChildHelper: NestedScrollingChildHelper


    /**
     * 消费的滚动
     */
    private var mScrollConsumed: IntArray = IntArray(2)
    private var mScrollOffset = IntArray(2)

    private var mLastMotionY = 0F


    private var mNestedYOffset = 0
    private var mChange = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        mChildHelper = NestedScrollingChildHelper(this)
        // 开启内部嵌套滚动
        isNestedScrollingEnabled = true


        val vc = ViewConfiguration.get(context)
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity
        mMaxFlingVelocity = vc.scaledMaximumFlingVelocity
        mScroller = Scroller(context)
    }

    // =================================== 重写 onTouchEvent
    private var mVelocityTracker: VelocityTracker? = null
    private var fling //判断当前是否是可以进行惯性滑动
            = false
    private var mMinFlingVelocity = 0
    private var mMaxFlingVelocity = 0
    private var mScroller: Scroller? = null
    private var mLastFlingX = 0
    private var mLastFlingY = 0
    private val mFlingConsumed = IntArray(2)


    private fun cancelFling() {
        fling = false
        mLastFlingX = 0
        mLastFlingY = 0
    }

    private fun fling(velocityX: Int, velocityY: Int): Boolean {
        //判断速度是否足够大。如果够大才执行fling
        var velocityXTmp = velocityX
        var velocityYTmp = velocityY
        if (Math.abs(velocityXTmp) < mMinFlingVelocity) {
            velocityXTmp = 0
        }
        if (Math.abs(velocityYTmp) < mMinFlingVelocity) {
            velocityYTmp = 0
        }
        if (velocityXTmp == 0 && velocityYTmp == 0) {
            return false
        }
        //通知父控件，开始进行惯性滑动
//        if (mOrientation == LinearLayout.VERTICAL) {
            //此方法确定开始滑动的方向和类型，为垂直方向，触摸滑动
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
//        } else {
//            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_NON_TOUCH)
//        }
        velocityXTmp = Math.max(-mMaxFlingVelocity, Math.min(velocityXTmp, mMaxFlingVelocity))
        velocityYTmp = Math.max(-mMaxFlingVelocity, Math.min(velocityYTmp, mMaxFlingVelocity))
        //开始惯性滑动
        doFling(velocityXTmp, velocityYTmp)
        return true
    }

    /**
     * 实际的fling处理效果
     */
    private fun doFling(velocityX: Int, velocityY: Int) {
        fling = true
        mScroller!!.fling(0, 0, velocityX, velocityY, Int.MIN_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MAX_VALUE)
        postInvalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset() && fling) {
            val x = mScroller!!.currX
            val y = mScroller!!.currY
            var dx = mLastFlingX - x
            var dy = mLastFlingY - y
            mLastFlingX = x
            mLastFlingY = y
            //在子控件处理fling之前，先判断父控件是否消耗
            if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, null, ViewCompat.TYPE_NON_TOUCH)) {
                //计算父控件消耗后，剩下的距离
                dx -= mScrollConsumed[0]
                dy -= mScrollConsumed[1]
            }
            //因为之前默认向父控件传递的竖直方向，所以这里子控件也消耗剩下的竖直方向
            var hResult = 0
            var vResult = 0
            var leaveDx = 0 //子控件水平fling 消耗的距离
            var leaveDy = 0 //父控件竖直fling 消耗的距离

            //在父控件消耗完之后，子控件开始消耗
            if (dx != 0) {
                leaveDx = childFlingX(dx)
                hResult = dx - leaveDx //得到子控件消耗后剩下的水平距离
            }
            if (dy != 0) {
                leaveDy = childFlingY(dy) //得到子控件消耗后剩下的竖直距离
                scrollTo(0, -dy)
                postInvalidate()
                vResult = dy - leaveDy
            }
            //将最后剩余的部分，再次还给父控件
//            dispatchNestedScroll(leaveDx, leaveDy, hResult, vResult, null, ViewCompat.TYPE_NON_TOUCH)
//            postInvalidate()

            if (mScroller!!.startY < dy && !canWebViewScrollDown()
                    && startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                    && !dispatchNestedPreFling(0f, mScroller!!.currVelocity)) {
                //滑动到底部时，将fling传递给父控件和RecyclerView
//                hasFling = true
                dispatchNestedFling(0f, mScroller!!.currVelocity, false)
            }
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            cancelFling()
        }
    }

    private fun canWebViewScrollDown(): Boolean {
        val offset = scrollY
        val range = getWebViewContentHeight() - height
        return if (range == 0) {
            false
        } else offset < range - 3
    }
    private var mWebViewContentHeight = 0
    private var density = 0f
    private fun getWebViewContentHeight(): Int {
        if(density == 0f){
            density = resources.displayMetrics.density
        }
        if (mWebViewContentHeight == 0) {
            mWebViewContentHeight = (contentHeight * density).toInt()
        }
        return mWebViewContentHeight
    }

    /**
     * 子控件消耗多少竖直方向上的fling,由子控件自己决定
     *
     * @param dy 父控件消耗部分竖直fling后,剩余的距离
     * @return 子控件竖直fling，消耗的距离
     */
    private fun childFlingY(dy: Int): Int {
        return 0
    }

    /**
     * 子控件消耗多少竖直方向上的fling,由子控件自己决定
     *
     * @param dx 父控件消耗部分水平fling后,剩余的距离
     * @return 子控件水平fling，消耗的距离
     */
    private fun childFlingX(dx: Int): Int {
        return 0
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onTouchEvent scrollX=" + event.getY() + ";rawY=" + event.rawY)
        val eventY = event.getY()
        var result = false

        val trackedEvent = MotionEvent.obtain(event);
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }

        event.offsetLocation(0F, mNestedYOffset.toFloat());

        cancelFling() //停止惯性滑动
        //添加速度检测器，用于处理fling效果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = eventY
                // 1.  发起滚动请求
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                mChange = false;
                result = super.onTouchEvent(event);
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = mLastMotionY - eventY;
                // 2. 先询问 NP 是否需要提前消耗滑动距离(部分或者全部)
                if (dispatchNestedPreScroll(0, deltaY.toInt(), mScrollConsumed, mScrollOffset)) {
                    // NP消耗了部分滑动距离
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
                    mNestedYOffset += mScrollOffset[1];
                }

                var oldY = getScrollY();
                mLastMotionY = eventY - mScrollOffset[1];
                var newScrollY = Math.max(0, oldY + deltaY.toInt());
                deltaY -= newScrollY - oldY;
                if (dispatchNestedScroll(
                                0, (newScrollY - deltaY).toInt(), 0,
                                deltaY.toInt(), mScrollOffset
                        )
                ) {
                    mLastMotionY -= mScrollOffset[1];
                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
                    mNestedYOffset += mScrollOffset[1];
                }
                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
                    if (mChange) {
                        mChange = false;
                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(trackedEvent);
                    } else {
                        result = super.onTouchEvent(trackedEvent);
                    }
                    trackedEvent.recycle();
                } else {
                    if (Math.abs(mLastMotionY - eventY) >= 10) {
                        if (!mChange) {
                            mChange = true;
                            super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0F, 0F, 0));
                        }
                    }

                }
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll();
//                result = super.onTouchEvent(event);

                //开始判断是否需要惯性滑动
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaxFlingVelocity.toFloat())
                val xvel = mVelocityTracker!!.xVelocity.toInt()
                val yvel = mVelocityTracker!!.yVelocity.toInt()
                // 3.2 fling，滑动惯性
                fling(xvel, yvel)
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.clear()
                }
//                lastY = -1
//                lastX = -1
            }
        }
        return result
    }

    // =================================== NestedScrolling

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled

    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper.startNestedScroll(axes, type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper.hasNestedScrollingParent(type)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun dispatchNestedScroll(
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed,
                offsetInWindow
        )
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(
            dx: Int,
            dy: Int,
            consumed: IntArray?,
            offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(
            velocityX: Float,
            velocityY: Float,
            consumed: Boolean
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }
}
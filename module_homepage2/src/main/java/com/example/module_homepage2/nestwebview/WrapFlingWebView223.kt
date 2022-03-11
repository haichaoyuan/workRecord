package com.example.module_homepage2.nestwebview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat


/**
 * 从 NestedScrollWebView2 抽离出复杂的 NestScroll 逻辑
 */
open class WrapFlingWebView223 : ProgressWebView, NestedScrollingChild2 {


    private val TAG: String = WrapFlingWebView223::class.java.getSimpleName()


    private val mScrollConsumed = IntArray(2)
    private val mScrollOffset = IntArray(2)

    private var mLastMotionY = 0

    private var mVelocityTracker: VelocityTracker? = null
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0
    private var mScroller: OverScroller? = null
    private var mLastScrollerY = 0


    private var mChildHelper: NestedScrollingChildHelper? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
        mScroller = OverScroller(getContext())
        val configuration = ViewConfiguration.get(getContext())
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity
    }

    private  fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private  fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

     fun fling(velocityY: Int) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        mScroller!!.fling(
            scrollX, scrollY,  // start
            0, velocityY,  // velocities
            0, 0, Int.MIN_VALUE, Int.MAX_VALUE,  // y
            0, 0
        ) // overscroll
        mLastScrollerY = scrollY
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {
            val x = mScroller!!.currX
            val y = mScroller!!.currY
            Log.d(TAG, "computeScroll: y : $y")
            val dy = y - mLastScrollerY
            if (dy != 0) {
                val scrollY = scrollY
                var dyUnConsumed = 0
                var consumedY = dy
                if (scrollY == 0) {
                    dyUnConsumed = dy
                    consumedY = 0
                } else if (scrollY + dy < 0) {
                    dyUnConsumed = dy + scrollY
                    consumedY = -scrollY
                }
                if (!dispatchNestedScroll(
                        0, consumedY, 0, dyUnConsumed, null,
                        ViewCompat.TYPE_NON_TOUCH
                    )
                ) {
                }
            }

            // Finally update the scroll positions and post an invalidation
            mLastScrollerY = y
            ViewCompat.postInvalidateOnAnimation(this)
        } else {
            // We can't scroll any more, so stop any indirect scrolling
            if (hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            }
            // and reset the scroller y
            mLastScrollerY = 0
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        val vtev = MotionEvent.obtain(event)
        val actionMasked = event.action
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = event.rawY.toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                mVelocityTracker!!.addMovement(vtev)
                mScroller!!.computeScrollOffset()
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }
            }
            MotionEvent.ACTION_UP -> {
                val velocityTracker = mVelocityTracker
                velocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = velocityTracker.yVelocity.toInt()
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(-initialVelocity)
                }
                stopNestedScroll()
                recycleVelocityTracker()
            }
            MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll()
                recycleVelocityTracker()
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.rawY.toInt()
                val deltaY = mLastMotionY - y
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    Log.d(
                        TAG,
                        "onTouchEvent: deltaY : " + deltaY + " , mScrollConsumedY : " + mScrollConsumed[1] + " , mScrollOffset : " + mScrollOffset[1]
                    )
                    vtev.offsetLocation(0f, mScrollConsumed[1].toFloat())
                }
                mLastMotionY = y
                val scrollY = scrollY
                var dyUnconsumed = 0
                if (scrollY == 0) {
                    dyUnconsumed = deltaY
                } else if (scrollY + deltaY < 0) {
                    dyUnconsumed = deltaY + scrollY
                    vtev.offsetLocation(0f, -dyUnconsumed.toFloat())
                }
                mVelocityTracker!!.addMovement(vtev)
                val result = super.onTouchEvent(vtev)
                if (dispatchNestedScroll(
                        0,
                        deltaY - dyUnconsumed,
                        0,
                        dyUnconsumed,
                        mScrollOffset
                    )
                ) {
                }
                return result
            }
            else -> {}
        }
        return super.onTouchEvent(vtev)
    }

    // NestedScrollingChild

    // NestedScrollingChild
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper?.setNestedScrollingEnabled(enabled)
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper!!.isNestedScrollingEnabled()
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper!!.startNestedScroll(axes)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper!!.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll() {
        mChildHelper?.stopNestedScroll()
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper?.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper!!.hasNestedScrollingParent()
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper!!.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper!!.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            offsetInWindow
        )
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int
    ): Boolean {
        return mChildHelper!!.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            offsetInWindow, type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(
        dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mChildHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mChildHelper!!.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper!!.dispatchNestedPreFling(velocityX, velocityY)
    }
}
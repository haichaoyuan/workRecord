package com.example.module_homepage2.nestwebview

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.OverScroller
import androidx.core.view.ViewCompat


/**
 * 从 NestedScrollWebView2 抽离出复杂的 NestScroll 逻辑
 */
open class WrapFlingWebView : WrapNestedScrollWebView {
    private var TAG = "WrapFlingWebView"

    private var mVelocityTracker: VelocityTracker? = null
    private var mLastScrollerY = 0

    private var mScroller: OverScroller? = null
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        mScroller = OverScroller(getContext())

        val configuration: ViewConfiguration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    // =================================== 重写 onTouchEvent
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e(TAG, "onTouchEvent scrollX=" + event.getY() + ";rawY=" + event.rawY)
        initVelocityTrackerIfNotExists();
        val trackedEvent = MotionEvent.obtain(event);

        val eventY = event.getY()
        var result = false;
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            super.mNestedYOffset = 0;
        }

        event.offsetLocation(0F, mNestedYOffset.toFloat());

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = eventY

                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                super.mChange = false;
                result = super.onTouchEvent(event);

                mVelocityTracker?.addMovement(trackedEvent);
                mScroller?.computeScrollOffset();
                if (mScroller != null && !mScroller!!.isFinished()) {
                    mScroller?.abortAnimation();
                }
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = mLastMotionY - eventY;

                if (dispatchNestedPreScroll(0, deltaY.toInt(), mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
                    mNestedYOffset += mScrollOffset[1];
                }

                var oldY = getScrollY();
                mLastMotionY = eventY - super.mScrollOffset[1];
                var newScrollY = Math.max(0, oldY + deltaY.toInt());
                deltaY -= newScrollY - oldY;
                if (dispatchNestedScroll(
                        0, (newScrollY - deltaY).toInt(), 0,
                        deltaY.toInt(), super.mScrollOffset
                    )
                ) {
                    mLastMotionY -= super.mScrollOffset[1];
                    trackedEvent.offsetLocation(0F, super.mScrollOffset[1].toFloat());
                    mNestedYOffset += super.mScrollOffset[1];
                }
                if (super.mScrollConsumed[1] == 0 && super.mScrollOffset[1] == 0) {
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


//                mLastMotionY = eventY
//
//                val scrollY = scrollY
//
//                var dyUnconsumed = 0
//                if (scrollY == 0) {
//                    dyUnconsumed = deltaY.toInt()
//                } else if (scrollY + deltaY < 0) {
//                    dyUnconsumed = (deltaY + scrollY).toInt()
//                    trackedEvent.offsetLocation(0f, (-dyUnconsumed).toFloat())
//                }
                mVelocityTracker!!.addMovement(trackedEvent)
//                result = super.onTouchEvent(trackedEvent)
//                if (dispatchNestedScroll(
//                        0,
//                        (deltaY - dyUnconsumed).toInt(), 0, dyUnconsumed, mScrollOffset
//                    )
//                ) {
//                }
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = mVelocityTracker?.yVelocity?.toInt()
                if (initialVelocity != null && Math.abs(initialVelocity) > mMinimumVelocity) {
                    fling(-initialVelocity)
                }
                result = super.onTouchEvent(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll();
                recycleVelocityTracker()
                result = super.onTouchEvent(event)
            }
        }
        return result
    }

    // =================================== mVelocityTracker
    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    open fun fling(velocityY: Int) {
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
}
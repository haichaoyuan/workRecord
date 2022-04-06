package com.example.module_homepage2.nestwebview

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
open class WrapFlingWebView2 : WrapNestedScrollWebView {
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
        Log.e(TAG, "onTouchEvent Y=" + event.getY() + ";rawY=" + event.rawY)
        val eventY = event.getY().toInt()
        var result = false;
        val trackedEvent = MotionEvent.obtain(event);
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }

        event.offsetLocation(0F, mNestedYOffset.toFloat());

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = eventY
                Log.i(TAG, "ACTION_DOWN mLastMotionY:$mLastMotionY")
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)

                mChange = false
                result = super.onTouchEvent(event);

                //
                initVelocityTrackerOrClear()
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaY = mLastMotionY - eventY
                Log.i(TAG, "ACTION_MOVE deltaY:$deltaY")

                // 联合滑动，为 true, 自身滑动为false
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    Log.i(TAG, "ACTION_MOVE mScrollConsumed:${mScrollConsumed[1]}, ${mScrollOffset[1]}")
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
                    mNestedYOffset += mScrollOffset[1];
                }
                // mScrollConsumed 消费的值，mScrollOffset 滑动偏移值，带正负号, 往下为 负数： 11, -11
                Log.i(TAG, "ACTION_MOVE mScrollConsumed:${mScrollConsumed[1]}, ${mScrollOffset[1]}")

                var oldY = getScrollY();
                mLastMotionY = eventY - mScrollOffset[1];
                // 解决，去除负数，否则会滑不下来
                var newScrollY = Math.max(0, oldY + deltaY);
                deltaY -= newScrollY - oldY;
                Log.i(TAG, "ACTION_MOVE (newScrollY - deltaY):${newScrollY - deltaY}, ${deltaY}")
                if (dispatchNestedScroll(0, (newScrollY - deltaY), 0, deltaY, mScrollOffset)) {
                    Log.i(TAG, "ACTION_MOVE mScrollOffset:${mScrollOffset[1]}")
                    mLastMotionY -= mScrollOffset[1];
                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
                    mNestedYOffset += mScrollOffset[1];
                }
                Log.i(TAG, "ACTION_MOVE mScrollOffset:${mScrollOffset[1]}")

                // 网页的内部滑动,
                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
                    // 网页的内部滑动,
                    Log.i(TAG, "ACTION_MOVE mChange")
                    if (mChange) {
                        mChange = false;
                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(trackedEvent);
                    } else {
                        result = super.onTouchEvent(trackedEvent);
                    }

                } else {
                    Log.i(TAG, "mLastMotionY - eventY:${mLastMotionY - eventY}")
                    if (Math.abs(mLastMotionY - eventY) >= 10) {
                        if (!mChange) {
                            mChange = true;
                            super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0F, 0F, 0));
                        }
                    }

                }
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "ACTION_UP ")
                //
                mVelocityTracker?.addMovement(trackedEvent)
                mVelocityTracker?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val initialVelocity = mVelocityTracker!!.getYVelocity()
                Log.i(TAG, "ACTION_UP initialVelocity:$initialVelocity")
                if (Math.abs(initialVelocity) >= mMinimumVelocity) {

                    if (!dispatchNestedPreFling(0f, -initialVelocity)) {
                        dispatchNestedFling(0f, -initialVelocity, true)
                        Log.i(TAG, "ACTION_UP fling:${(-initialVelocity).toInt()}")
                        fling((-initialVelocity).toInt())
                    }
                }
                else if (mScroller!!.springBack(scrollX, scrollY, 0, 0, 0,
                        height)) {
                    Log.i(TAG, "ACTION_UP springBack:$initialVelocity")
                    ViewCompat.postInvalidateOnAnimation(this)
                }

                stopNestedScroll();
                result = super.onTouchEvent(event);
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //
                mVelocityTracker?.clear()

                stopNestedScroll();
                result = super.onTouchEvent(event);
            }
        }

        mVelocityTracker?.addMovement(trackedEvent)
        trackedEvent.recycle();
        return result
    }

    private fun fling(velocityY: Int) {
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
        Log.i(TAG, "computeScroll ")
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {// 判断滑动动画是否已结束
            val x = mScroller!!.currX
            val y = mScroller!!.currY
            val dy = y - mLastScrollerY
            Log.i(TAG, "computeScroll computeScrollOffset y:$y,dy:$dy")
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
                Log.i(TAG, "computeScroll dispatchNestedScroll y:$y,dy:$dy consumedY:$consumedY, dyUnConsumed:$dyUnConsumed")
                if (!dispatchNestedScroll(
                        0, consumedY, 0, dyUnConsumed, null,
                        ViewCompat.TYPE_NON_TOUCH
                    )
                ) {
                }
                Log.i(TAG, "computeScroll dispatchNestedScroll consumedY:$consumedY, dyUnConsumed:$dyUnConsumed")
            }

            // Finally update the scroll positions and post an invalidation
            mLastScrollerY = y
            ViewCompat.postInvalidateOnAnimation(this)
        } else {
            Log.i(TAG, "computeScroll stopNestedScroll")
            // We can't scroll any more, so stop any indirect scrolling
            if (hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            }
            // and reset the scroller y
            mLastScrollerY = 0
        }
    }

    // =================================== mVelocityTracker
    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun initVelocityTrackerOrClear() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        } else {
            mVelocityTracker!!.clear()
        }
    }

    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }


}
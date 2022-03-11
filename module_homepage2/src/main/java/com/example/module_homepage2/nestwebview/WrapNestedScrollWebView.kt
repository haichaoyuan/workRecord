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
open class WrapNestedScrollWebView : ProgressWebView, NestedScrollingChild2, NestedScrollingChild3 {
    private var TAG = "WrapNestedScrollWebView"

    private var mChildHelper: NestedScrollingChildHelper


    /**
     * 消费的滚动
     */
    protected var mScrollConsumed: IntArray = IntArray(2)
    protected var mScrollOffset = IntArray(2)

    protected var mLastMotionY = 0 // onDown 记录，onMove 计算滑动的值


    var mNestedYOffset = 0
    protected var mChange = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        mChildHelper = NestedScrollingChildHelper(this)
        // 开启内部嵌套滚动
        isNestedScrollingEnabled = true
    }

    // =================================== 重写 onTouchEvent
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.e(TAG, "onTouchEvent Y=" + event.getY() + ";rawY=" + event.rawY)
//        val eventY = event.getY().toInt()
//        var result = false;
//        val trackedEvent = MotionEvent.obtain(event);
//        val action = event.action
//        if (action == MotionEvent.ACTION_DOWN) {
//            mNestedYOffset = 0;
//        }
//
//        event.offsetLocation(0F, mNestedYOffset.toFloat());
//
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                mLastMotionY = eventY
//                Log.i(TAG, "ACTION_DOWN mLastMotionY:$mLastMotionY")
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
//
//                mChange = false
//                result = super.onTouchEvent(event);
//            }
//            MotionEvent.ACTION_MOVE -> {
//                var deltaY = mLastMotionY - eventY
//                Log.i(TAG, "ACTION_MOVE deltaY:$deltaY")
//
//                // 联合滑动，为 true, 自身滑动为false
//                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
//                    Log.i(TAG, "ACTION_MOVE mScrollConsumed:${mScrollConsumed[1]}, ${mScrollOffset[1]}")
//                    deltaY -= mScrollConsumed[1];
//                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
//                    mNestedYOffset += mScrollOffset[1];
//                }
//                // mScrollConsumed 消费的值，mScrollOffset 滑动偏移值，带正负号, 往下为 负数： 11, -11
//                Log.i(TAG, "ACTION_MOVE mScrollConsumed:${mScrollConsumed[1]}, ${mScrollOffset[1]}")
//
//                var oldY = getScrollY();
//                mLastMotionY = eventY - mScrollOffset[1];
//                // 解决，去除负数，否则会滑不下来
//                var newScrollY = Math.max(0, oldY + deltaY);
//                deltaY -= newScrollY - oldY;
//                Log.i(TAG, "ACTION_MOVE (newScrollY - deltaY):${newScrollY - deltaY}, ${deltaY}")
//                if (dispatchNestedScroll(0, (newScrollY - deltaY), 0, deltaY, mScrollOffset)) {
//                    Log.i(TAG, "ACTION_MOVE mScrollOffset:${mScrollOffset[1]}")
//                    mLastMotionY -= mScrollOffset[1];
//                    trackedEvent.offsetLocation(0F, mScrollOffset[1].toFloat());
//                    mNestedYOffset += mScrollOffset[1];
//                }
//                Log.i(TAG, "ACTION_MOVE mScrollOffset:${mScrollOffset[1]}")
//
//                // 网页的内部滑动,
//                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
//                    // 网页的内部滑动,
//                    Log.i(TAG, "ACTION_MOVE mChange")
//                    if (mChange) {
//                        mChange = false;
//                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
//                        super.onTouchEvent(trackedEvent);
//                    } else {
//                        result = super.onTouchEvent(trackedEvent);
//                    }
//
//                } else {
//                    Log.i(TAG, "mLastMotionY - eventY:${mLastMotionY - eventY}")
//                    if (Math.abs(mLastMotionY - eventY) >= 10) {
//                        if (!mChange) {
//                            mChange = true;
//                            super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0F, 0F, 0));
//                        }
//                    }
//
//                }
//            }
//            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                stopNestedScroll();
//                result = super.onTouchEvent(event);
//            }
//        }
//        trackedEvent.recycle();
//        return result
//    }

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
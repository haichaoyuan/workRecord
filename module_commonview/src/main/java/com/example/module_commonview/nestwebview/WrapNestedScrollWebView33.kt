package com.shhxzq.stock.ui.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.annotation.Nullable
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.TYPE_TOUCH
import com.example.module_commonview.nestwebview.ProgressWebView


/**
 * 从 NestedScrollWebView2 抽离出复杂的 NestScroll 逻辑
 * sangxiaonian/EasyRefrush
 *
 */
open class WrapNestedScrollWebView33 : ProgressWebView, NestedScrollingChild2, NestedScrollingChild3 {
    private var TAG = "WrapNestedScrollWebView"
    private val mScrollingChildHelper = NestedScrollingChildHelper(this)
    private var mMinFlingVelocity = 0
    private var mMaxFlingVelocity = 0
    private var mScroller: Scroller? = null
    private var lastY = -1
    private var lastX = -1
    private val offset = IntArray(2)
    private val consumed = IntArray(2)
    private var mOrientation = LinearLayout.VERTICAL
    private var fling //判断当前是否是可以进行惯性滑动
            = false


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        isNestedScrollingEnabled = true
        val vc = ViewConfiguration.get(context)
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity
        mMaxFlingVelocity = vc.scaledMaximumFlingVelocity
        mScroller = Scroller(context)
    }




    private var mVelocityTracker: VelocityTracker? = null

    private var mNestedYOffset = 0
    private var mChange = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var result = false
        val action = event.actionMasked

        //new
        val trackedEvent = MotionEvent.obtain(event)


        cancelFling() //停止惯性滑动
        if (lastX == -1 || lastY == -1) {
            lastY = event.rawY.toInt()
            lastX = event.rawX.toInt()
        }

        //添加速度检测器，用于处理fling效果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //1.1 当手指按下,获取坐标
                lastY = event.rawY.toInt()
                lastX = event.rawX.toInt()
                //即将开始滑动，支持垂直方向的滑动
                if (mOrientation == LinearLayout.VERTICAL) {
                    //1.2 调用startNestedScroll，此方法确定开始滑动的方向和类型，为垂直方向，触摸滑动
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_TOUCH)
                } else {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH)
                }

                //new
                mChange = false;
                result = super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                event.offsetLocation(0F, mNestedYOffset.toFloat())

                val currentY = event.rawY.toInt()
                val currentX = event.rawX.toInt()
                // 2.1 获取差值
                var dy = lastY - currentY
                var dx = lastX - currentX
                // 2.2 dispatchNestedPreScroll ，先让父控件消耗一部分滑动
                //即将开始滑动，在开始滑动前，先通知父控件，确认父控件是否需要先消耗一部分滑动 true 表示需要先消耗一部分
                if (dispatchNestedPreScroll(dx, dy, consumed, offset, TYPE_TOUCH)) {
                    //如果父控件需要消耗，则处理父控件消耗的部分数据
                    dy -= consumed[1]
                    dx -= consumed[0]

                    //new
                    trackedEvent.offsetLocation(0F, consumed[1].toFloat());
                    mNestedYOffset += offset[1]
                }
                //剩余的自己再次消耗，
                var consumedX = 0
                var consumedY = 0
                if (mOrientation == LinearLayout.VERTICAL) {
                    consumedY = childConsumedY(dy)
                } else {
                    consumedX = childConsumeX(dx)
                }

                //new
                var oldY = getScrollY();
                lastY = currentY - offset[1];
                var newScrollY = Math.max(0, oldY + dy.toInt());
                dy -= newScrollY - oldY;
                // 2.3 dispatchNestedScroll，子控件消耗完，再次通知父控件
                //子控件的滑动事件处理完成之后，剩余的再次传递给父控件，让父控件进行消耗
                //因为没有滑动事件，因此次数自己滑动距离为0，剩余的再次全部还给父控件
//                if(dispatchNestedScroll(consumedX, consumedY, dx - consumedX, dy - consumedY, null, TYPE_TOUCH)){
                if(dispatchNestedScroll(consumedX, newScrollY - dy, dx - consumedX, dy, offset, TYPE_TOUCH)){
                    lastY -= offset[1];
                    trackedEvent.offsetLocation(0F, offset[1].toFloat());
                    mNestedYOffset += offset[1];
                }
                if(consumed[1] == 0 && offset[1] == 0) {
                    if (mChange) {
                        mChange = false;
                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(trackedEvent);
                    } else {
                        result = super.onTouchEvent(trackedEvent);
                    }
                    trackedEvent.recycle();
                } else {
                    if (Math.abs(lastY - currentY) >= 10) {
                        if (!mChange) {
                            mChange = true;
                            super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0F, 0F, 0));
                        }
                    }
                }
                //new del
//                lastY = currentY
//                lastX = currentX
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //3.1 stopNestedScroll，触摸滑动停止
                stopNestedScroll(TYPE_TOUCH)
                result = super.onTouchEvent(event)

                //开始判断是否需要惯性滑动
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaxFlingVelocity.toFloat())
                val xvel = mVelocityTracker!!.xVelocity.toInt()
                val yvel = mVelocityTracker!!.yVelocity.toInt()
                // 3.2 fling，滑动惯性
                fling(xvel, yvel)
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.clear()
                }
                lastY = -1
                lastX = -1
            }
        }
        return result
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
        if (mOrientation == LinearLayout.VERTICAL) {
            //此方法确定开始滑动的方向和类型，为垂直方向，触摸滑动
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
        } else {
            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, ViewCompat.TYPE_NON_TOUCH)
        }
        velocityXTmp = Math.max(-mMaxFlingVelocity, Math.min(velocityXTmp, mMaxFlingVelocity))
        velocityYTmp = Math.max(-mMaxFlingVelocity, Math.min(velocityYTmp, mMaxFlingVelocity))
        //开始惯性滑动
        doFling(velocityXTmp, velocityYTmp)
        return true
    }

    private var mLastFlingX = 0
    private var mLastFlingY = 0
    private val mScrollConsumed = IntArray(2)

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
                vResult = dy - leaveDy
            }
            //将最后剩余的部分，再次还给父控件
            dispatchNestedScroll(leaveDx, leaveDy, hResult, vResult, null, ViewCompat.TYPE_NON_TOUCH)
            postInvalidate()
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            cancelFling()
        }
    }

    private fun cancelFling() {
        fling = false
        mLastFlingX = 0
        mLastFlingY = 0
    }


    /**
     * 判断子子控件是否能够滑动，只有能滑动才能处理fling
     */
    private fun canScroll(): Boolean {
        //具体逻辑自己实现
        return true
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

    /**
     * 触摸滑动时候子控件消耗多少竖直方向上的 ,由子控件自己决定
     *
     * @param dy 父控件消耗部分竖直fling后,剩余的距离
     * @return 子控件竖直fling，消耗的距离
     */
    private fun childConsumedY(dy: Int): Int {
        super.scrollBy(0, dy)

        return 0
    }

    /**
     * 触摸滑动子控件消耗多少竖直方向上的,由子控件自己决定
     *
     * @param dx 父控件消耗部分水平fling后,剩余的距离
     * @return 子控件水平fling，消耗的距离
     */
    private fun childConsumeX(dx: Int): Int {
        return 0
    }


    // =================================== NestedScrolling


    /**
     * 开始滑动前调用，在惯性滑动和触摸滑动前都会进行调用，此方法一般在 onInterceptTouchEvent或者onTouch中，通知父类方法开始滑动
     * 会调用父类方法的 onStartNestedScroll onNestedScrollAccepted 两个方法
     *
     * @param axes 滑动方向
     * @param type 开始滑动的类型 the type of input which cause this scroll event
     * @return 有父视图并且开始滑动，则返回true 实际上就是看parent的 onStartNestedScroll 方法
     */
    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mScrollingChildHelper.startNestedScroll(axes, type)
    }


    /**
     * 子控件在开始滑动前，通知父控件开始滑动，同时由父控件先消耗滑动时间
     * 在子View的onInterceptTouchEvent或者onTouch中，调用该方法通知父View滑动的距离
     * 最终会调用父view的 onNestedPreScroll 方法
     *
     * @param dx             水平方向嵌套滑动的子控件想要变化的距离 dx<0 向右滑动 dx>0 向左滑动 （保持和 RecycleView 一致）
     * @param dy             垂直方向嵌套滑动的子控件想要变化的距离 dy<0 向下滑动 dy>0 向上滑动 （保持和 RecycleView 一致）
     * @param consumed       父控件消耗的距离，父控件消耗完成之后，剩余的才会给子控件，子控件需要使用consumed来进行实际滑动距离的处理
     * @param offsetInWindow 子控件在当前window的偏移量
     * @param type           滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     * @return true    表示父控件进行了滑动消耗，需要处理 consumed 的值，false表示父控件不对滑动距离进行消耗，可以不考虑consumed数据的处理，此时consumed中两个数据都应该为0
     */
    override fun dispatchNestedPreScroll(dx: Int, dy: Int, @Nullable consumed: IntArray?, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }


    /**
     * 在dispatchNestedPreScroll 之后进行调用
     * 当滑动的距离父控件消耗后，父控件将剩余的距离再次交个子控件，
     * 子控件再次消耗部分距离后，又继续将剩余的距离分发给父控件,由父控件判断是否消耗剩下的距离。
     * 如果四个消耗的距离都是0，则表示没有神可以消耗的了，会直接返回false，否则会调用父控件的
     * onNestedScroll 方法，父控件继续消耗剩余的距离
     * 会调用父控件的
     *
     * @param dxConsumed     水平方向嵌套滑动的子控件滑动的距离(消耗的距离)    dx<0 向右滑动 dx>0 向左滑动 （保持和 RecycleView 一致）
     * @param dyConsumed     垂直方向嵌套滑动的子控件滑动的距离(消耗的距离)    dy<0 向下滑动 dy>0 向上滑动 （保持和 RecycleView 一致）
     * @param dxUnconsumed   水平方向嵌套滑动的子控件未滑动的距离(未消耗的距离)dx<0 向右滑动 dx>0 向左滑动 （保持和 RecycleView 一致）
     * @param dyUnconsumed   垂直方向嵌套滑动的子控件未滑动的距离(未消耗的距离)dy<0 向下滑动 dy>0 向上滑动 （保持和 RecycleView 一致）
     * @param offsetInWindow 子控件在当前window的偏移量
     * @return 如果返回true, 表示父控件又继续消耗了
     */
    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    /**
     * 子控件停止滑动，例如手指抬起，惯性滑动结束
     *
     * @param type 停止滑动的类型 TYPE_TOUCH，TYPE_NON_TOUCH
     */
    override fun stopNestedScroll(type: Int) {
        mScrollingChildHelper.stopNestedScroll(type)
    }


    /**
     * 设置当前子控件是否支持嵌套滑动，如果不支持，那么父控件是不能够响应嵌套滑动的
     *
     * @param enabled true 支持
     */
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    /**
     * 当前子控件是否支持嵌套滑动
     */
    override fun isNestedScrollingEnabled(): Boolean {
        return mScrollingChildHelper.isNestedScrollingEnabled
    }

    /**
     * 判断当前子控件是否拥有嵌套滑动的父控件
     */
    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mScrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int, consumed: IntArray) {
        mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
    }
}
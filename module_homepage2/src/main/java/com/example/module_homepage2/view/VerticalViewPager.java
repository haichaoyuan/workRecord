package com.example.module_homepage2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 上下滚动的 ViewPage
 */
public class VerticalViewPager extends ViewPager {

    boolean isAcceptTouch = false; //是否接受点击事件

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 最重要的设置，将viewpager翻转
        setPageTransformer(true, new VerticalPageTransformer());
        // 设置去掉滑到最左或最右时的滑动效果
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // 当前页的上一页
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // 抵消默认幻灯片过渡
                view.setTranslationX(view.getWidth() * -position);

                //设置从上滑动到Y位置
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // 当前页的下一页
                view.setAlpha(0);
            }
        }
    }

    /**
     * 交换触摸事件的X和Y坐标
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!isAcceptTouch){
            return false;
        }
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                getParent().requestDisallowInterceptTouchEvent(true); //父布局不要拦截此事件
                break;
            }
            case MotionEvent.ACTION_MOVE: {
//                if(父容器需要拦截的事件){
//                    parent.requestDisallowInterceptTouchEvent(false); //父布局需要要拦截此事件
//                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }
        return intercepted; //为所有子视图返回触摸的原始坐标
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!isAcceptTouch){
            return false;
        }
        return super.onTouchEvent(swapXY(ev));
    }
}
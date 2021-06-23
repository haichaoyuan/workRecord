package com.shhxzq.ztb.ui.home.ui.listener

import com.google.android.material.appbar.AppBarLayout

/**
 *
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    companion object {
        const val STATE_EXPANDED = 1
        const val STATE_COLLAPSED = 2
        const val STATE_IDLE = 3
    }

    private var mCurrentState = STATE_IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        onAppBarOffsetChanged(appBarLayout, verticalOffset)
        if(verticalOffset == 0){
            if(mCurrentState != STATE_EXPANDED){
                onStateChanged(appBarLayout, STATE_EXPANDED)
            }
            mCurrentState = STATE_EXPANDED
        } else if(Math.abs(verticalOffset) >= appBarLayout.totalScrollRange){
            if(mCurrentState != STATE_COLLAPSED){
                onStateChanged(appBarLayout, STATE_COLLAPSED)
            }
            mCurrentState = STATE_COLLAPSED
        } else {
            if(mCurrentState != STATE_IDLE){
                onStateChanged(appBarLayout, STATE_IDLE)
            }
            mCurrentState = STATE_IDLE
        }
    }

    // 状态发送改变
    abstract fun onStateChanged(appBarLayout: AppBarLayout, state:Int)

    // 发生偏移
    abstract fun onAppBarOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int)
}
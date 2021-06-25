package com.example.module_homepage2

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.module_homepage2.base.UIUtils
import com.example.module_homepage2.xtablayout.XTabLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.shhxzq.ztb.ui.home.ui.adapter.HomeFragmentViewPageAdapter
import com.shhxzq.ztb.ui.home.ui.helper.HomeHandler
import com.shhxzq.ztb.ui.home.ui.helper.HomePageHandler
import com.shhxzq.ztb.ui.home.ui.listener.AppBarStateChangeListener
import com.shhxzq.ztb.ui.home.ui.view.HomeMsgListFragment
import kotlinx.android.synthetic.main.actionbar_home_customized.*
import kotlinx.android.synthetic.main.activity_home_page2.*

class HomePage2Activity : AppCompatActivity() {
    private val CHANNELS = arrayOf("要闻", "快讯", "自选", "服务")
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    var oldverticalOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setTranslucentStatusEnable()
        setContentView(R.layout.activity_home_page2)

        initView()
    }

    private fun initView() {
        // actionBar 调节高度
        if (!UIUtils.hasLollipop()) {
            home_titlebar?.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelOffset(R.dimen.titlebar_statusBar_height2)
            )
            home_titlebar?.setPadding(
                resources.getDimensionPixelOffset(R.dimen.padding_page), 0,
                resources.getDimensionPixelOffset(R.dimen.home_msg_margin), 0
            )
        }

        initTabLayout()
        initViewPager()
        // 解决 AppBarLayout 过大，存在无法滑动的情况
        appbar.post {
            val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = layoutParams.behavior as AppBarLayout.Behavior
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })
        }
        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onAppBarOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                //获取滑动进度
                val height = top_title.measuredHeight.toFloat()
                var alpha = Math.abs(verticalOffset) / height
                if (alpha > 1) {
                    alpha = 1f
                }
                alpha = 1 - alpha
                Log.e(
                    "home2Fragment",
                    "height:$height,verticalOffset:$verticalOffset, alpha: $alpha "
                )
                home_top_bg.alpha = alpha

                val delta = -(oldverticalOffset - verticalOffset)
                home_top_bg.y += delta
                oldverticalOffset = verticalOffset
            }

            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int) {
//                Log.e("home2Fragment", "state:$state")
//                if (state == STATE_COLLAPSED) {
//                    //下面的方法会出现监听不到的情况，所以这里变成折叠状态，最好再设置一次透明度
////                    setLeftTitleAlpha(255f);
//                    top_title.visibility = View.GONE
//                } else if (state == STATE_IDLE) {
//                    top_title.visibility = View.VISIBLE
//                }
            }
        })

    }

    private fun initTabLayout() {
        for (i in CHANNELS.indices) {
            tab_layout.addTab(tab_layout.newTab().setText(CHANNELS[i]), i == CHANNELS.size - 1)
        }
        tab_layout!!.tabMode = TabLayout.MODE_FIXED
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.addOnTabSelectedListener(object : XTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: XTabLayout.Tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                val position = tab.position
                viewPager.currentItem = position
            }

            override fun onTabUnselected(tab: XTabLayout.Tab) {}
            override fun onTabReselected(tab: XTabLayout.Tab) {}
        })
        tab_layout.post {
//          解决默认未设置新的字体大小
            tab_layout.getTabAt(0)?.select()
        }
    }

    val handler = HomePageHandler { msg, handler ->
        val position = msg?.arg1?:0
        viewPager.currentItem = position
        true
    }


    private fun initViewPager() {
        //主页
        val homeMsgListFragment = HomeMsgListFragment(1)
        val homeMsgListFragment2 = HomeMsgListFragment(2)
        val homeMsgListFragment3 = HomeMsgListFragment(3)
        val homeMsgListFragment4 = HomeMsgListFragment(4)
        fragmentList.add(homeMsgListFragment)
        fragmentList.add(homeMsgListFragment2)
        fragmentList.add(homeMsgListFragment3)
        fragmentList.add(homeMsgListFragment4)
        val fragmentPageAdapter = HomeFragmentViewPageAdapter(
            this, fragmentList
        )
        viewPager.adapter = fragmentPageAdapter
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val tabAt: XTabLayout.Tab? = tab_layout.getTabAt(position)
                if (tabAt != null && !tabAt.isSelected) {
                    tabAt.select()
                }
            }
        })
    }

    // =========================================================================================
    // =================================== 生命周期
    // =========================================================================================
    override fun onResume() {
        super.onResume()
        home_header_view.executeCallback()
        home_header_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        home_header_view.onPause()
    }

    override fun onDestroy() {
        home_header_view.removeCallback()
        super.onDestroy()
    }

    // =========================================================================================
    // =================================== super
    // =========================================================================================
    private fun setTranslucentStatusEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}
package com.example.module_homepage2

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.module_homepage2.base.UIUtils
import com.example.module_homepage2.nestwebview.NestedScrollWebViewFragment
import com.example.module_homepage2.xtablayout.XTabLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.shhxzq.ztb.ui.home.ui.adapter.HomeFragmentViewPageAdapter
import com.shhxzq.ztb.ui.home.ui.helper.HomeHandler
import com.shhxzq.ztb.ui.home.ui.listener.AppBarStateChangeListener
import com.shhxzq.ztb.ui.home.ui.view.HomeMsgListFragment
import kotlinx.android.synthetic.main.actionbar_home_customized.*
import kotlinx.android.synthetic.main.activity_home_page2.*

class HomePage2Activity : AppCompatActivity() {
    private val CHANNELS = arrayOf("要闻", "快讯", "自选", "服务")
    private val fragmentList: SparseArray<Fragment> = SparseArray()
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

    val handler = HomeHandler { msg, handler ->
        val position = msg?.arg1 ?: 0
        viewPager.currentItem = position
        true
    }

    /**
     * HomeFragmentViewPageAdapter 的 createFragment
     */
    private fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = fragmentList[position]
        if (fragment == null) {
            fragment = when (position) {
                //要闻
                0 -> getFragment("https://b2b-api.10jqka.com.cn/b2bgw/resource/h5/private/YXZQ/latest/UpLimitSTD/UpLimitSTD/index.html#/ZhangTing?ckey=620CBE220016")
                //快讯
                1 -> getFragment("https://h5-ztb-uat.yongxingsec.com:10443/h5/news/v2/headlines/headlines.html?deviceId=AcBsespatA26IDRyMx4CVR1663605301&v=6.0.0&snsAccount=SNS20211105257695182520599114&time=2022030709&deviceType=2&hasActive=1")
                2 -> getFragment("https://h5-ztb-uat.yongxingsec.com:10443/h5/news/v2/flash/flash.html?deviceId=AcBsespatA26IDRyMx4CVR1663605301&v=6.0.0&snsAccount=SNS20211105257695182520599114&time=2022030707&deviceType=2&hasActive=1")
                else -> HomeMsgListFragment(1)
            }
        }
        return fragment
    }

    private fun getFragment(detailUrl: String): Fragment {
        val mBundle = Bundle()
        val homeFragment = NestedScrollWebViewFragment()
        mBundle.putString(NestedScrollWebViewFragment.PARAMS_URL, detailUrl)
        homeFragment.arguments = mBundle
        return homeFragment
    }

    private fun initViewPager() {
        fragmentList.clear()
        val fragmentPageAdapter = HomeFragmentViewPageAdapter(
            this.supportFragmentManager, CHANNELS.size, this::createFragment
        )
        viewPager.adapter = fragmentPageAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val tabAt: XTabLayout.Tab? = tab_layout.getTabAt(position)
                if (tabAt != null && !tabAt.isSelected) {
                    tabAt.select()
                }
            }


            override fun onPageScrollStateChanged(state: Int) {
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
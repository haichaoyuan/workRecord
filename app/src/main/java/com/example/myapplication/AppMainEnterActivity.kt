package com.example.myapplication

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_util.utils.jump.ActivityJumpHelper
import com.example.lib_util.utils.jump.ModuleMenu
import com.example.module_homepage2.AllServiceActivity
import com.example.module_homepage2.viewmodel.AppMainEnterViewModel
import com.example.module_homepage2.xtablayout.XTabLayout
import com.example.myapplication.adapter.AllServiceAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_app_main_enter.*

class AppMainEnterActivity : FragmentActivity() {
    private var mSmoothScroller: RecyclerView.SmoothScroller? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null

    private val model: AppMainEnterViewModel by viewModels()

    private var isFirstTabSelect = false
    private var isFirstTabSelect2 = false
    /**
     * 是否处于滚动状态，避免连锁反应
     */
    private var isScroll: Boolean = false
    private var isStartSmoothScroll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_app_main_enter)

        model.init(this)
        // 2. tab 列表
        initTabList()

        // 3. 底部网格
        initBottomGrid()
    }

    /**
     *  tab 列表
     */
    private fun initTabList() {
        for (i in model.CHANNELS.indices) {
            tab_layout.addTab(
                tab_layout.newTab().setText(model.CHANNELS[i]), i == model.CHANNELS.size - 1
            )
        }
        tab_layout!!.tabMode = TabLayout.MODE_SCROLLABLE
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, com.example.module_homepage2.R.color.color_3B4B87))
        tab_layout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, com.example.module_homepage2.R.color.color_3B4B87))
        tab_layout.addOnTabSelectedListener(object : XTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: XTabLayout.Tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                val position = tab.position

                if (!isScroll) {
                    Log.e(AllServiceActivity.TAG, "mSmoothScroller,$position")
                    isStartSmoothScroll = true
                    mSmoothScroller?.targetPosition = position
                    mLinearLayoutManager?.startSmoothScroll(mSmoothScroller)
                    Log.e(AllServiceActivity.TAG, "mSmoothScroller,$position end")

                    if(!isFirstTabSelect){ //剔除第一次
                        isFirstTabSelect = !isFirstTabSelect
                        return
                    } else {
                        // 收缩顶部热门推荐
                        appbar.setExpanded(false)
                    }
                }
            }

            override fun onTabUnselected(tab: XTabLayout.Tab) {}
            override fun onTabReselected(tab: XTabLayout.Tab) {}
        })
        tab_layout.post {
            //          解决默认未设置新的字体大小
            tab_layout.getTabAt(0)?.select()
        }
    }

    /**
     * 底部网格
     */
    private fun initBottomGrid() {
        if(model.allServicesEntity == null){
            return
        }
        mSmoothScroller = object:LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                // 定位的view 出现在第一个
                return SNAP_TO_START
            }
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 40F / displayMetrics.densityDpi
            }
        }
        mLinearLayoutManager = LinearLayoutManager(this)
        recycler.layoutManager = mLinearLayoutManager

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e(AllServiceActivity.TAG, "newState:$newState")
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//开始拖动
                    isStartSmoothScroll = false
                    isScroll = true
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) { //非滚动
                    isScroll = false
                } else {
                    isScroll = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isFirstTabSelect2){ //剔除第一次
                    isFirstTabSelect2 = !isFirstTabSelect2
                    return
                }
                Log.e(AllServiceActivity.TAG, "dy:$dy")
                if (isStartSmoothScroll) {
                    return
                }
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val tabAt: XTabLayout.Tab? =
                    tab_layout!!.getTabAt(layoutManager!!.findFirstVisibleItemPosition())
                if (tabAt != null && !tabAt.isSelected) {
                    tabAt.select()
                }
                Log.e(AllServiceActivity.TAG, "dy getTabAt")
            }
        })
        var allServiceAdapter = AllServiceAdapter(model.allServicesEntity!!.result, recycler)
        allServiceAdapter.onItemClickListener = View.OnClickListener {
            if (it.tag != null && it.tag is ModuleMenu) {
                val menuRes = it.tag as ModuleMenu
                ActivityJumpHelper.menuClick(this, menuRes)
            }
        }
        recycler.adapter = allServiceAdapter
    }

    private fun setWindowStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.example.module_homepage2.R.color.titlebar_bg_color)
        }
    }
}
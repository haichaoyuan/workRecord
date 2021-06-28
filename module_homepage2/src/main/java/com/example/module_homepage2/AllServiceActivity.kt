package com.example.module_homepage2

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.module_homepage2.adapter.AllServiceAdapter
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.listener.HomeMenuItemClickListener
import com.example.module_homepage2.viewmodel.AllServicesViewModel
import com.example.module_homepage2.xtablayout.XTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_all_service.*
import kotlinx.android.synthetic.main.activity_all_service.tab_layout
import kotlinx.android.synthetic.main.activity_home_page2.*

class AllServiceActivity : FragmentActivity() {
    companion object {
        const val TAG = "AllServiceActivity"
    }
    private lateinit var menuAdapter: HomeMenu2Adapter
    private var mSmoothScroller: RecyclerView.SmoothScroller? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null

    private val model: AllServicesViewModel by viewModels()

    /**
     * 是否处于滚动状态，避免连锁反应
     */
    private var isScroll = false
    private var isStartSmoothScroll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setWindowStatusBarColor()
        setContentView(R.layout.activity_all_service)

        btn_left.setOnClickListener {
            finish()
        }
        tv_title.text = "全部服务"

        model.init(this)
        // 1.九宫格
        initNineCell()

        // 2. tab 列表
        initTabList()

        // 3. 底部网格
        initBottomGrid()

    }

    /**
     * 九宫格
     */
    private fun initNineCell() {
        menuAdapter = HomeMenu2Adapter(this, model.hotMenus)
        all_service_gridview!!.numColumns = 5
        all_service_gridview!!.adapter = menuAdapter
        menuAdapter.setHomeMenuItemClickListener(object : HomeMenuItemClickListener {
            override fun onItemClick(appMenuRes: AppMenuRes) {
                when (appMenuRes.targetUrl) {
                    AppMenuRes.TARGET_MORE.toString() -> startActivity(
                        Intent(
                            this@AllServiceActivity, AllServiceActivity::class.java
                        )
                    )
                }
            }
        })
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
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.addOnTabSelectedListener(object : XTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: XTabLayout.Tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                val position = tab.position
                if (!isScroll) {
                    Log.e(TAG, "mSmoothScroller,$position")
                    isStartSmoothScroll = true
                    mSmoothScroller?.targetPosition = position
                    mLinearLayoutManager?.startSmoothScroll(mSmoothScroller)
                    Log.e(TAG, "mSmoothScroller,$position end")
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

        recycler.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e(TAG, "newState:$newState")
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //非滚动
                    isScroll = false
                    isStartSmoothScroll = false
                } else {
                    isScroll = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e(TAG, "dy:$dy")
                if(isStartSmoothScroll){
                    return
                }
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val tabAt: XTabLayout.Tab? = tab_layout!!.getTabAt(layoutManager!!.findFirstVisibleItemPosition())
                if (tabAt != null && !tabAt.isSelected) {
                    tabAt.select()
                }
                Log.e(TAG, "dy getTabAt")
            }
        })
        recycler.adapter = AllServiceAdapter(model.allServicesEntity!!.result, recycler)
    }


    // =========================================================================================
    // =================================== super
    // =========================================================================================
    private fun setWindowStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.titlebar_bg_color)
        }
    }
}
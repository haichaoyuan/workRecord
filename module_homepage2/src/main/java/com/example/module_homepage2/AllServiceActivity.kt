package com.example.module_homepage2

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.listener.HomeMenuItemClickListener
import com.example.module_homepage2.xtablayout.XTabLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_all_service.*
import kotlinx.android.synthetic.main.activity_all_service.tab_layout
import kotlinx.android.synthetic.main.activity_home_page2.*

class AllServiceActivity : FragmentActivity() {
    private val CHANNELS = arrayOf("行情", "交易", "理财", "特色数据", "特色服务", "业务办理", "资讯")
    private lateinit var menuAdapter: HomeMenu2Adapter
    private lateinit var defaultMenus: List<AppMenuRes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setWindowStatusBarColor()
        setContentView(R.layout.activity_all_service)

        btn_left.setOnClickListener {
            finish()
        }
        tv_title.text = "全部服务"


        // 九宫格
        defaultMenus = initMenus()
        menuAdapter = HomeMenu2Adapter(this, defaultMenus)
        all_service_gridview!!.numColumns = 5
        all_service_gridview!!.adapter = menuAdapter
        menuAdapter.setHomeMenuItemClickListener(object : HomeMenuItemClickListener {
            override fun onItemClick(appMenuRes: AppMenuRes) {
                when (appMenuRes.targetUrl) {
                    AppMenuRes.TARGET_MORE.toString() -> startActivity(
                        Intent(
                            this@AllServiceActivity,
                            AllServiceActivity::class.java
                        )
                    )
                }
            }
        })

        // tab 列表
        for (i in CHANNELS.indices) {
            tab_layout.addTab(tab_layout.newTab().setText(CHANNELS[i]), i == CHANNELS.size - 1)
        }
        tab_layout!!.tabMode = TabLayout.MODE_SCROLLABLE
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.setTabTextColors(Color.BLACK, ContextCompat.getColor(this, R.color.color_3B4B87))
        tab_layout.addOnTabSelectedListener(object : XTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: XTabLayout.Tab) {
                //点击tab的时候，RecyclerView自动滑到该tab对应的item位置
                val position = tab.position
            }

            override fun onTabUnselected(tab: XTabLayout.Tab) {}
            override fun onTabReselected(tab: XTabLayout.Tab) {}
        })
        tab_layout.post {
//          解决默认未设置新的字体大小
            tab_layout.getTabAt(0)?.select()
        }
    }

    private fun initMenus(): MutableList<AppMenuRes> {
        val appMenuRes: MutableList<AppMenuRes> = ArrayList() // 自选股
        val res1 = AppMenuRes()
        res1.menuName = getString(R.string.home_menu_mystock)
        res1.menuImageUrl = R.mipmap.icon_self_select_stock.toString()
        res1.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res1.targetUrl = AppMenuRes.TARGET_MYSTOCK.toString()
        res1.isSimulated = true // 投顾
        val res12 = AppMenuRes()
        res12.menuName = getString(R.string.home_investment_advisor)
        res12.menuImageUrl = R.mipmap.icon_investment_advisor.toString()
        res12.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res12.targetUrl = AppMenuRes.TARGET_SD_TG.toString()
        res12.isSimulated = true // 新股IPO
        val res2 = AppMenuRes()
        res2.menuName = getString(R.string.home_info_ipo)
        res2.menuImageUrl = R.mipmap.icon_ipo.toString()
        res2.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res2.targetUrl =
            AppMenuRes.TARGET_IPO.toString() //            "XJBDataStoreRemote.BASE_H5_NEW_URL" + "dataCenter/newStockIPO.html"
        res2.isSimulated = true //模拟炒股
        val res3 = AppMenuRes()
        res3.menuName = getString(R.string.home_menu_simulate_stock_speculation)
        res3.menuImageUrl = R.mipmap.icon_simulate_stock_speculation.toString()
        res3.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res3.targetUrl = AppMenuRes.TARGET_SIMULATE.toString()
        res3.isSimulated = true //热门板块
        val res4 = AppMenuRes()
        res4.menuName = getString(R.string.home_menu_hotblock)
        res4.menuImageUrl = R.mipmap.icon_hot_plate.toString()
        res4.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res4.targetUrl = AppMenuRes.TARGET_HOTBLOCK.toString()
        res4.isSimulated = true // 线上开户
        val res5 = AppMenuRes()
        res5.menuName = getString(R.string.home_menu_icon_online_account_opening)
        res5.menuImageUrl = R.mipmap.icon_online_account_opening.toString()
        res5.menuType = AppMenuRes.MENU_TYPE_H5
        res5.targetUrl = AppMenuRes.TARGET_OA.toString()
        res5.isSimulated = true //普通交易
        val res6 = AppMenuRes()
        res6.menuName = getString(R.string.home_menu_normal_trade)
        res6.menuImageUrl = R.mipmap.icon_normal_transaction.toString()
        res6.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res6.targetUrl = AppMenuRes.TARGET_TRADE.toString()
        res6.isSimulated = true //科创板开通
        val res7 = AppMenuRes()
        res7.menuName = getString(R.string.home_menu_open_science_technology)
        res7.menuImageUrl = R.mipmap.icon_open_science_technology_board.toString()
        res7.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res7.targetUrl = AppMenuRes.TARGET_OPEN_SCIENCE_TECHNOLOGY.toString()
        res7.isSimulated = true //创业板开通
        val res8 = AppMenuRes()
        res8.menuName = getString(R.string.home_menu_open_second_board)
        res8.menuImageUrl = R.mipmap.icon_open_second_board.toString()
        res8.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res8.targetUrl = AppMenuRes.TARGET_OPEN_SECOND_BOARD.toString()
        res8.isSimulated = true // 更多
        val res9 = AppMenuRes()
        res9.menuName = getString(R.string.home_menu_more)
        res9.menuImageUrl = R.mipmap.icon_more.toString()
        res9.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res9.targetUrl = AppMenuRes.TARGET_MORE.toString()
        res9.isSimulated = true
        appMenuRes.add(res1)
        appMenuRes.add(res12)
        appMenuRes.add(res2)
        appMenuRes.add(res3)
        appMenuRes.add(res4)
        appMenuRes.add(res5)
        appMenuRes.add(res6)
        appMenuRes.add(res7)
        appMenuRes.add(res8)
        appMenuRes.add(res9)
        return appMenuRes
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
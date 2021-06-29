package com.example.module_homepage2.view;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.module_homepage2.AllServiceActivity
import com.example.module_homepage2.R
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.base.*
import com.example.module_homepage2.listener.HomeMenuItemClickListener
import com.shhxzq.ztb.ui.home.ui.adapter.StockIndexPageAdapter
import com.shhxzq.ztb.ui.home.ui.helper.HomeHandler

/**
 * Des:首页顶部， 2.0 版本
 */
class HomeHeaderView2 : BaseCustomView {
    var homeGridview: MyGridView? = null //九宫格
    var vpAd: ViewPager? = null
    var vpStock: ViewPager? = null //上滚指数
    var bannerLayout: RelativeLayout? = null
    var pointGroup: LinearLayout? = null

    private var menuAdapter: HomeMenu2Adapter? = null
    private var stockIndexPageAdapter //指数 viewpager
            : StockIndexPageAdapter? = null

    // =================================== 计时器
    private val TIME_INTERVAL: Long = 1000
    private var index: Long = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun setAttrs(attrs: AttributeSet) {}
    override fun initView(inflater: LayoutInflater) {
        val view = inflater.inflate(R.layout.custom_home_top2, this, true)
        homeGridview = view.findViewById(R.id.home_gridview)
        vpAd = view.findViewById(R.id.vp_ad)
        vpStock = view.findViewById(R.id.vp_stock)
        bannerLayout = view.findViewById(R.id.banner_layout)
        pointGroup =
            view.findViewById(R.id.indicator) //        hsIndexView = view.findViewById(R.id.hsIndexView)

        initDefault()
    }

    private fun initDefault() { //设置广告栏
        initAd()

        // 九宫格
        initMenus()
        menuAdapter = HomeMenu2Adapter(context, defaultMenus)
        homeGridview!!.numColumns = 5
        homeGridview!!.adapter = menuAdapter
        menuAdapter!!.setHomeMenuItemClickListener(object : HomeMenuItemClickListener {
            override fun onItemClick(appMenuRes: AppMenuRes) {
                when (appMenuRes.targetUrl) {
                    AppMenuRes.TARGET_MORE.toString() -> context.startActivity(
                        Intent(context, AllServiceActivity::class.java)
                    )
                }
            }
        })

        //上滑的指数栏
        initStockIndexVp()
    }

    private fun initMenus() {
        val appMenuRes: MutableList<AppMenuRes> = ArrayList() // 自选股
        val res1 = AppMenuRes()
        res1.menuName = context.getString(R.string.home_menu_mystock)
        res1.menuImageUrl = R.mipmap.icon_self_select_stock.toString()
        res1.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res1.targetUrl = AppMenuRes.TARGET_MYSTOCK.toString()
        res1.isSimulated = true // 投顾
        val res12 = AppMenuRes()
        res12.menuName = context.getString(R.string.home_investment_advisor)
        res12.menuImageUrl = R.mipmap.icon_investment_advisor.toString()
        res12.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res12.targetUrl = AppMenuRes.TARGET_SD_TG.toString()
        res12.isSimulated = true // 新股IPO
        val res2 = AppMenuRes()
        res2.menuName = context.getString(R.string.home_info_ipo)
        res2.menuImageUrl = R.mipmap.icon_ipo.toString()
        res2.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res2.targetUrl =
            AppMenuRes.TARGET_IPO.toString() //            "XJBDataStoreRemote.BASE_H5_NEW_URL" + "dataCenter/newStockIPO.html"
        res2.isSimulated = true //模拟炒股
        val res3 = AppMenuRes()
        res3.menuName = context.getString(R.string.home_menu_simulate_stock_speculation)
        res3.menuImageUrl = R.mipmap.icon_simulate_stock_speculation.toString()
        res3.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res3.targetUrl = AppMenuRes.TARGET_SIMULATE.toString()
        res3.isSimulated = true //热门板块
        val res4 = AppMenuRes()
        res4.menuName = context.getString(R.string.home_menu_hotblock)
        res4.menuImageUrl = R.mipmap.icon_hot_plate.toString()
        res4.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res4.targetUrl = AppMenuRes.TARGET_HOTBLOCK.toString()
        res4.isSimulated = true // 线上开户
        val res5 = AppMenuRes()
        res5.menuName = context.getString(R.string.home_menu_icon_online_account_opening)
        res5.menuImageUrl = R.mipmap.icon_online_account_opening.toString()
        res5.menuType = AppMenuRes.MENU_TYPE_H5
        res5.targetUrl = AppMenuRes.TARGET_OA.toString()
        res5.isSimulated = true //普通交易
        val res6 = AppMenuRes()
        res6.menuName = context.getString(R.string.home_menu_normal_trade)
        res6.menuImageUrl = R.mipmap.icon_normal_transaction.toString()
        res6.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res6.targetUrl = AppMenuRes.TARGET_TRADE.toString()
        res6.isSimulated = true //科创板开通
        val res7 = AppMenuRes()
        res7.menuName = context.getString(R.string.home_menu_open_science_technology)
        res7.menuImageUrl = R.mipmap.icon_open_science_technology_board.toString()
        res7.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res7.targetUrl = AppMenuRes.TARGET_OPEN_SCIENCE_TECHNOLOGY.toString()
        res7.isSimulated = true //创业板开通
        val res8 = AppMenuRes()
        res8.menuName = context.getString(R.string.home_menu_open_second_board)
        res8.menuImageUrl = R.mipmap.icon_open_second_board.toString()
        res8.menuType = AppMenuRes.MENU_TYPE_NATIVE
        res8.targetUrl = AppMenuRes.TARGET_OPEN_SECOND_BOARD.toString()
        res8.isSimulated = true // 更多
        val res9 = AppMenuRes()
        res9.menuName = context.getString(R.string.home_menu_more)
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
        defaultMenus = appMenuRes
    }

    /**
     * 广告栏的
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initAd() {
        //设置广告栏的高度
        val width = UIUtils.widthPixels(context) // 宽高比 ： 0.586 -> 0.405
        bannerLayout!!.layoutParams = LayoutParams(width, (width * 0.405).toInt())

        vpAd!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val adapter = vpAd!!.adapter as AdvertiseAdapter?
                if (adapter == null || adapter.count == 1) {
                    return
                }
                if (position == adapter.count - 1) {
                    selectPageItem(0)
                } else if (position == 0) {
                    selectPageItem(adapter.count - 3)
                } else {
                    selectPageItem(position - 1)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (vpAd!!.adapter == null) return
                    if (vpAd!!.currentItem == 0) {
                        vpAd!!.setCurrentItem(vpAd!!.adapter!!.count - 2, false)
                    } else if (vpAd!!.currentItem == vpAd!!.adapter!!.count - 1) {
                        vpAd!!.setCurrentItem(1, false)
                    }
                }
            }
        }) // 接收点击事件
        vpAd!!.setOnTouchListener { v, arg1 ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    fun initStockIndexVp() {
        stockIndexPageAdapter = StockIndexPageAdapter(context)
        vpStock!!.adapter = stockIndexPageAdapter
    }

    // =================================== 生命周期
    fun onResume() { //        hsIndexView!!.onResume()
        //        onResume(stockIndexPageAdapter!!.codeInfoList) { item: StockQuoteInfo? ->
        //            stockIndexPageAdapter!!.updateContent(item!!)
        //            true
        //        }
        // 此处更新假数据
        // 上证指数
        Handler().postDelayed({
            var item = StockQuoteInfo()
            item.exch = Exch.SSE
            item.alias = "上证指数"
            item.code = "000001"
            item.chgPct = 1.123 //涨跌幅
            item.newPrice = 21.123 //现价
            item.chgValue = 1.123 //涨跌额
            stockIndexPageAdapter!!.updateContent(item!!)
            item = StockQuoteInfo()
            item.exch = Exch.SZSE
            item.code = "399001"
            item.alias = "深证成指"
            item.chgPct = 2.123 //涨跌幅
            item.newPrice = 31.123 //现价
            item.chgValue = 2.123 //涨跌额
            stockIndexPageAdapter!!.updateContent(item!!)
            item = StockQuoteInfo()
            item.exch = Exch.SZSE
            item.code = "399006"
            item.alias = "创业板指"
            item.chgPct = -10.123 //涨跌幅
            item.newPrice = 211.123 //现价
            item.chgValue = -1.123 //涨跌额
            stockIndexPageAdapter!!.updateContent(item!!)

            item = StockQuoteInfo()
            item.exch = Exch.SSE
            item.code = "000688"
            item.alias = "科创50"
            item.chgPct = -10.123 //涨跌幅
            item.newPrice = 211.123 //现价
            item.chgValue = -1.123 //涨跌额
            stockIndexPageAdapter!!.updateContent(item!!)

        }, 300)

        Handler().postDelayed({
            val list = ArrayList<Advertise>()
            var item = Advertise()
            item.advertiseTargetUrl =
                "http://h5devtest.yongxingsec.com/h5/activity/buyFund/buyFundYyhty.html"
            item.code = "1"
            item.advertisePath =
                "https://img-pre.ivsky.com/img/tupian/pre/202008/12/hailang-006.jpg"
            item.timer = "3"
            item.advertiseType = "0"
            list.add(item)
            item = Advertise()
            item.advertiseTargetUrl =
                "http://img0.imgtn.bdimg.com/it/u=1735688044,4235283864&fm=26&gp=0.jpg"
            item.code = "1"
            item.advertisePath = "http://10.199.101.221:8086/h5/stib/intro/intro.html"
            item.timer = "3"
            item.advertiseType = "0"
            list.add(item)
            updateAdvertise(list)

        }, 200)
    }

    fun onPause() { //        hsIndexView!!.onPause()
        //        StockIndexHelper.onPause()
    }
    // =========================================================================================
    // =================================== update data
    // =========================================================================================
    /**
     * 更新九宫格
     */
    fun updateView(appMenuRes: List<AppMenuRes?>) {
        menuAdapter!!.setItems(appMenuRes)
    }

    /**
     * 更新广告
     */
    fun updateAdvertise(advertises: List<Advertise?>?) {
        val adapter = AdvertiseAdapter(context, advertises)
        vpAd!!.adapter = adapter
        if (advertises != null && advertises.size > 1) {
            initDots(advertises.size)
            pointGroup!!.visibility = VISIBLE
            vpAd!!.setCurrentItem(1, false) //            mHandler.removeMessages(0)
            //            mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        } else {
            pointGroup!!.visibility = GONE
        }
    }

    private fun initDots(count: Int) { //初始化banner底部的小圆点
        pointGroup!!.removeAllViews()
        for (i in 0 until count) {
            val pointImage = ImageView(context)
            pointImage.setImageResource(R.drawable.bg_page_dot)
            val PointSize =
                getResources().getDimension(R.dimen.info_channel_banner_indicator_radius)
                    .toInt() * 2
            val params = LayoutParams(PointSize, PointSize)
            if (i > 0) {
                params.leftMargin =
                    getResources().getDimensionPixelSize(R.dimen.info_channel_banner_indicator_margin)
                pointImage.isSelected = false
            } else {
                pointImage.isSelected = true
            }
            pointImage.layoutParams = params
            pointGroup!!.addView(pointImage)
        }
    }

    /**
     * 默认首页顶部菜单
     *
     * @return
     */
    private var defaultMenus: List<AppMenuRes> = ArrayList()

    fun executeCallback() {
        mHandler.removeMessages(0)
        mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL)
    }

    fun removeCallback() {
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun selectPageItem(item: Int) { //选择banner底部小圆点的当前位置
        if (pointGroup == null || pointGroup!!.visibility == GONE) {
            return
        }
        for (i in 0 until pointGroup!!.childCount) {
            pointGroup!!.getChildAt(i).isSelected = i == item
        }
    }

    /**
     * 计时器，viewpage 切换
     */
    private val mHandler = HomeHandler { x, y ->
        if (context != null && !(context as Activity).isFinishing) {
            index++
            if (index % 3 == 0L) {
                if (vpStock != null && vpStock!!.adapter != null && vpStock?.adapter?.count!! > 0) { //3s 切换指数
                    val currentStockItem = vpStock!!.currentItem
                    var nextStockItem = currentStockItem + 1
                    if (nextStockItem > vpStock!!.adapter!!.count - 1) {
                        nextStockItem = 0
                    }
                    vpStock!!.currentItem = nextStockItem
                }
            } else if (index % 5 == 0L) {
                if (vpAd != null && vpAd!!.adapter != null && vpAd!!.adapter!!.count!! > 0) { //5S 切换广告
                    val currentItem = vpAd!!.currentItem
                    var nextItem = currentItem + 1
                    if (nextItem > vpAd!!.adapter!!.count - 1) {
                        nextItem = 0
                    }
                    vpAd!!.currentItem = nextItem
                }
            } else if (index == (Int.MAX_VALUE - 1).toLong()) {
                index = 0
            }
            y.removeMessages(0)
            y.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        }
        true
    }
}
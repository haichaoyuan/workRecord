package com.example.module_homepage2.view;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.lib_util.utils.LogUtils
import com.example.module_homepage2.R
import com.example.module_homepage2.adapter.Advertise2Adapter
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.adapter.LittleAdBannerAdapter
import com.example.module_homepage2.base.*
import com.example.module_homepage2.listener.HomeMenuItemClickListener
import com.shhxzq.ztb.ui.home.ui.adapter.StockIndexPageAdapter
import com.shhxzq.ztb.ui.home.ui.constant.AppMenuDataConst
import com.shhxzq.ztb.ui.home.ui.helper.HomeHandler

/**
 * Des:首页顶部， 2.0 版本
 */
class HomeHeaderView2 : BaseCustomView {
    var bannerLayout: RelativeLayout? = null
    var vpAdBanner: ViewPager? = null //头部轮播
    var pointGroup: LinearLayout? = null
    var homeGridview: MyGridView? = null //九宫格
    var littleBannerLayout: RelativeLayout? = null
    var vpLittleBanner: ViewPager? = null //循环广告轮播
    var vpStockIndex: ViewPager? = null //上滚指数

    private var menuAdapter: HomeMenu2Adapter? = null
    private var stockIndexPageAdapter //指数 viewpager
            : StockIndexPageAdapter? = null

    /**
     * 默认首页顶部菜单
     *
     * @return
     */
    private var defaultMenus: List<AppMenuRes> = ArrayList()

    // =================================== 计时器
    private val TIME_INTERVAL: Long = 1000
    private var index: Long = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun setAttrs(attrs: AttributeSet) {}
    override fun initView(inflater: LayoutInflater) {
        val view = inflater.inflate(R.layout.custom_home_top, this, true)
        bannerLayout = view.findViewById(R.id.banner_layout)
        vpAdBanner = view.findViewById(R.id.vp_ad_banner)
        pointGroup = view.findViewById(R.id.indicator)
        homeGridview = view.findViewById(R.id.home_gridview)
        vpStockIndex = view.findViewById(R.id.vp_stock_index)
        littleBannerLayout = view.findViewById(R.id.layout_little_banner)
        vpLittleBanner = view.findViewById(R.id.vp_little_banner)

        initDefault()
    }

    private fun initDefault() { //设置广告栏
        initAdBanner()
        // 九宫格
        initMenus()
        //上滑的指数栏
        initStockIndexVp()
    }

    /**
     * 广告栏的
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initAdBanner() {
        //设置广告栏的高度
        val width = UIUtils.widthPixels(context) // 宽高比 ： 0.586 -> 0.405
        bannerLayout!!.layoutParams = LayoutParams(width, (width * 0.405).toInt())
        littleBannerLayout!!.layoutParams =
            LayoutParams(width, (width * 0.208 + UIUtils.dp2px(context, 15f)).toInt())

        vpAdBanner!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val adapter = vpAdBanner!!.adapter as Advertise2Adapter?
                if (adapter == null || adapter.count == 1) {
                    return
                }
                if (position == adapter.lastAd + 1) {
                    selectPageItem(adapter.firstAd - adapter.offset)
                } else if (position == adapter.firstAd - 1) {
                    selectPageItem(adapter.lastAd - adapter.offset)
                } else {
                    selectPageItem(position - adapter.offset)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (vpAdBanner == null) {
                    return
                }
                val adapter = vpAdBanner!!.adapter as Advertise2Adapter? ?: return
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    val currentItem = vpAdBanner!!.currentItem
                    if (currentItem >= adapter.lastAd + 1) {
                        vpAdBanner!!.setCurrentItem(adapter.firstAd, false)
                    } else if (currentItem <= adapter.firstAd - 1) {
                        vpAdBanner!!.setCurrentItem(adapter.lastAd, false)
                    }
                }
            }
        }) // 接收点击事件
        vpAdBanner!!.setOnTouchListener { v, arg1 ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        vpLittleBanner?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                if (vpLittleBanner == null) {
                    return
                }
                val adapter = vpLittleBanner!!.adapter as Advertise2Adapter? ?: return
                val currentItem = vpLittleBanner!!.currentItem
                LogUtils.i(TAG, "=============== state:$state currentItem:$currentItem")
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (currentItem >= adapter.lastAd + 1) {
                        LogUtils.i(TAG, "currentItem:$currentItem -> ${adapter.firstAd}")
                        vpLittleBanner!!.setCurrentItem(adapter.firstAd, false)
                    } else if (currentItem <= adapter.firstAd - 1) {
                        LogUtils.i(TAG, "currentItem:$currentItem -> ${adapter.lastAd}")
                        vpLittleBanner!!.setCurrentItem(adapter.lastAd, false)
                    }
                }
            }
        })
    }

    private fun initMenus() {
        defaultMenus = AppMenuDataConst.getHomeMenusMock(context)
        menuAdapter = HomeMenu2Adapter(context, defaultMenus)
        homeGridview!!.numColumns = 5
        homeGridview!!.adapter = menuAdapter
        menuAdapter!!.setHomeMenuItemClickListener(object : HomeMenuItemClickListener {
            override fun onItemClick(appMenuRes: AppMenuRes) {
//                if (appMenuRes.targetUrl == AppMenuConst.TARGET_NINE_GRID_MORE.toString()) {
//                    context.startActivity(
//                        Intent(context, AllServiceActivity::class.java)
//                    )
//                } else {
//                    AppMenuJumpHelper.menuClickImp(context as Activity, appMenuRes)
//                }
            }
        })
    }

    private fun initStockIndexVp() {
        stockIndexPageAdapter = StockIndexPageAdapter(context)
        vpStockIndex!!.adapter = stockIndexPageAdapter
        vpStockIndex!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (vpStockIndex!!.adapter == null) return
                    if (vpStockIndex!!.currentItem >= vpStockIndex!!.adapter!!.count - 1) {
                        vpStockIndex!!.setCurrentItem(0, false)
                    }
                }
            }
        }) // 接收点击事件
    }

    // =================================== 生命周期 mock
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
                "http://10.199.101.221:8086/h5/stib/intro/intro.html"
            item.code = "1"
            item.advertisePath =
                "http://img0.imgtn.bdimg.com/it/u=1735688044,4235283864&fm=26&gp=0.jpg"
            item.timer = "3"
            item.advertiseType = "0"
            list.add(item)
            setAdBanner(list)

            setLittleAdBanner(list)

        }, 200)
    }

    fun onPause() {
        //        hsIndexView!!.onPause()
        //        StockIndexHelper.onPause()
    }

    // =========================================================================================
    // =================================== update data
    // =========================================================================================
    /**
     * 更新广告
     */
    fun setAdBanner(advertises: List<Advertise?>?) {
        val adapter = Advertise2Adapter(context, advertises)
        if (advertises == null || advertises.isNotEmpty()) {
            vpAdBanner!!.background = null
        }
        vpAdBanner!!.adapter = adapter
        if (advertises != null && advertises.size > 1) {
            initDots(advertises.size)
            pointGroup!!.visibility = VISIBLE
            vpAdBanner!!.setCurrentItem(adapter.firstAd, false)
            mHandler.removeMessages(0)
            mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        } else {
            pointGroup!!.visibility = GONE
        }
    }

    /**
     * 更新循环banner
     */
    fun setLittleAdBanner(advertises: List<Advertise?>?) {
        if (littleBannerLayout == null || vpLittleBanner == null) {
            return
        }
        if (advertises == null || advertises.isEmpty()) {
            littleBannerLayout!!.visibility = GONE
            return
        }


        littleBannerLayout!!.visibility = VISIBLE
        val littleAdapter = LittleAdBannerAdapter(context, advertises)
        vpLittleBanner!!.adapter = littleAdapter
        vpLittleBanner!!.setCurrentItem(littleAdapter.firstAd, false)
    }

    /**
     * 更新九宫格
     */
    fun updateView(appMenuRes: List<AppMenuRes?>) {
        menuAdapter!!.setItems(appMenuRes)
    }

    private fun selectPageItem(item: Int) {
        //选择banner底部小圆点的当前位置
        if (pointGroup == null || pointGroup!!.visibility == GONE) {
            return
        }
        for (i in 0 until pointGroup!!.childCount) {
            val point: ImageView = pointGroup!!.getChildAt(i) as ImageView
            point.isSelected = i == item
            val pointSize =
                getResources().getDimension(R.dimen.info_channel_banner_indicator_new_radius)
                    .toInt() * 2
            val layoutParams: LayoutParams = point.layoutParams as LayoutParams
            if (i == item) {
                layoutParams.width = pointSize * 8 / 3
            } else {
                layoutParams.width = pointSize
            }
            point.layoutParams = layoutParams
        }
    }


    private fun initDots(count: Int) {
        //初始化banner底部的小圆点
        pointGroup!!.removeAllViews()
        for (i in 0 until count) {
            val pointImage = ImageView(context)
            pointImage.setImageResource(R.drawable.bg_page_dot)
            val PointSize =
                getResources().getDimension(R.dimen.info_channel_banner_indicator_new_radius)
                    .toInt() * 2
            val params: LayoutParams
            if (i > 0) {
                params = LayoutParams(PointSize, PointSize)
                params.leftMargin =
                    getResources().getDimensionPixelSize(R.dimen.info_channel_banner_indicator_margin)
                pointImage.isSelected = false
            } else {
                params = LayoutParams(PointSize * 8 / 3, PointSize)
                pointImage.isSelected = true
            }
            pointImage.layoutParams = params
            pointGroup!!.addView(pointImage)
        }
    }

    // =========================================================================================
    // =================================== 定时器，循环切换广告
    // =========================================================================================

    /**
     * 手动执行
     */
    fun executeCallback() {
        mHandler.removeMessages(0)
        mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL)
    }

    fun removeCallback() {
        mHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 计时器，viewpage 切换
     */
    private val mHandler = HomeHandler { x, y ->
        if (context != null && !(context as Activity).isFinishing) {
            index++
            if (index % 3 == 0L) {
                changeIndexNum()
            } else if (index % 5 == 0L) {
                changeAd()
            } else if (index == (Int.MAX_VALUE - 1).toLong()) {
                index = 0
            }
            y.removeMessages(0)
            y.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        }
        true
    }

    /**
     * 切换广告
     */
    private fun changeAd() {
        if (vpAdBanner!!.adapter != null) {
            //5S 切换广告
            val currentItem = vpAdBanner!!.currentItem
            var nextItem = currentItem + 1
            if (nextItem > vpAdBanner!!.adapter!!.count - 1) {
                nextItem = 0
            }
            vpAdBanner!!.currentItem = nextItem
        }
    }

    /**
     * 切换指数
     */
    private fun changeIndexNum() {
        if (vpStockIndex!!.adapter != null) {
            //3s 切换指数
            val currentStockItem = vpStockIndex!!.currentItem
            var nextStockItem = currentStockItem + 1
            if (nextStockItem > vpStockIndex!!.adapter!!.count - 1) {
                nextStockItem = 0
            }
            vpStockIndex!!.currentItem = nextStockItem
        }
    }

    companion object {
        const val TAG = "HomeHeaderView2"
    }
}
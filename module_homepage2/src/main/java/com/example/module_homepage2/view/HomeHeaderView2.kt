package com.example.module_homepage2.view;
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.module_homepage2.R
import com.example.module_homepage2.adapter.HomeMenu2Adapter
import com.example.module_homepage2.base.*
import com.shhxzq.ztb.ui.home.ui.adapter.StockIndexPageAdapter
import com.shhxzq.ztb.ui.home.ui.helper.HomeHandler
import java.util.*

/**
 * Des:首页顶部， 2.0 版本
 */
class HomeHeaderView2 : BaseCustomView {
    var homeGridview: MyGridView? = null //九宫格
    var vpAd: ViewPager? = null
    var vpStock : ViewPager? = null //上滚指数
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
        pointGroup = view.findViewById(R.id.indicator)
//        hsIndexView = view.findViewById(R.id.hsIndexView)

        initDefault()
    }

    fun initDefault() {
        menuAdapter = HomeMenu2Adapter(context, defaultMenus)
        homeGridview!!.numColumns = 5
        homeGridview!!.adapter = menuAdapter
        initVpAd()

        //设置广告栏的高度
        val width = UIUtils.widthPixels(context)
        bannerLayout!!.layoutParams = LayoutParams(width, (width * 0.586).toInt())
        //上滑的指数栏
        initStockIndexVp()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initVpAd() {
        vpAd!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
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
        })

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
    fun onResume() {
//        hsIndexView!!.onResume()
//        onResume(stockIndexPageAdapter!!.codeInfoList) { item: StockQuoteInfo? ->
//            stockIndexPageAdapter!!.updateContent(item!!)
//            true
//        }
    }

    fun onPause() {
//        hsIndexView!!.onPause()
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
            vpAd!!.setCurrentItem(1, false)
            mHandler.removeMessages(0)
            mHandler.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        } else {
            pointGroup!!.visibility = GONE
        }
    }

    private fun initDots(count: Int) {
        //初始化banner底部的小圆点
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
    private val defaultMenus: List<AppMenuRes>
        private get() {
            val appMenuRes: MutableList<AppMenuRes> = ArrayList()
            val res1 = AppMenuRes()
            res1.menuName = context.getString(R.string.home_menu_mystock)
            res1.menuImageUrl = R.drawable.ico_menu_mystock.toString()
            res1.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res1.targetUrl = AppMenuRes.TARGET_MYSTOCK.toString()
            res1.isSimulated = true
            val res12 = AppMenuRes()
            res12.menuName = "投顾"
            res12.menuImageUrl = R.drawable.ico_menu_mystock.toString()
            res12.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res12.targetUrl = AppMenuRes.TARGET_MYSTOCK.toString()
            res12.isSimulated = true
            val res2 = AppMenuRes()
            res2.menuName = context.getString(R.string.home_menu_stockindex)
            res2.menuImageUrl = R.drawable.ico_menu_stockindex.toString()
            res2.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res2.targetUrl = AppMenuRes.TARGET_STOCKINDEX.toString()
            res2.isSimulated = true
            val res3 = AppMenuRes()
            res3.menuName = context.getString(R.string.home_menu_ranking)
            res3.menuImageUrl = R.drawable.ico_menu_ranking.toString()
            res3.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res3.targetUrl = AppMenuRes.TARGET_CHANGELIST.toString()
            res3.isSimulated = true
            val res4 = AppMenuRes()
            res4.menuName = context.getString(R.string.home_menu_hotblock)
            res4.menuImageUrl = R.drawable.ico_menu_hotblock.toString()
            res4.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res4.targetUrl = AppMenuRes.TARGET_HOTBLOCK.toString()
            res4.isSimulated = true
            val res5 = AppMenuRes()
            res5.menuName = context.getString(R.string.home_info_ipo)
            res5.menuImageUrl = R.drawable.ico_menu_ipo.toString()
            res5.menuType = AppMenuRes.MENU_TYPE_H5
            res5.targetUrl = "XJBDataStoreRemote.BASE_H5_NEW_URL" + "dataCenter/newStockIPO.html"
            res5.isSimulated = true
            val res6 = AppMenuRes()
            res6.menuName = context.getString(R.string.home_info_winnerslist)
            res6.menuImageUrl = R.drawable.ico_menu_winnerlist.toString()
            res6.menuType = AppMenuRes.MENU_TYPE_H5
            res6.targetUrl = "XJBDataStoreRemote.BASE_H5_NEW_URL" + "dataCenter/longHuBang.html"
            res6.isSimulated = true
            val res7 = AppMenuRes()
            res7.menuName = context.getString(R.string.home_menu_oa)
            res7.menuImageUrl = R.drawable.ico_menu_oa.toString()
            res7.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res7.targetUrl = AppMenuRes.TARGET_OA.toString()
            res7.isSimulated = true
            val res8 = AppMenuRes()
            res8.menuName = context.getString(R.string.home_menu_trade)
            res8.menuImageUrl = R.drawable.ico_menu_trade.toString()
            res8.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res8.targetUrl = AppMenuRes.TARGET_TRADE.toString()
            res8.isSimulated = true
            val res9 = AppMenuRes()
            res9.menuName = "更多"
            res9.menuImageUrl = R.drawable.ico_menu_trade.toString()
            res9.menuType = AppMenuRes.MENU_TYPE_NATIVE
            res9.targetUrl = AppMenuRes.TARGET_TRADE.toString()
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

    fun removeCallback() {
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun selectPageItem(item: Int) {
        //选择banner底部小圆点的当前位置
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
                //3s 切换指数
                val currentStockItem = vpStock!!.currentItem
                var nextStockItem = currentStockItem + 1
                if (nextStockItem > vpStock!!.adapter!!.count - 1) {
                    nextStockItem = 0
                }
                vpStock!!.currentItem = nextStockItem
            } else if (index % 5 == 0L) {
                //5S 切换广告
                val currentItem = vpAd!!.currentItem
                var nextItem = currentItem + 1
                if (nextItem > vpAd!!.adapter!!.count - 1) {
                    nextItem = 0
                }
                vpAd!!.currentItem = nextItem
            } else if (index == (Int.MAX_VALUE - 1).toLong()) {
                index = 0
            }
            y.removeMessages(0)
            y.sendEmptyMessageDelayed(0, TIME_INTERVAL)
        }
        true
    }
}
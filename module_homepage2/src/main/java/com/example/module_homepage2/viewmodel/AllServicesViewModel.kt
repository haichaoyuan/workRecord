package com.example.module_homepage2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.module_homepage2.R
import com.example.module_homepage2.base.AppMenuRes
import com.example.module_homepage2.entity.AllServicesEntity
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class AllServicesViewModel: ViewModel() {
    lateinit var hotMenus: List<AppMenuRes>
        private set
    var allServicesEntity: AllServicesEntity? = null
        private set
    var CHANNELS:ArrayList<String> = ArrayList(arrayOf("行情", "交易", "理财", "特色数据", "特色服务", "业务办理", "资讯").toList())

    fun init(context: Context) {
        allServicesEntity = getAllServicesFromJson(context, "all_services.json")
        hotMenus = getHotMenus(context)
        if(allServicesEntity != null){
            CHANNELS.clear()
            for(item in allServicesEntity!!.result){
                CHANNELS.add(item.name)
            }
        }
    }

    private fun getAllServicesFromJson(context: Context, fileName:String):AllServicesEntity? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
            var line:String?
            while ((bf.readLine().also { line = it }) != null){
                stringBuilder.append(line)
            }
            // string -> List
            return Gson().fromJson(stringBuilder.toString(), AllServicesEntity::class.java)
        } catch (e:IOException){
            e.printStackTrace()
        }
        return null
    }

    /**
     * 顶部热门菜单
     */
    private fun getHotMenus(context: Context): List<AppMenuRes> {
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
        return appMenuRes
    }

}
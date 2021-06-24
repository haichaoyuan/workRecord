package com.shhxzq.ztb.ui.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.module_homepage2.R
import com.example.module_homepage2.base.*
import java.util.*

/**
 * 上下滚动指数 ViewPageAdapter
 */
class StockIndexPageAdapter(private val context: Context) : PagerAdapter() {
    private var views: MutableList<LinearLayout> = ArrayList()
    private var stockProtoList: MutableList<StockQuoteInfo> = ArrayList()
    var codeInfoList: MutableList<CodeInfo> = ArrayList()
        get

    init {
        initConfigCodes()
        initView()
    }

    /**
     * 本地写死 三指数  上证、深证、创业板
     */
    private fun initConfigCodes() {
        stockProtoList.clear()
        var quoteInfo = StockQuoteInfo()
        quoteInfo.exch = Exch.SSE
        quoteInfo.code = "000001"
        quoteInfo.alias = "上证指数"
        stockProtoList.add(quoteInfo)

        quoteInfo = StockQuoteInfo()
        quoteInfo.exch = Exch.SZSE
        quoteInfo.code = "399001"
        quoteInfo.alias = "深证成指"
        stockProtoList.add(quoteInfo)

        quoteInfo = StockQuoteInfo()
        quoteInfo.exch = Exch.SZSE
        quoteInfo.code = "399006"
        quoteInfo.alias = "创业板指"
        stockProtoList.add(quoteInfo)

        quoteInfo = StockQuoteInfo()
        quoteInfo.exch = Exch.SSE
        quoteInfo.code = "000688"
        quoteInfo.alias = "科创50"
        stockProtoList.add(quoteInfo)

        codeInfoList.clear()
        for (item in stockProtoList) {
            codeInfoList.add(CodeInfo(item.exch, item.code, item.alias))
        }
    }

    private fun initView() {
        views = ArrayList()
        for (item in stockProtoList) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_stock_index, null) as LinearLayout
            view.tag = item

            view.setOnClickListener { v: View ->
//                StockDetailActivity.gotoDetail(
//                    v.context,
//                    item.exch,
//                    item.code,
//                    stockProtoList
//                )
            }
            updateContent(item)
            views.add(view)
        }
    }

    fun updateContent(data: StockQuoteInfo) {
        for (itemView in views) {
            val stockProto = itemView.tag as StockQuoteInfo
            if (stockProto.exch === data.exch && stockProto.code == data.code) {
                val blockName =
                    itemView.findViewById<TextView>(R.id.block_name)
                val blockNum =
                    itemView.findViewById<TextView>(R.id.block_num)
                val blockPercent =
                    itemView.findViewById<TextView>(R.id.block_percent)
                val leadStockValue =
                    itemView.findViewById<TextView>(R.id.lead_stock_value)
                // 上证指数
                blockName.text = data.alias
                // 指数
                val percentValue = data.chgPct.asChangePctFormat(stockProto)
                var color = data.chgPct.getColor(context)
                blockNum.setTextColor(color)
                blockNum.text = data.newPrice.asPriceFormat(data)
                // 百分数
                blockPercent.text = data.chgValue.asChangeFormat(data) +"%"
                blockPercent.setTextColor(color)
                //额外说明
                var currentTime = DateTimeUtils.getCurrentTimeWithMonth2Minute()
                leadStockValue.text = currentTime
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = views[position]
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return if (views == null) {
            0
        } else views.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }


}
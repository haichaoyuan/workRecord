package com.example.module_homepage2.base

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.module_homepage2.R

/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 行情帮助类
 *
 */
object QuoteHelper  {

    private var riseColor: Int = -1
    private var fallColor: Int = -1
    private var evenColor: Int = -1




    /**
     * 获取涨颜色（避免列表多次从资源获取的消耗，直接保存变量）
     */
    fun getRiseColor(context: Context): Int {
        if (riseColor == -1) {
            riseColor = ContextCompat.getColor(context, R.color.quote_red)
        } else {
            riseColor
        }
        return riseColor
    }

    /**
     * 获取跌颜色（避免列表多次从资源获取的消耗，直接保存变量）
     */
    fun getFallColor(context: Context): Int {
        if (fallColor == -1) {
            fallColor = ContextCompat.getColor(context,R.color.quote_green)
        } else {
            fallColor
        }
        return fallColor
    }

    /**
     * 获取平颜色（避免列表多次从资源获取的消耗，直接保存变量）
     */
    fun getEvenColor(context: Context): Int {
        if (evenColor == -1) {
            evenColor = ContextCompat.getColor(context, R.color.quote_even)
        } else {
            evenColor
        }
        return evenColor
    }


    //单位
    fun getStockUnit(stock: StockStaticInfo?): String {
        if (stock == null) return "手"
        return when {
            stock.exch == Exch.HKEX -> "股"
            stock.subType == SecSubType.TECHNOLOGY_INNOVATION_BOARD_STOCK -> {
                //如果是科创板里的cdr，显示单位为份
                if (stock.isCDR) {
                    "份"
                } else {
                    "股"
                }
            }
            else -> "手"
        }
    }



}
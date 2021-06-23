package com.example.module_homepage2.base

import kotlinx.android.parcel.Parcelize

/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 领涨股
 *
 */
@Parcelize
class LeaderStock(
        var price: Double = 0.0,//最新价
        var chgValue: Double = QuoteConstant.DEFAULT_DOUBLE,//涨跌值
        var chgPct: Double = QuoteConstant.DEFAULT_DOUBLE// 涨跌幅
) : StockBaseInfo()
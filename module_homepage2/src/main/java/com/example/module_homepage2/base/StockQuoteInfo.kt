package com.example.module_homepage2.base

import android.os.Parcelable
import android.util.SparseArray
import kotlinx.android.parcel.Parcelize
/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 证券 行情信息
 *
 */
@Parcelize
data class StockQuoteInfo(
        var time: Int = 0,//时间  HHmmss
        var date: Int = 0,//日期  YYYYMMDD
        var minutes: Int = 0,// 0点后的分钟数
        var phase: Phase = Phase.UNKNOWN,//当前交易状态
        var newPrice: Double = 0.0,//现价
        var swing: Double = 0.0,//振幅
        var openPrice: Double = 0.0, //开盘价
        var highPrice: Double = 0.0,//当日最高价
        var lowPrice: Double = 0.0,//当日最低价
        var chgValue: Double = QuoteConstant.DEFAULT_DOUBLE,//涨跌额
        var chgPct: Double = QuoteConstant.DEFAULT_DOUBLE,//涨跌幅
        var chgPctWeight: Double = QuoteConstant.DEFAULT_DOUBLE,//加权涨幅
        var dealVol: Long = 0L,//成交量
        var dealValue: Double = 0.0,// 成交额
        var insideVol: Long = 0L,//内盘
        var outsideVol: Long = 0L,//外盘
        var volRatio: Double = 0.0, //量比
        var chgRatio: Double = 0.0,//换手率
        var upSpeed: Double = 0.0,//涨速
        var committee: Double = 0.0, //委比
        var commitDiff: Long = 0L,//委差
        var afterDealVol: Long = 0L, //盘后量
        var afterDealValue: Double = 0.0, //盘后额
        var upCount: Int = QuoteConstant.DEFAULT_INT,//涨家数
        var downCount: Int = QuoteConstant.DEFAULT_INT,//跌家数
        var flatCount: Int = QuoteConstant.DEFAULT_INT,//平家数
        var subStockCount: Int = 0,//股票数量
        var leaderStock: LeaderStock? = null, //领涨股

        var rankMoneyValue: Double = 0.0, //资金流入榜单用，
        var dayMainMoneyIn: Double = 0.0, //当日主力资金净流入

        var iopv: Double = 0.0,//IOPV

        var buyOrderList: SparseArray<OrderLevel>? = null,//买档位 从0开始
        var sellOrderList: SparseArray<OrderLevel>? = null,//卖档位 从0开始

        //条件选股扩展（恶心人）
        var valueStr :List<String>?=null, //条件选股字段
        var newPriceStr :String ?=null,//新价格，String
        var changePctStr:String?=null,//涨跌幅，String

        //债券相关
        var buyBackPrice: Double = 0.0,//回购价格
        var bondDays: Int = 0,//产品期数
        var bondStockPrice: Double = 0.0,//转股价值
        var incomePer10w: Double = 0.0,//10w元收益(元)
        var incomePer1k: Double = 0.0,//1千元收益（元）
        var incomeRatePerYear: Double = 0.0,//年华收益率
        var jxEndDate: Int = 0,//计息结束日期（YYYYMMdd）
        var jxStartDate: Int = 0,//计息开始日期
        var todayBuyDate: Int = 0,//今日购买日期（格式：YYYYMMDD）
        var zjAvailableDate: Int = 0,//资金可用日期（格式：YYYYMMDD）
        var zjFetchableDate: Int = 0,//资金可取日期（格式：YYYYMMDD）
        var zkDays: Int = 0,//占款天数
        var zqPremiumRatio: Double = 0.0//溢价率

) : StockStaticInfo()


/**
 * 档位数据
 * @param price  挂单价格
 * @param vol  挂单量
 */
@Parcelize
data class OrderLevel(val price: Double = 0.0, val vol: Long = 0L) : Parcelable
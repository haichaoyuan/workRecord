package com.example.module_homepage2.base

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 证券 静态字段
 *
 */
open class StockStaticInfo(
        var currency: Currency = Currency.CNY, //币种
        var isCDR: Boolean = false,//是否是CDR
        var isGDR: Boolean = false,//是否是GDR
        var isGem: Boolean = false,//是否创业板
        var isGemR: Boolean = false,//是否是创业板注册制
        var hasMarginMark: Boolean = false,//是否是融资
        var hasSecurityMark: Boolean = false,//是否是融券
        var hasHKMarginMark: Boolean = false,//是否是港股融资
        var isDeficit: Boolean = false,//是否亏本（未盈利）
        var isProControl: Boolean = false,//是否协议控制
        var isSameRight: Boolean = false, // 是否同股同权
        var isTwentyPct: Boolean = false, // 是否涨跌幅20%
        var isFirstDay: Boolean = false, //是否新股第一天
        var ipoPrice: Double = 0.0,//ipo价格
        var limitUp: Double = 0.0, // 涨停价
        var limitDown: Double = 0.0,//跌停价
        var handCount: Int = 0,//每手股数
        var precise: Int = 2,//股票精度
        var preClose: Double = 0.0,//昨收
        var totalStocks: Long = 0,//总股本
        var circulationStocks: Long = 0,//流通股本
        var totalMKV: Double = 0.0,//总市值
        var circulationMKV: Double = 0.0,//流通市值
        var netValue: Double = 0.0,//净值，每股净资产
        var peRatio: Double = 0.0,//动态市盈率
        var peRatioStatic: Double = 0.0,// 静态市盈率
        var peRatioTTM: Double = 0.0, //TTM 市盈率
        var pbRatio: Double = 0.0,//市净率

        var roe: Double = 0.0, //最新净资产收益率
        var eps: Double = 0.0, //每股收益
        var pastFiveDayAvgVol: Double = 0.0, //前5日平均每分钟成交量

) : StockBaseInfo() ,Cloneable{
    constructor(parcel: Parcel) : this(
            Currency.valueOf(parcel.readString() ?: Currency.UNKNOWN.toString()),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble()) {

        exch = Exch.valueOf(parcel.readString() ?: Exch.UNKNOWN.toString())
        code = parcel.readString() ?: ""
        alias = parcel.readString() ?: ""
        type = SecType.valueOf(parcel.readString() ?: SecType.UNKNOWN.toString())
        subType = SecSubType.valueOf(parcel.readString() ?: SecSubType.UNKONW.toString())

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(currency.toString())
        parcel.writeByte(if (isCDR) 1 else 0)
        parcel.writeByte(if (isGDR) 1 else 0)
        parcel.writeByte(if (isGem) 1 else 0)
        parcel.writeByte(if (isGemR) 1 else 0)
        parcel.writeByte(if (hasMarginMark) 1 else 0)
        parcel.writeByte(if (hasSecurityMark) 1 else 0)
        parcel.writeByte(if (hasHKMarginMark) 1 else 0)
        parcel.writeByte(if (isDeficit) 1 else 0)
        parcel.writeByte(if (isProControl) 1 else 0)
        parcel.writeByte(if (isSameRight) 1 else 0)
        parcel.writeByte(if (isTwentyPct) 1 else 0)
        parcel.writeByte(if (isFirstDay) 1 else 0)
        parcel.writeDouble(ipoPrice)
        parcel.writeDouble(limitUp)
        parcel.writeDouble(limitDown)
        parcel.writeInt(handCount)
        parcel.writeInt(precise)
        parcel.writeDouble(preClose)
        parcel.writeLong(totalStocks)
        parcel.writeLong(circulationStocks)
        parcel.writeDouble(totalMKV)
        parcel.writeDouble(circulationMKV)
        parcel.writeDouble(netValue)
        parcel.writeDouble(peRatio)
        parcel.writeDouble(peRatioStatic)
        parcel.writeDouble(peRatioTTM)
        parcel.writeDouble(pbRatio)
        parcel.writeDouble(roe)
        parcel.writeDouble(eps)
        parcel.writeDouble(pastFiveDayAvgVol)

        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockStaticInfo> {
        override fun createFromParcel(parcel: Parcel): StockStaticInfo {
            return StockStaticInfo(parcel)
        }

        override fun newArray(size: Int): Array<StockStaticInfo?> {
            return arrayOfNulls(size)
        }
    }
}
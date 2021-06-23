package com.example.module_homepage2.base

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 股票基类
 *
 */
open class StockBaseInfo(
        var exch: Exch = Exch.UNKNOWN,  //市场
        var code: String = "",  // 市场代码
        var alias: String = "",//名称
        var type: SecType = SecType.UNKNOWN,  //大类别
        var subType: SecSubType = SecSubType.UNKONW  //子类别
) : Parcelable {
    constructor(parcel: Parcel) : this(
            Exch.valueOf(parcel.readString() ?: Exch.UNKNOWN.toString()),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            SecType.valueOf(parcel.readString() ?: SecType.UNKNOWN.toString()),
            SecSubType.valueOf(parcel.readString() ?: SecSubType.UNKONW.toString())) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(exch.toString())
        parcel.writeString(code)
        parcel.writeString(alias)
        parcel.writeString(type.toString())
        parcel.writeString(subType.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockBaseInfo> {
        override fun createFromParcel(parcel: Parcel): StockBaseInfo {
            return StockBaseInfo(parcel)
        }

        override fun newArray(size: Int): Array<StockBaseInfo?> {
            return arrayOfNulls(size)
        }
    }
}
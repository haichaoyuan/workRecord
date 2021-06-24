package com.example.module_homepage2.base

import android.content.Context
import com.example.module_homepage2.base.QuoteConstant.Companion.DEFAULT_DOUBLE
import com.example.module_homepage2.base.QuoteConstant.Companion.DEFAULT_FLOAT
import com.example.module_homepage2.base.QuoteConstant.Companion.NO_DATA


/**
 * Created by dylan on 2019-08-28.
 * Desc: 一些格式化的扩展
 */

/**
 * 按价格格式化
 */
fun Number.asPriceFormat(info: StockStaticInfo?): String {
    //价格为0表示没有值，显示--
    if (this is Float && (!isFinite() || this == 0f)) {
        return NO_DATA
    }

    if (this is Double && (!isFinite() || this == 0.0)) {
        return NO_DATA
    }

    // 先按 优品的精度处理
    info?.let {
        return format(info.precise)
    }
//
//    info?.let {
//        return when {
//            it.type == SecType.FUND || (it.type == SecType.BOND && it.getExch()==Exchange.SZ) || it.type == SecType.REVERSE_REPURCHASE -> format( 3)
//            it.getExch() == Exchange.HK -> format( 3)
//            it.getExch() == Exchange.SH && it.subType == SecSubType.STOCK_B -> format( 3)
//            else -> format( 2)
//        }
//    }
    return format()
}

/**
 * 按净值格式化（保留4位小数，且0为无效值，显示--）IOPV
 */
fun Number.asNetValueFormat(): String {
    return format(4, ignoreZero = true)
}

/**
 * 按涨跌额格式化
 */
fun Number.asChangeFormat(info: StockStaticInfo? = null): String {
    //正常涨跌可能为0，所以如果是指定的Float值，则认为无值
    if (this.toFloat() == DEFAULT_FLOAT) {
        return NO_DATA
    }
    info?.let {
        return format(info.precise, showSign = true)
    }
    return format(showSign = true)
}

/**
 * 按涨跌幅格式化
 */
fun Number.asChangePctFormat(info: StockBaseInfo? = null): String {
    //正常涨跌可能为0，所以如果是指定的Float值，则认为无值
    if (this.toFloat() == DEFAULT_FLOAT) {
        return NO_DATA
    }
    if (this is Float && (!isFinite())) {
        return NO_DATA
    }

    if (this is Double && (!isFinite())) {
        return NO_DATA
    }
    return format(showSign = true, isPercent = true)
}

/**
 * 按涨跌幅格式化
 */
fun Number.asChangePctWithoutSign(info: StockBaseInfo? = null): String {
    //正常涨跌可能为0，所以如果是指定的Float值，则认为无值
    if (this.toFloat() == DEFAULT_FLOAT) {
        return NO_DATA
    }
    if (this is Float && (!isFinite())) {
        return NO_DATA
    }

    if (this is Double && (!isFinite())) {
        return NO_DATA
    }
    return format(showSign = false, isPercent = true,keepNum = 3)
}


/**
 * 按百分比格式化
 */
fun Number.asPercentFormat(showSign: Boolean = false, info: StockBaseInfo? = null): String {
    if (this is Float && !isFinite()) {
        return NO_DATA
    }

    if (this is Double && !isFinite()) {
        return NO_DATA
    }
    if (this.toFloat() == DEFAULT_FLOAT) {
        return NO_DATA
    }
    return format(showSign = showSign, isPercent = true)
}

/**
 * 按技术指标格式化
 */
fun Number.asIndicatorFormat(info: StockBaseInfo? = null): String {
    //价格为0表示没有值，显示--
    if (this is Float && (!isFinite())) {
        return NO_DATA
    }

    if (this is Double && (!isFinite())) {
        return NO_DATA
    }
    return format(3)
}

/**
 * 自定义格式化
 */
fun Number.format(keepNum: Int = 2,
                  isPercent: Boolean = false,
                  showSign: Boolean = false,
                  showGroup: Boolean = false,
                  ignoreZero: Boolean = false): String {
    return if (isInValid(ignoreZero)) {
        NO_DATA
    } else {
        Formatter.format(this, keepNum, isPercent, showSign, showGroup)
    }
}

/**
 * 按金额/成交量来格式化（配置是否带单位，英文还是中文）
 */
fun Number.asAmountFormat(forceEnglish: Boolean = false, showUnit: Boolean = true, ignoreZero: Boolean = false): String {
    return if (isInValid(ignoreZero)) {
        NO_DATA
    } else {
        Formatter.formatAmount(this, forceEnglish, showUnit)
    }
}

/**
 * 按手数来格式化（带手或股或份为单位）
 */
fun Number.asHandFormat(info: StockStaticInfo? = null, showHandUnit: Boolean = true, ignoreZero: Boolean = false): String {
    return if (isInValid(ignoreZero)) {
        NO_DATA
    } else {
        val handUnit = if (showHandUnit) QuoteHelper.getStockUnit(info) else ""
        val handCount = info?.handCount ?: 1
        // 优品返回的已经是手了   科创板显示的是股，需要*每首股数
        if (info?.subType == SecSubType.TECHNOLOGY_INNOVATION_BOARD_STOCK) {
            Formatter.formatAmount(this.toDouble() * handCount) + handUnit
        } else {
            Formatter.formatAmount(this.toDouble()) + handUnit
        }

    }
}


/**
 * 根据值的正负来获取不同的颜色
 */
fun Number.getColor(context: Context): Int {
    return when {
        (this is Float) && this == DEFAULT_FLOAT -> QuoteHelper.getEvenColor(context)
        (this is Double) && this == DEFAULT_DOUBLE -> QuoteHelper.getEvenColor(context)
        MathUtil.isNegative(this) -> QuoteHelper.getFallColor(context)
        MathUtil.isPositive(this) -> QuoteHelper.getRiseColor(context)
        else -> QuoteHelper.getEvenColor(context)
    }
}

/**
 * 根据两个值对比的大小来获取不同的颜色
 */
fun Number.getColor(context: Context, other: Number): Int {
    return when {
        (this is Float) && this == DEFAULT_FLOAT -> QuoteHelper.getEvenColor(context)
        MathUtil.isNegative(this.toDouble() - other.toDouble()) -> QuoteHelper.getFallColor(context)
        MathUtil.isPositive(this.toDouble() - other.toDouble()) -> QuoteHelper.getRiseColor(context)
        else -> QuoteHelper.getEvenColor(context)
    }
}

fun Number.isInValid(ignoreZero: Boolean = false): Boolean {
    if (ignoreZero) {
        //价格为0表示没有值，显示--
        if (this is Float && (!isFinite() || this == 0f || this == DEFAULT_FLOAT)) {
            return true
        }
        if (this is Double && (!isFinite() || this == 0.0 || this == DEFAULT_DOUBLE)) {
            return true
        }
        if (this is Long && (this == 0L)) {
            return true
        }
        return false
    } else {
        //价格为0表示没有值，显示--
        if (this is Float && (!isFinite() || this == DEFAULT_FLOAT)) {
            return true
        }
        if (this is Double && (!isFinite() || this == DEFAULT_DOUBLE)) {
            return true
        }
        return false
    }
}
package com.example.module_homepage2.base

import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Created by dylan on 2019-08-27.
 * Desc: 格式化工具
 */
object Formatter {

    const val FORMAT_MONEY_GROUP = "#,##0.###"
    const val FORMAT_WITH_THREE_DECIMAL = "0.000"
    const val FORMAT_WITH_TWO_DECIMAL = "0.00"

    /**
     * 指定保留N位小数的运算，并转化为String返回
     *
     * 格式化用以下方式补0更加高效，可对比多种方式进行测试
     *
     * 循环执行1000次格式化
     * String.format("%.2f", 0.11332) --------------29ms
     * DecimalFormat("0.00").format(0.11332) -------96ms
     * 以下方式 ---------------------------------11ms
     */
    fun keepDecimalToString(v: Number, keepNum: Int): String {
        val num = MathUtil.keepDecimal(v, keepNum).toString()
        //判断还需补几个0在后面
        val needSuffixZero = keepNum - (num.split(".").getOrNull(1)?.length ?: 0)
        //如果需要就按要求补0，不需要则直接返回
        return if (needSuffixZero > 0) {
            val sb = StringBuilder(num)
            needSuffixZero.downTo(1).forEach {
                sb.append("0")
            }
            sb.toString()
        } else {
            num
        }

    }

    /**
     * 格式化数字
     *
     * @param value 待格式化的数字
     * @param keepNum 需要保留几位小数
     * @param isPercent 是否是百分比显示（如果百分比显示会乘以100）
     * @param showSign 是否要显示+号
     * @param showGroup 是否要按三个数字一组显示
     */
    fun format(value: Number, keepNum: Int = 2, isPercent: Boolean = false, showSign: Boolean = false, showGroup: Boolean = false): String {
        val sign = if (showSign && MathUtil.isPositive(value)) "+" else ""
        val temp = value.toDouble()
        return if (isPercent) {
            if (showGroup) {
                // add 1107: fix crash bug， format 方法参数，string 类型改为 double 类型
                //  NumberFormat.getInstance().format 方法若传入 String， 报错 Cannot format given Object as a Number
                sign + NumberFormat.getInstance().format(keepDecimalToString(temp * 100, keepNum).toDouble()) + "%"
            } else {
                sign + keepDecimalToString(temp * 100, keepNum) + "%"
            }
        } else {
            if (showGroup) {
                //同上
                sign + NumberFormat.getInstance().format(keepDecimalToString(temp, keepNum).toDouble())
            } else {
                sign + keepDecimalToString(temp, keepNum)
            }
        }
    }

    /**
     * 格式化数字（通过Decimal格式化，效率没那么高，但支持外部直接传入对应的Format来进行格式化，通用性更高一些）
     *
     * @param value 待格式化的数字
     * @param keepNum 需要保留几位小数
     * @param isPercent 是否是百分比显示（如果百分比显示会乘以100）
     * @param showSign 是否要显示+号
     * @param showGroup 是否要按三个数字一组显示
     */
    fun formatByDecimal(value: Number, format: String = FORMAT_WITH_TWO_DECIMAL, isPercent: Boolean = false,
                        showSign: Boolean = false, showGroup: Boolean = false): String {
        val sign = if (showSign && MathUtil.isPositive(value)) "+" else ""
        val df = DecimalFormat(format)
        if (showGroup) {
            df.isGroupingUsed = true
        }
        return if (isPercent) {
            sign + df.format(value.toDouble() * 100.0) + "%"
        } else {
            sign + df.format(value)
        }
    }

    /**
     * 格式化金额，默认设置一些参数，方便外部使用，也可直接调用format自己设置
     */
    fun formatMoney(value: Number, showSign: Boolean = false) = format(value, 2, showSign = showSign, showGroup = true)


    /**
     * 数量9999以内原样展示，超过1万缩写。数字保留4位，加上一位单位。比如：1.123万，25.42亿
     */
    fun formatAmount(value: Number, forceEnglish: Boolean = false, showUnit: Boolean = true): String {
        if (forceEnglish) return formatNumInEnglish(value, showUnit)
        return formatNumInChinese(value, showUnit)
    }

    /**
     * 格式化为中文
     *
     * @param ignoreMaxValue    小于该参数的数字不缩写
     * @param significantDigits 有效数字
     * @param isMoney           是否按资金格式化（资金保留两位小数）
     *
     * 规则：
     * 低于10万（不包含），原样显示整数；
    超过10万，显示2位小数，单位万；
    超过100万，显示1位小数，单位万；
    超过1000万，显示数字，单位万
    超过1亿，显示2位小数，单位亿，
    超过100亿，显示1位小数，单位亿，
    超过1000亿，显示数字，单位亿。
    超过1万亿，显示2位小数，单位万亿；
    超过100万亿，显示1位小数，单位万亿；
    超过1000万亿，显示数字，单位万亿
     */
    private fun formatNumInChinese(num: Number, showUnit: Boolean = true, ignoreMaxValue: Double = 100000.0,
                                   significantDigits: Int = 4, isMoney: Boolean = false, showPlus: Boolean = false): String {
        var positive = true
        // 结果字符串
        var resultStr: String
        // 判断正负值
        if (MathUtil.isNegative(num)) {
            positive = false
        }
        val temp = num.toDouble().absoluteValue
        //小于10w
        if (temp < ignoreMaxValue) {
            // 小于指定值不缩写，保留两位小数（金额保留两位小数，数量则只保留有效位）
            val df = DecimalFormat("##0.00").apply {
                decimalFormatSymbols = DecimalFormatSymbols().apply {
                    decimalSeparator = '.'
                }
            }
            resultStr = df.format(temp)
            if (!isMoney) { //去掉尾数0
                resultStr = resultStr.toBigDecimal().stripTrailingZeros().toPlainString()
            }
        } else if (temp >= 100000000000000.0) {
            // 处理大于100万亿的缩写
            val result = temp / 100000000000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, significantDigits) + "万亿"
            } else {
                formatToSignificantDigits(result, significantDigits)
            }
        } else if (temp >= 10000000000000.0) {  // 4位以上按万，亿，万亿缩写，缩写后保留4位有效数字
            // 处理大于10万亿的缩写
            val result = temp / 1000000000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, significantDigits) + "万亿"
            } else {
                formatToSignificantDigits(result, significantDigits)
            }
        } else if (temp >= 1000000000000.0) {  // 4位以上按万，亿，万亿缩写，缩写后保留4位有效数字
            // 处理大于万亿的缩写
            val result = temp / 1000000000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, 3) + "万亿"
            } else {
                formatToSignificantDigits(result, 3)
            }
        } else if (temp >= 10000000000.0) { //大于1000万,就按亿缩写
            // 处理大于亿的缩写
            val result = temp / 100000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, significantDigits) + "亿"
            } else {
                formatToSignificantDigits(result, significantDigits)
            }
        } else if (temp >= 1000000000.0) {
            // 处理大于10亿的缩写
            val result = temp / 100000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, significantDigits) + "亿"
            } else {
                formatToSignificantDigits(result, significantDigits)
            }
        } else if (temp >= 100000000.0) {
            // 处理大于亿的缩写
            val result = temp / 100000000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, 3) + "亿"
            } else {
                formatToSignificantDigits(result, 3)
            }
        } else {
            // 处理大于万的缩写
            val result = temp / 10000.0
            resultStr = if (showUnit) {
                formatToSignificantDigits(result, significantDigits) + "万"
            } else {
                formatToSignificantDigits(result, significantDigits)
            }
        }

        if (!positive) {
            resultStr = "-$resultStr"
        } else if (showPlus) {
            resultStr = "+$resultStr"
        }
        return resultStr
    }

    /**
     * 格式化为英文
     */
    private fun formatNumInEnglish(num: Number, showUnit: Boolean = true, ignoreMaxValue: Double = 1000.0,
                                   significantDigits: Int = 3, isMoney: Boolean = false, showPlus: Boolean = false): String {
        var positive = true
        var resultStr: String  // 结果字符串
        // 判断正负值
        // 判断正负值
        if (MathUtil.isNegative(num)) {
            positive = false
        }
        val temp = num.toDouble().absoluteValue
        if (temp < ignoreMaxValue) {
            // 小于指定值不缩写，保留两位小数（金额保留两位小数，数量则只保留有效位）
            val df = DecimalFormat("##0.00")
            resultStr = df.format(temp)
            if (!isMoney) {
                resultStr = resultStr.toBigDecimal().stripTrailingZeros().toPlainString()
            }
        } else if (temp >= 100000000000000.0) {
            // 处理大于trillion的缩写
            val result = temp / 1000000000000.0
            resultStr = formatToSignificantDigits(result, significantDigits) + if (showUnit) "T" else ""
        } else if (temp >= 1000000000000.0) {
            // 处理大于trillion的缩写
            val result = temp / 1000000000000.0
            resultStr = formatToSignificantDigits(result, 3) + if (showUnit) "T" else ""
        } else if (temp >= 100000000000.0) {
            // 处理大于billion的缩写
            val result = temp / 1000000000.0
            resultStr = formatToSignificantDigits(result, significantDigits) + if (showUnit) "B" else ""
        } else if (temp >= 1000000000.0) {
            // 处理大于billion的缩写
            val result = temp / 1000000000.0
            resultStr = formatToSignificantDigits(result, 3) + if (showUnit) "B" else ""
        } else if (temp >= 1000000.0) {
            // 处理大于million的缩写
            val result = temp / 1000000.0
            resultStr = formatToSignificantDigits(result, significantDigits) + if (showUnit) "M" else ""
        } else {
            // 处理大于k的缩写
            val result = temp / 1000.0
            resultStr = formatToSignificantDigits(result, significantDigits) + if (showUnit) "K" else ""
        }
        if (!positive) {
            resultStr = "-$resultStr"
        } else if (showPlus) {
            resultStr = "+$resultStr"
        }
        return resultStr
    }

    /**
     * 保留几位有效数字
     * @param number 原始数字
     * @param significantDigits 保留几位有效数字
     */
    private fun formatToSignificantDigits(number: Double, significantDigits: Int): String {
        //如果这个值的整数部分已经大于目标有效数字，直接就取整数部分即可
        //最后一位刚好是5 不会进位，因为实际是0.4999999999
        val plusNum = number + 0.0000000001
        val numberIntString = number.roundToInt().toString()
        return if (numberIntString.length > significantDigits) {
            numberIntString
        } else {
            val bd = BigDecimal(plusNum, MathContext(significantDigits))
            bd.toPlainString()
        }
    }
}
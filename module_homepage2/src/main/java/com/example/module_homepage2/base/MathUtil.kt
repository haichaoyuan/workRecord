package com.example.module_homepage2.base

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Created by dylan on 2018/3/30.
 * Desc: 计算工具
 */
object MathUtil {


    var precision = 0.0000001

    /**
     * 指定保留N位小数的运算
     */
    fun keepDecimal(v: Number, keepNum: Int): Double {
        val temp = 10.0.pow(keepNum.toDouble())
        return (v.toDouble() * temp).roundToLong() / temp
    }

    /**
     * 判断一个数据是否大于0
     */
    fun isPositive(v: Number): Boolean {
        return v.toDouble() - 0 > precision
    }
    /**
     * 判断一个数据是否小于0
     */
    fun isNegative(v: Number): Boolean {
        return 0 - v.toDouble() > precision
    }

    /**
     * 判断两个值是否相等
     */
    fun isEqual(v: Number, v2: Number): Boolean {
        return abs(v.toDouble() - v2.toDouble()) < precision
    }
}


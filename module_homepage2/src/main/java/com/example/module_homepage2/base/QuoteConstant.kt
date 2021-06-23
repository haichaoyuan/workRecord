package com.example.module_homepage2.base

/**
 *
 * Author: chuanchao
 * Data: 2020/12/7
 * Desc: 常量定义
 *
 */
class QuoteConstant {

    companion object {

        //指定非0默认值是因为有些值0是有意义的（比如涨跌、涨跌幅）
        const val DEFAULT_FLOAT = -9999f
        const val DEFAULT_DOUBLE = -9999.0
        const val DEFAULT_INT = -1
        const val DEFAULT_PRECISE = 2

        const val UP_ERROR_MSG = "优品请求出错"

        const val NO_DATA = "--"

    }
}
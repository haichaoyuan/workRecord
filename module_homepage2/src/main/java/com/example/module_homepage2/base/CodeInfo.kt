package com.example.module_homepage2.base

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * Author: chuanchao
 * Data: 2020/12/10
 * Desc: 请求用
 *
 */
@Parcelize
data class CodeInfo(val exch: Exch, val code: String, val alias: String = "") : Parcelable {

    companion object {

        /**
         * 老的自选示例 (010000-000001)
         */
        fun convertFromOld(codeStr: String): CodeInfo {

            val split = codeStr.split("-")
            val exch = when (split.firstOrNull()) {
                "000000" -> Exch.SZSE
                "020000" -> Exch.HKEX
                "030000" -> Exch.BLOCK
                "040000" -> Exch.SSEQ
                "050000" -> Exch.NEEQ
                else -> Exch.SSE
            }
            val code = split.lastOrNull()

            return CodeInfo(exch, code ?: "")
        }

    }

    override fun toString(): String {
        return "".plus(exch).plus(code)
    }

    //这些转换真的烦

    /**
     * 自选用
     */
    fun getNewCodeInfoStr(): String {
        return getOldExchCode() + "-" + code
    }


    /**
     * 提醒用
     */
    fun getCodeInfoStr(): String {
        return getNewExchCode() + "-" + code;
    }


    fun getNewExchCode(): String {
        return when (exch) {
            Exch.SZSE -> "00"
            Exch.HKEX -> "02"
            Exch.BLOCK -> "03"
            Exch.SSEQ -> "04"
            Exch.NEEQ -> "05"
            else -> "01"
        }
    }

    /**
     * 添加自选需要把市场 转成6位字符串(老规则)
     * SZSE                  = 0;
     * SSE                   = 1;
     * HKEX                  = 2;
     * CEFC                  = 3;//板块
     * SSEO                  = 4;//上证期权
     * NEEQ                  = 5;//股转（新三板）
     */
    private fun getOldExchCode(): String {
        return when (exch) {
            Exch.SZSE -> "000000"
            Exch.HKEX -> "020000"
            Exch.BLOCK -> "030000"
            Exch.SSEQ -> "040000"
            Exch.NEEQ -> "050000"
            else -> "010000"
        }
    }

}
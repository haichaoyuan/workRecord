package com.example.module_homepage2.base

object NumberUtil {
    /**
     * 强制转换为 int
     */
    @JvmStatic
    @JvmOverloads
    fun toInt(str: String?, default: Int = 0): Int {
        if (str == null) {
            return default
        }
        return try {
            str.toInt()
        } catch (e: Exception) {
            default
        }

    }

    /**
     * 强制转换为 double
     */
    @JvmStatic
    @JvmOverloads
    fun toDouble(str: String?, default: Double = 0.0): Double {
        if (str == null) {
            return default
        }
        return try {
            str.toDouble()
        } catch (e: Exception) {
            default
        }

    }

}
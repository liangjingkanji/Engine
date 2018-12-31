/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils

import android.app.Activity
import android.util.DisplayMetrics
import androidx.annotation.IntDef
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.regex.Pattern

/*
    单位换算工具
*/
object UnitUtils {


    @IntDef(MSEC, SEC, MIN, HOUR, DAY)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Memory

    @IntDef(BYTE, KB, MB, GB)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Time


    const val BYTE = 1
    const val KB = 1024
    const val MB = 1048576
    const val GB = 1073741824
    const val MSEC = 1
    const val SEC = 1000
    const val MIN = 60000
    const val HOUR = 3600000
    const val DAY = 86400000

    /**
     * 距离单位超过千米自动换算km
     *
     * @param distance 单位/m
     */
    fun distance(distance: Float): String {

        return if (distance >= 1000) {
            val format = DecimalFormat("#.##")
            format.format(distance / 1000.0) + "km"
        } else {
            distance.toInt().toString() + "m"
        }
    }
}


/*
 * 判断是否为浮点数，包括double和float
 * @param str 传入的字符串
 * @return 是浮点数返回true,否则返回false
 */
fun String.isDouble(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[.\\d]*$")
    return pattern.matcher(this)
        .matches()
}

/*
 * 判断是否为整数
 * @param str 传入的字符串
 * @return 是整数返回true,否则返回false
 */
fun String.isInteger(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
    return pattern.matcher(this)
        .matches()
}


/**
 * 是否是数字
 * @receiver String
 * @return Boolean
 */
fun String.isNumber(): Boolean {
    return if (this == ".") {
        false
    } else {
        isDouble() || isInteger()
    }
}

/**
 * 保留两个小数点
 */
fun Double.m2(): String {
    val df = DecimalFormat("#.00")
    val d = df.format(this)
    return when {
        d == ".00" -> "0"
        d.substring(0, 1) == "." -> "0$d"
        else -> d
    }
}

/**
 * 保留两个小数点
 */
fun Float.m2(): String {
    val df = DecimalFormat("#.00")
    val d = df.format(this)
    return when {
        d == ".00" -> "0"
        d.substring(0, 1) == "." -> "0$d"
        else -> d
    }
}

// 屏幕宽度（像素）
fun Activity.getWith(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}

// 屏幕高度（像素）
fun Activity.getHeight(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.heightPixels
}

/**
 * 数字转成百分比形式 (1 = 100%)
 * @receiver String
 * @param fraction Boolean
 * @param decimalCount Int
 * @return String
 */
fun String.percent(fraction: Boolean, decimalCount: Int = 2): String {

    if (!isNumber()) {
        return ""
    }

    val numberFormat = NumberFormat.getPercentInstance()

    if (fraction) {
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.minimumFractionDigits = decimalCount
    }

    return try {
        numberFormat.format(this)
    } catch (e: Exception) {
        ""
    }
}

fun Double.percent(fraction: Boolean, decimalCount: Int = 2): String {

    val numberFormat = NumberFormat.getPercentInstance()

    if (fraction) {
        numberFormat.maximumFractionDigits = decimalCount
        numberFormat.minimumFractionDigits = decimalCount
    }

    return try {
        numberFormat.format(this)
    } catch (e: Exception) {
        ""
    }
}


fun Double?.format(
    roundingMode: RoundingMode = RoundingMode.UP
): String {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = roundingMode
    return numberFormat.format(this ?: 0.0)
}


/**
 * 默认看做 "分" 处理(除以100)
 */
fun Long?.format(
    roundingMode: RoundingMode = RoundingMode.UP
): String {

    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = roundingMode
    return numberFormat.format((this ?: 0) / 100.0)
}


fun String?.format(
    roundingMode: RoundingMode = RoundingMode.UP
): String {
    return when {
        isNullOrEmpty() -> "0.00"
        this!!.contains(",") || this.contains("¥") -> this
        else -> {
            val numberFormat = NumberFormat.getInstance()
            numberFormat.minimumFractionDigits = 2
            numberFormat.maximumFractionDigits = 2
            numberFormat.roundingMode = roundingMode
            try {
                numberFormat.format(this)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                "0.00"
            }
        }
    }

}

fun BigDecimal.format(
    roundingMode: RoundingMode = RoundingMode.UP
): String {
    return setScale(2, roundingMode).toPlainString()
}

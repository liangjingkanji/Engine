/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package com.drake.engine.utils

import android.app.Activity
import android.util.DisplayMetrics
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import com.drake.engine.base.getApp
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
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
    fun toKM(distance: Float): String {

        return if (distance >= 1000) {
            val format = DecimalFormat("#.##")
            format.format(distance / 1000.0) + "km"
        } else {
            distance.toInt().toString() + "m"
        }
    }

    /**
     * 判断目标时间是否大于当前时间
     *
     * @param dateStr 目标时间
     */
    fun greaterThanCurDate(dateStr: String): Boolean {
        val time = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.CHINA)
        try {
            val date = format.parse(dateStr)
            return time < date!!.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: Long, format: String = "yyyy-MM-dd"): String {
        val date = Date(millis)
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return sf.format(date)
    }

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: String?, format: String = "yyyy-MM-dd"): String {
        if (millis.isNullOrEmpty()) {
            return ""
        }
        val date = Date(java.lang.Long.parseLong(millis))
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return sf.format(date)
    }
}


// converts dp value into px
fun Int.px(): Int = (this * getApp().resources.displayMetrics.density).toInt()

fun Float.px(): Int = (this * getApp().resources.displayMetrics.density).toInt()

// converts sp value into px
fun Int.sp2px(): Int = (this * getApp().resources.displayMetrics.scaledDensity).toInt()

fun Float.sp2px(): Int = (this * getApp().resources.displayMetrics.scaledDensity).toInt()

// converts px value into dp
fun Int.dp(): Float = this.toFloat() / getApp().resources.displayMetrics.density

fun Float.dp(): Float = this.toFloat() / getApp().resources.displayMetrics.density

// converts px value into sp
fun Int.sp(): Float = this / getApp().resources.displayMetrics.scaledDensity

fun Float.sp(): Float = this / getApp().resources.displayMetrics.scaledDensity

fun dimen(@DimenRes resource: Int): Int = getApp().resources.getDimensionPixelSize(resource)


/**
 * 是否为浮点数, 无论Float或者Double
 * @receiver String
 * @return Boolean
 */
fun String.isDouble(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[.\\d]*$")
    return pattern.matcher(this).matches()
}

/**
 * 是否为整数
 * @receiver String
 * @return Boolean
 */
fun String.isInteger(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
    return pattern.matcher(this).matches()
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

/**
 * 屏幕宽度
 * @receiver Activity
 * @return Int 像素单位
 */
fun Activity.getWith(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}

/**
 * 屏幕高度
 * @receiver Activity
 * @return Int 像素单位
 */
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




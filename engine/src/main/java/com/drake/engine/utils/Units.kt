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

import androidx.annotation.IntDef
import com.drake.engine.base.app
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
object Units {

    //<editor-fold desc="Memory">
    @IntDef(BYTE, KB, MB, GB)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Memory

    const val BYTE = 1
    const val KB = 1024
    const val MB = 1048576
    const val GB = 1073741824 //</editor-fold>


    //<editor-fold desc="Time">
    @IntDef(MSEC, SEC, MIN, HOUR, DAY)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Time

    const val MSEC = 1
    const val SEC = 1000
    const val MIN = 60000
    const val HOUR = 3600000
    const val DAY = 86400000

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: Long, format: String? = "yyyy-MM-dd"): String {
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(millis)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        return sf.format(date)
    }

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: String?, format: String? = "yyyy-MM-dd"): String {
        if (millis.isNullOrEmpty()) {
            return ""
        }
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(java.lang.Long.parseLong(millis))
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        return sf.format(date)
    } //</editor-fold>

    /**
     * 距离单位超过千米自动换算km
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
}


//<editor-fold desc="Display">
@Deprecated("rename", ReplaceWith("dp"), DeprecationLevel.ERROR)
fun Int.px(): Int = (this * app.resources.displayMetrics.density).toInt()

@Deprecated("rename", ReplaceWith("dp"), DeprecationLevel.ERROR)
fun Float.px(): Int = (this * app.resources.displayMetrics.density).toInt()

@Deprecated("rename", ReplaceWith("sp"), DeprecationLevel.ERROR)
fun Int.sp2px(): Int = (this * app.resources.displayMetrics.scaledDensity).toInt()

@Deprecated("rename", ReplaceWith("sp"), DeprecationLevel.ERROR)
fun Float.sp2px(): Int = (this * app.resources.displayMetrics.scaledDensity).toInt()

@Deprecated("rename", ReplaceWith("toDp()"), DeprecationLevel.ERROR)
fun Int.dp(): Float = this.toFloat() / app.resources.displayMetrics.density

@Deprecated("rename", ReplaceWith("toDp()"), DeprecationLevel.ERROR)
fun Float.dp(): Float = this.toFloat() / app.resources.displayMetrics.density

@Deprecated("rename", ReplaceWith("toSp()"), DeprecationLevel.ERROR)
fun Int.sp(): Float = this / app.resources.displayMetrics.scaledDensity

@Deprecated("rename", ReplaceWith("toSp()"), DeprecationLevel.ERROR)
fun Float.sp(): Float = this / app.resources.displayMetrics.scaledDensity

/*converts dp value into px*/
val Int.dp
    get() = (this * app.resources.displayMetrics.density).toInt()

/*converts dp value into px*/
val Float.dp
    get() = (this * app.resources.displayMetrics.density).toInt()

/*converts sp value into px*/
val Int.sp
    get() = (this * app.resources.displayMetrics.scaledDensity).toInt()

/*converts sp value into px*/
val Float.sp
    get() = (this * app.resources.displayMetrics.scaledDensity).toInt()

/*converts px value into dp*/
fun Int.toDp(): Float = this.toFloat() / app.resources.displayMetrics.density

/*converts px value into dp*/
fun Float.toDp(): Float = this.toFloat() / app.resources.displayMetrics.density

/*converts px value into sp*/
fun Int.toSp(): Float = this / app.resources.displayMetrics.scaledDensity

/*converts px value into sp*/
fun Float.toSp(): Float = this / app.resources.displayMetrics.scaledDensity

//</editor-fold>


//<editor-fold desc="Number">
/**
 * 是否为浮点数, 无论Float或者Double
 */
fun String.isDouble(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[.\\d]*$")
    return pattern.matcher(this).matches()
}

/**
 * 是否为整数
 */
fun String.isInteger(): Boolean {
    val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
    return pattern.matcher(this).matches()
}


/**
 * 是否是数字
 */
fun String.isNumber(): Boolean {
    return if (this == ".") {
        false
    } else {
        isDouble() || isInteger()
    }
}

//</editor-fold>

//<editor-fold desc="Memory">

val Number.KB: Long
    get() {
        return when (this) {
            is Int -> this * 1024L
            is Float -> (this * 1024).toLong()
            is Double -> (this * 1024).toLong()
            is Short -> this * 1024L
            is Long -> this * 1024L
            else -> throw java.lang.IllegalArgumentException("Unsupported type: $this")
        }
    }

val Number.MB: Long
    get() = this.KB * 1024

val Number.GB: Long
    get() = this.MB * 1024

//</editor-fold>

//<editor-fold desc="Fraction">
/**
 * # 表示非0补齐,比如 3.001 ->0.##-> 3
 * 0 表示补齐0,比如3.00 ->0.00-> 3.00
 * % 表示乘以 100 和作为百分比显示 10.3001->#.00#%->1030.01%
 * */
fun Number.fractionDigitPattern(pattern: String = "0.##"): String {
    val decimalFormat = DecimalFormat(pattern)
    return decimalFormat.format(this)
}

fun Double?.fractionDigit(roundingMode: RoundingMode = RoundingMode.UP): String {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = roundingMode
    return numberFormat.format(this ?: 0.0)
}


/**
 * 默认看做 "分" 处理(除以100)
 */
fun Long?.fractionDigit(roundingMode: RoundingMode = RoundingMode.UP): String {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = roundingMode
    return numberFormat.format((this ?: 0) / 100.0)
}


fun String?.fractionDigit(roundingMode: RoundingMode = RoundingMode.UP): String {
    return when {
        isNullOrEmpty() -> "0.00"
        this.contains(",") || this.contains("¥") -> this
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

fun BigDecimal.fractionDigit(roundingMode: RoundingMode = RoundingMode.UP): String {
    return setScale(2, roundingMode).toPlainString()
} //</editor-fold>




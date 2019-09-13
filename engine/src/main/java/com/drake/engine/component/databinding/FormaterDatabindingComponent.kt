/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.databinding

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.drake.engine.utils.DateUtils
import com.drake.engine.utils.format


/**
 * 给TextView设置软妹币
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: String?) {
    if (!number.isNullOrEmpty()) {
        val format = "¥${number.format()}"

        if (format != text.toString())
            text = format
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: Double) {
    val format = "¥${number.format()}"
    if (format != text.toString())
        text = format
}

/**
 * 设置rmb，已经除100
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: Long) {
    val format = "¥${number.format()}"

    if (format != text.toString())
        text = format
}

/**
 * 格式化数字(Ceil)
 * 默认保留两位小数
 */
@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.setFormatNumber(number: Double?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.setFormatNumber(number: Long?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}

/**
 * 格式化数字(Ceil)
 * 默认保留两位小数
 */
@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.formatNumber(number: String?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}

/**
 * 隐藏手机号码
 */
@BindingAdapter("hiddenTel")
fun TextView.setHiddenTel(str: String?) {
    if (str.isNullOrEmpty()) {
        return
    }
    val spitText = str.substring(0, 3) + "****" + str.substring(7)
    text = spitText
}

/**
 * 隐藏的身份证号码
 */
@BindingAdapter("hiddenIdCard")
fun TextView.setHiddenIdCard(str: String?) {
    if (str.isNullOrEmpty() && text.toString() == str) {
        return
    }

    val result = str!!.substring(0, 3) + "********" + text.substring(5)
    text = result
}


@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun TextView.setDateFromMillis(milli: Long, format: String) {

    val formatText = DateUtils.formatDate(milli, format)
    val oldText = text.toString()

    if (milli == 0L || formatText == oldText) {
        return
    }

    text = formatText
}

/**
 * 根据毫秒值来显示时间
 */
@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun TextView.setDateFromMillis(milli: String?, format: String) {

    val formatText = DateUtils.formatDate(milli, format)
    val oldText = text.toString()

    if (milli.isNullOrEmpty() || formatText == oldText) {
        return
    }

    text = formatText
}

/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.databinding

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.drake.engine.utils.DateUtils
import com.drake.engine.utils.format


// <editor-fold desc="人民币">

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

// </editor-fold>

// <editor-fold desc="前缀后缀">

/**
 * 格式化数字(Ceil)
 * 默认保留两位小数
 */

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.setPrefixPostFix(number: Double?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.setPrefixPostFix(number: Long?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}


@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["format", "prefix", "postfix"], requireAll = false)
fun TextView.setPrefixPostFix(number: String?, prefix: String = "", postfix: String = "") {
    val format = "$prefix${number.format()}$postfix"
    if (format != text.toString())
        text = format
}

// </editor-fold>


// <editor-fold desc="时间">

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

@BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: Long, format: String) {
    var temp = format
    if (TextUtils.isEmpty(temp)) {
        temp = "yyyy-MM-dd"
    }

    val formatText = DateUtils.formatDate(second * 1000, temp)

    val oldText = text.toString()
    if (second == 0L || formatText == oldText) {
        return
    }
    text = formatText
}

@BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: String, format: String) {
    var temp = format

    if (TextUtils.isEmpty(temp)) {
        temp = "yyyy-MM-dd"
    }

    val formatText = DateUtils.formatDate(java.lang.Long.parseLong(second) * 1000, temp)

    val oldText = text.toString()

    if (TextUtils.isEmpty(second) || formatText == oldText) {
        return
    }
    text = formatText
}

// </editor-fold>
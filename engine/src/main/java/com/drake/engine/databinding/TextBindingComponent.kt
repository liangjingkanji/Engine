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

package com.drake.engine.databinding

import android.annotation.SuppressLint
import android.graphics.Paint
import android.webkit.WebView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.math.RoundingMode
import java.text.NumberFormat

object TextBindingComponent {

    // <editor-fold desc="货币">

    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb")
    @JvmStatic
    fun TextView.formatCNY(number: String?) {
        if (!number.isNullOrEmpty()) {
            val format = "¥${number.format()}"
            if (format != text.toString()) text = format
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb")
    @JvmStatic
    fun TextView.formatCNY(number: Double) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = RoundingMode.UP
        val format = "¥${numberFormat.format(number ?: 0.0)}"
        if (format != text.toString()) text = format
    }

    /**
     * 设置rmb，已经除100
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb")
    @JvmStatic
    fun TextView.formatCNY(number: Long) {
        /**
         * 默认看做 "分" 处理(除以100)
         */
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = RoundingMode.UP
        val format = "¥${numberFormat.format(number / 100.0)}"
        if (format != text.toString()) text = format
    }

    // </editor-fold>

    @BindingAdapter("del")
    @JvmStatic
    fun TextView.setDel(isAdd: Boolean) {
        if (isAdd) {
            paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   // 设置中划线并加清晰
        }
    }

    @BindingAdapter("url")
    @JvmStatic
    fun WebView.setUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
        }
    }

}
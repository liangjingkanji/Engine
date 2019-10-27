/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar

/**
 * 通过过滤器可以判断是否支持手势划动
 */
class FilterSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatSeekBar(context, attrs) {


    var onFilter: (FilterSeekBar.() -> Boolean)? = null

    fun onFilter(block: FilterSeekBar.() -> Boolean) {
        this.onFilter = block
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return onFilter?.invoke(this) ?: false
    }
}

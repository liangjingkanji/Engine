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

import androidx.viewpager.widget.ViewPager

/**
 *可设置是否支持划动
 */
class FixedViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    var touchEnabled = false // 默认不支持划动

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return touchEnabled
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return touchEnabled
    }
}

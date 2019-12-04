/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.base

import android.graphics.Color
import android.view.MotionEvent
import androidx.databinding.ViewDataBinding
import com.drake.engine.swipeback.SwipeBackHelper

abstract class EngineSwipeActivity<B : ViewDataBinding> : EngineActivity<B>() {

    private var swipeBackHelper: SwipeBackHelper? = null

    var swipeEnable = true
        set(value) {
            field = value
            swipeBackHelper?.setEnable(field)
        }

    override fun init() {
        swipeBackHelper = SwipeBackHelper(this)
        swipeBackHelper?.setBackgroundColor(Color.WHITE)
        super.init()

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        swipeBackHelper?.dispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        swipeBackHelper?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

}
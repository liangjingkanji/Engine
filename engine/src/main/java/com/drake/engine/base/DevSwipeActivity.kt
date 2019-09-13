/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.base

import android.graphics.Color
import android.os.Build
import android.view.MotionEvent
import androidx.databinding.ViewDataBinding
import com.drake.engine.component.swipeback.SwipeBackHelper
import org.jetbrains.anko.doFromSdk

abstract class DevSwipeActivity<B : ViewDataBinding> : DevActivity<B>() {

    private var swipeBackHelper: SwipeBackHelper? = null

    var swipeEnable = true
        set(value) {
            field = value
            doFromSdk(Build.VERSION_CODES.KITKAT) {
                swipeBackHelper?.setEnable(field)
            }
        }

    override fun init() {
        doFromSdk(Build.VERSION_CODES.KITKAT) {
            swipeBackHelper = SwipeBackHelper(this)
            swipeBackHelper?.setBackgroundColor(Color.WHITE)
        }
        super.init()

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        swipeBackHelper?.dispatchTouchEvent(ev!!)
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        swipeBackHelper?.onTouchEvent(event!!)
        return super.onTouchEvent(event)
    }

}
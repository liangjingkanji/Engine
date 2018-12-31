/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.base

import android.graphics.Color
import android.os.Build
import android.view.MotionEvent
import androidx.databinding.ViewDataBinding
import com.designer.library.component.swipeback.SwipeBackHelper
import org.jetbrains.anko.doFromSdk

abstract class SwipeBackActivity<B : ViewDataBinding> : BaseActivity<B>() {

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
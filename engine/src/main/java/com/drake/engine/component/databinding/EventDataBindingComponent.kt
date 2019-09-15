/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.databinding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import androidx.databinding.BindingAdapter
import com.drake.engine.widget.SmoothCheckBox
import com.jakewharton.rxbinding3.view.clicks
import java.util.concurrent.TimeUnit


// <editor-fold desc="状态">

@BindingAdapter("enable")
fun View.setEnableBind(enable: Boolean) {
    isEnabled = enable
}

@BindingAdapter("enable")
fun View.setEnableBind(enable: Any?) {
    isEnabled = enable != null
}

@BindingAdapter("selected")
fun View.setSelectedBind(selected: Boolean) {
    isSelected = selected
}

@BindingAdapter("selected")
fun View.setSelectedBind(selected: Any?) {
    isSelected = selected != null
}


@BindingAdapter("activated")
fun View.setActivatedBind(activated: Boolean) {
    isActivated = activated
}


@BindingAdapter("activated")
fun View.setActivatedBind(activated: Any?) {
    isActivated = activated != null
}

@BindingAdapter("checked")
fun SmoothCheckBox.setCheckedBind(checked: Boolean) {
    this.isChecked = checked
}

@BindingAdapter("checked")
fun SmoothCheckBox.setCheckedBind(checked: Any?) {
    this.isChecked = checked != null
}

// </editor-fold>


// <editor-fold desc="点击事件">

/**
 * 防止暴力点击
 */
@SuppressLint("CheckResult")
@BindingAdapter("click")
fun View.setPreventClickListener(onClickListener: View.OnClickListener?) {
    if (onClickListener != null) {
        clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { onClickListener.onClick(this) }
    }
}

/**
 * 自动将点击事件映射到Activity上
 *
 * @param isPrevent 是否只支持快速点击
 */
@SuppressLint("CheckResult")
@BindingAdapter("hit")
fun View.hit(isPrevent: Boolean = true) {
    var context = context

    while (context is ContextWrapper) {
        if (context is View.OnClickListener) {
            val clickListener = context as View.OnClickListener

            val observable = clicks()

            if (isPrevent) {
                observable.throttleFirst(500, TimeUnit.MILLISECONDS)
            }

            observable.subscribe { clickListener.onClick(this) }
        }
        context = context.baseContext
    }
}


/**
 * 关闭当前界面
 *
 * @param enabled 是否启用
 */
@SuppressLint("CheckResult")
@BindingAdapter("finish")
fun View.finishActivity(enabled: Boolean = true) {
    if (enabled) {
        var temp = context
        var activity: Activity? = null

        while (temp is ContextWrapper) {
            if (temp is Activity) {
                activity = temp
            }
            temp = temp.baseContext
        }

        val finalActivity = activity

        clicks().throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finalActivity!!.finishAfterTransition()
            } else {
                finalActivity!!.finish()
            }
        }
    }
}

// </editor-fold>



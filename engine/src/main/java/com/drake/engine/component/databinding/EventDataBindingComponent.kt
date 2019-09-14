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
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding3.view.clicks
import java.util.concurrent.TimeUnit

/**
 * 针对事件处理组件
 */

// <editor-fold desc="状态">

/**
 * 设置当前的View不可用状态
 */
@BindingAdapter("enable")
fun View.setViewEnable(enable: Boolean) {
    isEnabled = enable
}

@BindingAdapter("selected")
fun View.setViewSelected(selected: Boolean) {
    isSelected = selected
}

/**
 * 如果传入对象不为null则激活
 */
@BindingAdapter("enable")
fun View.setViewEnable(enable: Any?) {
    isEnabled = enable != null
}

@BindingAdapter("selected")
fun View.setViewSelected(selected: Any?) {
    isSelected = selected != null
}


/**
 * 设置激活状态
 *
 * @param activated 根据布尔类型来判断是否激活
 */
@BindingAdapter("activated")
fun View.setViewActivated(activated: Boolean) {
    isActivated = activated
}

/**
 * 设置激活状态
 *
 * @param activated 如果对象不为null则激活
 */
@BindingAdapter("activated")
fun View.setViewActivated(activated: Any?) {
    isActivated = activated != null
}

// </editor-fold>

// <editor-fold desc="点击事件">

/**
 * 防止暴力点击
 */
@SuppressLint("CheckResult")
@BindingAdapter("click")
fun View.setPreventClickListener(onClickListener: OnClickListener?) {
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
@BindingAdapter("app:hit")
fun View.hitView(isPrevent: Boolean) {
    var context = context

    while (context is ContextWrapper) {
        if (context is OnClickListener) {
            val clickListener = context as OnClickListener

            val observable = clicks()

            if (isPrevent) {
                observable.throttleFirst(500, TimeUnit.MILLISECONDS)
            }

            observable.subscribe { clickListener.onClick(this) }
        }
        context = context.baseContext
    }
}


class EventDataBindingComponent {
    companion object {
        /**
         * 自动将点击事件映射到Activity上
         *
         * @param isPrevent 是否只支持快速点击
         */
        @JvmStatic
        @SuppressLint("CheckResult")
        @BindingAdapter("android:hit")
        fun View.setHitView(isPrevent: Boolean) {
            var context = context

            while (context is ContextWrapper) {
                if (context is OnClickListener) {
                    val clickListener = context as OnClickListener

                    val observable = clicks()

                    if (isPrevent) {
                        observable.throttleFirst(500, TimeUnit.MILLISECONDS)
                    }

                    observable.subscribe { clickListener.onClick(this) }
                }
                context = context.baseContext
            }
        }
    }
}


/**
 * 自动将点击事件映射到Activity上
 *
 * @param isPrevent 是否只支持快速点击
 */
@SuppressLint("CheckResult")
@BindingAdapter("app:hit")
fun View.setHitView(isPrevent: Boolean) {
    var context = context

    while (context is ContextWrapper) {
        if (context is OnClickListener) {
            val clickListener = context as OnClickListener

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
 * @param isFinish 是否启用
 */
@SuppressLint("CheckResult")
@BindingAdapter("finish")
fun View.finishCurrentActivity(isFinish: Boolean) {
    if (isFinish) {
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
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                finalActivity!!.finishAfterTransition()
            } else {
                finalActivity!!.finish()
            }
        }
    }
}

// </editor-fold>


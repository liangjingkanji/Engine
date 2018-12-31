/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.databinding

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 快速支持DataBinding
 */

fun <A : ViewDataBinding> Activity.setContentView(@LayoutRes layout: Int): A {
    return DataBindingUtil.setContentView(this, layout)
}

fun <A : ViewDataBinding> ViewGroup.inflate(@LayoutRes layout: Int): A {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layout, this, false)
}

fun <A : ViewDataBinding> View.bind(): A {
    return DataBindingUtil.bind(this)!!
}
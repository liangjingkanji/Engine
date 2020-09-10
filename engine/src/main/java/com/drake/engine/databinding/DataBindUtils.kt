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

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <A : ViewDataBinding> Activity.setContent(@LayoutRes layout: Int): A {
    return DataBindingUtil.setContentView(this, layout)
}

fun <A : ViewDataBinding> ViewGroup.inflate(@LayoutRes layout: Int): A {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layout, this, false)
}

fun <A : ViewDataBinding> Context.inflate(@LayoutRes layout: Int): A {
    return DataBindingUtil.inflate(LayoutInflater.from(this), layout, null, false)
}

fun <A : ViewDataBinding> View.bind(): A {
    return DataBindingUtil.bind(this)!!
}
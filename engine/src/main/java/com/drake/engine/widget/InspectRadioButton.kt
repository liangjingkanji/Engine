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
package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

/** 可以在RadioButton点击后判断是否拦截, 如果拦截则不会产生切换效果 */
class InspectRadioButton(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatRadioButton(context, attrs) {

    private var interceptor: Interceptor? = null

    /** 拦截器 */
    fun setInterceptor(listener: Interceptor) {
        this.interceptor = listener
    }

    override fun toggle() {
        if (interceptor?.onIntercept() != true) super.toggle()
    }

    /** 切换状态前拦截点击事件 */
    interface Interceptor {
        /** 返回true表示拦截 */
        fun onIntercept(): Boolean
    }

}
/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.drake.engine.R

/**
 * 解决 IllegalArgumentException: pointerIndex out of range
 */
class FixedViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    /** 是否支持划动 属性设置 [app:pager_scrollable] */
    var scrollable: Boolean = true

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FixedViewPager)
        scrollable = attributes.getBoolean(R.styleable.FixedViewPager_pager_scrollable, true)
        attributes.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (scrollable) {
            try {
                return super.onInterceptTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (scrollable) {
            try {
                return super.onTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
}
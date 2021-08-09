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

    /** 是否支持划动 */
    var scrollable: Boolean = true

    /** 该属性可以启用高度为wrap_content, ViewPager默认是match_parent */
    var wrapHeight: Boolean = false

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FixedViewPager)
        scrollable = attributes.getBoolean(R.styleable.FixedViewPager_pager_scrollable, true)
        wrapHeight = attributes.getBoolean(R.styleable.FixedViewPager_wrap_height, false)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (wrapHeight) {
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                if (h > height) height = h
            }
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
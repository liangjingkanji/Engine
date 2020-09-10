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
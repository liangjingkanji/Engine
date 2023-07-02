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

package com.drake.engine.base

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class EngineActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    FinishBroadcastActivity(contentLayoutId), OnClickListener {

    lateinit var binding: B
    lateinit var rootView: View

    private var onTouchEvent: (MotionEvent.() -> Boolean)? = null

    override fun setContentView(layoutResId: Int) {
        rootView = layoutInflater.inflate(layoutResId, null)
        setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)!!
        init()
    }

    open fun init() {
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()
    override fun onClick(v: View) {}

    // <editor-fold desc="生命周期">

    /**
     * 触摸事件
     * @param block 返回值表示是否拦截事件
     */
    fun onTouchEvent(block: MotionEvent.() -> Boolean) {
        onTouchEvent = block
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val b = super.dispatchTouchEvent(event)
        return onTouchEvent?.invoke(event) ?: b
    }

    fun requireActivity(): AppCompatActivity {
        return this
    }

    // </editor-fold>


    // <editor-fold desc="界面关闭">

    /**
     * 关闭界面
     */
    fun finishTransition() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            super.finish()
        }
    }

    // </editor-fold>
}

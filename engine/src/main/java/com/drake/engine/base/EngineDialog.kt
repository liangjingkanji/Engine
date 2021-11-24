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

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class EngineDialog<B : ViewDataBinding>(context: Context, @StyleRes themeResId: Int = 0) :
    Dialog(context, themeResId), OnClickListener {

    lateinit var binding: B

    override fun setContentView(layoutResID: Int) {
        binding = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        setContentView(binding.root)
        init()
    }

    open fun init() {
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure")
            e.printStackTrace()
        }
    }

    protected abstract fun initView()
    protected abstract fun initData()
    override fun onClick(v: View) {}
}

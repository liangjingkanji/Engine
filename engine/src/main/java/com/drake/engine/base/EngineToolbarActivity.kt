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

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.drake.engine.R

/**
 * 在项目layout目录下创建`engine_toolbar.xml`可以覆写标题栏布局
 */
abstract class EngineToolbarActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    EngineActivity<B>(contentLayoutId) {

    lateinit var rootViewWithoutToolbar: View
    lateinit var toolbar: LinearLayout
    lateinit var toolbarLeft: TextView
    lateinit var toolbarRight: TextView
    lateinit var toolbarTitle: TextView

    override fun setTitle(title: CharSequence?) {
        if (this::toolbarTitle.isInitialized) toolbarTitle.text = title ?: return
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    @SuppressLint("InflateParams")
    override fun setContentView(layoutResId: Int) {
        rootView = layoutInflater.inflate(R.layout.layout_engine_toolbar, null)
        setContentView(rootView)
        rootViewWithoutToolbar = layoutInflater.inflate(layoutResId, null)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        (rootView as ViewGroup).addView(rootViewWithoutToolbar, layoutParams)
        binding = DataBindingUtil.bind(rootViewWithoutToolbar)!!
        toolbar = findViewById(R.id.toolbar) ?: return
        toolbarLeft = findViewById(R.id.toolbarLeft) ?: return
        toolbarRight = findViewById(R.id.toolbarRight) ?: return
        toolbarTitle = findViewById(R.id.toolbarTitle) ?: return
        if (this::toolbarLeft.isInitialized) {
            toolbarLeft.setOnClickListener { onBack(it) }
        }
        init()
    }

    open fun onBack(v: View) {
        finishTransition()
    }
}

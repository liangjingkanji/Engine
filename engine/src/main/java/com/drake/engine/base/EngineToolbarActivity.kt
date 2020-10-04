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
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.drake.engine.R
import com.drake.engine.databinding.bind
import com.drake.statusbar.immersive
import kotlinx.android.synthetic.main.engine_toolbar.*

abstract class EngineToolbarActivity<B : ViewDataBinding> : EngineActivity<B>() {

    override fun setTitle(title: CharSequence) {
        tv_title.text = title
    }

    @SuppressLint("InflateParams")
    override fun setContentView(layoutResID: Int) {
        val root = layoutInflater.inflate(R.layout.engine_toolbar, null) as ViewGroup
        setContentView(root)

        rootView = layoutInflater.inflate(layoutResID, null)

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        ll_root.addView(rootView, layoutParams)
        binding = rootView.bind()

        immersive(toolbar)

        iv_action_back.setOnClickListener { onBack() }

        init()
    }


    protected fun onBack() {
        finishTransition()
    }
}

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

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class EngineBottomSheetDialogFragment<B : ViewDataBinding> : BottomSheetDialogFragment(),
    OnClickListener {

    lateinit var binding: B
    lateinit var behavior: BottomSheetBehavior<FrameLayout>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adjustView =
            view ?: dialog?.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0) ?: return
        binding = DataBindingUtil.bind(adjustView)!!

        val frameLayout =
            dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        behavior = BottomSheetBehavior.from(frameLayout)

        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    abstract fun initView()
    abstract fun initData()
    override fun onClick(v: View) {}
}

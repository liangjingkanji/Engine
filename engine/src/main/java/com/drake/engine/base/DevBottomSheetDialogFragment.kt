/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hwangjr.rxbus.RxBus

abstract class DevBottomSheetDialogFragment<B : ViewDataBinding> : BottomSheetDialogFragment(),
    OnClickListener {

    lateinit var binding: B
    lateinit var behavior: BottomSheetBehavior<FrameLayout>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = DataBindingUtil.bind(view!!)!!
        @Suppress("DEPRECATION")
        if (!RxBus.get().hasRegistered(this)) {
            RxBus.get().register(this)
        }

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

    override fun onClick(v: View) {

    }

    fun setArgument(bundle: Bundle): DevBottomSheetDialogFragment<B> {
        super.setArguments(bundle)
        return this
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }

    abstract fun initData()

    abstract fun initView()

}

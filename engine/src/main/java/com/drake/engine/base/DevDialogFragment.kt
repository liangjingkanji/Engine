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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.hwangjr.rxbus.RxBus

abstract class DevDialogFragment<B : ViewDataBinding> : DialogFragment(), OnClickListener {

    lateinit var binding: B

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = DataBindingUtil.bind(view!!)!!
        @Suppress("DEPRECATION")
        if (!RxBus.get().hasRegistered(this)) {
            RxBus.get().register(this)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }

    abstract fun initData()

    abstract fun initView()
}

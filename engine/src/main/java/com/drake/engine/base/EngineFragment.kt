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
import androidx.fragment.app.Fragment

abstract class EngineFragment<B : ViewDataBinding> : Fragment(), OnClickListener {

    lateinit var binding: B

    override fun onClick(v: View) {
    }

    protected abstract fun initView()

    protected abstract fun initData()

    override fun onResume() {

        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }

        super.onResume()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding = DataBindingUtil.bind(view!!)!!
    }
}

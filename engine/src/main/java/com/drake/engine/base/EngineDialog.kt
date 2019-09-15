/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/14/19 10:46 AM
 */

package com.drake.engine.base

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.ViewDataBinding
import com.drake.engine.component.databinding.inflate

abstract class EngineDialog<B : ViewDataBinding>(context: Context) : Dialog(context),
    OnClickListener {

    lateinit var binding: B

    override fun onClick(v: View) {
    }

    protected abstract fun initView()

    protected abstract fun initData()

    override fun setContentView(layoutResID: Int) {
        binding = context.inflate(layoutResID)
        setContentView(binding.root)
        init()
    }

    open fun init() {
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }
}

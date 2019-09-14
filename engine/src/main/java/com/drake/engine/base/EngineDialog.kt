/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/14/19 10:46 AM
 */

package com.drake.engine.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.drake.engine.component.databinding.inflate

abstract class EngineDialog<B : ViewDataBinding>(context: Context) : Dialog(context),
    OnClickListener {

    lateinit var binding: B

    override fun onClick(v: View) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }


    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    override fun setContentView(layoutResID: Int) {

        binding = context.inflate(layoutResID)
        setContentView(binding.root)
    }

    open fun init(savedInstanceState: Bundle?) {
        try {
            initView(savedInstanceState)
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }
}

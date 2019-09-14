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
import com.hwangjr.rxbus.RxBus

abstract class DevFragment<B : ViewDataBinding> : Fragment(), OnClickListener {

    lateinit var binding: B
    protected var isVisibleToUser: Boolean = false // 是否用户可见
    protected var isInitView: Boolean = false // 是否初始化控件
    protected var isCreated: Boolean = false


    override fun onClick(v: View) {
    }

    fun setArgument(bundle: Bundle): DevFragment<B> {
        super.setArguments(bundle)
        return this
    }


    protected abstract fun initView(savedInstanceState: Bundle?)

    protected abstract fun initData()

    private fun judgeVisibleToUser() {
        if (isVisibleToUser && isInitView) {
            currentToVisible()
        }
    }

    fun currentToVisible() {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        judgeVisibleToUser()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding = DataBindingUtil.bind(view!!)!!

        if (!isCreated) {
            RxBus.get().register(this)
            isCreated = true
        }

        try {
            initView(savedInstanceState)
            initData()
            isInitView = true
            judgeVisibleToUser()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        isInitView = false
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }
}

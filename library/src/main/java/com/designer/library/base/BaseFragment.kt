/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.hwangjr.rxbus.RxBus

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), OnClickListener {

    lateinit var binding: B
    protected var isVisibleToUser: Boolean = false // 是否用户可见
    protected var isInitView: Boolean = false // 是否初始化控件
    protected var isCreated: Boolean = false


    override fun onClick(v: View) {
    }

    fun setArgument(bundle: Bundle): BaseFragment<B> {
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

/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager

abstract class EngineActivity<B : ViewDataBinding> : AppCompatActivity(), OnClickListener {

    lateinit var binding: B
    lateinit var rootView: View
    private var finishBroadcastReceiver: BroadcastReceiver? = null

    override fun setContentView(layoutResID: Int) {
        rootView = layoutInflater.inflate(layoutResID, null)
        setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)!!
        init()
    }

    override fun onDestroy() {
//        RxBus.get().unregister(this)
        unregisterBroadcast()
        super.onDestroy()
    }

    override fun onClick(v: View) {

    }

    open fun init() {
        registerBroadcast()
//        RxBus.get().register(this)
        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }

    /**
     * 注册销毁事件
     */
    private fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("activity_event")
        finishBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                val skipActivity = intent.getStringExtra("skip_activity")
                if (skipActivity != null && javaClass.simpleName == skipActivity) {
                    return
                }

                finish()
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(finishBroadcastReceiver!!, intentFilter)
    }

    protected fun unregisterBroadcast() {
        finishBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        finishBroadcastReceiver = null
    }

    protected abstract fun initView()

    protected abstract fun initData()

    fun finishTransition() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            super.finish()
        }
    }

    companion object {

        /**
         * 发送关闭全部界面的广播
         */
        @JvmOverloads
        fun finishAll(skipActivity: String? = null) {
            val intent = Intent().setAction("activity_event")
            skipActivity?.let {
                intent.putExtra("skip_activity", skipActivity)
            }
            LocalBroadcastManager.getInstance(App).sendBroadcast(intent)
        }
    }
}

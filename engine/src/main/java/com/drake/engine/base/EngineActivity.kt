/*
 * Copyright (C) 2018, HB CompanyLimited All rights reserved.
 * Project：App
 * Author：Drake
 * Date：12/4/19 11:20 PM
 */

package com.drake.engine.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import android.view.MotionEvent
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
    private var onBackPress: (() -> Boolean)? = null
    private var onTouchEvent: (MotionEvent.() -> Boolean)? = null

    override fun setContentView(layoutResID: Int) {
        rootView = layoutInflater.inflate(layoutResID, null)
        setContentView(rootView)
        binding = DataBindingUtil.bind(rootView)!!
        init()
    }

    open fun init() {
        registerBroadcast()

        try {
            initView()
            initData()
        } catch (e: Exception) {
            Log.e("日志", "初始化失败")
            e.printStackTrace()
        }
    }


    protected abstract fun initView()

    protected abstract fun initData()


    override fun onClick(v: View) {

    }


    // <editor-fold desc="生命周期">

    fun onTouchEvent(block: MotionEvent.() -> Boolean) {
        onTouchEvent = block
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return onTouchEvent?.invoke(event) ?: super.dispatchTouchEvent(event)
    }


    fun onBackPressed(block: () -> Boolean) {
        onBackPress = block
    }

    override fun onBackPressed() {
        if (onBackPress == null || onBackPress?.invoke() == false) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        unregisterBroadcast()
        super.onDestroy()
    }

    // </editor-fold>


    // <editor-fold desc="界面关闭">

    /**
     * 关闭界面
     */
    fun finishTransition() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            super.finish()
        }
    }


    /**
     * 注册广播
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

    /**
     * 注销广播
     */
    protected fun unregisterBroadcast() {
        finishBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        finishBroadcastReceiver = null
    }

    companion object {

        /**
         * 发送关闭全部界面的广播
         * @param ignoreActivity 被忽略的界面
         */
        @JvmOverloads
        fun finishAll(ignoreActivity: String? = null) {
            val intent = Intent().setAction("activity_event")
            ignoreActivity?.let {
                intent.putExtra("skip_activity", ignoreActivity)
            }
            LocalBroadcastManager.getInstance(getApp()).sendBroadcast(intent)
        }
    }

    // </editor-fold>
}

/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.observer

import android.app.ProgressDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.observers.DefaultObserver

/**
 * 自动加载对话框网络请求
 *
 *
 * 开始: 显示对话框
 * 错误: 提示错误信息, 关闭对话框
 * 完全: 关闭对话框
 */
abstract class DialogObserver<M>(
    var activity: FragmentActivity,
    private var cancelable: Boolean = true
) : DefaultObserver<M>(), LifecycleObserver {

    lateinit var dialog: ProgressDialog

    override fun onStart() {
        activity.lifecycle.addObserver(this)
        dialog = ProgressDialog(activity)
        dialog.setOnDismissListener { cancel() }
        dialog.setMessage("加载中")
        dialog.setCancelable(cancelable)
        dialog.show()
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    fun dismissDialog() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        dismissDialog()
        NetObserver.showErrorMsg(e)
    }

    override fun onComplete() {
        dismissDialog()
    }

}

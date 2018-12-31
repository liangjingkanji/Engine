/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import io.reactivex.observers.DefaultObserver

/**
 * 自动结束下拉刷新和上拉加载状态
 */
abstract class RefreshObserver<M>(val smartRefreshLayout: SmartRefreshLayout) :
    DefaultObserver<M>() {


    lateinit var refreshState: RefreshState

    init {
        refreshState = smartRefreshLayout.state
        smartRefreshLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }


    /**
     * 关闭进度对话框并提醒错误信息
     *
     * @param e 包括错误信息
     */
    override fun onError(e: Throwable) {
        finish(false)
        NetObserver.showErrorMsg(e)
    }

    fun finish(success: Boolean) {
        if (success) {
            if (refreshState == RefreshState.Refreshing) {
                smartRefreshLayout.finishRefresh()
            } else if (refreshState == RefreshState.Loading) {
                smartRefreshLayout.finishLoadMore()
            }
        } else {
            if (refreshState == RefreshState.Refreshing) {
                smartRefreshLayout.finishRefresh(false)
            } else if (refreshState == RefreshState.Loading) {
                smartRefreshLayout.finishLoadMore(false)
            }
        }
    }

    override fun onComplete() {
        finish(true)
    }
}

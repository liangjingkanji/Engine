/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.drake.brv.PageRefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import io.reactivex.observers.DefaultObserver

/**
 * 自动结束下拉刷新和上拉加载状态
 */
abstract class RefreshObserver<M>(val pageRefreshLayout: PageRefreshLayout) :
    DefaultObserver<M>() {


    var refreshState: RefreshState = pageRefreshLayout.state

    init {
        pageRefreshLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
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
                pageRefreshLayout.finishRefresh()
            } else if (refreshState == RefreshState.Loading) {
                pageRefreshLayout.finishLoadMore()
            }
        } else {
            if (refreshState == RefreshState.Refreshing) {
                pageRefreshLayout.finishRefresh(false)
            } else if (refreshState == RefreshState.Loading) {
                pageRefreshLayout.finishLoadMore(false)
            }
        }
    }

    override fun onComplete() {
        finish(true)
    }
}

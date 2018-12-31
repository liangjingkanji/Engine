/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.observer

import android.view.View
import android.view.View.OnAttachStateChangeListener
import com.designer.library.component.recycler.BaseRecyclerAdapter
import com.designer.library.component.recycler.PageRefreshLayout
import io.reactivex.observers.DefaultObserver

/**
 * 自动结束下拉刷新和上拉加载状态
 */
abstract class PageObserver<M>(
    val pageRefreshLayout: PageRefreshLayout,
    val adapter: BaseRecyclerAdapter? = null
) :
    DefaultObserver<M>() {

    init {

        adapter?.let {
            pageRefreshLayout.adapter = it
        }

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
        pageRefreshLayout.finish(false)
    }

    override fun onComplete() {
        pageRefreshLayout.finish(true)
    }

    /**
     * 自动判断是添加数据还是覆盖数据
     *
     * @param data 要添加的数据集
     */
    fun refreshData(data: List<Any>, hasMore: PageRefreshLayout.() -> Boolean) {
        pageRefreshLayout.refreshData(data, hasMore)
    }


    fun showEmpty() {
        pageRefreshLayout.showEmpty()
    }
}

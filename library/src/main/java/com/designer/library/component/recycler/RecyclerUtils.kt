/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler

import android.graphics.Rect
import android.util.NoSuchPropertyException
import android.view.View
import android.view.View.NO_ID
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * 详细设置适配器
 * @receiver RecyclerView
 * @param block BaseRecyclerAdapter.() -> Unit
 */
fun RecyclerView.setup(block: BaseRecyclerAdapter.(RecyclerView) -> Unit): BaseRecyclerAdapter {
    val baseAdapter = BaseRecyclerAdapter()
    baseAdapter.block(this)
    adapter = baseAdapter
    return baseAdapter
}

/**
 * 快速创建多类型
 * itemLayout和block二者选一, 分别对应 单一类型/一对多数据类型,
 * 普通多类型配置请使用 {@link RecyclerView.adapter(block: BaseRecyclerAdapter.() -> Unit): BaseRecyclerAdapter}
 *
 * @receiver RecyclerView
 * @param itemLayout Int
 * @param block (M.(Int) -> Int)?
 * @return BaseRecyclerAdapter
 */
inline fun <reified M> RecyclerView.setup(@LayoutRes itemLayout: Int = NO_ID, noinline block: (M.(Int) -> Int)? = null): BaseRecyclerAdapter {
    val baseAdapter = BaseRecyclerAdapter()
    when {
        itemLayout != NO_ID -> baseAdapter.addType<M>(itemLayout)
        block != null -> baseAdapter.addType(block)
        else -> throw NoSuchPropertyException("Please add item model type")
    }
    adapter = baseAdapter
    return baseAdapter
}

val RecyclerView.baseAdapter: BaseRecyclerAdapter
    get() = adapter as BaseRecyclerAdapter

fun RecyclerView.linear(
    @RecyclerView.Orientation orientation: Int = VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    return this
}

fun RecyclerView.grid(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = VERTICAL,
    reverseLayout: Boolean = false
): RecyclerView {
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
    return this
}

fun RecyclerView.staggered(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = VERTICAL
): RecyclerView {
    layoutManager =
            StaggeredGridLayoutManager(spanCount, orientation) as RecyclerView.LayoutManager?
    return this
}

fun RecyclerView.divider(
    @DrawableRes drawable: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    block: ((Rect, View, RecyclerView, RecyclerView.State) -> Boolean)? = null
): RecyclerView {
    val baseItemDecoration = BaseItemDecoration(drawable).setOrientation(orientation)
    block?.let {
        baseItemDecoration.onItemOffsets(block)
    }
    addItemDecoration(baseItemDecoration)
    return this
}




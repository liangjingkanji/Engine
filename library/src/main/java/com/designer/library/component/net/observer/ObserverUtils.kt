/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.observer

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.designer.library.component.recycler.BaseRecyclerAdapter
import com.designer.library.component.recycler.PageRefreshLayout
import com.designer.library.component.state.StateLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.reactivex.Observable


/**
 * 自动打印错误信息
 * @receiver Observable<M>
 * @param lifecycleOwner FragmentActivity? 如果不为null 则会根据当前activity销毁而取消订阅
 * @param block (M) -> UnitUtils
 */
fun <M> Observable<M>.net(
    lifecycleOwner: LifecycleOwner? = null,
    block: (NetObserver<M>.(M) -> Unit)? = null
) {

    subscribe(object : NetObserver<M>(lifecycleOwner) {
        override fun onNext(t: M) {
            block?.invoke(this, t)
        }
    })
}

/**
 * 自动处理多状态页面
 * @receiver Observable<M>
 * @param stateLayout StateLayout
 * @param block (M) -> UnitUtils
 */
fun <M> Observable<M>.state(stateLayout: StateLayout, block: StateObserver<M>.(M) -> Unit) {
    subscribe(object : StateObserver<M>(stateLayout) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}

fun <M> Observable<M>.state(view: View, block: StateObserver<M>.(M) -> Unit) {
    subscribe(object : StateObserver<M>(view) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}

fun <M> Observable<M>.state(activity: FragmentActivity, block: StateObserver<M>.(M) -> Unit) {
    subscribe(object : StateObserver<M>(activity) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}

fun <M> Observable<M>.state(fragment: Fragment, block: StateObserver<M>.(M) -> Unit) {
    subscribe(object : StateObserver<M>(fragment) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}

/**
 * 请求网络自动开启和关闭对话框
 * @receiver Observable<M>
 * @param activity FragmentActivity
 * @param cancelable Boolean 对话框是否可以被用户取消
 * @param block (M) -> Unit
 */
fun <M> Observable<M>.dialog(
    activity: FragmentActivity,
    cancelable: Boolean = true,
    block: (DialogObserver<M>.(M) -> Unit)? = null
) {
    subscribe(object : DialogObserver<M>(activity, cancelable) {
        override fun onNext(t: M) {
            block?.invoke(this, t)
        }
    })
}

/**
 * 自动结束下拉加载
 * @receiver Observable<M>
 * @param pageRefreshLayout SmartRefreshLayout
 * @param block (M) -> UnitUtils
 */
fun <M> Observable<M>.refresh(
    smartRefreshLayout: SmartRefreshLayout,
    block: RefreshObserver<M>.(M) -> Unit
) {
    subscribe(object : RefreshObserver<M>(smartRefreshLayout) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}

/**
 * 自动处理分页加载更多和下拉加载
 *
 * @receiver Observable<M>
 * @param pageRefreshLayout PageRefreshLayout
 * @param block (M) -> UnitUtils
 */
fun <M> Observable<M>.page(
    pageRefreshLayout: PageRefreshLayout,
    adapter: BaseRecyclerAdapter? = null,
    block: PageObserver<M>.(M) -> Unit
) {
    subscribe(object : PageObserver<M>(pageRefreshLayout, adapter) {
        override fun onNext(t: M) {
            block(t)
        }
    })
}
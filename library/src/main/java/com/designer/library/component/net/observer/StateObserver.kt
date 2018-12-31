/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.observer

import android.app.Activity
import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.fragment.app.Fragment
import com.designer.library.component.state.StateLayout
import com.designer.library.component.state.state
import io.reactivex.observers.DefaultObserver

/**
 * 自动显示多状态布局
 */
abstract class StateObserver<S> : DefaultObserver<S> {

    var stateLayout: StateLayout

    constructor(view: View) {
        stateLayout = view.state()
    }

    constructor(activity: Activity) {
        stateLayout = activity.state()
    }

    constructor(fragment: Fragment) {
        stateLayout = fragment.state()
    }

    constructor(stateLayout: StateLayout) {
        this.stateLayout = stateLayout
    }

    fun showEmpty() {
        stateLayout.showEmpty()
        cancel()
    }

    public override fun onStart() {
        stateLayout.showLoading()
        stateLayout.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                cancel()
            }
        })
    }

    override fun onError(e: Throwable) {
        stateLayout.showError()
    }

    override fun onComplete() {
        stateLayout.showContent()
    }
}

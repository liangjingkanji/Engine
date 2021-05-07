/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.drake.engine.keyboard

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * 支持键盘的工具类
 */

/**
 * 设置当前视图始终显示在键盘之上
 *
 * @param view 需要悬浮在键盘之上的视图
 * @param parent 当键盘显示隐藏时需要滚动的视图
 * @param onKeyboardChanged 键盘是否显示和高度回调
 */
@JvmOverloads
fun FragmentActivity.setAboveKeyboard(
    view: View,
    parent: ViewGroup = view.parent as ViewGroup,
    onKeyboardChanged: ((keyboardVisible: Boolean, keyboardHeight: Int) -> Unit)? = null
) {

    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    val originalParentY = parent.y
    var defaultDisplayBottom = 0

    val layoutChangeListener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            val displayRect = Rect()
            view.getWindowVisibleDisplayFrame(displayRect)
            if (defaultDisplayBottom == 0) defaultDisplayBottom = displayRect.bottom

            val locationRect = Rect()
            view.getGlobalVisibleRect(locationRect)
            val keyboardHeight = defaultDisplayBottom - displayRect.bottom

            if (keyboardHeight > defaultDisplayBottom / 4) {
                val scrollY = locationRect.bottom - displayRect.bottom + displayRect.top
                if (scrollY <= 0) return
                parent.y -= scrollY
                onKeyboardChanged?.invoke(true, keyboardHeight)
            } else {
                parent.y = originalParentY
                onKeyboardChanged?.invoke(false, keyboardHeight)
            }
        }
    }
    view.addOnLayoutChangeListener(layoutChangeListener)

    lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun release() {
            view.removeOnLayoutChangeListener(layoutChangeListener)
        }
    })
}


@Suppress("UNUSED_ANONYMOUS_PARAMETER")
fun View.addKeyboardListener(onKeyboardChanged: (keyboardVisible: Boolean, keyboardHeight: Int) -> Unit) {

    var originalDisplayRect: Rect? = null
    var originalDisplayBottom = 0

    View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        if (originalDisplayRect == null) {
            originalDisplayRect = Rect()
            getWindowVisibleDisplayFrame(originalDisplayRect)
            originalDisplayBottom = originalDisplayRect!!.bottom
        }

        val currentDisplayRect = Rect()
        getWindowVisibleDisplayFrame(currentDisplayRect)

        val keyboardHeight = originalDisplayBottom - currentDisplayRect.bottom

        if (keyboardHeight > originalDisplayBottom / 4) {
            onKeyboardChanged.invoke(true, keyboardHeight)
        } else {
            onKeyboardChanged.invoke(false, keyboardHeight)
        }
    }
}


/**
 * 键盘是否显示
 *
 */
fun Activity.keyboardVisible(): Boolean {
    return getKeyboardHeight() > window.decorView.bottom / 4
}

/**
 * 返回键盘高度
 */
fun Activity.getKeyboardHeight(): Int {
    val outRect = Rect()
    val decorView = window.decorView
    decorView.getWindowVisibleDisplayFrame(outRect)
    return decorView.bottom - outRect.bottom
}


/**
 * 切换键盘 显示/隐藏
 */
fun Context.toggleKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

/**
 * 隐藏软键盘
 */
fun Activity.hideKeyboard() {
    val im = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus
    if (view != null) {
        if (im.isActive) {
            im.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}


/**
 * 显示键盘
 *
 */
fun Activity.showKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.showSoftInput(view, 0)
}

/**
 * 强制弹出输入法
 */
fun EditText.showKeyboard() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}


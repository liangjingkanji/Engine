/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
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

@file:Suppress("DEPRECATION", "unused")

package com.drake.engine.dialog

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.drake.engine.utils.dp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.min


/**
 * 设置对话框宽度(默认为屏幕宽度的80%(如果横屏则为屏幕高度)), 同时对话框背景为透明
 * 如果要求xml布局的宽度有效, 请嵌套一层
 * @param width 宽度, 单位dp, 0 表示使用[percent]百分比宽度
 * @param marginHorizontal 水平外间距, 单位dp
 * @param percent 占屏幕宽度百分比, 1 表示全屏宽度
 */
fun Dialog.setMaxWidth(
    @IntRange(from = 0) width: Int = 0,
    @IntRange(from = 0) marginHorizontal: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) percent: Float = 0.8f,
) {
    window?.apply {
        val lp = attributes
        when (width) {
            0 -> {
                val displayMetrics = context.resources.displayMetrics
                val maxWidth = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
                lp.width = (maxWidth * percent).toInt() - marginHorizontal.dp
            }
            else -> {
                lp.width = width.dp - marginHorizontal.dp
            }
        }
        attributes = lp
        setBackgroundDrawableResource(android.R.color.transparent)
    }
}

/**
 * 设置对话框宽度(默认为屏幕宽度的80%(如果横屏则为屏幕高度)), 同时对话框背景为透明
 * 如果要求xml布局的宽度有效, 请嵌套一层
 * @param width 宽度, 单位dp, 0 表示使用[percent]百分比宽度
 * @param marginHorizontal 水平外间距, 单位dp
 * @param percent 占屏幕宽度百分比, 1 表示全屏宽度
 */
fun DialogFragment.setMaxWidth(
    @IntRange(from = 0) width: Int = 0,
    @IntRange(from = 0) marginHorizontal: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) percent: Float = 0.8f,
) = dialog?.setMaxWidth(width, marginHorizontal, percent)

/**
 * 设置对话框宽度(默认为屏幕宽度的80%(如果横屏则为屏幕高度)), 同时对话框背景为透明
 * 如果要求xml布局的宽度有效, 请嵌套一层
 * @param width 宽度, 单位dp, 0 表示使用[percent]百分比宽度
 * @param marginHorizontal 水平外间距, 单位dp
 * @param percent 占屏幕宽度百分比, 1 表示全屏宽度
 */
fun BottomSheetDialogFragment.setMaxWidth(
    @IntRange(from = 0) width: Int = 0,
    @IntRange(from = 0) marginHorizontal: Int = 0,
    @FloatRange(from = 0.0, to = 1.0) percent: Float = 0.8f,
) {
    dialog?.let {
        it.window?.apply {
            val lp = attributes
            when (width) {
                0 -> {
                    val displayMetrics = context.resources.displayMetrics
                    val maxWidth = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
                    lp.width = (maxWidth * percent).toInt() - marginHorizontal.dp
                }
                else -> {
                    lp.width = width.dp - marginHorizontal.dp
                }
            }
            attributes = lp
            it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}

/**
 * 设置列表对话框的分割线
 * 分割线的间距由Drawable来控制
 */
fun AlertDialog.setDivider(@DrawableRes divider: Int) {
    listView?.apply {
        overscrollFooter = ColorDrawable(Color.TRANSPARENT)
        this.divider = ContextCompat.getDrawable(context, divider)
    }
}

/**
 * 下拉菜单
 */
fun View.pullMenu(list: List<String>): ListPopupWindow {
    return ListPopupWindow(context).apply {
        anchorView = this@pullMenu
        setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, list))
    }
}

fun View.pullMenu(vararg list: String): ListPopupWindow {
    return ListPopupWindow(context).apply {
        anchorView = this@pullMenu
        setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, list))
    }
}

/**
 * 警告对话框
 */
fun Activity.alert(block: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.block()
    return builder.show()
}

fun AlertDialog.Builder.yes(
    text: String,
    block: AlertDialog.() -> Unit = {}
) {
    setPositiveButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}


fun AlertDialog.Builder.yes(@StringRes text: Int, block: AlertDialog.() -> Unit = {}) {
    setPositiveButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}


fun AlertDialog.Builder.medium(
    text: String,
    block: AlertDialog.() -> Unit = {}
) {
    setNeutralButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}

fun AlertDialog.Builder.medium(@StringRes text: Int, block: AlertDialog.() -> Unit = {}) {
    setNeutralButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}


fun AlertDialog.Builder.no(
    text: String,
    block: AlertDialog.() -> Unit = {}
) {
    setNegativeButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}


fun AlertDialog.Builder.no(@StringRes text: Int, block: AlertDialog.() -> Unit = {}) {
    setNegativeButton(text) { dialogInterface, _ -> (dialogInterface as AlertDialog).block() }
}


/**
 * 进度对话框
 * @receiver Activity
 * @param msg String?
 * @param block [@kotlin.ExtensionFunctionType] Function1<ProgressDialog, Unit>
 */
fun Activity.progress(msg: String? = null, block: ProgressDialog.() -> Unit = {}): ProgressDialog {
    return ProgressDialog(this).apply {
        setMessage(msg)
        block()
        show()
    }
}
/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.utils

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.setup
import com.drake.engine.base.getApp
import org.jetbrains.anko.dip

/**
 * 解决常见的MaterialDesignDialog需求
 */

/**
 * 设置对话框的宽度
 * 必须在对话框显示之后执行
 */
fun Dialog.setWidth(withDipValue: Int) {
    val window = window
    window?.setLayout(getApp().dip(withDipValue.toFloat()), WRAP_CONTENT)
}


/**
 * 设置对话框的高度
 * 必须在对话框显示之后执行
 */
fun Dialog.setHeight(heightDipValue: Int) {
    val window = window
    window?.setLayout(WRAP_CONTENT, getApp().dip(heightDipValue.toFloat()))
}

/**
 * 设置对话框的宽高
 * 必须在对话框显示之后执行
 */
fun Dialog.setSize(withDipValue: Int, heightDipValue: Int) {
    val window = window
    window?.setLayout(getApp().dip(withDipValue.toFloat()), getApp().dip(heightDipValue.toFloat()))
}

/**
 * 设置背景透明
 */
fun Dialog.setTransparent() {
    val window = window
    window?.setBackgroundDrawableResource(android.R.color.transparent)
}


/**
 * 设置适配器
 *
 */
fun Dialog.setAdapter(block: BindingAdapter.(RecyclerView) -> Unit): Dialog {
    val context = context
    val recyclerView = RecyclerView(context)
    recyclerView.setup(block)
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    setContentView(recyclerView)
    return this
}

/**
 * 设置列表对话框的分割线
 * 分割线的间距由Drawable来控制
 */
fun AlertDialog.setDivider(@DrawableRes divider: Int) {
    val listView = this.listView
    if (listView != null) {
        listView.overscrollFooter = ColorDrawable(Color.TRANSPARENT)
        listView.divider = ContextCompat.getDrawable(getApp(), divider)
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
 * 解决DialogFragment宽度限制问题
 */
fun androidx.fragment.app.DialogFragment.setWidthNoLimit() {
    @Suppress("DEPRECATION")
    setStyle(
        androidx.fragment.app.DialogFragment.STYLE_NO_TITLE,
        android.R.style.Theme_Material_Light_Dialog_MinWidth
    )
}

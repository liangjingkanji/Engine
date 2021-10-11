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
package com.drake.engine.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.View.inflate
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.recyclerview.widget.RecyclerView
import com.drake.engine.R
import com.drake.engine.utils.dp

/**
 * 在按下位置附近弹出菜单, 弥补PopupMenu不足
 *
 * 在此项目基础上修改 https://github.com/JavaNoober/FloatMenu
 * @param anchor 由指定视图触发的点击事件. 用于获取当前手指点击坐标. 假设为RecyclerView则会自动根据Item来获取坐标
 * 假设anchor无法确定, 可以在[show]函数中指定坐标参数
 *
 * @param T 用于[show]传递指定类型的对象给点击事件
 */
@SuppressLint("ClickableViewAccessibility")
class FloatMenu<T>(val context: Context, var anchor: View? = null) : PopupWindow(context) {

    /** 菜单列表 */
    var items = arrayListOf<MenuItem<T>>()

    /** 菜单距离附着的视图垂直偏移量 */
    var verticalOffset: Int = 10.dp

    /** 菜单距离附着视图的对齐方式 */
    var gravity = Gravity.TOP or Gravity.START

    /** 指定菜单选项布局, 通过在自己的项目中复写同名文件可以实现全局替换 */
    var itemResId = R.layout.item_float_menu

    private val screenPoint: Point
    private var touchX = 0
    private var touchY = 0
    private lateinit var menuLayout: LinearLayout
    private var tag: T? = null
    private var itemWidth = 180.dp

    /**
     * 菜单列表
     * @param title 标题
     * @param iconRes 图标
     * @param onClick 点击列表监听者
     */
    data class MenuItem<T>(
        var title: String? = null,
        var iconRes: Int = View.NO_ID,
        var onClick: Consumer<T>? = null,
    )

    init {
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable())
        setAnchor(anchor)
        val dm = context.resources.displayMetrics
        screenPoint = Point(dm.widthPixels, dm.heightPixels)
    }

    /**
     * 添加菜单选项
     * @param title 标题
     * @param iconRes 图标
     * @param onClick 点击事件监听者
     */
    fun addItem(
        title: String,
        @DrawableRes iconRes: Int = -1,
        onClick: Consumer<T>? = null
    ) = apply {
        items.add(MenuItem(title, iconRes, onClick))
    }

    /** 设置附着的视图, 用于获取点击坐标 */
    fun setAnchor(anchor: View?) = apply {
        this.anchor = anchor
        if (anchor is RecyclerView) {
            anchor.addOnItemTouchListener(RecyclerViewTouchListener())
        } else {
            anchor?.setOnTouchListener(MenuTouchListener())
        }
    }

    /**
     * 显示浮动菜单
     * @param tag 传递给点击事件的标签, 在[MenuItem.onClick]接受
     * @param touchX 点击x坐标, 在没有指定[anchor]时要求指定坐标才能定位显示
     * @param touchY 点击y坐标
     */
    @JvmOverloads
    fun show(tag: T? = null, touchX: Int = this.touchX, touchY: Int = this.touchY) {
        this.tag = tag
        inflateMenuLayout()
        if (isShowing) return
        //it is must ,other wise 'setOutsideTouchable' will not work under Android5.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundDrawable(BitmapDrawable())
        }
        if (touchX <= screenPoint.x / 2) {
            if (touchY + height < screenPoint.y) {
                animationStyle = R.style.Animation_top_left
                showAtLocation(anchor, gravity, touchX, touchY + verticalOffset)
            } else {
                animationStyle = R.style.Animation_bottom_left
                showAtLocation(anchor, gravity, touchX, touchY - height - verticalOffset)
            }
        } else {
            if (touchY + height < screenPoint.y) {
                animationStyle = R.style.Animation_top_right
                showAtLocation(anchor, gravity, touchX - itemWidth, touchY + verticalOffset)
            } else {
                animationStyle = R.style.Animation_bottom_right
                showAtLocation(anchor, gravity, touchX - itemWidth, touchY - height - verticalOffset)
            }
        }
    }

    /** 填充菜单布局, 在[show]函数内调用 */
    private fun inflateMenuLayout() {
        menuLayout = LinearLayout(context)
        menuLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_shadow))
        menuLayout.orientation = LinearLayout.VERTICAL
        for (item in items) {
            val tvItem = inflate(context, itemResId, null) as TextView
            tvItem.isClickable = true
            if (item.iconRes != View.NO_ID) {
                val drawable = ContextCompat.getDrawable(context, item.iconRes)
                drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                tvItem.setCompoundDrawables(drawable, null, null, null)
            }
            tvItem.text = item.title
            tvItem.setOnClickListener {
                dismiss()
                item.onClick?.accept(tag)
            }
            tvItem.measure(0, 0)
            itemWidth = tvItem.measuredWidth
            menuLayout.addView(tvItem)
        }
        contentView = menuLayout
    }

    private inner class MenuTouchListener : OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, e: MotionEvent): Boolean {
            if (e.action == MotionEvent.ACTION_DOWN) {
                touchX = e.rawX.toInt()
                touchY = e.rawY.toInt()
            }
            return false
        }
    }

    /** 用于监听RecyclerView的Item点击坐标 */
    private inner class RecyclerViewTouchListener : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            if (e.action == MotionEvent.ACTION_DOWN) {
                touchX = e.rawX.toInt()
                touchY = e.rawY.toInt()
            }
            return false
        }
    }
}
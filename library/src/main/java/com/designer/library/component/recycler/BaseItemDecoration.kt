/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.designer.library.base.Library

/**
 * 用于创建分隔物的帮助类
 * - 自定义图片分隔物
 * - 自定义颜色分隔物
 */
class BaseItemDecoration
/**
 * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
 * [LinearLayoutManager].
 *
 * @param context     Current context, it will be used to access resources.
 * @param orientation Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
 */
private constructor(private val context: Context, orientation: Int) :
    RecyclerView.ItemDecoration() {
    private val mBounds = Rect()
    var divider: Drawable? = null
        private set
    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var mOrientation: Int = 0
    var onItemOffsets: ((Rect, View, RecyclerView, RecyclerView.State) -> Boolean)? = null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        if (divider == null) {
            Log.w(
                TAG,
                "@android:attr/listDivider was not set in the theme used for this " + "DividerItemDecoration. Please set that attribute all call setDrawable()"
            )
        }
        a.recycle()
        setOrientation(orientation)
    }

    constructor(@DrawableRes drawableRes: Int) : this(Library.app!!, VERTICAL) {
        setDrawable(drawableRes)
    }

    constructor(dividerDrawable: Drawable) : this(Library.app!!, VERTICAL) {
        divider = dividerDrawable
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int): BaseItemDecoration {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw IllegalArgumentException(
                "Invalid orientation. It should be either HORIZONTAL or VERTICAL"
            )
        }
        mOrientation = orientation
        return this
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable?): BaseItemDecoration {
        drawable?.let { throw IllegalArgumentException("Drawable cannot be null.") }
        divider = drawable
        return this
    }

    /**
     * 自定义分隔物
     *
     * @param drawableRes 分隔物
     */
    fun setDrawable(@DrawableRes drawableRes: Int): BaseItemDecoration {
        val drawable =
            context.resources.getDrawable(drawableRes)
                ?: throw IllegalArgumentException("Drawable cannot be null.")
        divider = drawable
        return this
    }

    fun onItemOffsets(onItemOffsets: (Rect, View, RecyclerView, RecyclerView.State) -> Boolean): BaseItemDecoration {
        this.onItemOffsets = onItemOffsets
        return this
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        onItemOffsets?.let {
            val isReturn = onItemOffsets!!.invoke(outRect, view, parent, state)
            if (isReturn) {
                return
            }
        }

        if (divider == null || parent.getChildLayoutPosition(view) == state.itemCount - 1) {
            return
        }

        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, divider!!.intrinsicHeight)
        } else {
            outRect.set(0, 0, divider!!.intrinsicWidth, 0)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left,
                parent.paddingTop,
                right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - divider!!.intrinsicHeight
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft,
                top,
                parent.width - parent.paddingRight,
                bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + Math.round(child.translationX)
            val left = right - divider!!.intrinsicWidth
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
        canvas.restore()
    }

    companion object {

        val HORIZONTAL = LinearLayout.HORIZONTAL
        val VERTICAL = LinearLayout.VERTICAL

        private val TAG = "DividerItem"
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}

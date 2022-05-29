package com.drake.engine.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * 禁止滚动
 * 禁止显示滚动条
 * 配合[android.widget.ImageView.ScaleType.FIT_START]可实现图片顶部对齐裁剪
 */
class FixedScrollView(context: Context?, attrs: AttributeSet?) : ScrollView(context, attrs) {

    init {
        isVerticalScrollBarEnabled = false
        isHorizontalFadingEdgeEnabled = false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}
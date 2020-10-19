package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.drake.engine.R

/**
 * 解决 IllegalArgumentException: pointerIndex out of range
 *
 * @property scrollable 是否支持划动 属性设置 [app:pager_scrollable]
 */
class FixedViewPager : ViewPager {

    var scrollable: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.FixedViewPager)
        scrollable = attributes.getBoolean(R.styleable.FixedViewPager_pager_scrollable, true)
        attributes.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var isIntercept = false
        try {
            isIntercept = super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isIntercept && scrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        var consume = false
        try {
            consume = super.onTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return consume && scrollable
    }
}
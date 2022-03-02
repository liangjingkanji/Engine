package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.drake.engine.R

/** 可以设置最大高度的ScrollView */
class MaxHeightScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var maxHeight: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView)
        maxHeight = typedArray.getDimensionPixelSize(R.styleable.MaxHeightScrollView_scroll_maxHeight, 0)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = if (maxHeight == 0) heightMeasureSpec else maxHeight
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST))
    }
}
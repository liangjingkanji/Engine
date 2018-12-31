/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.designer.library.R
import kotlinx.android.synthetic.main.two_text_layout.view.*

/**
 * Author     : majingcheng
 * Date       : 2018-06-12 15:04
 */
class DifferentSizeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var leftText: String? = null           //左边文字
    private var rightText: String? = null          //右边文字
    private var lastText: String? = null           //最后小字
    private var leftSize: Float = 0f               //左边文字大小
    private var rightSize: Float = 0f              //右边文字大小
    private var lastSize: Float = 0f               //最后文字小字
    private var leftColor: Int = 0xC50032          //左边文字颜色
    private var rightColor: Int = 0xC50032         //右边文字颜色
    private var lastColor: Int = 0xC50032          //右边文字颜色

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.DifferentSizeTextView)
        leftText = typeArray.getString(R.styleable.DifferentSizeTextView_leftText)
        rightText = typeArray.getString(R.styleable.DifferentSizeTextView_rightText)
        lastText = typeArray.getString(R.styleable.DifferentSizeTextView_lastText)
        leftSize = typeArray.getDimension(R.styleable.DifferentSizeTextView_leftSize, leftSize)
        rightSize = typeArray.getDimension(R.styleable.DifferentSizeTextView_rightSize, rightSize)
        lastSize = typeArray.getDimension(R.styleable.DifferentSizeTextView_lastSize, lastSize)
        leftColor = typeArray.getColor(R.styleable.DifferentSizeTextView_leftColor, leftColor)
        rightColor = typeArray.getColor(R.styleable.DifferentSizeTextView_rightColor, rightColor)
        lastColor = typeArray.getColor(R.styleable.DifferentSizeTextView_lastColor, lastColor)
        typeArray.recycle()
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.two_text_layout, this)
        leftText?.let { left_text.text = it }
        rightText?.let { right_text.text = it }
        lastText?.let { last_text.text = it }
        left_text.textSize = px2sp(context, leftSize).toFloat()
        right_text.textSize = px2sp(context, rightSize).toFloat()
        last_text.textSize = px2sp(context, lastSize).toFloat()
        left_text.setTextColor(leftColor)
        right_text.setTextColor(rightColor)
        last_text.setTextColor(lastColor)
    }

    fun setLeftText(leftText: String) {
        leftText.let { left_text.text = it }
    }

    fun setRightText(rightText: String) {
        rightText.let { right_text.text = it }
    }

    fun setLastText(lastText: String) {
        lastText.let { last_text.text = it }
    }

    fun setDifferentText(index: Long) {
        val s = PriceUtils.getPrice(index)
        if (isNumber(s)) {
            if (!s.contains(".")) {   //整数
                rightText = s
                lastText = ""
            } else {                        //小数
                val number = s.split(".")
                rightText = number[0]
                lastText = "." + number[1]
            }
            right_text.text = rightText
            last_text.text = lastText
        }
    }

    private fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    private fun isNumber(str: String): Boolean {
        val reg = "^[0-9]+(.[0-9]+)?$"
        return str.matches(reg.toRegex())
    }
}
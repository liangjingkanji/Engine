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
import kotlinx.android.synthetic.main.vip_progress_layout.view.*

/**
 * vip进度条
 */
class VipProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var mProgress: Int
    var progressText: String?

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.VipProgress)
        mProgress = typeArray.getInt(R.styleable.VipProgress_mProgress, 0)
        progressText = typeArray.getString(R.styleable.VipProgress_progressText)
        typeArray.recycle()
        initView()
        initData()
    }

    private fun initView() {
        View.inflate(context, R.layout.vip_progress_layout, this)
        progressBar.measure(0, 0)
        ll.measure(0, 0)
    }

    private fun initData() {
        if (!progressText.isNullOrEmpty()) {
            text.text = progressText
        }
        progressBar.progress = mProgress
        progressBar.post {
            ll.x = progressBar.progress * progressBar.measuredWidth.toFloat() / 100
        }
    }

    fun setVipProgress(process: Int) {
        mProgress = process
        initData()
    }

    fun setVipText(s: String) {
        progressText = s
        if (!progressText.isNullOrEmpty()) {
            text.text = progressText
        }
    }
}
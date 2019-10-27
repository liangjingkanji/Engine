/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 跑马灯效果
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        ellipsize = TextUtils.TruncateAt.MARQUEE
        setSingleLine(true)
    }

    override fun isFocused(): Boolean {
        return true
    }

}

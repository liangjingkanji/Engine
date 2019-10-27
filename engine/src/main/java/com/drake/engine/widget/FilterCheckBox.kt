/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox


class FilterCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatCheckBox(context, attrs) {

    var onFilter: (FilterCheckBox.() -> Boolean)? = null

    fun onFilter(block: FilterCheckBox.() -> Boolean) {
        this.onFilter = block
    }

    override fun toggle() {
        onFilter?.let {
            if (!onFilter!!(this)) {
                super.toggle()
            }
        }
    }

}

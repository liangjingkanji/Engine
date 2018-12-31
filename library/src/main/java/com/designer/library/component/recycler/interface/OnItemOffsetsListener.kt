/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler.`interface`

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnItemOffsetsListener {

    fun onItemOffset(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Boolean
}

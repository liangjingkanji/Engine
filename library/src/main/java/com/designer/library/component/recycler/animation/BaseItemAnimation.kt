/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler.animation

import android.view.View


interface BaseItemAnimation {

    /**
     * 处理item被添加的时候的进入动画
     *
     * @param view item view
     */
    fun onItemEnterAnimation(view: View)
}

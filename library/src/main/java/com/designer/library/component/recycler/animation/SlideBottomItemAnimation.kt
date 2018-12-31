/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler.animation

import android.animation.ObjectAnimator
import android.view.View


class SlideBottomItemAnimation : BaseItemAnimation {

    override fun onItemEnterAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0F)
            .setDuration(300)
            .start()
    }
}

/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler.animation

import androidx.annotation.IntDef
import com.designer.library.component.recycler.animation.AnimationType.Companion.ALPHA
import com.designer.library.component.recycler.animation.AnimationType.Companion.SCALE
import com.designer.library.component.recycler.animation.AnimationType.Companion.SLIDE_BOTTOM
import com.designer.library.component.recycler.animation.AnimationType.Companion.SLIDE_LEFT
import com.designer.library.component.recycler.animation.AnimationType.Companion.SLIDE_RIGHT


@IntDef(ALPHA, SCALE, SLIDE_BOTTOM, SLIDE_LEFT, SLIDE_RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class AnimationType {
    companion object {

        const val ALPHA = 0x00000001

        const val SCALE = 0x00000002

        const val SLIDE_BOTTOM = 0x00000003

        const val SLIDE_LEFT = 0x00000004

        const val SLIDE_RIGHT = 0x00000005
    }
}

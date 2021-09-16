/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.engine.databinding


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.View.NO_ID
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.drake.engine.base.app
import com.drake.engine.utils.throttleClick
import com.google.android.material.button.MaterialButton


object DataBindingComponent {

    /**
     * 在绑定视图时可以用于Model来处理UI, 由于破坏视图和逻辑解耦的规则不是很建议使用
     * 这会导致不方便业务逻辑进行单元测试
     *
     * @see OnBindListener 该接口支持泛型定义具体视图
     *
     * @receiver View
     * @param listener OnBindListener<View>
     */
    @BindingAdapter("onBind")
    @JvmStatic
    fun View.setBindListener(listener: OnBindListener) {
        listener.onBind(this)
    }

    @BindingAdapter("paddingStart", "paddingEnd", requireAll = false)
    @JvmStatic
    fun View.setPaddingRtl(start: View?, end: View?) {
        post {
            val startFinal = (start?.width ?: 0) + paddingStart
            val endFinal = (end?.width ?: 0) + paddingEnd
            setPaddingRelative(startFinal, paddingTop, endFinal, paddingBottom)
        }
    }

    // <editor-fold desc="图片">

    @BindingAdapter(
        value = ["leftDrawable", "topDrawable", "rightDrawable", "bottomDrawable"],
        requireAll = false
    )
    @JvmStatic
    fun TextView.setImageDrawable(
        drawableLeft: Int, drawableTop: Int, drawableRight: Int, drawableBottom: Int
    ) {
        if (drawableLeft != 0 || drawableTop != 0 || drawableRight != 0 || drawableBottom != 0) {
            setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft, drawableTop, drawableRight, drawableBottom
            )
        }
    }

    @BindingAdapter("android:background")
    @JvmStatic
    fun View.setBackgroundRes(drawableId: Int) {
        if (drawableId != 0 && drawableId != NO_ID) {
            setBackgroundResource(drawableId)
        }
    }

    @BindingAdapter("android:backgroundTint")
    @JvmStatic
    fun MaterialButton.setBackgroundTintRes(color: Int) {
        if (color != 0 && color != NO_ID) {
            backgroundTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
        }
    }


    @BindingAdapter("android:src")
    @JvmStatic
    fun ImageView.setImageRes(drawableId: Int) {
        if (drawableId != 0 && drawableId != NO_ID) {
            setImageResource(drawableId)
        }
    }

    // </editor-fold>


    // <editor-fold desc="隐藏">

    /**
     * 隐藏控件
     */
    @BindingAdapter("invisible")
    @JvmStatic
    fun View.setInvisible(visibilityVar: Boolean) {
        visibility = if (visibilityVar) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    @BindingAdapter("invisible")
    @JvmStatic
    fun View.setInvisible(visibilityVar: Any?) {
        visibility = if (visibilityVar != null) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    /**
     * 取消控件
     */
    @BindingAdapter("gone")
    @JvmStatic
    fun View.setGone(visibilityVar: Boolean) {
        visibility = if (visibilityVar) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    @BindingAdapter("gone")
    @JvmStatic
    fun View.setGone(visibilityVar: Any?) {
        visibility = if (visibilityVar != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    // </editor-fold>


    // <editor-fold desc="阴影">

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun View.setElevationOf(dp: Int) {
        ViewCompat.setElevation(this, (dp * app.resources.displayMetrics.density).toInt().toFloat())
    }

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun CardView.setElevationOf(dp: Int) {
        cardElevation = dp.toFloat()
    }

    // </editor-fold>

    // <editor-fold desc="状态">

    @BindingAdapter("android:enabled")
    @JvmStatic
    fun View.setEnableBind(enable: Boolean) {
        isEnabled = enable
    }

    @BindingAdapter("android:enabled")
    @JvmStatic
    fun View.setEnableBind(enable: Any?) {
        isEnabled = enable != null
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun View.setSelectedBind(selected: Boolean) {
        isSelected = selected
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun View.setSelectedBind(selected: Any?) {
        isSelected = selected != null
    }


    @BindingAdapter("activated")
    @JvmStatic
    fun View.setActivatedBind(activated: Boolean) {
        isActivated = activated
    }


    @BindingAdapter("activated")
    @JvmStatic
    fun View.setActivatedBind(activated: Any?) {
        isActivated = activated != null
    }

    // </editor-fold>


    // <editor-fold desc="点击事件">

    /**
     * 防止暴力点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("click")
    @JvmStatic
    fun View.setPreventClickListener(onClickListener: View.OnClickListener?) {
        if (onClickListener != null) {
            throttleClick { onClickListener.onClick(this) }
        }
    }


    /**
     * 自动将点击事件映射到Activity上
     *
     * @param isPrevent 是否只支持快速点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("hit")
    @JvmStatic
    fun View.hit(isPrevent: Boolean = true) {
        var context = context

        while (context is ContextWrapper) {
            if (context is View.OnClickListener) {
                val clickListener = context as View.OnClickListener

                if (isPrevent) {
                    throttleClick { clickListener.onClick(this) }
                } else setOnClickListener(clickListener)
            }
            context = context.baseContext
        }
    }


    /**
     * 关闭当前界面
     * @param enabled 是否启用
     */
    @SuppressLint("CheckResult", "ObsoleteSdkInt")
    @BindingAdapter("finish")
    @JvmStatic
    fun View.finishActivity(enabled: Boolean = true) {
        if (enabled) {
            var temp = context
            var activity: Activity? = null

            while (temp is ContextWrapper) {
                if (temp is Activity) {
                    activity = temp
                }
                temp = temp.baseContext
            }

            val finalActivity = activity

            throttleClick {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finalActivity!!.finishAfterTransition()
                } else {
                    finalActivity!!.finish()
                }
            }
        }
    }

    // </editor-fold>


    /**
     * 在绑定视图时可以用于Model来处理UI, 由于破坏视图和逻辑解耦的规则不是很建议使用
     * 这会导致不方便业务逻辑进行单元测试
     *
     */
    interface OnBindListener {

        fun onBind(v: View)
    }
}
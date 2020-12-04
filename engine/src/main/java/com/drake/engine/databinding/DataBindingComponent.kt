/*
 * Copyright (C) 2018 Drake, Inc.
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
import android.graphics.Paint
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.View.NO_ID
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import com.drake.engine.base.app
import com.drake.engine.utils.throttleClick
import com.google.android.material.button.MaterialButton
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


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
    @JvmStatic
    @BindingAdapter("onBind")
    fun View.setView(listener: OnBindListener) {
        listener.onBind(this)
    }

    @JvmStatic
    @BindingAdapter("paddingStart", "paddingEnd", requireAll = false)
    fun View.bindPaddingEnd(start: View?, end: View?) {
        post {
            val startFinal = (start?.width ?: 0) + paddingStart
            val endFinal = (end?.width ?: 0) + paddingEnd
            setPaddingRelative(startFinal, paddingTop, endFinal, paddingBottom)
        }
    }
}


// <editor-fold desc="图片">

@BindingAdapter(
    value = ["leftDrawable", "topDrawable", "rightDrawable", "bottomDrawable"], requireAll = false
)
fun TextView.setTextViewDrawable(
    drawableLeft: Int, drawableTop: Int, drawableRight: Int, drawableBottom: Int
) {
    if (drawableLeft != 0 || drawableTop != 0 || drawableRight != 0 || drawableBottom != 0) {
        setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft, drawableTop, drawableRight, drawableBottom
        )
    }
}

@BindingAdapter("android:background")
fun View.setBackgroundRes(drawableId: Int) {
    if (drawableId != 0 && drawableId != NO_ID) {
        setBackgroundResource(drawableId)
    }
}

@BindingAdapter("android:backgroundTint")
fun MaterialButton.setBackgroundTintRes(color: Int) {
    if (color != 0 && color != NO_ID) {
        backgroundTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
    }
}


@BindingAdapter("android:src")
fun ImageView.setImageRes(drawableId: Int) {
    if (drawableId != 0 && drawableId != NO_ID) {
        setImageResource(drawableId)
    }
}

// </editor-fold>


// <editor-fold desc="显示">

/**
 * 隐藏控件
 */
@BindingAdapter("invisible")
fun View.setInvisible(visibilityVar: Boolean) {
    visibility = if (visibilityVar) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("invisible")
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
fun View.setGone(visibilityVar: Boolean) {
    visibility = if (visibilityVar) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("gone")
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
fun View.setElevationOf(dp: Int) {
    ViewCompat.setElevation(this, (dp * app.resources.displayMetrics.density).toInt().toFloat())
}

@BindingAdapter("android:elevation")
fun CardView.setElevationOf(dp: Int) {
    cardElevation = dp.toFloat()
}

// </editor-fold>


@BindingAdapter("del")
fun TextView.setDel(isAdd: Boolean) {
    if (isAdd) {
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   // 设置中划线并加清晰
    }
}

@BindingAdapter("url")
fun WebView.setUrl(url: String?) {
    if (!url.isNullOrEmpty()) {
        loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
    }
}


// <editor-fold desc="状态">

@BindingAdapter("android:enabled")
fun View.setEnableBind(enable: Boolean) {
    isEnabled = enable
}

@BindingAdapter("android:enabled")
fun View.setEnableBind(enable: Any?) {
    isEnabled = enable != null
}

@BindingAdapter("selected")
fun View.setSelectedBind(selected: Boolean) {
    isSelected = selected
}

@BindingAdapter("selected")
fun View.setSelectedBind(selected: Any?) {
    isSelected = selected != null
}


@BindingAdapter("activated")
fun View.setActivatedBind(activated: Boolean) {
    isActivated = activated
}


@BindingAdapter("activated")
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


// <editor-fold desc="人民币">

@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: String?) {
    if (!number.isNullOrEmpty()) {
        val format = "¥${number.format()}"
        if (format != text.toString()) text = format
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: Double) {
    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = RoundingMode.UP
    val format = "¥${numberFormat.format(number ?: 0.0)}"
    if (format != text.toString()) text = format
}

/**
 * 设置rmb，已经除100
 */
@SuppressLint("SetTextI18n")
@BindingAdapter("rmb")
fun TextView.setRMB(number: Long) {
    /**
     * 默认看做 "分" 处理(除以100)
     */
    val numberFormat = NumberFormat.getInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = RoundingMode.UP
    val format = "¥${numberFormat.format(number / 100.0)}"
    if (format != text.toString()) text = format
}

// </editor-fold>


// <editor-fold desc="时间">

@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun TextView.setDateFromMillis(milli: Long, format: String? = "yyyy-MM-dd") {
    /**
     * 格式化毫秒
     */
    val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
    val date = Date(milli)
    val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
    val formatText = sf.format(date)
    if (milli == 0L || formatText == text.toString()) {
        return
    }
    text = formatText
}


/**
 * 根据毫秒值来显示时间
 */
@BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
fun TextView.setDateFromMillis(milli: String?, format: String? = "yyyy-MM-dd") {
    var formatText = ""
    val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
    val date = Date(java.lang.Long.parseLong(formatText))
    val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
    formatText = sf.format(date)
    if (milli.isNullOrEmpty() || formatText == text.toString()) {
        return
    }
    text = formatText
}

@BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: Long, format: String? = "yyyy-MM-dd") {
    /**
     * 格式化毫秒
     */
    val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
    val date = Date(second * 1000)
    val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
    val formatText = sf.format(date)
    if (second == 0L || formatText == text.toString()) {
        return
    }
    text = formatText
}

@BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: String, format: String? = "yyyy-MM-dd") {
    /**
     * 格式化毫秒
     */
    val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
    val date = Date(java.lang.Long.parseLong(second) * 1000)
    val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
    val formatText = sf.format(date)
    if (TextUtils.isEmpty(second) || formatText == text.toString()) {
        return
    }
    text = formatText
}

// </editor-fold>
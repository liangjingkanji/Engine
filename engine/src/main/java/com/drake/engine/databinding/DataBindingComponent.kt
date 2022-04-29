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
import android.graphics.Paint
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.View.NO_ID
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.drake.engine.utils.throttleClick
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingMethods(
    BindingMethod(type = View::class, attribute = "android:enabled", method = "enabled"),
    BindingMethod(type = View::class, attribute = "android:selected", method = "selected"),
    BindingMethod(type = View::class, attribute = "android:activated", method = "activated"),
)
object DataBindingComponent {

    //<editor-fold desc="间距">
    @BindingAdapter("paddingStart", "paddingEnd", requireAll = false)
    @JvmStatic
    fun setPaddingHorizontal(v: View, start: View?, end: View?) {
        v.post {
            val startFinal = (start?.width ?: 0) + v.paddingStart
            val endFinal = (end?.width ?: 0) + v.paddingEnd
            v.setPaddingRelative(startFinal, v.paddingTop, endFinal, v.paddingBottom)
        }
    }
    //</editor-fold>

    // <editor-fold desc="图片">

    @BindingAdapter(
        value = ["leftDrawable", "topDrawable", "rightDrawable", "bottomDrawable"],
        requireAll = false
    )
    @JvmStatic
    fun setImageDrawable(
        v: TextView,
        leftDrawable: Int, topDrawable: Int, rightDrawable: Int, bottomDrawable: Int
    ) {
        v.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
    }

    @BindingAdapter(
        value = ["startDrawable", "topDrawable", "endDrawable", "bottomDrawable"],
        requireAll = false
    )
    @JvmStatic
    fun setImageDrawableRelative(
        v: TextView,
        startDrawable: Int, topDrawable: Int, endDrawable: Int, bottomDrawable: Int
    ) {
        v.setCompoundDrawablesRelativeWithIntrinsicBounds(startDrawable, topDrawable, endDrawable, bottomDrawable)
    }

    @BindingAdapter("android:background")
    @JvmStatic
    fun setBackgroundRes(v: View, drawableId: Int) {
        if (drawableId > NO_ID) v.setBackgroundResource(drawableId) else v.background = null
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun setImageDrawable(v: ImageView, @DrawableRes drawableId: Int) {
        if (drawableId > NO_ID) v.setImageResource(drawableId) else v.setImageDrawable(null)
    }

    // </editor-fold>


    // <editor-fold desc="隐藏">

    /**
     * 隐藏控件
     * @param isVisible 当为true则显示[View.VISIBLE], 否则隐藏[View.INVISIBLE]
     */
    @BindingAdapter("invisible")
    @JvmStatic
    fun setVisibleOrInvisible(v: View, isVisible: Boolean) {
        v.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    /**
     * 隐藏控件
     * @param isVisible 当不为null则显示[View.VISIBLE], 否则隐藏[View.INVISIBLE]
     */
    @BindingAdapter("invisible")
    @JvmStatic
    fun setVisibleOrInvisible(v: View, isVisible: Any?) {
        v.visibility = if (isVisible != null) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    /**
     * 隐藏控件
     * @param isVisible 当为true则显示[View.VISIBLE], 否则隐藏[View.GONE]
     */
    @BindingAdapter("gone")
    @JvmStatic
    fun setVisibleOrGone(v: View, isVisible: Boolean) {
        v.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 隐藏控件
     * @param isVisible 当不为null则显示[View.VISIBLE], 否则隐藏[View.INVISIBLE]
     */
    @BindingAdapter("gone")
    @JvmStatic
    fun setVisibleOrGone(v: View, isVisible: Any?) {
        v.visibility = if (isVisible != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    // </editor-fold>


    // <editor-fold desc="阴影">

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun setElevation(v: View, dp: Int) {
        ViewCompat.setElevation(v, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), v.resources.displayMetrics))
    }

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun setElevation(v: CardView, dp: Int) {
        v.cardElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), v.resources.displayMetrics)
    }

    // </editor-fold>

    // <editor-fold desc="状态">

    @BindingAdapter("android:enabled")
    @JvmStatic
    fun setEnabled(v: View, enable: Any?) {
        v.isEnabled = enable != null
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun setSelected(v: View, selected: Any?) {
        v.isSelected = selected != null
    }

    @BindingAdapter("activated")
    @JvmStatic
    fun setActivated(v: View, activated: Any?) {
        v.isActivated = activated != null
    }

    // </editor-fold>


    // <editor-fold desc="点击事件">

    /**
     * 防止暴力点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("click")
    @JvmStatic
    fun setThrottleClickListener(v: View, onClickListener: View.OnClickListener?) {
        if (onClickListener != null) {
            v.throttleClick { onClickListener.onClick(this) }
        }
    }


    /**
     * 自动将点击事件映射到Activity上
     * @param throttle 是否只支持快速点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("hit")
    @JvmStatic
    fun hit(v: View, throttle: Boolean = true) {
        var context = v.context
        while (context is ContextWrapper) {
            if (context is View.OnClickListener) {
                val clickListener = context as View.OnClickListener
                if (throttle) {
                    v.throttleClick { clickListener.onClick(this) }
                } else {
                    v.setOnClickListener(clickListener)
                }
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
    fun finishActivity(v: View, enabled: Boolean = true) {
        if (enabled) {
            var temp = v.context
            var activity: Activity? = null

            while (temp is ContextWrapper) {
                if (temp is Activity) {
                    activity = temp
                }
                temp = temp.baseContext
            }

            val finalActivity = activity

            v.throttleClick {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finalActivity!!.finishAfterTransition()
                } else {
                    finalActivity!!.finish()
                }
            }
        }
    }

    // </editor-fold>


    // <editor-fold desc="货币">

    /**
     * 格式化RMB
     * @param number 货币数量
     * @param unit 货币种类, 默认为 ¥
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb", "rmbUnit", requireAll = false)
    @JvmStatic
    fun formatCNY(v: TextView, number: String?, unit: String?) {
        if (!number.isNullOrEmpty() && v.text.contentEquals(number)) {
            val format = "${unit ?: "¥"}${number.format()}"
            if (format != v.text.toString()) v.text = format
        }
    }

    /**
     * 格式化RMB
     * @param number 货币数量
     * @param prefix 货币种类, 默认为 ¥
     * @param roundingMode 货币保留2位小数时的规则, 默认为[java.math.RoundingMode.UP]
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb", "rmbUnit", "roundingMode", requireAll = false)
    @JvmStatic
    fun formatCNY(v: TextView, number: Double?, prefix: String?, roundingMode: RoundingMode?) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = roundingMode ?: RoundingMode.UP
        val format = "${prefix ?: "¥"}${numberFormat.format(number ?: 0.0)}"
        if (format != v.text.toString()) v.text = format
    }

    /**
     * 设置rmb，默认看做 "分" 处理(除以100)
     * @param number 货币数量
     * @param prefix 货币种类, 默认为 ¥
     * @param roundingMode 货币保留2位小数时的规则, 默认为[java.math.RoundingMode.UP]
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb", "rmbUnit", "roundingMode", requireAll = false)
    @JvmStatic
    fun formatCNY(v: TextView, number: Long?, prefix: String?, roundingMode: RoundingMode?) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = roundingMode ?: RoundingMode.UP
        val format = "${prefix ?: "¥"}${numberFormat.format(number ?: 0 / 100.0)}"
        if (format != v.text.toString()) v.text = format
    }

    // </editor-fold>

    // <editor-fold desc="时间">

    /**
     * 根据时间产生格式化字符串
     * @param milli 指定时间, 单位毫秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
    @JvmStatic
    fun setDateFromMillis(v: TextView, milli: Long, format: String? = "yyyy-MM-dd") {
        if (milli < 0) {
            v.text = ""
            return
        }
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(milli)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (v.text.contentEquals(formatText)) return
        v.text = formatText
    }


    /**
     * 根据时间产生格式化字符串
     * @param milli 指定时间, 单位毫秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
    @JvmStatic
    fun setDateFromMillis(v: TextView, milli: String?, format: String? = "yyyy-MM-dd") {
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val finalMilli = milli?.toLongOrNull() ?: return
        if (finalMilli < 0 || milli.isNullOrBlank()) {
            v.text = ""
            return
        }
        val date = Date(finalMilli)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (v.text.contentEquals(formatText)) return
        v.text = formatText
    }

    /**
     * 根据时间产生格式化字符串
     * @param second 指定时间, 单位秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
    @JvmStatic
    fun setDateFromSecond(v: TextView, second: Long, format: String? = "yyyy-MM-dd") {
        if (second < 0) {
            v.text = ""
            return
        }
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(second * 1000)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (v.text.contentEquals(formatText)) return
        v.text = formatText
    }

    /**
     * 根据时间产生格式化字符串
     * @param second 指定时间, 单位秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
    @JvmStatic
    fun setDateFromSecond(v: TextView, second: String?, format: String? = "yyyy-MM-dd") {
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val finalSecond = second?.toLongOrNull() ?: return
        if (finalSecond < 0 || second.isNullOrBlank()) {
            v.text = ""
            return
        }
        val date = Date(finalSecond * 1000)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (v.text.contentEquals(formatText)) return
        v.text = formatText
    }

    // </editor-fold>

    //<editor-fold desc="字符串">
    @BindingAdapter("del")
    @JvmStatic
    fun setDel(v: TextView, isAdd: Boolean) {
        if (isAdd) {
            v.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   // 设置中划线并加清晰
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setTextOfNumber(v: TextView, number: Int) {
        val finalText = number.toString()
        if (!v.text.contentEquals(finalText)) {
            v.text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setTextOfNumber(v: TextView, number: Long) {
        val finalText = number.toString()
        if (!v.text.contentEquals(finalText)) {
            v.text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setTextOfNumber(v: TextView, number: Double) {
        val finalText = number.toString()
        if (!v.text.contentEquals(finalText)) {
            v.text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setTextOfNumber(v: TextView, number: Float) {
        val finalText = number.toString()
        if (!v.text.contentEquals(finalText)) {
            v.text = finalText
        }
    }

    @BindingAdapter("android:textColor")
    @JvmStatic
    fun setTextColor(text: TextView, color: String) {
        text.setTextColor(android.graphics.Color.parseColor(color))
    }

    //</editor-fold>

    //<editor-fold desc="网页">
    @BindingAdapter("url")
    @JvmStatic
    fun setUrl(v: WebView, url: String?) {
        if (!url.isNullOrEmpty()) {
            v.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
        }
    }
    //</editor-fold>

    //<editor-fold desc="回调">
    /**
     * 可将视图传递给函数调用. 在绑定视图时可以用于Model来处理UI, 由于破坏视图和逻辑解耦的规则不是很建议使用
     * 使用方法:
     *  onBind="{(v)->m.bind(v)}" 或者  onBind="{m::bind}", 此写法要求method方法签名一致
     *      fun bind(v: View) {
     *          if (v is TextView) {
     *              // ... do something
     *          }
     *      }
     */
    @BindingAdapter("onBind")
    @JvmStatic
    fun setOnBindListener(v: View, listener: OnBindListener) {
        listener.onBind(v)
    }

    interface OnBindListener {
        fun onBind(v: View)
    }
    //</editor-fold>
}
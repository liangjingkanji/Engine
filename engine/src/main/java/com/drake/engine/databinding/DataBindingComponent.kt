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
import android.util.Log
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
    fun View.setPaddingHorizontal(start: View?, end: View?) {
        post {
            val startFinal = (start?.width ?: 0) + paddingStart
            val endFinal = (end?.width ?: 0) + paddingEnd
            setPaddingRelative(startFinal, paddingTop, endFinal, paddingBottom)
        }
    }
    //</editor-fold>

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
        if (drawableId > NO_ID) setBackgroundResource(drawableId) else background = null
    }

    @BindingAdapter("android:src")
    @JvmStatic
    fun ImageView.setImageDrawable(@DrawableRes drawableId: Int) {
        if (drawableId > NO_ID) setImageResource(drawableId) else setImageDrawable(null)
    }

    // </editor-fold>


    // <editor-fold desc="隐藏">

    /**
     * 隐藏控件
     * @param isVisible 当为true则显示[View.VISIBLE], 否则隐藏[View.INVISIBLE]
     */
    @BindingAdapter("invisible")
    @JvmStatic
    fun View.setVisibleOrInvisible(isVisible: Boolean) {
        visibility = if (isVisible) {
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
    fun View.setVisibleOrInvisible(isVisible: Any?) {
        visibility = if (isVisible != null) {
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
    fun View.setVisibleOrGone(isVisible: Boolean) {
        visibility = if (isVisible) {
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
    fun View.setVisibleOrGone(isVisible: Any?) {
        visibility = if (isVisible != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


    // </editor-fold>


    // <editor-fold desc="阴影">

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun View.setElevation(dp: Int) {
        ViewCompat.setElevation(this, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics))
    }

    @BindingAdapter("android:elevation")
    @JvmStatic
    fun CardView.setElevation(dp: Int) {
        Log.d("日志", "(DataBindingComponent.kt:170) -> setElevation    ")
        cardElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
    }

    // </editor-fold>

    // <editor-fold desc="状态">

    @BindingAdapter("android:enabled")
    @JvmStatic
    fun View.setEnabled(enable: Any?) {
        isEnabled = enable != null
    }

    @BindingAdapter("selected")
    @JvmStatic
    fun View.setSelected(selected: Any?) {
        isSelected = selected != null
    }

    @BindingAdapter("activated")
    @JvmStatic
    fun View.setActivated(activated: Any?) {
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
    fun View.setThrottleClickListener(onClickListener: View.OnClickListener?) {
        if (onClickListener != null) {
            throttleClick { onClickListener.onClick(this) }
        }
    }


    /**
     * 自动将点击事件映射到Activity上
     * @param throttle 是否只支持快速点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter("hit")
    @JvmStatic
    fun View.hit(throttle: Boolean = true) {
        var context = context
        while (context is ContextWrapper) {
            if (context is View.OnClickListener) {
                val clickListener = context as View.OnClickListener
                if (throttle) {
                    throttleClick { clickListener.onClick(this) }
                } else {
                    setOnClickListener(clickListener)
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


    // <editor-fold desc="货币">

    /**
     * 格式化RMB
     * @param number 货币数量
     * @param unit 货币种类, 默认为 ¥
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("rmb", "rmbUnit", requireAll = false)
    @JvmStatic
    fun TextView.formatCNY(number: String?, unit: String?) {
        if (!number.isNullOrEmpty() && text.contentEquals(number)) {
            val format = "${unit ?: "¥"}${number.format()}"
            if (format != text.toString()) text = format
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
    fun TextView.formatCNY(number: Double?, prefix: String?, roundingMode: RoundingMode?) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = roundingMode ?: RoundingMode.UP
        val format = "${prefix ?: "¥"}${numberFormat.format(number ?: 0.0)}"
        if (format != text.toString()) text = format
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
    fun TextView.formatCNY(number: Long?, prefix: String?, roundingMode: RoundingMode?) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        numberFormat.roundingMode = roundingMode ?: RoundingMode.UP
        val format = "${prefix ?: "¥"}${numberFormat.format(number ?: 0 / 100.0)}"
        if (format != text.toString()) text = format
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
    fun TextView.setDateFromMillis(milli: Long, format: String? = "yyyy-MM-dd") {
        if (milli < 0) {
            text = ""
            return
        }
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(milli)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (text.contentEquals(formatText)) return
        text = formatText
    }


    /**
     * 根据时间产生格式化字符串
     * @param milli 指定时间, 单位毫秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateMilli", "dateFormat"], requireAll = false)
    @JvmStatic
    fun TextView.setDateFromMillis(milli: String?, format: String? = "yyyy-MM-dd") {
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val finalMilli = milli?.toLongOrNull() ?: return
        if (finalMilli < 0 || milli.isNullOrBlank()) {
            text = ""
            return
        }
        val date = Date(finalMilli)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (text.contentEquals(formatText)) return
        text = formatText
    }

    /**
     * 根据时间产生格式化字符串
     * @param second 指定时间, 单位秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
    @JvmStatic
    fun TextView.setDateFromSecond(second: Long, format: String? = "yyyy-MM-dd") {
        if (second < 0) {
            text = ""
            return
        }
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val date = Date(second * 1000)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (text.contentEquals(formatText)) return
        text = formatText
    }

    /**
     * 根据时间产生格式化字符串
     * @param second 指定时间, 单位秒, 如果小于0将设置为空字符串
     * @param format 格式化文本
     */
    @BindingAdapter(value = ["dateSecond", "dateFormat"], requireAll = false)
    @JvmStatic
    fun TextView.setDateFromSecond(second: String?, format: String? = "yyyy-MM-dd") {
        val finalFormat = if (format.isNullOrBlank()) "yyyy-MM-dd" else format
        val finalSecond = second?.toLongOrNull() ?: return
        if (finalSecond < 0 || second.isNullOrBlank()) {
            text = ""
            return
        }
        val date = Date(finalSecond * 1000)
        val sf = SimpleDateFormat(finalFormat, Locale.CHINA)
        val formatText = sf.format(date)
        if (text.contentEquals(formatText)) return
        text = formatText
    }

    // </editor-fold>

    //<editor-fold desc="字符串">
    @BindingAdapter("del")
    @JvmStatic
    fun TextView.setDel(isAdd: Boolean) {
        if (isAdd) {
            paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   // 设置中划线并加清晰
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun TextView.setTextOfNumber(number: Int) {
        val finalText = number.toString()
        if (!text.contentEquals(finalText)) {
            text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun TextView.setTextOfNumber(number: Long) {
        val finalText = number.toString()
        if (!text.contentEquals(finalText)) {
            text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun TextView.setTextOfNumber(number: Double) {
        val finalText = number.toString()
        if (!text.contentEquals(finalText)) {
            text = finalText
        }
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun TextView.setTextOfNumber(number: Float) {
        val finalText = number.toString()
        if (!text.contentEquals(finalText)) {
            text = finalText
        }
    }

    //</editor-fold>

    //<editor-fold desc="网页">
    @BindingAdapter("url")
    @JvmStatic
    fun WebView.setUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
        }
    }
    //</editor-fold>

    //<editor-fold desc="回调">
    /**
     * 在绑定视图时可以用于Model来处理UI, 由于破坏视图和逻辑解耦的规则不是很建议使用
     *
     * @see OnBindListener 该接口支持泛型定义具体视图
     *
     * @receiver View
     * @param listener OnBindListener<View>
     */
    @BindingAdapter("onBind")
    @JvmStatic
    fun View.setOnBindListener(listener: OnBindListener) {
        listener.onBind(this)
    }

    /**
     * 在绑定视图时可以用于Model来处理UI, 由于破坏视图和逻辑解耦的规则不是很建议使用
     */
    interface OnBindListener {
        fun onBind(v: View)
    }
    //</editor-fold>
}
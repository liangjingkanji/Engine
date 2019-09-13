/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.databinding

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.View.NO_ID
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.drake.brv.DefaultDecoration
import com.drake.engine.base.getApp
import com.drake.engine.utils.DateUtils
import com.drake.engine.widget.SmoothCheckBox
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.dip

/**
 * 视图显示组件
 */


// <editor-fold desc="图片">

/**
 * 通过Drawable Id 设置文本四周的图片
 */
@BindingAdapter(
    value = ["leftDrawable", "topDrawable", "rightDrawable", "bottomDrawable"],
    requireAll = false
)
fun TextView.setTextViewDrawable(
    drawableLeft: Int,
    drawableTop: Int,
    drawableRight: Int,
    drawableBottom: Int
) {
    if (drawableLeft != 0 || drawableTop != 0 || drawableRight != 0 || drawableBottom != 0) {
        setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft,
            drawableTop,
            drawableRight,
            drawableBottom
        )
    }
}

@BindingAdapter(
    value = ["leftDrawable", "topDrawable", "rightDrawable", "bottomDrawable"],
    requireAll = false
)
fun TextView.setTextViewDrawable(
    drawableLeft: Drawable?,
    drawableTop: Drawable?,
    drawableRight: Drawable?,
    drawableBottom: Drawable?
) {
    if (drawableLeft != null || drawableTop != null || drawableRight != null || drawableBottom != null) {
        setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft,
            drawableTop,
            drawableRight,
            drawableBottom
        )
    }
}

/**
 * 背景
 */
@BindingAdapter("backgroundResource")
fun View.setBackgroundDrawable(drawableId: Int) {
    if (drawableId != 0 && drawableId != NO_ID) {
        setBackgroundResource(drawableId)
    }
}

@BindingAdapter("backgroundTint")
fun MaterialButton.setBackgroundTint(color: Int) {
    if (color != 0 && color != NO_ID) {
        backgroundTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
    }
}

/**
 * 图片
 */
@BindingAdapter("imageResource")
fun ImageView.setBindImageResource(drawableId: Int) {
    if (drawableId != 0 && drawableId != NO_ID) {
        setImageResource(drawableId)
    }
}

/**
 * 加载圆角图片
 *
 * @param url            加载图片的url
 * @param roundedCorners 设置圆角, 如果该参数不加则会为圆形
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["imgCircular", "placeHolder", "roundedCorners"], requireAll = false)
fun ImageView.loadCircular(
    url: Any?,
    placeHolder: Drawable? = null, @Dimension roundedCorners: Int = 0
) {

    val requestOptions = RequestOptions()

    if (roundedCorners == 0) {
        requestOptions.circleCrop()
    } else {
        requestOptions.transforms(CenterCrop(), RoundedCorners(getApp().dip(roundedCorners)))
    }

    if (placeHolder == null && drawable != null) {
        requestOptions.placeholder(drawable)
    } else if (placeHolder != null) {
        requestOptions.placeholder(placeHolder)
    }

    if (url == null || (url is CharSequence && url.isEmpty()) || url is Int && url == NO_ID) {
        Glide.with(context).load(placeHolder).apply(requestOptions).into(this)
    } else {
        Glide.with(context).load(url).apply(requestOptions).into(this)
    }

}

/**
 * 加载图片
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["img", "placeHolder"], requireAll = false)
fun ImageView.loadImage(url: Any?, placeHolder: Drawable? = null) {

    if (url == null || (url is CharSequence && url.isEmpty()) || url is Int && url == NO_ID) {
        placeHolder?.let {
            setImageDrawable(it)
        }
        return
    }

    val requestOptions = RequestOptions()

    if (placeHolder == null && drawable != null) {
        requestOptions.placeholder(drawable)
    } else if (placeHolder != null) {
        requestOptions.placeholder(placeHolder)
    }

    Glide.with(context).load(url).apply(requestOptions).into(this)
}

@BindingAdapter(value = ["dateFromSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: Long, format: String) {
    var temp = format
    if (TextUtils.isEmpty(temp)) {
        temp = "yyyy-MM-dd"
    }

    val formatText = DateUtils.formatDate(second * 1000, temp)

    val oldText = text.toString()
    if (second == 0L || formatText == oldText) {
        return
    }
    text = formatText
}

@BindingAdapter(value = ["dateFromSecond", "dateFormat"], requireAll = false)
fun TextView.setDateFromSecond(second: String, format: String) {
    var temp = format

    if (TextUtils.isEmpty(temp)) {
        temp = "yyyy-MM-dd"
    }

    val formatText = DateUtils.formatDate(java.lang.Long.parseLong(second) * 1000, temp)

    val oldText = text.toString()

    if (TextUtils.isEmpty(second) || formatText == oldText) {
        return
    }
    text = formatText
}

// </editor-fold>


// <editor-fold desc="隐藏">

/**
 * 隐藏控件
 */
@BindingAdapter("invisible")
fun View.setViewVisibilityINVISIBLE(visibilityVar: Boolean) {
    visibility = if (visibilityVar) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("invisible")
fun View.setViewVisibilityINVISIBLE(visibilityVar: Any?) {
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
fun View.setViewVisibilityGONE(visibilityVar: Boolean) {
    visibility = if (visibilityVar) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("gone")
fun View.setViewVisibilityGONE(visibilityVar: Any?) {
    visibility = if (visibilityVar != null) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

// </editor-fold>


// <editor-fold desc="阴影">


@BindingAdapter("shadow")
fun View.setShadow(shadowDip: Int) {
    ViewCompat.setElevation(this, getApp().dip(shadowDip).toFloat())
}

@BindingAdapter("shadow")
fun CardView.setShadow(shadowDip: Int) {
    cardElevation = shadowDip.toFloat()
}

// </editor-fold>

// <editor-fold desc="RecyclerView">

@BindingAdapter("divider")
fun RecyclerView.setDivider(dividerDrawable: Drawable) {
    addItemDecoration(DefaultDecoration(context).apply { setDrawable(dividerDrawable) })
}

// </editor-fold>

@BindingAdapter("checked")
fun SmoothCheckBox.setBindChecked(isCheckedVar: Boolean) {
    isChecked = isCheckedVar
}

@BindingAdapter("addMiddleLine")
fun TextView.setMiddleLine(isAdd: Boolean) {
    if (isAdd) {
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   //设置中划线并加清晰
    }
}

@BindingAdapter("loadUrl")
fun WebView.setUrl(url: String?) {
    if (!url.isNullOrEmpty()) {
        loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
    }
}
/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.databinding

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
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
import com.drake.engine.base.App
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.dip


// <editor-fold desc="图片">

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

/**
 * 加载圆角图片
 *
 * @param url           图片来源
 * @param holder           占位图, 如果不设置且存在 android:src 则为占位图
 * @param corner 设置圆角, 默认为圆
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["imgCorner", "holder", "corner"], requireAll = false)
fun ImageView.loadImgCorner(
    url: Any?,
    holder: Drawable? = null, @Dimension corner: Int = 0
) {

    val requestOptions = RequestOptions()

    if (corner == 0) {
        requestOptions.circleCrop()
    } else {
        requestOptions.transforms(CenterCrop(), RoundedCorners(App.dip(corner)))
    }

    if (holder == null && drawable != null) {
        requestOptions.placeholder(drawable)
    } else if (holder != null) {
        requestOptions.placeholder(holder)
    }

    if (url == null || (url is CharSequence && url.isEmpty()) || url is Int && url == NO_ID) {
        Glide.with(context).load(holder).apply(requestOptions).into(this)
    } else {
        Glide.with(context).load(url).apply(requestOptions).into(this)
    }
}


@SuppressLint("CheckResult")
@BindingAdapter(value = ["img", "holder"], requireAll = false)
fun ImageView.loadImg(url: Any?, holder: Drawable? = null) {

    if (url == null || (url is CharSequence && url.isEmpty()) || url is Int && url == NO_ID) {
        holder?.let {
            setImageDrawable(it)
        }
        return
    }

    val requestOptions = RequestOptions()

    if (holder == null && drawable != null) {
        requestOptions.placeholder(drawable)
    } else if (holder != null) {
        requestOptions.placeholder(holder)
    }

    Glide.with(context).load(url).apply(requestOptions).into(this)
}


@SuppressLint("CheckResult")
@BindingAdapter(value = ["gif", "holder"], requireAll = false)
fun ImageView.loadGif(url: Any?, holder: Drawable? = null) {

    if (url == null || (url is CharSequence && url.isEmpty()) || url is Int && url == NO_ID) {
        holder?.let {
            setImageDrawable(it)
        }
        return
    }

    val requestOptions = RequestOptions()

    if (holder == null && drawable != null) {
        requestOptions.placeholder(drawable)
    } else if (holder != null) {
        requestOptions.placeholder(holder)
    }

    Glide.with(context).asGif().load(url).apply(requestOptions).into(this)
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


@BindingAdapter("elevation")
fun View.setElevationDp(dp: Int) {
    ViewCompat.setElevation(this, App.dip(dp).toFloat())
}

@BindingAdapter("elevation")
fun CardView.setElevationDp(dp: Int) {
    cardElevation = dp.toFloat()
}

// </editor-fold>

// <editor-fold desc="RecyclerView">

@BindingAdapter(value = ["divider", "orientation"], requireAll = false)
fun RecyclerView.setDivider(divider: Drawable, orientation: Boolean) {

    val decoration = if (orientation) {
        DefaultDecoration(context).apply { setDrawable(divider) }
    } else DefaultDecoration(context, RecyclerView.HORIZONTAL).apply { setDrawable(divider) }

    addItemDecoration(decoration)
}

@BindingAdapter(value = ["divider", "orientation"], requireAll = false)
fun RecyclerView.setDivider(divider: Int, orientation: Boolean) {

    val decoration = if (orientation) {
        DefaultDecoration(context).apply { setDrawable(divider) }
    } else DefaultDecoration(context, RecyclerView.HORIZONTAL).apply { setDrawable(divider) }

    addItemDecoration(decoration)
}

// </editor-fold>


@BindingAdapter("del")
fun TextView.setDel(isAdd: Boolean) {
    if (isAdd) {
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG   // 设置中划线并加清晰
    }
}

@BindingAdapter("loadUrl")
fun WebView.setUrl(url: String?) {
    if (!url.isNullOrEmpty()) {
        loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
    }
}
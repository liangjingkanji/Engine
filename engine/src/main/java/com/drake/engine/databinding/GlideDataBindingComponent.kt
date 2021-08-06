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
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Dimension
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.drake.engine.base.app

object GlideDataBindingComponent {
    /**
     * 加载圆角图片
     *
     * @param url 图片来源
     * @param holder 占位图, 如果不设置且存在 android:src 则为占位图
     * @param corner 设置圆角, 默认为圆
     */
    @BindingAdapter(value = ["imgCorner", "holder", "corner"], requireAll = false)
    @JvmStatic
    fun ImageView.loadImageCornerWithHolder(
        url: Any?,
        holder: Drawable?,
        @Dimension corner: Int = 0
    ) {
        val requestOptions = if (corner == 0) {
            RequestOptions().circleCrop().placeholder(holder)
        } else {
            RequestOptions().transforms(
                CenterCrop(),
                RoundedCorners((corner * app.resources.displayMetrics.density).toInt())
            ).placeholder(holder)
        }
        Glide.with(context).load(url).apply(requestOptions).into(this)
    }

    @BindingAdapter(value = ["imgCorner", "corner"], requireAll = false)
    @JvmStatic
    fun ImageView.loadImageCorner(url: Any?, @Dimension corner: Int = 0) {
        val requestOptions = if (corner == 0) {
            RequestOptions().circleCrop().placeholder(drawable)
        } else {
            RequestOptions().transforms(
                CenterCrop(),
                RoundedCorners((corner * app.resources.displayMetrics.density).toInt())
            ).placeholder(drawable)
        }
        Glide.with(context).load(url).apply(requestOptions).into(this)
    }


    @BindingAdapter(value = ["img", "holder"])
    @JvmStatic
    fun ImageView.loadImageWithHolder(url: Any?, holder: Drawable?) {
        Glide.with(context).load(url).apply(RequestOptions().placeholder(holder)).into(this)
    }

    @BindingAdapter("img")
    @JvmStatic
    fun ImageView.loadImage(url: Any?) {
        Glide.with(context).load(url).apply(RequestOptions().placeholder(drawable)).into(this)
    }


    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["gif", "holder"], requireAll = false)
    @JvmStatic
    fun ImageView.loadGif(url: Any?, holder: Drawable? = null) {

        if (url == null || (url is CharSequence && url.isEmpty()) || (url is Int && url == View.NO_ID)) {
            holder?.let {
                setImageDrawable(it)
            }
            return
        }

        val requestOptions = if (holder == null && drawable != null) {
            RequestOptions().placeholder(drawable)
        } else {
            RequestOptions().placeholder(holder)
        }

        Glide.with(context).asGif().load(url).apply(requestOptions).into(this)
    }
}
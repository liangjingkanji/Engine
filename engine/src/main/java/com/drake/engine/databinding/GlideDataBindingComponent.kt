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
import android.widget.ImageView
import androidx.annotation.Dimension
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.drake.engine.base.app

object GlideDataBindingComponent {

    /**
     * 加载圆形图片
     * @param url 图片来源
     * @param holder 占位图
     * @param corner 设置圆角, 如果设置参数则默认为圆形
     */
    @BindingAdapter(value = ["imgCorner", "holder", "corner"], requireAll = false)
    @JvmStatic
    @Deprecated("使用imgCircle或img替换")
    fun loadImageCornerWithHolder(
        v: ImageView,
        url: Any?,
        holder: Drawable?,
        @Dimension corner: Int?
    ) {
        if (url == null && holder == null) v.setImageDrawable(null)
        val requestBuilder = Glide.with(v.context).load(url).placeholder(holder)
        if (corner == null) {
            requestBuilder.circleCrop()
        } else {
            requestBuilder.transform(
                CenterCrop(),
                RoundedCorners((corner * app.resources.displayMetrics.density).toInt())
            )
        }
        requestBuilder.into(v)
    }

    /**
     * 加载圆形图片
     * @param url 图片来源
     * @param holder 占位图
     */
    @BindingAdapter(value = ["imgCircle", "holder"], requireAll = false)
    @JvmStatic
    fun loadImageCircle(v: ImageView, url: Any?, holder: Drawable?) {
        if (url == null && holder == null) v.setImageDrawable(null)
        Glide.with(v.context).load(url).circleCrop().placeholder(holder).into(v)
    }

    /**
     * 加载图片
     * @param url 图片来源
     * @param holder 占位图
     * @param corner 图片四周圆角半径值
     */
    @BindingAdapter(value = ["img", "holder", "corner"], requireAll = false)
    @JvmStatic
    fun loadImageWithHolder(v: ImageView, url: Any?, holder: Drawable?, corner: Int?) {
        if (url == null && holder == null) v.setImageDrawable(null)
        val requestBuilder = Glide.with(v.context).load(url).placeholder(holder)
        if (corner != null) {
            requestBuilder.transform(
                CenterCrop(),
                RoundedCorners((corner * app.resources.displayMetrics.density).toInt())
            )
        }
        requestBuilder.into(v)
    }

    /**
     * 加载圆形Gif
     * @param url 图片来源
     * @param holder 占位图
     */
    @BindingAdapter(value = ["gifCircle", "holder"], requireAll = false)
    @JvmStatic
    fun loadGifCircle(v: ImageView, url: Any?, holder: Drawable?) {
        Glide.with(v.context).asGif().load(url).circleCrop().placeholder(holder).into(v)
    }

    /**
     * 加载Gif图片
     * @param url 图片来源
     * @param holder 占位图
     * @param corner 图片四周圆角半径值
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["gif", "holder", "corner"], requireAll = false)
    @JvmStatic
    fun loadGifWithHolder(v: ImageView, url: Any?, holder: Drawable? = null, corner: Int?) {
        val requestBuilder = Glide.with(v.context).asGif().load(url).placeholder(holder)
        if (corner != null) {
            requestBuilder.transform(
                CenterCrop(),
                RoundedCorners((corner * app.resources.displayMetrics.density).toInt())
            )
        }
        requestBuilder.into(v)
    }
}
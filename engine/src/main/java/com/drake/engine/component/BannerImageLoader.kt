/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        Glide.with(context)
            .load(path)
            .into(imageView)
    }
}
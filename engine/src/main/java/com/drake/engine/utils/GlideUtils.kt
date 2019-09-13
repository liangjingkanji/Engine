/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.utils

import android.content.Context
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * 使用Glide加载图片
 */


fun Context.downloadImage(url: String, with: Int = 0, height: Int = 0): Observable<File> {
    return Observable.create<File> {
        val download = Glide.with(this).download(url)
        val futureTarget = if (with == 0 && height == 0) {
            download.submit()
        } else {
            download.submit(with, height)
        }
        val file = futureTarget.get()
        if (!it.isDisposed) {
            it.onNext(file)
            it.onComplete()
        }
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}


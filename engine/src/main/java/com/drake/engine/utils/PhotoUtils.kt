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

package com.drake.engine.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore

/**
 * 保存图片到相册
 * @param fileName 相册文件显示名称
 * @param format 图片格式
 * @param quality 图片质量
 */
private fun Bitmap.saveToPhoto(
    context: Context,
    fileName: String? = null,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100,
): Boolean {
    val contentValues = ContentValues()
    fileName?.let {
        contentValues.put(MediaStore.MediaColumns.TITLE, fileName)
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    }
    val mimeType = when (format) {
        Bitmap.CompressFormat.JPEG -> "image/jpeg"
        Bitmap.CompressFormat.PNG -> "image/png"
        Bitmap.CompressFormat.WEBP -> "image/webp"
        else -> "image/png"
    }
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
    try {
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return false
        context.sendBroadcast(Intent("com.android.camera.NEW_PICTURE", uri))
        context.contentResolver.openOutputStream(uri).use {
            compress(format, quality, it)
        }
        return true
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        return false
    }
}
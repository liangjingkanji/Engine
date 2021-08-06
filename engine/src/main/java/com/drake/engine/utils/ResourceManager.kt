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

@file:Suppress("MemberVisibilityCanBePrivate", "UseCompatLoadingForDrawables")

package com.drake.engine.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import com.drake.engine.base.app

class ResourceManager(val path: String) {

    companion object {
        const val RES_TYPE_STRING = "string"
        const val RES_TYPE_DRAWABLE = "string"
        const val RES_TYPE_ID = "id"
        const val RES_TYPE_LAYOUT = "layout"
        const val RES_TYPE_XML = "xml"
        const val RES_TYPE_STYLE = "style"
        const val RES_TYPE_ARRAY = "array"
        const val RES_TYPE_MIPMAP = "mipmap"
        const val RES_TYPE_COLOR = "color"
    }

    var resources: Resources
    var packageInfo: PackageInfo

    init {
        val clazz = AssetManager::class.java
        val assetManager = clazz.newInstance()
        clazz.getMethod("addAssetPath", String::class.java).invoke(assetManager, path)
        resources =
            Resources(assetManager, app.resources.displayMetrics, app.resources.configuration)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageInfo = app.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
    }

    fun drawable(name: String): Drawable? {
        val identifier = resources.getIdentifier(name, RES_TYPE_DRAWABLE, packageInfo.packageName)
        return resources.getDrawable(identifier)
    }

    fun mipmap(name: String): Drawable? {
        val identifier = resources.getIdentifier(name, RES_TYPE_MIPMAP, packageInfo.packageName)
        return resources.getDrawable(identifier)
    }

    fun string(name: String): String {
        val identifier = resources.getIdentifier(name, RES_TYPE_STRING, packageInfo.packageName)
        return resources.getString(identifier)
    }

    fun stringArray(name: String): Array<out String> {
        val identifier = resources.getIdentifier(name, RES_TYPE_ARRAY, packageInfo.packageName)
        return resources.getStringArray(identifier)
    }

    fun color(name: String): Int {
        val identifier = resources.getIdentifier(name, RES_TYPE_COLOR, packageInfo.packageName)
        return resources.getColor(identifier)
    }

    fun layout(name: String): XmlResourceParser {
        val identifier = resources.getIdentifier(name, RES_TYPE_LAYOUT, packageInfo.packageName)
        return resources.getLayout(identifier)
    }

    fun xml(name: String): XmlResourceParser {
        val identifier = resources.getIdentifier(name, RES_TYPE_XML, packageInfo.packageName)
        return resources.getXml(identifier)
    }
}
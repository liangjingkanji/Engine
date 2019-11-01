/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/15/19 3:27 PM
 */
@file:Suppress("SENSELESS_COMPARISON")

package com.drake.engine.utils

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.math.min


@Suppress("MemberVisibilityCanBePrivate")
object LogCat {

    private var defaultTag = "日志"
    private var enabled = true

    val trees = mutableListOf<Tree>()


    /**
     * 设置日志配置
     *
     * @param tag     过滤TAG
     * @param enabled 是否打印
     */
    fun setConfig(tag: String = defaultTag, enabled: Boolean = this.enabled) {
        this.enabled = enabled
        defaultTag = tag
    }


    /**
     * 日志信息将被分发到Tree中
     * @param tree Tree
     */
    fun addTree(tree: Tree) {
        trees.add(tree)
    }

    // <editor-fold desc="日志输出">

    fun v(message: String, tag: String = defaultTag, t: Throwable? = null) {
        if (enabled) {
            android.util.Log.v(tag, message)

            if (trees.isNotEmpty()) {
                trees.forEach {
                    it.log(message, tag, t, LogLevel.LOG_LEVEL_VERBOSE)
                }
            }
        }
    }

    fun i(message: String, tag: String = defaultTag, t: Throwable? = null) {
        if (enabled) {
            android.util.Log.i(tag, message)

            if (trees.isNotEmpty()) {
                trees.forEach {
                    it.log(message, tag, t, LogLevel.LOG_LEVEL_INFO)
                }
            }
        }
    }

    fun d(message: String, tag: String = defaultTag, t: Throwable? = null) {
        if (enabled) {
            android.util.Log.d(tag, message)

            if (trees.isNotEmpty()) {
                trees.forEach {
                    it.log(message, tag, t, LogLevel.LOG_LEVEL_DEBUG)
                }
            }
        }
    }


    fun w(message: String, tag: String = defaultTag, t: Throwable? = null) {
        if (enabled) {
            android.util.Log.w(tag, message)

            if (trees.isNotEmpty()) {
                trees.forEach {
                    it.log(message, tag, t, LogLevel.LOG_LEVEL_WARN)
                }
            }
        }
    }

    fun e(message: String, tag: String = defaultTag, t: Throwable? = null) {
        if (enabled) {
            android.util.Log.e(tag, message, t)

            if (trees.isNotEmpty()) {
                trees.forEach {
                    it.log(message, tag, t, LogLevel.LOG_LEVEL_ERROR)
                }
            }
        }
    }


    /**
     * Json格式输出Log
     *
     * @param tag     标签
     * @param url     Url
     * @param message Json字符串
     */
    fun json(message: String?, tag: String = defaultTag, url: String? = null) {

        synchronized(LogCat::class.java) {

            if (!enabled || message == null) return

            val jsonTokener = JSONTokener(message)


            var actualMessage: String

            actualMessage = when (val value = jsonTokener.nextValue()) {

                is JSONObject -> {
                    value.toString(2)
                }
                is JSONArray -> {
                    value.toString(2)
                }
                else -> value.toString()
            }

            if (!url.isNullOrEmpty()) actualMessage = "$url\r\n$actualMessage"

            big(actualMessage, tag)
        }
    }

    /**
     * 如果字符串超长将分段输出
     *
     * @param message
     */
    fun big(message: String, tag: String = defaultTag) {
        synchronized(this) {
            val max = 3900
            val length = message.length
            var startIndex = 0
            var endIndex = max

            if (length <= max) {
                i(message, tag)
            } else {
                while (startIndex < length) {
                    endIndex = min(length, endIndex)

                    val content = message.substring(startIndex, endIndex)
                    i(content, tag)

                    startIndex += max
                    endIndex += max
                }
            }
        }
    }

    // </editor-fold>
}

interface Tree {
    fun log(message: String, tag: String, t: Throwable?, level: LogLevel)
}

enum class LogLevel {
    LOG_LEVEL_VERBOSE,
    LOG_LEVEL_INFO,
    LOG_LEVEL_DEBUG,
    LOG_LEVEL_WARN,
    LOG_LEVEL_ERROR,
}

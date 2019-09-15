/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/15/19 3:27 PM
 */
package com.drake.engine.utils

import android.text.TextUtils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * @author Town
 * @created at 2018/7/5 16:30
 * @Last Modified by: Town
 * @Last Email: android@townwang.com
 * @Last Modified time: 2018/7/5 16:30
 * @Remarks Log日志封装类
 */
class LogCat private constructor() {

    companion object {

        private var TAG = "日志"

        private var enabled = true

        private val subLength = 4

        private val END = "}\r\n"

        private var parseResponse: JSONObject? = null//最终被解析的JSON对象

        private var isReduce: Boolean = false // 是否删除多余的JSONObject(默认的JSONArray会被嵌套了一个JSONObject)
        private val jsonBuilder = StringBuilder() // 最终输出的数据容器

        /**
         * 设置日志配置
         *
         * @param TAG     过滤TAG
         * @param isDebug 是否打印
         */
        fun setConfig(TAG: String, isDebug: Boolean) {
            enabled = isDebug

            Companion.TAG = TAG
        }

        // <editor-fold desc="普通日志">

        fun i(msg: String) {
            if (enabled)
                android.util.Log.i(TAG, msg)
        }

        fun d(msg: String) {
            if (enabled)
                android.util.Log.d(TAG, msg)
        }

        fun e(msg: String) {
            if (enabled)
                android.util.Log.e(TAG, msg)
        }

        fun v(msg: String) {
            if (enabled)
                android.util.Log.v(TAG, msg)
        }

        fun i(tag: String, msg: String) {
            if (enabled)
                android.util.Log.i(tag, msg)
        }

        fun d(tag: String, msg: String) {
            if (enabled)
                android.util.Log.i(tag, msg)
        }

        fun e(tag: String, msg: String) {
            if (enabled)
                android.util.Log.i(tag, msg)
        }

        fun v(tag: String, msg: String) {
            if (enabled)
                android.util.Log.i(tag, msg)
        }

        // </editor-fold>


        /**
         * Json格式输出Log
         *
         * @param tag     标签
         * @param url     请求Url
         * @param message Json字符串
         */
        fun json(tag: String = TAG, url: String? = null, message: String) {

            var jsonObject: JSONObject? = null

            try {
                jsonObject = JSONObject(message)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            if (enabled) {
                synchronized(LogCat::class.java) {
                    try {
                        if (jsonObject is JSONArray) {
                            isReduce = true
                            val tempJson = JSONObject()
                            parseResponse = tempJson.putOpt("", jsonObject)
                        } else {
                            isReduce = false
                            parseResponse = jsonObject
                        }
                        synchronized(LogCat::class.java) {
                            formatJson(tag, url, parseResponse, isReduce)

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        /***
         * 功能：
         */
        private fun formatJson(
            TagName: String,
            url: String?,
            response: JSONObject?,
            isReduce: Boolean
        ) {
            if (checkParams(TagName, response)) {
                return
            }
            jsonBuilder.setLength(0)

//            START
            appendSb("$TagName", false)

            if (url != null) {
                appendSb(
                    "----------------------------------------------------------------------------------------",
                    false
                )
                appendSb("url = [$url]", false)
                appendSb(
                    "----------------------------------------------------------------------------------------",
                    false
                )
            }

            appendSb("{", false)
            printFormatJson(TagName, response!!)
            appendSb("}", false)
//            END

            if (isReduce) {
                jsonBuilder.delete(jsonBuilder.indexOf("{"), jsonBuilder.indexOf("["))
                jsonBuilder.delete(
                    jsonBuilder.lastIndexOf(END),
                    jsonBuilder.lastIndexOf(END) + subLength
                )
            }

            logOut(
                TagName,
                jsonBuilder.toString()
            )
        }

        /**
         * 功能：检查参数是否异常：
         *
         * @param TagName  必填
         * @param response 必填
         */
        private fun checkParams(TagName: String, response: JSONObject?): Boolean {
            return null == response || TextUtils.isEmpty(response.toString()) || TextUtils.isEmpty(
                TagName
            )
        }

        /**
         * 功能：遍历所有子json对象,并对孩子进行递归操作
         * 对JSONObject,JSONArray,Object。进行区分判断。
         */
        private fun printFormatJson(TagName: String, response: JSONObject) {
            try {
                val jsonObject = response.keys()
                while (jsonObject.hasNext()) {
                    val key = jsonObject.next()
                    when {
                        response.get(key) is JSONObject -> {
                            appendSb(
                                "\"$key\":{",
                                false
                            )
                            printFormatJson(
                                TagName,
                                response.get(key) as JSONObject
                            )
                            val isEndValue = jsonObject.hasNext()//判断是否还有下一个元素
                            appendSb("  }", isEndValue)

                        }

                        response.get(key) is JSONArray -> {
                            appendSb(
                                "\"$key\":[",
                                false
                            )
                            iteratorArray(
                                TagName,
                                response.get(key) as JSONArray
                            )
                            val isEndValue = jsonObject.hasNext()//判断是否还有下一个元素
                            appendSb(
                                " " + " ]",
                                isEndValue
                            )

                        }

                        response.get(key) != null -> {
                            val isEndValue = jsonObject.hasNext()//判断是否还有下一个元素
                            getTypeData(
                                response,
                                key,
                                !isEndValue
                            )
                        }
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        /**
         * 功能：对基本类型进行转换（String,其他的类型其实不用处理）
         * 说明：对null,进行非空处理，对字符串进行添加 "" 操作
         */
        private fun getTypeData(response: JSONObject, key: String, isEndValue: Boolean) {
            try {
                if (response.get(key) is Int) {
                    val value = response.get(key) as Int
                    appendSb(
                        "\t\"$key\":$value",
                        !isEndValue
                    )

                } else if (response.get(key) is String || null == response.get(key) || TextUtils.equals(
                        "null",
                        response.get(key).toString()
                    )
                ) {
                    if (response.get(key) is String) {
                        val value = response.get(key) as String
                        if (null == value) {
                            appendSb(
                                "\t\"$key\":null",
                                !isEndValue
                            )
                        } else {
                            appendSb(
                                "\t\"$key\":\"$value\"",
                                !isEndValue
                            )
                        }
                    } else if (TextUtils.equals("null", response.get(key).toString())) {
                        appendSb(
                            "\t\"$key\":null",
                            !isEndValue
                        )

                    } else {
                        val value = response.get(key) as String
                        if (null == value) {
                            appendSb(
                                "\t\"$key\":null",
                                !isEndValue
                            )

                        } else {
                            appendSb(
                                "\t\"$key\":\"$value\"",
                                !isEndValue
                            )
                        }
                    }
                } else if (response.get(key) is Float) {
                    val value = response.get(key) as Float
                    appendSb(
                        "\t\"$key\":\"$value\"",
                        !isEndValue
                    )

                } else if (response.get(key) is Double) {
                    val value = response.get(key) as Double
                    appendSb(
                        "\t\"$key\":\"$value\"",
                        !isEndValue
                    )

                } else if (response.get(key) is Boolean) {
                    val value = response.get(key) as Boolean
                    appendSb(
                        "\t\"$key\":\"$value\"",
                        !isEndValue
                    )

                } else if (response.get(key) is Char) {
                    val value = response.get(key) as Char
                    appendSb(
                        "\t\"$key\":\"$value\"",
                        !isEndValue
                    )

                } else if (response.get(key) is Long) {
                    val value = response.get(key) as Long
                    appendSb(
                        "\t\"$key\":\"$value\"",
                        !isEndValue
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (es: Exception) {
                es.printStackTrace()
            }

        }

        /**
         * 功能：对JSONArray进行遍历
         *
         * @param TagName
         * @param response;
         */
        private fun iteratorArray(TagName: String, response: JSONArray) {
            try {
                for (i in 0 until response.length()) {
                    if (response.get(i) is JSONObject) {
                        appendSb("{", false)
                        printFormatJson(
                            TagName,
                            response.get(i) as JSONObject
                        )
                        appendSb(
                            "  }",
                            response.length() > i + 1
                        )

                    } else if (response.get(i) is JSONArray) {
                        iteratorArray(
                            TagName,
                            response.get(i) as JSONArray
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        /**
         * 功能：添加数据，以及逗号，换行。
         *
         * @param addComma 逗号。
         */
        private fun appendSb(append: String, addComma: Boolean) {
            jsonBuilder.append(append)
            if (addComma) {
                jsonBuilder.append(",")
            }
            jsonBuilder.append("\r\n")
        }

        /**
         * 功能： LOG输出长度有限制。（需分层输出）
         * max:通过测试不建议修改数据值，修改成4000,会丢失数据。
         *
         * @param content
         */
        private fun logOut(tagName: String, content: String) {
            var temp = content
            val max = 3900
            val length = temp.length.toLong()

            if (length < max || length == max.toLong()) {
                i(tagName, temp)
            } else {
                while (temp.length > max) {
                    val logContent = temp.substring(0, max)
                    temp = temp.replace(logContent, "")
                    i(tagName, logContent)
                }
                i(tagName, temp)
            }
        }
    }
}
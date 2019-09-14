/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.net.params

import org.json.JSONArray

/**
 * 请求参数的接口
 *
 * @param <R> 请求成功的Model
</R> */
interface JsonParams<R> {

    val isPrintString: String

    fun put(name: String, value: Boolean): R

    fun put(name: String, value: Int): R

    fun put(name: String, value: Float): R

    fun put(name: String, value: Double): R

    fun put(name: String, value: String): R

    fun put(name: String, value: JSONArray): R

    fun printString(): R
}

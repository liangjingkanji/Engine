/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.net.exception

/**
 *  对应网络请求后台定义的错误信息
 * @property errorMessage String 网络请求错误信息
 * @property errorCode Int 网络请求错误码
 * @constructor
 */
class ResponseException(val errorMessage: String, val errorCode: Int) : Throwable()

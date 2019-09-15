/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.net.listener

import com.drake.engine.component.net.NetConfig
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.connect.Interceptor
import com.yanzhenjie.kalle.connect.http.Chain
import java.io.IOException

/**
 * 响应体的日志打印器
 */
class DefaultInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        NetConfig.listener?.net(request, response)
        return response
    }
}


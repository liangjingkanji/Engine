/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/15/19 6:24 PM
 */

package com.drake.engine.component.net

import com.drake.engine.component.net.convert.DefaultConverter
import com.drake.engine.component.net.listener.DefaultInterceptor
import com.drake.engine.component.net.listener.NetListener
import com.yanzhenjie.kalle.Kalle
import com.yanzhenjie.kalle.KalleConfig

object NetConfig {

    var HOST: String = ""

    var listener: NetListener? = null

    fun set(block: KalleConfig.Builder.() -> Unit) {
        val builder = KalleConfig.newBuilder()
        builder.block()
        Kalle.setConfig(builder.build())
    }

    fun setDefault(successCode: Int = 1) {
        set {
            addInterceptor(DefaultInterceptor())
            converter(DefaultConverter(successCode))
        }
    }
}
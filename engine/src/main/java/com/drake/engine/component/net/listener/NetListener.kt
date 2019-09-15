/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/15/19 6:23 PM
 */

package com.drake.engine.component.net.listener

import com.yanzhenjie.kalle.Request
import com.yanzhenjie.kalle.Response

interface NetListener {

    fun net(request: Request, response: Response)
    fun parse(body: String): String
}
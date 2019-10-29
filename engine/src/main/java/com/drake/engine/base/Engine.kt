/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.base

import android.app.Application


private lateinit var application: Application

fun Application.initEngine() {
    application = this
}

fun getApp(): Application {
    return application
}


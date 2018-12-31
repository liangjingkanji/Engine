/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.base

import android.app.Application
import com.designer.library.base.Library.app

object Library {
    var app: Application? = null
}


fun getApp(): Application {
    return app!!
}

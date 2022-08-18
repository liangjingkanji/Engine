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

package com.drake.engine.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("StaticFieldLeak")
lateinit var app: Context

object Engine {

    /** 初始化本框架 */
    fun initialize(app: Application) {
        com.drake.engine.base.app = app
        app.registerActivityLifecycleCallbacks(activityCallbacks)
    }

    /** 当前activity */
    @JvmStatic
    val currentActivity: AppCompatActivity?
        get() = activityCallbacks.currentActivityWeak?.get()

    /** 默认的生命周期回调 */
    @JvmStatic
    var activityCallbacks: EngineActivityCallbacks = EngineActivityCallbacks()
}



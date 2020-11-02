/*
 * Copyright (C) 2018 Drake, Inc.
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

package com.drake.engine.sample

import android.util.Log
import com.drake.engine.base.EngineActivity
import com.drake.engine.sample.databinding.ActivityMainBinding
import com.drake.engine.utils.preference
import com.drake.engine.utils.serializable
import com.hulab.debugkit.dev


class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var model: Model? by serializable("model")
    private var name: String? by preference("name")

    override fun initView() {
        binding.v = this
        binding.m = model
    }

    override fun initData() {
        dev {
            function {
                Log.d("日志", "(MainActivity.kt:42)    name = ${name}")
                Log.d("日志", "(MainActivity.kt:41)    model = ${model}")
            }

            function {
                name = null
                model = Model(23)
            }
        }
    }
}






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
import androidx.datastore.preferences.createDataStore
import com.drake.engine.base.EngineActivity
import com.drake.engine.sample.databinding.ActivityMainBinding
import com.drake.engine.utils.Preference
import com.drake.engine.utils.preference
import com.hulab.debugkit.dev
import com.tencent.mmkv.MMKV


class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var model: Model? by preference()
    private var name: String? by preference()

    override fun initView() {
        binding.v = this
        // binding.m = model
    }

    override fun initData() {
        MMKV.initialize(this)
        val kv = MMKV.defaultMMKV()
        dev {
            function {
                kv.ashmemFD()
                // Plist.writeSet("set", setOf("吴彦祖", "金城武"))
                // Preference.write("set" to mutableSetOf(1, 2))
                Preference.write("set" to Model(23))
                // Log.d("日志", "(MainActivity.kt:42)    name = ${name}")
                // Log.d("日志", "(MainActivity.kt:41)    model = ${model}")
            }

            function {
                val name = Preference.read<Set<String>>("set")
                Log.d("日志", "(MainActivity.kt:48)    name = ${name}")
                // name = null
                // model = Model(23)
            }

            function {
                // val name = Preference.read("aaa", mutableSetOf("aaa", "bbb"))
                val name: Model? = Preference.read("aaa")
                Log.d("日志", "(MainActivity.kt:57)    name = ${name}")
            }

            function {
                this@MainActivity.createDataStore(
                    name = "settings"
                )
            }
        }
    }
}






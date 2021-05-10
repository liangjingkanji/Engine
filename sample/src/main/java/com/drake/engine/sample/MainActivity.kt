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

import com.drake.brv.utils.BRV
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineActivity
import com.drake.engine.sample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : EngineActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun initView() {
        BRV.modelId = BR.m
        binding.v = this
        // binding.m = model
    }

    override fun initData() {
        rv.linear().setup {
            addType<Model>(R.layout.item_image)
        }.models = getData()
    }

    private fun getData(): MutableList<Model> {
        val list = mutableListOf<Model>()
        for (i in 0..40) {
            if (i == 10) {
                list.add(Model())
            } else list.add(Model("image"))
        }
        return list
    }
}






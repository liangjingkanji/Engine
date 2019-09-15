/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.engine.base.Engine
import com.hulab.debugkit.dev

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Engine.app = this.application

        dev {
            val json = getString(R.string.json)
            function {


            }

            function {
                LogCat.json("日志", "地址", json)

            }
        }

    }
}

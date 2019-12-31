/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.os.Bundle
import android.util.Log
import com.drake.engine.base.EngineActivity
import com.drake.engine.sample.databinding.ActivityMainBinding
import com.example.lib.getUserName


class MainActivity : EngineActivity<ActivityMainBinding>() {


    var model = Model(26)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.v = this
        binding.m = model

        Log.d("日志", "(MainActivity.kt:27)    result = ${getUserName()}")
    }

    override fun initView() {

    }

    override fun initData() {

    }
}






/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.engine.component.databinding.setContent
import com.drake.engine.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    var model = Model()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = setContent<ActivityMainBinding>(R.layout.activity_main)

        binding.v = this
        binding.m = model


//        tv.text = SpanUtils().append("吴彦祖").appendImage(R.drawable.ic_success).create()
    }


}






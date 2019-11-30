/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.drake.engine.databinding.setContent
import com.drake.engine.sample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    var model = Model(26)

    lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = setContent<ActivityMainBinding>(R.layout.activity_main)

        binding.v = this
        binding.m = model
    }
}






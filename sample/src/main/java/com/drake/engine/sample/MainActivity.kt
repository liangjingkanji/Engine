/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drake.engine.component.databinding.setContent
import com.drake.engine.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var img: Int = 0
    lateinit var imgDrawable: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = setContent<ActivityMainBinding>(R.layout.activity_main)

    }
}

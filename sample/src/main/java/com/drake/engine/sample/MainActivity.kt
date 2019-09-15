/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/13/19 4:09 PM
 */

package com.drake.engine.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.drake.engine.base.Engine
import com.drake.engine.component.databinding.inflate
import com.drake.engine.component.databinding.setContent
import com.drake.engine.component.net.post
import com.drake.engine.sample.databinding.ActivityMainBinding
import com.hulab.debugkit.dev
import org.jetbrains.anko.contentView

class MainActivity : AppCompatActivity() {

    var img: Int = 0
    lateinit var imgDrawable: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = setContent<ActivityMainBinding>(R.layout.activity_main)
        img = R.drawable.ic_success
        imgDrawable = resources.getDrawable(R.drawable.ic_success)

        binding.v = this

        post<String>("") {

        }

    }
}

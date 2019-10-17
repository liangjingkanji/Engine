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
import com.drake.engine.utils.SpanUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var model = Model()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = setContent<ActivityMainBinding>(R.layout.activity_main)

        binding.v = this
        binding.m = model


        tv.text = SpanUtils(tv).append("吴彦祖").appendImage(R.drawable.ic_success).create()

/*
        object : ReplacementSpan() {

            override fun getSize(
                p0: Paint,
                p1: CharSequence?,
                p2: Int,
                p3: Int,
                p4: Paint.FontMetricsInt?
            ): Int {
            }

            override fun draw(
                p0: Canvas,
                p1: CharSequence?,
                p2: Int,
                p3: Int,
                p4: Float,
                p5: Int,
                p6: Int,
                p7: Int,
                p8: Paint
            ) {
            }

        }*/
    }
}






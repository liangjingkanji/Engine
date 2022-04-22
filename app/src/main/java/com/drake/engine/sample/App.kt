package com.drake.engine.sample

import android.app.Application
import com.drake.brv.utils.BRV
import com.drake.engine.base.Engine

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Engine.initialize(this)
        BRV.modelId = BR.m
    }
}
package com.drake.engine.sample

import android.app.Application
import com.drake.brv.utils.BRV

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        BRV.modelId = BR.m
    }
}
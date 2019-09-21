package com.drake.engine.sample

import android.app.Application
import com.drake.engine.base.initEngine

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initEngine()
    }
}
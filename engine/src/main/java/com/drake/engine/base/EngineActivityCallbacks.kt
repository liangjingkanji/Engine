package com.drake.engine.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

class EngineActivityCallbacks : Application.ActivityLifecycleCallbacks {

    var currentActivityWeak: WeakReference<AppCompatActivity>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val currentActivity = activity as? AppCompatActivity ?: return
        currentActivityWeak = WeakReference(currentActivity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
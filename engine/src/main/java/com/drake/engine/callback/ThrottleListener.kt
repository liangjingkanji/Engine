package com.drake.engine.callback

import android.view.View

fun View.throttleClick(interval: Int = 500, block: View.() -> Unit) {
    setOnClickListener(ThrottleClickListener(interval, block))
}

class ThrottleClickListener(val interval: Int = 500, var block: View.() -> Unit) :
    View.OnClickListener {


    var lastTime: Long = 0

    override fun onClick(v: View) {

        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > interval) {
            lastTime = currentTime
            block(v)
        }
    }
}
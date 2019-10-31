package com.drake.engine.dialog

import android.widget.Toast
import com.drake.engine.base.getApp

object Toasts {
    val toast = Toast(getApp())
}

fun toast(message: Int): Toast = Toast
    .makeText(getApp(), message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

fun toast(message: CharSequence): Toast = Toast
    .makeText(getApp(), message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }


fun longToast(message: Int): Toast = Toast
    .makeText(getApp(), message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

fun longToast(message: CharSequence): Toast = Toast
    .makeText(getApp(), message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

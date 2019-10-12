package com.drake.engine.component.dialogs

import android.widget.Toast
import com.drake.engine.base.App


fun toast(message: Int): Toast = Toast
    .makeText(App, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

fun toast(message: CharSequence): Toast = Toast
    .makeText(App, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }


fun longToast(message: Int): Toast = Toast
    .makeText(App, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }


fun longToast(message: CharSequence): Toast = Toast
    .makeText(App, message, Toast.LENGTH_LONG)
    .apply {
        show()
    }

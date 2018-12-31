/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import com.designer.library.base.Library
import java.util.regex.Pattern

/**
 * 状态栏透明
 */

private const val MIN_API = 19
private const val DEFAULT_COLOR = 0
private const val DEFAULT_ALPHA = 0f

// </editor-fold>

// <editor-fold desc="屏幕尺寸">

/**
 * 导航栏高度
 *
 * @return 如果当前设备存在导航栏返回导航栏高度, 否则返回0
 */
val navigationBarHeight: Int
    get() {
        val resources = Library.app!!.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)

        return if (hasNavigationBar()) {
            height
        } else {
            0
        }
    }

/**
 * 状态栏高度
 */
val statusBarHeight: Int
    get() {
        val context = Library.app
        var result = 24
        val resId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

/**
 * 是否是Flyme4以上
 */
val isFlyme4: Boolean
    get() = (Build.FINGERPRINT.contains("Flyme_OS_4")
            || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
            || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE)
        .matcher(Build.DISPLAY)
        .find())

/**
 * 是否是MIUI6以上
 */
val isMIUI6: Boolean
    get() {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val mtd = clz.getMethod("get", String::class.java)
            var `val` = mtd.invoke(null, "ro.miui.ui.version.name") as String
            `val` = `val`.replace("[vV]".toRegex(), "")
            val version = Integer.parseInt(`val`)
            return version >= 6
        } catch (e: Exception) {
            return false
        }

    }

/**
 * 设置状态栏透明
 * @receiver Activity
 * @param color Int 状态栏颜色
 * @param alpha Float 状态栏透明度
 */
@JvmOverloads
fun Activity.immersive(
    color: Int = DEFAULT_COLOR, @FloatRange(
        from = 0.0,
        to = 1.0
    ) alpha: Float = DEFAULT_ALPHA
) {

    when {
        Build.VERSION.SDK_INT >= 21 -> {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = mixtureColor(color, alpha)
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        }
        Build.VERSION.SDK_INT >= MIN_API -> {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            setTranslucentView(window.decorView as ViewGroup, color, alpha)
        }
        Build.VERSION.SDK_INT > 16 -> {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }
}
//</editor-fold>

//<editor-fold desc="暗色模式">


/**
 * 设置状态栏暗色模式 (状态栏白色颜色字体)
 * @receiver Activity
 * @param darkMode Boolean
 * @param color Int
 * @param alpha Int
 */
@JvmOverloads
fun Activity.darkMode(
    darkMode: Boolean = true,
    color: Int = DEFAULT_COLOR,
    alpha: Float = DEFAULT_ALPHA
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        darkModeForM(window, darkMode)
        immersive(color, alpha)
    } else if (isFlyme4) {
        darkModeForFlyme4(window, darkMode)
        immersive(color, alpha)
    } else if (isMIUI6) {
        darkModeForMIUI6(window, darkMode)
        immersive(color, alpha)
    } else {
        if (darkMode) {
            immersive(Color.LTGRAY, 1f)
        } else {
            immersive(color, alpha)
        }
    }
}

/**
 * android 6.0设置字体颜色
 */
@RequiresApi(Build.VERSION_CODES.M)
private fun darkModeForM(window: Window, darkMode: Boolean) {
    var systemUiVisibility = window.decorView.systemUiVisibility
    systemUiVisibility = if (darkMode) {
        systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    window.decorView.systemUiVisibility = systemUiVisibility
}

/**
 * 设置Flyme4+的darkMode,darkMode时候字体颜色及icon变黑
 * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
 */
private fun darkModeForFlyme4(window: Window?, dark: Boolean): Boolean {
    var result = false
    if (window != null) {
        try {
            val e = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField(
                "MEIZU_FLAG_DARK_STATUS_BAR_ICON"
            )
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(e)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(e, value)
            window.attributes = e
            result = true
        } catch (var8: Exception) {
            Log.e("BarUtils", "darkIcon: failed")
        }

    }

    return result
}

/**
 * 设置MIUI6+的状态栏是否为darkMode,darkMode时候字体颜色及icon变黑
 * http://dev.xiaomi.com/doc/p=4769/
 */
private fun darkModeForMIUI6(window: Window, darkMode: Boolean): Boolean {
    val clazz = window.javaClass
    return try {
        var darkModeFlag = 0
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField =
            clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
        extraFlagField.invoke(window, if (darkMode) darkModeFlag else 0, darkModeFlag)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

}

//</editor-fold>

// <editor-fold desc="间距">

/**
 * 增加View的上内边距, 增加高度为状态栏高度, 防止视图和状态栏重叠
 * 如果是RelativeLayout种设置padding值会导致centerInParent等属性无法正常显示
 */
fun View.setStatusBarPadding() {
    if (Build.VERSION.SDK_INT >= MIN_API) {
        val lp = layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += statusBarHeight//增高
        }
        setPadding(
            paddingLeft, paddingTop + statusBarHeight,
            paddingRight, paddingBottom
        )
    }
}

/**
 * 清除View的上内间距
 */
fun View.clearPadding() {
    if (Build.VERSION.SDK_INT >= MIN_API) {
        setPadding(
            paddingLeft, 0,
            paddingRight, paddingBottom
        )
    }
}

/**
 * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
 */
fun View.setStatusBarMargin() {
    if (Build.VERSION.SDK_INT >= MIN_API) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin += statusBarHeight//增高
        }
        layoutParams = lp
    }
}

/**
 * 创建假的透明栏
 */
fun Activity.setTranslucentView(
    container: ViewGroup, color: Int,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float
) {
    if (Build.VERSION.SDK_INT >= MIN_API) {
        val mixtureColor = mixtureColor(color, alpha)
        var translucentView: View? = container.findViewById(android.R.id.custom)
        if (translucentView == null && mixtureColor != 0) {
            translucentView = View(container.context)
            translucentView.id = android.R.id.custom
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight
            )
            container.addView(translucentView, lp)
        }
        translucentView?.setBackgroundColor(mixtureColor)
    }
}
// </editor-fold>

// <editor-fold desc="辅助">

/**
 * 计算颜色和透明度产生新的色值
 */
fun mixtureColor(color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
    val a = if (color and -0x1000000 == 0) 0xff else color.ushr(24)
    return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
}

/**
 * 是否有导航栏
 */
fun hasNavigationBar(): Boolean {
    var hasNavigationBar = false
    val rs = Library.app!!.resources
    val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
    if (id > 0) {
        hasNavigationBar = rs.getBoolean(id)
    }
    try {
        val systemPropertiesClass = Class.forName("android.os.SystemProperties")
        val m = systemPropertiesClass.getMethod("get", String::class.java)
        val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
        if ("1" == navBarOverride) {
            hasNavigationBar = false
        } else if ("0" == navBarOverride) {
            hasNavigationBar = true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return hasNavigationBar
}

fun Activity.setNavigationBarHidden() {

    if (Build.VERSION.SDK_INT in 12..18) {
        val v = window.decorView
        v.systemUiVisibility = View.GONE

    } else if (Build.VERSION.SDK_INT >= 19) {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView.systemUiVisibility = uiOptions
    }
}

// </editor-fold>
// <editor-fold desc="沉浸">

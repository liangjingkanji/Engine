package com.drake.engine.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.drake.engine.base.getApp


fun getPreferences(name: String = getApp().packageName): Preferences {
    return Preferences(name)
}

fun Activity.getCurrentPreferences(): Preferences {
    return getPreferences(localClassName)
}


fun Fragment.getCurrentPreferences(): Preferences {
    return getPreferences(activity!!.localClassName)
}

inline fun getEdit(
    name: String = getApp().packageName,
    commit: Boolean = false,
    block: SharedPreferences.Editor.() -> Unit
) {
    val editor = Preferences(name).edit()
    block(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}


class Preferences(name: String = getApp().packageName) {

    // <editor-fold desc="设置值">

    fun clear() {
        edit().clear().commit()
    }

    fun putLong(key: String?, value: Long = 0) {
        edit().putLong(key, value).commit()
    }

    fun putInt(key: String?, value: Int = 0) {
        edit().putInt(key, value).commit()
    }

    fun remove(key: String?) {
        edit().remove(key).commit()
    }

    fun putBoolean(key: String?, value: Boolean = false) {
        edit().putBoolean(key, value).commit()
    }

    fun putStringSet(key: String?, values: Set<String>? = setOf()) {
        edit().putStringSet(key, values).commit()
    }

    fun putFloat(key: String?, value: Float = 0F) {
        edit().putFloat(key, value).commit()
    }

    fun putString(key: String?, value: String? = null) {
        edit().putString(key, value).commit()
    }

    // </editor-fold>

    private var sp = getApp().getSharedPreferences(name, Context.MODE_PRIVATE)


    // <editor-fold desc="访问值">

    fun getBoolean(key: String?, defValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun getInt(key: String?, defValue: Int = 0): Int {
        return sp.getInt(key, defValue)
    }

    fun getAll(): MutableMap<String, *> {
        return sp.all
    }

    fun getLong(key: String?, defValue: Long = 0): Long {
        return sp.getLong(key, defValue)
    }

    fun getFloat(key: String?, defValue: Float = 0F): Float {
        return sp.getFloat(key, defValue)
    }

    fun getStringSet(key: String?, defValues: Set<String>? = setOf()): MutableSet<String>? {
        return sp.getStringSet(key, defValues)
    }


    fun getString(key: String?, defValue: String? = ""): String? {
        return sp.getString(key, defValue)
    }

    // </editor-fold>


    fun contains(key: String?): Boolean {
        return sp.contains(key)
    }

    fun edit(): SharedPreferences.Editor {
        return sp.edit()
    }

    // <editor-fold desc="监听器">

    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sp.registerOnSharedPreferenceChangeListener(listener)
    }


    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        sp.unregisterOnSharedPreferenceChangeListener(listener)
    }

    // </editor-fold>

}

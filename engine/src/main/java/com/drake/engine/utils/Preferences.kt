package com.drake.engine.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.drake.engine.base.getApp
import java.io.*


fun getPreferences(name: String = getApp().packageName): Preferences {
    return Preferences(name)
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

fun SharedPreferences.Editor.putAny(key: String, any: Serializable) {
    try {
        val byteOutput = ByteArrayOutputStream()
        val objOutput = ObjectOutputStream(byteOutput)
        objOutput.writeObject(any)

        val str = Base64.encodeToString(byteOutput.toByteArray(), Base64.DEFAULT)
        putString(key, str)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}


class Preferences(name: String = getApp().packageName) {

    // <editor-fold desc="设置值">

    fun clear() {
        edit().clear().apply()
    }

    fun putLong(key: String?, value: Long = 0) {
        edit().putLong(key, value).apply()
    }

    fun putInt(key: String?, value: Int = 0) {
        edit().putInt(key, value).apply()
    }

    fun remove(key: String?) {
        edit().remove(key).apply()
    }

    fun putBoolean(key: String?, value: Boolean = false) {
        edit().putBoolean(key, value).apply()
    }

    fun putStringSet(key: String?, values: MutableSet<String>? = mutableSetOf()) {
        edit().putStringSet(key, values).apply()
    }

    fun putFloat(key: String?, value: Float = 0F) {
        edit().putFloat(key, value).apply()
    }

    fun putString(key: String?, value: String? = null) {
        edit().putString(key, value).apply()
    }


    fun putAny(key: String, any: Serializable) {
        try {
            val byteOutput = ByteArrayOutputStream()
            val objOutput = ObjectOutputStream(byteOutput)
            objOutput.writeObject(any)

            val str = Base64.encodeToString(byteOutput.toByteArray(), Base64.DEFAULT)
            putString(key, str)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // </editor-fold>

    var sp = getApp().getSharedPreferences(name, Context.MODE_PRIVATE)


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

    fun getStringSet(
        key: String?,
        defValues: MutableSet<String>? = mutableSetOf()
    ): MutableSet<String>? {
        return sp.getStringSet(key, defValues)
    }


    fun getString(key: String?, defValue: String? = ""): String? {
        return sp.getString(key, defValue)
    }

    /**
     * 读取本地已存储对象
     *
     * @param key String
     * @return T?
     */
    inline fun <reified T : Serializable> getAny(key: String): T? {
        return try {
            val any = sp.getString(key, null) ?: return null

            val base64 = Base64.decode(any, Base64.DEFAULT)
            val byteInput = ByteArrayInputStream(base64)
            val objInput = ObjectInputStream(byteInput)

            val readObject = objInput.readObject()

            if (readObject is T) {
                readObject
            } else null

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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

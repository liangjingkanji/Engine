/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.engine.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.drake.engine.base.app
import java.io.*


object Preference {

    var context = app
    var name: String = context.packageName

    //<editor-fold desc="写入">
    fun write(vararg params: Pair<String, Any?>) = write(name, false, * params)

    fun write(name: String, vararg params: Pair<String, Any?>) = write(name, false, *params)

    fun write(commit: Boolean, vararg params: Pair<String, Any?>) = write(name, false, *params)

    @SuppressLint("ApplySharedPref")
    fun write(name: String, commit: Boolean, vararg params: Pair<String, Any?>) {
        val block: SharedPreferences.Editor.() -> Unit = {
            params.forEach {
                when (val value = it.second) {
                    null -> {
                    }
                    is Int -> putInt(it.first, value)
                    is Long -> putLong(it.first, value)
                    is String -> putString(it.first, value)
                    is Float -> putFloat(it.first, value)
                    is Boolean -> putBoolean(it.first, value)
                    is Short -> putInt(it.first, value.toInt())
                    is Double -> putString(it.first, value.toString())
                    is Serializable -> putAny(it.first, value)
                    else -> throw IllegalArgumentException("SharedPreferences save ${it.first} has wrong type ${value.javaClass.name}")
                }
                return@forEach
            }
        }
        val editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().apply(block)
        if (commit) editor.commit() else editor.apply()
    }
    //</editor-fold>

    //<editor-fold desc="读取">
    inline fun <reified T> read(key: String): T {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return when (T::class.java) {
            Int::class.java -> preference.getInt(key, 0) as T
            Long::class.java -> preference.getLong(key, 0) as T
            String::class.java -> preference.getString(key, "") as T
            Float::class.java -> preference.getFloat(key, 0F) as T
            Boolean::class.java -> preference.getBoolean(key, false) as T
            else -> throw IllegalArgumentException("SharedPreferences save [${key}] has wrong type ${T::class.java.name}")
        }
    }

    inline fun <reified T> read(key: String, defValue: T): T {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return when (T::class.java) {
            Int::class.java -> preference.getInt(key, defValue as Int) as T
            Long::class.java -> preference.getLong(key, defValue as Long) as T
            String::class.java -> preference.getString(key, defValue as String) as T
            Float::class.java -> preference.getFloat(key, defValue as Float) as T
            Boolean::class.java -> preference.getBoolean(key, defValue as Boolean) as T
            else -> throw IllegalArgumentException("SharedPreferences save ${key} has wrong type ${T::class.java.name}")
        }
    }

    inline fun <reified T : Serializable> readSerializable(key: String): T? {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return preference.getAny(key) as? T
    }

    inline fun <reified T : Serializable> readSerializable(key: String, defValue: T): T {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return preference.getAny(key) as? T ?: defValue
    }
    //</editor-fold>


    fun remove(key: String) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply()
    }

    fun remove(name: String? = null, key: String) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply()
    }

    fun clear(name: String? = null) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply()
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

    fun SharedPreferences.getAny(key: String): Any? {
        return try {
            val any = this.getString(key, null) ?: return null
            val base64 = Base64.decode(any, Base64.DEFAULT)
            val byteInput = ByteArrayInputStream(base64)
            val objInput = ObjectInputStream(byteInput)
            val readAny = objInput.readObject()
            readAny
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


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

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.drake.engine.base.app
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


object Preference {

    var context = app
    var name: String = context.packageName

    //<editor-fold desc="写入">
    fun write(vararg params: Pair<String, Any?>) = write(name, * params)

    fun write(name: String?, vararg params: Pair<String, Any?>) {
        val block: SharedPreferences.Editor.() -> Unit = {
            params.forEach {
                when (val value = it.second) {
                    null -> remove(it.first)
                    is Int -> putInt(it.first, value)
                    is Long -> putLong(it.first, value)
                    is String -> putString(it.first, value)
                    is Float -> putFloat(it.first, value)
                    is Boolean -> putBoolean(it.first, value)
                    else -> throw IllegalArgumentException("SharedPreferences save ${it.first} has wrong type ${value.javaClass.name}")
                }
                return@forEach
            }
        }
        val editor =
            context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().apply(block)
        editor.apply()
    }
    //</editor-fold>

    //<editor-fold desc="读取">
    inline fun <reified T> read(key: String, name: String = this.name): T {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return when (T::class.java) {
            Int::class.java -> preference.getInt(key, 0) as T
            Long::class.java -> preference.getLong(key, 0L) as T
            String::class.java -> preference.getString(key, "") as T
            Float::class.java -> preference.getFloat(key, 0F) as T
            Boolean::class.java -> preference.getBoolean(key, false) as T
            else -> throw IllegalArgumentException("SharedPreferences save [${key}] has wrong type ${T::class.java.name}")
        }
    }


    inline fun <reified T> read(key: String, defValue: T, name: String = this.name): T {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)

        return when (T::class.java) {
            Int::class.java -> preference.getInt(key, defValue as Int) as T
            Long::class.java -> preference.getLong(key, defValue as Long) as T
            String::class.java -> preference.getString(key, defValue as String) as T
            Float::class.java -> preference.getFloat(key, defValue as Float) as T
            Boolean::class.java -> preference.getBoolean(key, defValue as Boolean) as T
            else -> throw IllegalArgumentException("SharedPreferences save [${key}] has wrong type ${T::class.java.name}")
        }
    }


    //</editor-fold>

    //<editor-fold desc="对象">

    inline fun <reified T> readSerializable(key: String, name: String = this.name): T? {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return preference.readSerializable<T>(key)
    }

    fun writeSerializable(key: String, obj: Serializable?, name: String = this.name) {
        val preference = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        preference.edit().apply {
            this.writeSerializable(key, obj)
            apply()
        }
    }

    fun SharedPreferences.Editor.writeSerializable(key: String, obj: Serializable?) {
        if (obj == null) {
            remove(key)
        } else {
            try {
                val byteOutput = ByteArrayOutputStream()
                val objOutput = ObjectOutputStream(byteOutput)
                objOutput.writeObject(obj)
                val str = Base64.encodeToString(byteOutput.toByteArray(), Base64.DEFAULT)
                putString(key, str)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inline fun <reified T> SharedPreferences.readSerializable(key: String): T? {
        return try {
            val any = this.getString(key, null) ?: return null
            val base64 = Base64.decode(any, Base64.DEFAULT)
            val byteInput = ByteArrayInputStream(base64)
            val objInput = ObjectInputStream(byteInput)
            val obj = objInput.readObject()
            obj as? T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    //</editor-fold>

    //<editor-fold desc="删除">
    fun remove(key: String, name: String = this.name) {
        val adjustName = if (name.isNullOrBlank()) this.name else name
        context.getSharedPreferences(adjustName, Context.MODE_PRIVATE).edit().remove(key).apply()
    }

    fun clear(name: String = this.name) {
        val adjustName = if (name.isNullOrBlank()) this.name else name
        context.getSharedPreferences(adjustName, Context.MODE_PRIVATE).edit().clear().apply()
    }
    //</editor-fold>
}

/**
 * 序列化委托代理属性
 */
inline fun <reified T> preference(
    key: String,
    default: T? = null,
    name: String = Preference.name
): ReadWriteProperty<Any?, T> {

    val isInit = AtomicBoolean(true)

    return object : ReadWriteProperty<Any?, T> {

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            Preference.write(name, key to value)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            if (isInit.get()) {
                isInit.set(false)
                return when (T::class.java) {
                    Int::class.java -> 0 as T
                    Long::class.java -> 0L as T
                    String::class.java -> "" as T
                    Float::class.java -> 0F as T
                    Boolean::class.java -> false as T
                    else -> throw IllegalArgumentException("SharedPreferences save [${key}] has wrong type ${T::class.java.name}")
                }
            }
            return if (default == null) {
                Preference.read(key, name)
            } else {
                Preference.read(key, default, name)
            }
        }
    }
}

inline fun <reified T : Serializable> serializable(
    key: String,
    name: String = Preference.name
): ReadWriteProperty<Any?, T?> {

    val isInit = AtomicBoolean(true)

    return object : ReadWriteProperty<Any?, T?> {

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            Preference.writeSerializable(key, value, name)
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (isInit.get()) {
                isInit.set(false)
                return null
            }
            return Preference.readSerializable<T>(key, name)
        }
    }
}





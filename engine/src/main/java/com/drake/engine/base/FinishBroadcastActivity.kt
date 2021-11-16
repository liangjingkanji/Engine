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

package com.drake.engine.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.Serializable

/** activity继承本类可以实现接受广播, 默认广播实现为finish全部 */
open class FinishBroadcastActivity(contentLayoutId: Int = 0) : AppCompatActivity(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }

    /** 发送给所有界面的广播 */
    open var receiver: BroadcastReceiver? = null

    /**
     * 注册广播
     */
    protected open fun registerBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("FinishBroadcastActivity")
        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val flag = intent.getSerializableExtra("finish_skip")
                    if (flag != null && this@FinishBroadcastActivity.javaClass == flag) return
                    finish()
                }
            }
        }
        receiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, intentFilter)
        }
    }

    /**
     * 注销广播
     */
    protected open fun unregisterBroadcast() {
        receiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            receiver = null
        }
    }

    companion object {
        /**
         * 发送关闭全部界面的广播
         * @param skip 被忽略的界面
         */
        @JvmStatic
        @JvmOverloads
        fun finishAll(skip: Serializable? = null) {
            val intent = Intent().setAction("FinishBroadcastActivity")
            skip?.let {
                intent.putExtra("finish_skip", skip)
            }
            LocalBroadcastManager.getInstance(app).sendBroadcast(intent)
        }
    }

}
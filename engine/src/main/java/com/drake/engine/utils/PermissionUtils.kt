/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.drake.engine.utils

import android.app.Activity
import android.text.TextUtils
import com.drake.engine.component.dialog.alert
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

fun Activity.permission(
    vararg permission: String,
    function: String = "该功能",
    onGranted: (MutableList<String>) -> Unit
) {

    AndPermission.with(this)
        .runtime()
        .permission(*permission)
        .onGranted { data: MutableList<String> -> onGranted(data) }
        .onDenied { data ->
            if (AndPermission.hasAlwaysDeniedPermission(this, data)) {

                val permissionString = TextUtils.join("\n", Permission.transformText(this, data))
                val msg = function + "需要以下权限才能正常工作\n\n" + permissionString + "\n\n" +
                        "但是您已经拒绝过权限申请\n所以现在需要进入设置界面手动勾选权限"


                alert {
                    setMessage(msg)

                    setNegativeButton("仍然拒绝") { dialogInterface, i -> }
                    setPositiveButton("手动开启") { dialogInterface, i ->
                        AndPermission.with(this@permission)
                            .runtime()
                            .setting()
                            .start()
                    }
                }
            }

        }
        .rationale { context, data, executor -> executor.execute() }
        .start()
}

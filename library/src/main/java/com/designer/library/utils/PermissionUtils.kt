/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils

import android.app.Activity
import android.text.TextUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import org.jetbrains.anko.alert

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
                    message = msg
                    negativeButton("仍然拒绝") {}
                    positiveButton("手动开启") {
                        AndPermission.with(this@permission)
                            .runtime()
                            .setting()
                            .start()
                    }
                }.show()
            }

        }
        .rationale { context, data, executor -> executor.execute() }
        .start()
}

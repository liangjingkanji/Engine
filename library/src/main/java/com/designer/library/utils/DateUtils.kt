/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    /**
     * 判断目标时间是否大于当前时间
     *
     * @param dateStr 目标时间
     */
    fun greaterThanCurDate(dateStr: String): Boolean {
        val time = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.CHINA)
        try {
            val date = format.parse(dateStr)
            return time < date.time
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: Long, format: String = "yyyy-MM-dd"): String {
        val date = Date(millis)
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return sf.format(date)
    }

    /**
     * 格式化毫秒
     */
    fun formatDate(millis: String?, format: String = "yyyy-MM-dd"): String {
        if (millis.isNullOrEmpty()) {
            return ""
        }
        val date = Date(java.lang.Long.parseLong(millis))
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return sf.format(date)
    }

}

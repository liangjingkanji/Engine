/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils

import android.app.Activity
import androidx.core.content.ContextCompat
import cn.qqtheme.framework.picker.DateTimePicker
import cn.qqtheme.framework.util.ConvertUtils
import com.designer.library.R
import java.util.*

/**
 * Author     : majingcheng
 * Date       : 2018/10/8
 * 年月日时分选择器
 */
class YearMonthDayTimePicker {
    private var onYearMonthDayTime: ((String) -> Unit)? =
        null       //前一个为整个数据的position,后一个为item中的position

    fun onYearMonthDayTime(onYearMonthDayTime: (String) -> Unit): YearMonthDayTimePicker {
        this.onYearMonthDayTime = onYearMonthDayTime
        return this
    }

    lateinit var picker: DateTimePicker

    fun showYearMonthDayTimePicker(activity: Activity) {
        val c = Calendar.getInstance()
        val startHour = 0
        val startMinute = 0
        val endHour = 23
        val endMinute = 59
        picker = DateTimePicker(activity, DateTimePicker.HOUR_24)
        picker.setDateRangeEnd(c.get(Calendar.YEAR) + 3, 12, 31)
        picker.setTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setCancelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setSubmitTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDividerColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setLabelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setPressedTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDateRangeStart(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
        picker.setTimeRangeStart(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
        picker.setTimeRangeStart(startHour, startMinute)
        picker.setTimeRangeEnd(endHour, endMinute)
        picker.setTopPadding(ConvertUtils.toPx(activity, 20f))
        picker.setWidth(-1)
        picker.setOnDateTimePickListener(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
            onYearMonthDayTime!!.invoke("$year-$month-$day $hour:00")
        })
        picker.show()
    }


    fun showYearMonthDayAndTimePicker(activity: Activity) {
        val c = Calendar.getInstance()
        val startHour = 0
        val startMinute = 0
        val endHour = 23
        val endMinute = 59
        picker = DateTimePicker(activity, DateTimePicker.HOUR_24)
        picker.setDateRangeEnd(c.get(Calendar.YEAR) + 3, 12, 31)
        picker.setTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setCancelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setSubmitTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDividerColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setLabelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setPressedTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDateRangeStart(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
        picker.setTimeRangeStart(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
        picker.setTimeRangeStart(startHour, startMinute)
        picker.setTimeRangeEnd(endHour, endMinute)
        picker.setTopPadding(ConvertUtils.toPx(activity, 20f))
        picker.setWidth(-1)
        picker.setOnDateTimePickListener(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
            onYearMonthDayTime!!.invoke("$year-$month-$day $hour:$minute")
        })
        picker.show()
    }

    /**
     * 年月日
     */
    fun showYearMonthDay(activity: Activity) {
        val c = Calendar.getInstance()
        val startHour = 0
        val startMinute = 0
        val endHour = 23
        val endMinute = 59
        picker = DateTimePicker(activity, DateTimePicker.YEAR_MONTH_DAY)
        picker.setDateRangeEnd(c.get(Calendar.YEAR) + 3, 12, 31)
        picker.setTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setCancelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setSubmitTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDividerColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setLabelTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setPressedTextColor(ContextCompat.getColor(activity, R.color.textColor))
        picker.setDateRangeStart(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1,
            c.get(Calendar.DAY_OF_MONTH)
        )
        picker.setTimeRangeStart(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))
        picker.setTimeRangeStart(startHour, startMinute)
        picker.setTimeRangeEnd(endHour, endMinute)
        picker.setTopPadding(ConvertUtils.toPx(activity, 20f))
        picker.setWidth(-1)
        picker.setOnDateTimePickListener(DateTimePicker.OnYearMonthDayTimePickListener { year, month, day, hour, minute ->
            onYearMonthDayTime!!.invoke("$year-$month-$day")
        })
        picker.show()
    }
}

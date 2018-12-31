/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget


import java.text.DecimalFormat

object PriceUtils {
    fun getPrice(price: Int): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(price.toDouble() / 100)
    }

    fun getPrice(price: Long): String {
        val decimalFormat = DecimalFormat("0.00")
        return if (price % 100 == 0L) {
            // 如果整除不需要小数点后两位
            (price / 100).toString()
        } else {
            decimalFormat.format(price.toDouble() / 100)
        }
    }
}

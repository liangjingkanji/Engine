/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.span

import android.text.InputFilter
import android.text.Spanned

class DigitsInputFilter(
    var decimalDigits: Int = 2,
    var integerDigits: Int = Int.MAX_VALUE,
    var totalDigits: Int = Int.MAX_VALUE,
    var pasteEnable: Boolean = true
) : InputFilter {

    private var currentLimitDigits = integerDigits

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        if ("" == source.toString()) {
            return null
        }

        if (!pasteEnable && source.length > 1) {
            return ""
        }

        val dValue = dest.toString()
        val splitArray = dValue.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        when (splitArray.size) {
            1 -> {

                currentLimitDigits = if (dValue.indexOf(".") != -1) {
                    totalDigits
                } else {
                    integerDigits
                }

                if (source.length > 1) {

                    val sValue = source.toString()
                    val subSplitArray =
                        sValue.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    when (subSplitArray.size) {
                        1 -> if (source.length + dest.length > currentLimitDigits) {
                            return source.subSequence(0, currentLimitDigits - dest.length)
                        }
                        2 -> {
                            var content = ""

                            if (dstart == dest.length) {
                                content += if (subSplitArray[0].length + dest.length > integerDigits) {
                                    subSplitArray[0].subSequence(
                                        0,
                                        integerDigits - dest.length
                                    )
                                } else {
                                    subSplitArray[0]
                                }

                                content += if (subSplitArray[1].length > decimalDigits) {
                                    "." + subSplitArray[1].substring(0, decimalDigits)

                                } else {
                                    "." + subSplitArray[1]
                                }
                                return content

                            } else {
                                content += if (subSplitArray[0].length + dest.length > integerDigits) {
                                    subSplitArray[0].subSequence(
                                        0,
                                        integerDigits - dest.length
                                    )
                                } else {
                                    subSplitArray[0]
                                }
                            }
                            return content
                        }

                        else -> {
                        }
                    }

                }

                if (splitArray[0].length >= currentLimitDigits && "." != source.toString()) {
                    return ""
                }
            }

            2 -> {
                val integerValue = splitArray[0]
                val dotValue = splitArray[1]
                val dotIndex = dValue.indexOf(".")

                if (dstart <= dotIndex) {

                    if (integerValue.length >= integerDigits) {
                        return ""
                    } else if (source.length + integerValue.length >= integerDigits) {
                        return source.subSequence(0, integerDigits - integerValue.length)
                    }

                } else {

                    if (dotValue.length >= decimalDigits) {
                        return ""
                    } else if (source.length + dotValue.length >= decimalDigits) {
                        return source.subSequence(0, decimalDigits - dotValue.length)
                    }
                }
            }
        }

        return null
    }
}
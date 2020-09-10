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

package com.drake.engine.keyboard

import android.text.InputFilter
import android.text.Spanned

/**
 * 限制数字输入
 * @property decimalDigits Int 小数位数
 * @property integerDigits Int 整数位数
 * @property totalDigits Int 全部位数
 * @property pasteEnable Boolean 是否允许复制粘贴
 */
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
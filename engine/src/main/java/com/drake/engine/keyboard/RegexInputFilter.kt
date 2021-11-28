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
import android.widget.EditText

/**
 * 为EditText添加正则输入过滤器
 * @param beforeRegex 正则匹配每次输入内容, 如果不匹配则不会允许显示到输入框中, 如果为null则不参与匹配
 * @param afterRegex 正则匹配输入后内容, 如果不匹配则不会允许显示到输入框中, 如果为null则不参与匹配
 * @param ignoreCause 忽略大小写
 */
fun EditText.inputFilterRegex(
    beforeRegex: String? = null,
    afterRegex: String? = null,
    ignoreCause: Boolean = false,
): RegexInputFilter {
    val regexInputFilter = RegexInputFilter(beforeRegex, afterRegex, ignoreCause)
    filters = arrayOf(regexInputFilter)
    return regexInputFilter
}

/**
 * 正则输入过滤器
 * @param beforeRegex 正则匹配每次输入内容, 如果不匹配则不会允许显示到输入框中, 如果为null则不参与匹配
 * @param afterRegex 正则匹配输入后内容, 如果不匹配则不会允许显示到输入框中, 如果为null则不参与匹配
 * @param ignoreCause 忽略大小写
 */
class RegexInputFilter(
    var beforeRegex: String? = null,
    var afterRegex: String? = null,
    var ignoreCause: Boolean = false,
) : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        if (end == 0) return null // 删除
        var beforeMatch = true
        beforeRegex?.let {
            val regex = if (ignoreCause) it.toRegex(RegexOption.IGNORE_CASE) else it.toRegex()
            beforeMatch = source.matches(regex)
        }
        var afterMatch = true
        afterRegex?.let {
            val regex = if (ignoreCause) it.toRegex(RegexOption.IGNORE_CASE) else it.toRegex()
            val expect = StringBuilder(dest).insert(dstart, source)
            afterMatch = expect.matches(regex)
        }
        return if (beforeMatch && afterMatch) null else ""
    }
}
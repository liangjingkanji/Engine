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


class ConstUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    enum class MemoryUnit {
        BYTE,
        KB,
        MB,
        GB
    }

    enum class TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }

    companion object {

        /**
         * Byte与Byte的倍数
         */
        val BYTE = 1
        /******************** 存储相关常量  */
        /**
         * KB与Byte的倍数
         */
        val KB = 1024
        /**
         * MB与Byte的倍数
         */
        val MB = 1048576
        /**
         * GB与Byte的倍数
         */
        val GB = 1073741824
        /**
         * 秒与毫秒的倍数
         */
        val SEC = 1000
        /**
         * 分与毫秒的倍数
         */
        val MIN = 60000
        /******************** 时间相关常量  */
        /**
         * 时与毫秒的倍数
         */
        val HOUR = 3600000
        /**
         * 天与毫秒的倍数
         */
        val DAY = 86400000
        /**
         * 正则：手机号（简单）
         */
        val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"
        /**
         * 正则：手机号（精确）
         *
         * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
         *
         * 联通：130、131、132、145、155、156、175、176、185、186
         *
         * 电信：133、153、173、177、180、181、189
         *
         * 全球星：1349
         *
         * 虚拟运营商：170
         */
        val REGEX_MOBILE_EXACT =
            "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"
        /**
         * 正则：电话号码
         */
        val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"
        /******************** 正则相关常量  */
        /**
         * 正则：身份证号码15位
         */
        val REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"
        /**
         * 正则：身份证号码18位
         */
        val REGEX_ID_CARD18 =
            "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"
        /**
         * 正则：邮箱
         */
        val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
        /**
         * 正则：URL
         */
        val REGEX_URL = "[a-zA-z]+://[^\\s]*"
        /**
         * 正则：汉字
         */
        val REGEX_ZH = "^[\\u4e00-\\u9fa5]+$"
        /**
         * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$"
        /**
         * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
         */
        val REGEX_DATE =
            "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"
        /**
         * 正则：IP地址
         */
        val REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"
        /**
         * 正则：双字节字符(包括汉字在内)
         */
        val REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]"
        /**
         * 正则：空白行
         */
        val REGEX_BLANK_LINE = "\\n\\s*\\r"
        /**
         * 正则：QQ号
         */
        val REGEX_TENCENT_NUM = "[1-9][0-9]{4,}"
        /************** 以下摘自http://tool.oschina.net/regex  */
        /**
         * 正则：中国邮政编码
         */
        val REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)"
        /**
         * 正则：正整数
         */
        val REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$"
        /**
         * 正则：负整数
         */
        val REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$"
        /**
         * 正则：整数
         */
        val REGEX_INTEGER = "^-?[1-9]\\d*$"
        /**
         * 正则：非负整数(正整数 + 0)
         */
        val REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$"
        /**
         * 正则：非正整数（负整数 + 0）
         */
        val REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$"
        /**
         * 正则：正浮点数
         */
        val REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"
        /**
         * 正则：负浮点数
         */
        val REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$"
    }
    /************** If u want more please visit http://toutiao.com/i6231678548520731137/  */
}
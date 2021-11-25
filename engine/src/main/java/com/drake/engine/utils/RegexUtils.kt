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

import java.util.regex.Pattern

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/08/02
 * desc  : utils about regex
</pre> *
 */
object RegexUtils {
    /**
     * 数字和字母的结合 ( 6 - 20 位)
     */
    fun isPassword(value: String): Boolean {
        val regex = "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{6,20}$".toRegex()
        return value.matches(regex)
    }
    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Return whether input matches regex of simple mobile.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileSimple(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input)
    }

    /**
     * Return whether input matches regex of exact mobile.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileExact(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input)
    }

    /**
     * Return whether input matches regex of telephone number.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isTel(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_TEL, input)
    }

    /**
     * Return whether input matches regex of id card number which length is 15.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isIDCard15(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_ID_CARD15, input)
    }

    /**
     * Return whether input matches regex of id card number which length is 18.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isIDCard18(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_ID_CARD18, input)
    }

    /**
     * Return whether input matches regex of email.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isEmail(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_EMAIL, input)
    }

    /**
     * Return whether input matches regex of url.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isURL(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_URL, input)
    }

    /**
     * Return whether input matches regex of Chinese character.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isZh(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_ZH, input)
    }

    /**
     * Return whether input matches regex of username.
     *
     * scope for "a-z", "A-Z", "0-9", "_", "Chinese character"
     *
     * can't end with "_"
     *
     * length is between 6 to 20.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isUsername(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_USERNAME, input)
    }

    /**
     * Return whether input matches regex of date which pattern is "yyyy-MM-dd".
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isDate(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_DATE, input)
    }

    /**
     * Return whether input matches regex of ip address.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isIP(input: CharSequence?): Boolean {
        return isMatch(RegexConstants.REGEX_IP, input)
    }

    /**
     * Return whether input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMatch(regex: String?, input: CharSequence?): Boolean {
        return input != null && input.length > 0 && Pattern.matches(regex, input)
    }

    /**
     * Return the list of input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return the list of input matches the regex
     */
    fun getMatches(regex: String?, input: CharSequence?): List<String>? {
        if (input == null) {
            return null
        }
        val matches: MutableList<String> = ArrayList()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        while (matcher.find()) {
            matches.add(matcher.group())
        }
        return matches
    }

    /**
     * Splits input around matches of the regex.
     *
     * @param input The input.
     * @param regex The regex.
     * @return the array of strings computed by splitting input around matches of regex
     */
    fun getSplits(input: String?, regex: String): Array<String>? {
        return input?.split(regex.toRegex())?.toTypedArray()
    }

    /**
     * Replace the first subsequence of the input sequence that matches the
     * regex with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing the first matching subsequence by the replacement
     * string, substituting captured subsequences as needed
     */
    fun getReplaceFirst(
        input: String?,
        regex: String?,
        replacement: String?
    ): String? {
        return if (input == null) {
            null
        } else Pattern.compile(regex)
            .matcher(input)
            .replaceFirst(replacement)
    }

    /**
     * Replace every subsequence of the input sequence that matches the
     * pattern with the given replacement string.
     *
     * @param input       The input.
     * @param regex       The regex.
     * @param replacement The replacement string.
     * @return the string constructed by replacing each matching subsequence by the replacement
     * string, substituting captured subsequences as needed
     */
    fun getReplaceAll(
        input: String?,
        regex: String?,
        replacement: String?
    ): String? {
        return if (input == null) {
            null
        } else Pattern.compile(regex)
            .matcher(input)
            .replaceAll(replacement)
    }
}
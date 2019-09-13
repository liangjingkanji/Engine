/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.net.convert

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.yanzhenjie.kalle.Response
import com.yanzhenjie.kalle.simple.Converter
import com.yanzhenjie.kalle.simple.SimpleResponse
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * 网络请求GSON转换器
 */
@Suppress("UNCHECKED_CAST")
class GsonConverter(var successCode: Int = 1, var onResponse: (Response.() -> String)? = null) :
    Converter {


    @Throws(Exception::class)
    override fun <S, F> convert(
        succeed: Type,
        failed: Type,
        response: Response,
        fromCache: Boolean
    ): SimpleResponse<S, F> {
        var succeedData: S? = null
        var failedData: F? = null

        val json: String = if (onResponse == null) {
            response.body().string()
        } else {
            onResponse?.let { response.it() }!!
        }

        var code = response.code()
        when {
            code in 200..299 -> {
                try {
                    val jsonObject = JSONObject(json)
                    val responseCode = jsonObject.optInt("code")

                    if (responseCode == successCode) {
                        run {
                            if (succeed === String::class.java) {
                                succeedData = json as S
                            } else {
                                try {
                                    succeedData = GSON.fromJson<S>(json, succeed)
                                } catch (e: JsonSyntaxException) {
                                    e.printStackTrace()
                                    failedData = "无法解析数据" as F
                                }
                            }
                        }
                    } else {
                        failedData = jsonObject.optString("errorMessage") as F
                        code = responseCode
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    failedData = "无法解析错误信息" as F
                }

            }
            code in 400..499 -> failedData = "发生异常错误" as F
            code >= 500 -> failedData = "服务器开小差啦" as F
        }

        return SimpleResponse.newBuilder<S, F>().code(code)
            .headers(response.headers())
            .fromCache(fromCache)
            .succeed(succeedData)
            .failed(failedData)
            .build()
    }

    companion object {

        private val GSON = GsonBuilder().serializeNulls().create()
    }
}

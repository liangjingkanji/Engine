@file:Suppress("unused")

package com.drake.engine.reactive

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * 指定时间内只接受第一次的事件
 * @receiver View
 * @param interval Long
 * @param block [@kotlin.ExtensionFunctionType] Function1<View, Unit>
 */
fun View.throttle(
    interval: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Observable<View> {
    return Observable.create<View> { emitter ->
        setOnClickListener { emitter.onNext(it) }
    }.throttleFirst(interval, timeUnit)
}

/**
 * 指定时间内连续输入会导致事件被过滤
 * @receiver EditText
 * @param time Long 默认500毫秒
 * @param timeUnit TimeUnit
 * @return Observable<String>
 */
fun EditText.debounce(
    time: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS
): Observable<String> {
    return afterTextChanged().throttleFirst(time, timeUnit)
}

/**
 * 文本改变后观察者
 * @receiver TextView
 * @return Observable<String>
 */
fun TextView.afterTextChanged(): Observable<String> {

    return Observable.create {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                it.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}

fun Array<out TextView>.afterTextChanged(): List<Observable<String>> {
    return map {
        it.afterTextChanged()
    }
}
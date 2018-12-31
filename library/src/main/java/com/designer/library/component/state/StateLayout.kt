/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.state

import android.content.Context
import android.net.ConnectivityManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.collection.ArrayMap
import com.designer.library.R

/**
 * 功能强大的多状态布局
 *
 *
 * 全局配置
 * 单例配置
 * 不支持RecyclerView
 * 可以代码替换或者布局使用
 * 无网络情况下showLoading显示错误布局, 有网则显示加载中布局
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var contentId: Int = 1
    private val contentViews = ArrayMap<Int, View>()

    @LayoutRes
    var errorLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    @LayoutRes
    var emptyLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    @LayoutRes
    var loadingLayout: Int = NO_ID
        set(value) {
            if (field != value) {
                remove(field)
                field = value
            }
        }

    private var onEmpty: (View.() -> Unit)? = null
    private var onError: (View.() -> Unit)? = null
    private var onLoading: (View.() -> Unit)? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        try {
            emptyLayout = attributes.getResourceId(R.styleable.StateLayout_empty_layout, NO_ID)
            errorLayout = attributes.getResourceId(R.styleable.StateLayout_error_layout, NO_ID)
        } finally {
            attributes.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1 || childCount == 0) {
            throw UnsupportedOperationException("Must contain child view of one")
        }
        if (contentViews.size == 0) {
            val view = getChildAt(0)
            setContentView(view)
        }
    }

    fun onEmpty(block: View.() -> Unit) {
        onEmpty = block
    }

    fun onLoading(block: View.() -> Unit) {
        onLoading = block
    }

    fun onError(block: View.() -> Unit) {
        onError = block
    }

    /**
     * 有网则显示加载中, 无网络直接显示错误
     */
    fun showLoading() {
        if (context.isNetworkConnected()) {
            if (loadingLayout == NO_ID) {
                loadingLayout = StateConfig.loadingLayout
            }

            if (loadingLayout != NO_ID) {
                show(loadingLayout)
            }
        } else {
            showError()
        }
    }

    /**
     * 判断是否有网络连接
     */
    private fun Context.isNetworkConnected(): Boolean {
        if (context != null) {
            val mConnectivityManager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun showEmpty() {
        if (emptyLayout == NO_ID) {
            emptyLayout = StateConfig.emptyLayout
        }
        if (emptyLayout != NO_ID) {
            show(emptyLayout)
        }
    }

    fun showError() {
        if (errorLayout == NO_ID) {
            errorLayout = StateConfig.errorLayout
        }
        if (errorLayout != NO_ID) {
            show(errorLayout)
        }
    }

    fun showContent() {
        show(contentId)
    }

    /**
     * 显示视图
     */
    private fun show(layoutId: Int) {
        for (view in contentViews.values) {
            view.visibility = View.GONE
        }

        try {
            layout(layoutId)!!.visibility = View.VISIBLE
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun remove(layoutId: Int?) {
        if (contentViews.containsKey(layoutId)) {
            val vg = contentViews.remove(layoutId)
            removeView(vg)
        }
    }

    @Throws(NullPointerException::class)
    private fun layout(@LayoutRes layoutId: Int): View? {
        if (contentViews.containsKey(layoutId)) {
            return contentViews[layoutId]
        }

        val view = LayoutInflater.from(context).inflate(layoutId, this, false)

        addView(view)
        contentViews[layoutId] = view

        when (layoutId) {
            emptyLayout -> {
                if (onEmpty == null) {
                    StateConfig.onEmpty?.let {
                        onEmpty = it
                    }
                }
                onEmpty?.invoke(view)
            }
            errorLayout -> {
                if (onError == null) {
                    StateConfig.onError?.let {
                        onError = it
                    }
                }
                onError?.invoke(view)
            }
            loadingLayout -> {
                if (onLoading == null) {
                    StateConfig.onLoading?.let {
                        onLoading = it
                    }
                }
                onLoading?.invoke(view)
            }
        }
        return view
    }

    internal fun setContentView(view: View) {
        contentViews[contentId] = view
    }
}

/**
 * 全局的单列多状态布局配置
 */
object StateConfig {

    @LayoutRes
    var errorLayout = View.NO_ID
    @LayoutRes
    var emptyLayout = View.NO_ID
    @LayoutRes
    var loadingLayout = View.NO_ID

    internal var onEmpty: (View.() -> Unit)? = null
    internal var onError: (View.() -> Unit)? = null
    internal var onLoading: (View.() -> Unit)? = null

    fun onEmpty(block: View.() -> Unit) {
        onEmpty = block
    }

    fun onLoading(block: View.() -> Unit) {
        onLoading = block
    }

    fun onError(block: View.() -> Unit) {
        onError = block
    }
}

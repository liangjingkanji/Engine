/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.designer.library.R
import com.designer.library.component.state.StateConfig
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshInternal
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.impl.RefreshContentWrapper
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener

/**
 * 适用于RecyclerView的自动刷新的布局
 * - 自动刷新
 * - 自动添加数据
 * - 自动多状态
 */
open class PageRefreshLayout : SmartRefreshLayout, OnMultiPurposeListener {

    var firstPage = 1
    var emptyLayout = View.NO_ID
    var errorLayout = View.NO_ID
    var adapter: BaseRecyclerAdapter? = null

    companion object {

        private const val STATE_CONTENT = 0
        private const val STATE_EMPTY = 1
        private const val STATE_ERROR = 2
    }

    var page = firstPage
    var hasMore = true
    private var currentState = STATE_CONTENT
    private var emptyView: View? = null
    private var errorView: View? = null
    private var contentView: View? = null
    private var onEmpty: (View.() -> Unit)? = null
    private var onError: (View.() -> Unit)? = null
    private var onRefresh: (PageRefreshLayout.() -> Unit)? = null
    private var onLoadMore: (PageRefreshLayout.() -> Unit)? = null
    private var onState: (PageRefreshLayout.(RefreshState, RefreshState) -> Unit)? = null
    private var onHeaderAndFooterListener: OnHeaderAndFooterListener? = null

    fun onEmpty(block: View.() -> Unit) {
        onEmpty = block
    }

    /**
     * 静默刷新数据
     * @param data List<Any>
     */
    fun silentRefresh(data: List<Any>, hasMore: PageRefreshLayout.() -> Boolean) {
        page = firstPage
        setNoMoreData(false)
        refreshData(data, hasMore)
        if (this.hasMore) {
            finishLoadMore(true)
        } else {
            finishLoadMoreWithNoMoreData()
        }
    }

    fun onError(block: View.() -> Unit) {
        onError = block
    }

    fun onRefresh(block: PageRefreshLayout.() -> Unit) {
        onRefresh = block
    }

    fun onLoadMore(block: PageRefreshLayout.() -> Unit) {
        onLoadMore = block
    }

    fun onState(block: PageRefreshLayout.(RefreshState, RefreshState) -> Unit) {
        onState = block
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.PageRefreshLayout)
        try {
            emptyLayout = attributes.getResourceId(
                R.styleable.PageRefreshLayout_empty_layout,
                StateConfig.emptyLayout
            )
            errorLayout = attributes.getResourceId(
                R.styleable.PageRefreshLayout_error_layout,
                StateConfig.errorLayout
            )
        } finally {
            attributes.recycle()
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }

    @SuppressLint("RestrictedApi")
    override fun setRefreshContent(content: View): RefreshLayout {
        val thisView = this
        if (mRefreshContent != null) {
            super.removeView(mRefreshContent.view)
        }
        super.addView(content, 0)
        if (mRefreshHeader != null && mRefreshHeader.spinnerStyle == SpinnerStyle.FixedBehind) {
            super.bringChildToFront(content)
            if (mRefreshFooter != null && mRefreshFooter.spinnerStyle != SpinnerStyle.FixedBehind) {
                super.bringChildToFront(mRefreshFooter.view)
            }
        } else if (mRefreshFooter != null && mRefreshFooter.spinnerStyle == SpinnerStyle.FixedBehind) {
            super.bringChildToFront(content)
            if (mRefreshHeader != null && mRefreshHeader.spinnerStyle == SpinnerStyle.FixedBehind) {
                super.bringChildToFront(mRefreshHeader.view)
            }
        }
        mRefreshContent = RefreshContentWrapper(content)
        if (mHandler != null) {
            val fixedHeaderView =
                if (mFixedHeaderViewId > 0) thisView.findViewById<View>(mFixedHeaderViewId) else null
            val fixedFooterView =
                if (mFixedFooterViewId > 0) thisView.findViewById<View>(mFixedFooterViewId) else null

            mRefreshContent.setScrollBoundaryDecider(mScrollBoundaryDecider)
            mRefreshContent.setEnableLoadMoreWhenContentNotFull(mEnableLoadMoreWhenContentNotFull)
            mRefreshContent.setUpComponent(mKernel, fixedHeaderView, fixedFooterView)
        }
        return this
    }

    private fun init() {
        setOnMultiPurposeListener(this)
        setEnableLoadMoreWhenContentNotFull(false)
        isEnableLoadMore = false

        if (emptyView == null && emptyLayout != NO_ID) {
            emptyView = getView(emptyLayout)
        }

        if (errorView == null && errorLayout != NO_ID) {
            errorView = getView(errorLayout)
        }

        if (contentView == null) {
            for (i in 0 until childCount) {
                val view = getChildAt(i)
                if (view !is RefreshInternal) {
                    contentView = view
                    break
                }
            }
        }
    }

    fun setEmptyAndErrorLayout(@LayoutRes emptyLayout: Int, @LayoutRes errorLayout: Int): PageRefreshLayout {
        emptyView = getView(emptyLayout)
        errorView = getView(errorLayout)
        return this
    }

    fun setEmptyLayout(@LayoutRes emptyLayout: Int): PageRefreshLayout {
        emptyView = getView(emptyLayout)
        return this
    }

    fun setErrorLayout(@LayoutRes emptyLayout: Int): PageRefreshLayout {
        errorView = getView(errorLayout)
        return this
    }

    fun refreshData(data: List<Any?>?, hasMore: PageRefreshLayout.() -> Boolean) {
        if (data.isNullOrEmpty()) {
            showEmpty()
            return
        } else {
            showContent()
        }
        if (adapter == null && contentView != null && contentView is RecyclerView) {
            adapter = (contentView as RecyclerView).baseAdapter
        }
        adapter?.let {
            // 下拉刷新/初始化数据
            if (page == firstPage) {
                adapter!!.models = data
            } else {
                adapter!!.addModels(data)
            }
            page++
        }
        this.hasMore = hasMore()
    }

    /**
     * 关闭下拉加载/上拉刷新
     */
    fun finish(success: Boolean) {
        if (state == RefreshState.Refreshing) {
            if (!isEnableLoadMore) {
                isEnableLoadMore = true
            }
            if (!success) {
                showError()
            }
            finishRefresh(success)
        }

        if (hasMore) {
            finishLoadMore(success)
        } else {
            finishLoadMoreWithNoMoreData()
        }
    }

    /**
     * 显示空布局(只有下拉刷新才能触发)
     */
    fun showEmpty() {
        if (currentState != STATE_EMPTY && contentView is RecyclerView) {
            currentState = STATE_EMPTY
            emptyView?.let {
                setRefreshContent(it)
                onEmpty?.invoke(emptyView!!)
            }
            finishRefresh()
        }
    }

    /**
     * 显示错误布局(自己有下拉刷新才能触发)
     */
    fun showError() {
        if (currentState != STATE_ERROR) {
            currentState = STATE_ERROR
            errorView?.let {
                setRefreshContent(it)
                onError?.invoke(errorView!!)
            }
            finishRefresh()
        }
    }

    /**
     * 显示内容布局
     */
    fun showContent() {
        if (currentState != STATE_CONTENT) {
            currentState = STATE_CONTENT

            contentView?.let {
                setRefreshContent(it)
            }
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (onLoadMore == null) {
            onRefresh?.invoke(this)
        } else {
            onLoadMore?.invoke(this)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = firstPage
        setNoMoreData(false)
        onRefresh?.invoke(this)
    }


    fun setOnHeaderAndFooterListener(onHeaderAndFooterListener: OnHeaderAndFooterListener): PageRefreshLayout {
        this.onHeaderAndFooterListener = onHeaderAndFooterListener
        return this
    }

    private fun getView(@LayoutRes layoutId: Int): View? {
        return if (layoutId == View.NO_ID) {
            null
        } else LayoutInflater.from(context).inflate(layoutId, this, false)

    }

    override fun onHeaderMoving(
        header: RefreshHeader,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        headerHeight: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onHeaderReleased(header: RefreshHeader, headerHeight: Int, maxDragHeight: Int) {
        if (onHeaderAndFooterListener != null) {
            onHeaderAndFooterListener!!.onHeaderReleased(header, headerHeight, maxDragHeight)
        }
    }

    override fun onHeaderStartAnimator(
        header: RefreshHeader,
        headerHeight: Int,
        maxDragHeight: Int
    ) {
        if (onHeaderAndFooterListener != null) {
            onHeaderAndFooterListener!!.onHeaderStartAnimator(header, headerHeight, maxDragHeight)
        }
    }

    override fun onHeaderFinish(header: RefreshHeader, success: Boolean) {
        if (onHeaderAndFooterListener != null) {
            onHeaderAndFooterListener!!.onHeaderFinish(header, success)
        }
    }

    override fun onFooterMoving(
        footer: RefreshFooter,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        footerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onFooterReleased(footer: RefreshFooter, footerHeight: Int, maxDragHeight: Int) {

    }

    override fun onFooterStartAnimator(
        footer: RefreshFooter,
        footerHeight: Int,
        maxDragHeight: Int
    ) {

    }

    override fun onFooterFinish(footer: RefreshFooter, success: Boolean) {

    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        onState?.invoke(this, oldState, newState)
    }

    class OnHeaderAndFooterListener {

        fun onHeaderMoving(
            header: RefreshHeader,
            isDragging: Boolean,
            percent: Float,
            offset: Int,
            headerHeight: Int,
            maxDragHeight: Int
        ) {
        }


        fun onHeaderReleased(header: RefreshHeader, headerHeight: Int, maxDragHeight: Int) {

        }


        fun onHeaderStartAnimator(header: RefreshHeader, headerHeight: Int, maxDragHeight: Int) {

        }


        fun onHeaderFinish(header: RefreshHeader, success: Boolean) {

        }


        fun onFooterMoving(
            footer: RefreshFooter,
            isDragging: Boolean,
            percent: Float,
            offset: Int,
            footerHeight: Int,
            maxDragHeight: Int
        ) {

        }


        fun onFooterReleased(footer: RefreshFooter, footerHeight: Int, maxDragHeight: Int) {

        }


        fun onFooterStartAnimator(footer: RefreshFooter, footerHeight: Int, maxDragHeight: Int) {

        }


        fun onFooterFinish(footer: RefreshFooter, success: Boolean) {

        }
    }
}

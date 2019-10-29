/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

@file:Suppress("UNCHECKED_CAST")

package com.drake.engine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import com.drake.engine.base.getApp

class BasePagerAdapter(layoutIds: ArrayList<Int>, val titles: ArrayList<String>? = null) :
    PagerAdapter() {

    private val viewDataBindings = ArrayList<ViewDataBinding>()

    init {

        for (layoutId in layoutIds) {
            val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(getApp()),
                layoutId,
                null,
                false
            )
            viewDataBindings.add(viewDataBinding)
        }

    }

    override fun getCount(): Int {
        return viewDataBindings.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewDataBinding = viewDataBindings[position]
        val view = viewDataBinding.root
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

    fun <B : ViewDataBinding> getViewDataBinding(position: Int): B {
        return viewDataBindings[position] as B
    }

    /**
     * 通过布局创建并且返回ViewDataBinding
     */
    fun <B : ViewDataBinding> add(layoutId: Int): B {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(getApp()),
            layoutId,
            null,
            false
        )
        viewDataBindings.add(viewDataBinding)
        return viewDataBinding as B
    }

    fun <B : ViewDataBinding> add(layoutId: Int, title: String? = null): B {

        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(getApp()),
            layoutId,
            null,
            false
        )

        viewDataBindings.add(viewDataBinding)

        if (titles != null && title != null) {
            titles.add(title)
        }

        return viewDataBinding as B
    }
}

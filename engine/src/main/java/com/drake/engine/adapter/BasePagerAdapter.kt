/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */


package com.drake.engine.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class BasePagerAdapter(var viewList: ArrayList<View>, var titles: ArrayList<String>? = null) :
    PagerAdapter() {

    override fun getCount(): Int {
        return viewList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = viewList[position]
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

}

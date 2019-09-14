/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 基础的ViewPager的FragmentPagerAdapter
 */
class BaseFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: Array<Fragment>,
    private val titles: Array<String>? = null
) :
    FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

}


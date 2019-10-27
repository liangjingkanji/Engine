/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

@file:Suppress("FunctionName", "unused")

package com.drake.engine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


fun FragmentActivity.FragmentPagerAdapter(
    fragments: Array<Fragment>,
    titles: Array<String>? = null
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)
}


fun Fragment.FragmentPagerAdapter(
    fragments: Array<Fragment>,
    titles: Array<String>? = null
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(childFragmentManager, fragments, titles)
}


/**
 *
 *
 * 基础的ViewPager的FragmentPagerAdapter
 */
class BaseFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    private val fragments: Array<Fragment>,
    private val titles: Array<String>? = null
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


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


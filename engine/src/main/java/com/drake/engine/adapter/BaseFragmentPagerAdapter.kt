/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */


package com.drake.engine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

fun FragmentActivity.FragmentPagerAdapter(
    titles: MutableList<String>? = null,
    fragments: MutableList<Fragment>
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)
}

fun Fragment.FragmentPagerAdapter(
    fragments: MutableList<Fragment>,
    titles: MutableList<String>? = null
): BaseFragmentPagerAdapter {
    return BaseFragmentPagerAdapter(childFragmentManager, fragments, titles)
}


class BaseFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    var fragments: MutableList<Fragment>,
    var titles: MutableList<String>? = null
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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


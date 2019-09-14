/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget

import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import java.util.*

fun CommonTabLayout.setTabEntity(vararg title: String) {

    val customTabEntities = ArrayList<CustomTabEntity>()

    for (aTitle in title) {
        customTabEntities.add(TabEntity(aTitle))
    }

    setTabData(customTabEntities)
}


class TabEntity(val title: String, val selectedIcon: Int = 0, val unselectedIcon: Int = 0) :
    CustomTabEntity {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unselectedIcon
    }
}

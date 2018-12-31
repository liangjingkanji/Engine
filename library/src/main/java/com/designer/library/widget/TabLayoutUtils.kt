/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget

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

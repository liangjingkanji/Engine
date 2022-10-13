/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.drake.engine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.IdRes

/**
 * 可以多行布局的RadioGroup，但是会用掉子RadioButton的OnCheckedChangeListener
 * A RadioGroup allow multiple rows layout, will use the RadioButton's OnCheckedChangeListener
 */
class NestedRadioGroup : RadioGroup {

    private var checking = false

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    fun init() {
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewRemoved(parent: View, child: View) {
                if (parent === this@NestedRadioGroup && child is ViewGroup) {
                    for (radioButton in getRadioButtonFromGroup(child)) {
                        radioButton.setOnCheckedChangeListener(null)
                    }
                }
            }

            override fun onChildViewAdded(parent: View, child: View) {
                setRadioButtonForChild(parent, child)
            }
        })
    }

    private fun setRadioButtonForChild(parent: View, child: View) {
        if (parent === this@NestedRadioGroup && child is ViewGroup) {
            for (radioButton in getRadioButtonFromGroup(child)) {
                var id = radioButton.id
                if (id == NO_ID) {
                    id = generateViewId()
                    radioButton.id = id
                }
                if (radioButton.isChecked) {
                    check(id)
                }
                radioButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) {
                        if (isChecked) {
                            radioButton.setOnCheckedChangeListener(null)
                            check(button.id)
                            radioButton.setOnCheckedChangeListener(this)
                        }
                    }
                })
            }
        }
    }

    /**
     * 通知子视图发生变化, 当动态添加[RadioButton]时需要执行本方法重新检索
     * 添加相同Id的[RadioButton]会导致单选失效
     */
    fun notifyChanged() {
        for (i in 0 until childCount) {
            setRadioButtonForChild(this, getChildAt(i))
        }
    }

    override fun check(@IdRes id: Int) {
        if (checking) return
        checking = true
        super.check(id)
        checking = false
    }

    /**
     * 返回对应Id的[RadioButton]的索引位置
     */
    fun indexOfId(@IdRes id: Int): Int {
        return getRadioButtonFromGroup(this).indexOfFirst { it.id == id }
    }

    /**
     * 选中对应索引的[RadioButton]
     */
    fun checkIndex(index: Int) {
        val button = getRadioButtonFromGroup(this).getOrNull(index) ?: return
        if (button.id != checkedRadioButtonId) {
            button.isChecked = true
        }
    }

    private fun getRadioButtonFromGroup(group: ViewGroup?): ArrayList<RadioButton> {
        if (group == null) return ArrayList()
        val list = ArrayList<RadioButton>()
        getRadioButtonFromGroup(group, list)
        return list
    }

    private fun getRadioButtonFromGroup(group: ViewGroup, list: ArrayList<RadioButton>) {
        var i = 0
        val count = group.childCount
        while (i < count) {
            val child = group.getChildAt(i)
            if (child is RadioButton) {
                list.add(child)
            } else if (child is ViewGroup) {
                getRadioButtonFromGroup(child, list)
            }
            i++
        }
    }
}
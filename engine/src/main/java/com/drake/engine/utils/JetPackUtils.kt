/*
 * Copyright (C) 2018 Drake, Inc.
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

package com.drake.engine.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

open class SaveViewModel(var saved: SavedStateHandle) : ViewModel()

inline fun <reified M : ViewModel> ViewModelStoreOwner.getVM(): M {
    return ViewModelProvider(this).get(M::class.java)
}

inline fun <reified M : ViewModel> FragmentActivity.getSaved(): M {
    return ViewModelProvider(
        this,
        SavedStateViewModelFactory(application, this)
    ).get(M::class.java)
}


inline fun <reified M : ViewModel> Fragment.getSaved(): M {

    return ViewModelProvider(
        this,
        SavedStateViewModelFactory(activity!!.application, this)
    ).get(M::class.java)
}


fun <M> LifecycleOwner.observe(liveData: LiveData<M>?, block: M?.() -> Unit) {
    liveData?.observe(this, Observer { it.block() })
}
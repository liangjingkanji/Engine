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
package com.drake.engine.sample

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

open class Model(var age: Int) : BaseObservable() {

    @Bindable
    var name = "吴彦祖"

}
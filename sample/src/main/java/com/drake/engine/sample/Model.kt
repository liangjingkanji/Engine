package com.drake.engine.sample

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

class Model(var age: Int) : BaseObservable(), Serializable {

    @Bindable
    var name = "吴彦祖"

}
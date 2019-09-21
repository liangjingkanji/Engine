package com.drake.engine.sample

import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import com.drake.engine.component.databinding.ObservableIml

class Model : ObservableIml {

    override val registry: PropertyChangeRegistry = PropertyChangeRegistry()

    @Bindable
    var name = "吴彦祖"


}
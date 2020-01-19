package com.drake.engine.widget

import androidx.databinding.BindingAdapter

@BindingAdapter("checked")
fun SmoothCheckBox.setCheckedBind(checked: Boolean) {
    this.isChecked = checked
}

@BindingAdapter("checked")
fun SmoothCheckBox.setCheckedBind(checked: Any?) {
    this.isChecked = checked != null
}
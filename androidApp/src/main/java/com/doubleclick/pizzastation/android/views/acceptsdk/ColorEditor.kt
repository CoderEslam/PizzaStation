package com.doubleclick.pizzastation.android.views.acceptsdk

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatCheckBox

internal object ColorEditor {
    @SuppressLint("RestrictedApi")
    fun setAppCompatCheckBoxColors(
        _checkbox: AppCompatCheckBox,
        _uncheckedColor: Int,
        _checkedColor: Int
    ) {
        val states = arrayOf(intArrayOf(-16842912), intArrayOf(16842912))
        val colors = intArrayOf(_uncheckedColor, _checkedColor)
        _checkbox.supportButtonTintList = ColorStateList(states, colors)
    }
}

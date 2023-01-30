package com.doubleclick.pizzastation.android.views.togglebuttongroup.button

import android.view.View
import android.widget.Checkable

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
open interface OnCheckedChangeListener {
    fun <T> onCheckedChanged(view: T, isChecked: Boolean) where T : View?, T : Checkable?
}

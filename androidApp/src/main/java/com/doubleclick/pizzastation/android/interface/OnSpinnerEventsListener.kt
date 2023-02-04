package com.doubleclick.pizzastation.android.`interface`

import android.widget.Spinner

interface OnSpinnerEventsListener {
    fun onPopupWindowOpened(spinner: Spinner?)
    fun onPopupWindowClosed(spinner: Spinner?)
}
package com.doubleclick.pizzastation.android.`interface`

import com.doubleclick.pizzastation.android.model.Extra

interface ExtraDeleteListener {
    fun onExtraDeleteListener(pos: Int, extra: Extra, posParent: Int)
}
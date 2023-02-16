package com.doubleclick.pizzastation.android.`interface`

import com.doubleclick.pizzastation.android.model.MenuModel

interface DeletedSliceListener {
    fun deleteSlice(pizza: MenuModel, position: Int)
}
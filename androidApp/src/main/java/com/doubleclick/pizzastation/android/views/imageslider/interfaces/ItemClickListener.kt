package com.doubleclick.pizzastation.android.views.imageslider.interfaces

import com.doubleclick.pizzastation.android.model.OffersModel

interface ItemClickListener {
    /**
     * Click listener selected item function.
     *
     * @param  position  selected item position
     */
    fun onItemSelected(position: Int, offersModel: OffersModel)
}
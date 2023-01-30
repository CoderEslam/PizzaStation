package com.doubleclick.pizzastation.android.model

import com.doubleclick.pizzastation.android.views.imageslider.constants.ScaleTypes

data class OffersModel(
    val id: Int,
    val offer_details: String,
    val offer_name: String,
    val offer_price: String,
    val offer_image: String,
    val user_id: Int,
    var scaleType: ScaleTypes? = null
)
package com.doubleclick.pizzastation.android.model

data class CartModel(
    val extra: MutableList<Extra>? = null,
    val id: Int = -1,
    val name: String,
    val price: String,
    val image: String = "",
    val quantity: String,
    val size: String,
    val user_id: Int = -1,
    var count: Int = -1
)
package com.doubleclick.pizzastation.android.model

data class CartModel(
    val extra: List<Extra>,
    val id: Int = -1,
    val name: String,
    val price: String,
    val quantity: String,
    val size: String,
    val user_id: Int = -1
)
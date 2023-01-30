package com.doubleclick.pizzastation.android.model

data class ItemOrder(
    val extra: List<Extra>,
    val name: String,
    val price: String,
    val quantity: String,
    val size: String
)
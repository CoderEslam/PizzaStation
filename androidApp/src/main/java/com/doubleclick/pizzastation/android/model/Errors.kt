package com.doubleclick.pizzastation.android.model

data class Errors(
    val extra: List<String>,
    val name: List<String>,
    val price: List<String>,
    val quantity: List<String>,
    val size: List<String>
)
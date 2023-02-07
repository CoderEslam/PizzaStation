package com.doubleclick.pizzastation.android.model


data class MenuModel(
    val FB: String,
    val L: String,
    val M: String,
    val Slice: String,
    val XXL: String,
    val category: String,
    val half_L: String,
    val half_stuffed_crust_L: String,
    val id: Int,
    val image: String,
    val name: String,
    val quarter_XXL: String,
    val status: String,
    val stuffed_crust_L: String,
    val stuffed_crust_M: String,
) : java.io.Serializable
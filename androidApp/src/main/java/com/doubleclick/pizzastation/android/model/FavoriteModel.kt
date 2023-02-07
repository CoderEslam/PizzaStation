package com.doubleclick.pizzastation.android.model


data class FavoriteModel(
    val id: Int,
    val menu: MenuModel,
    val menu_id: Int,
    val user_id: Int
)
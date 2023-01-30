package com.doubleclick.pizzastation.android.model

data class OrderModelData(
    val amount: String,
    val area_id: Int,
    val branch_id: Int,
    val delivery: String,
    val id: Int = -1,
    val items: List<ItemOrder>,
    val notes: String,
    val status: String = "mobile_app",
    val total: String,
    val user_id: Int = -1
)
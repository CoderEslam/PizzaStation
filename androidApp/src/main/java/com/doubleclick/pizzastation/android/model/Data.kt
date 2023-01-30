package com.doubleclick.pizzastation.android.model

data class Data(
    val amount: String,
    val area_id: Int,
    val branch_id: Int,
    val delivery: String,
    val id: Int,
    val items: List<ItemOrder>,
    val notes: String,
    val status: String,
    val total: String,
    val user_id: Int
)
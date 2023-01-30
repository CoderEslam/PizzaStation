package com.doubleclick.pizzastation.android.model

data class OrderModel(
    val add_stuffed_crust: String,
    val amount: String,
    val area_id: Int,
    val branch_id: Int,
    val client_id: Int,
    val created_at: String,
    val delivery: String,
    val id: Int,
    val notes: String,
    val status: String,
    val total: String,
    val updated_at: String,
    val user_id: Int
)
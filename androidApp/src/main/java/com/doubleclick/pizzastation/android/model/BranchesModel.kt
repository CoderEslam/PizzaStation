package com.doubleclick.pizzastation.android.model

data class BranchesModel(
    val branch_name: String,
    val branch_number: String,
    val created_at: String,
    val google_map_location: String,
    val government_id: Int,
    val id: Int,
    val location: String,
    val updated_at: String,
    val user_id: Int
)
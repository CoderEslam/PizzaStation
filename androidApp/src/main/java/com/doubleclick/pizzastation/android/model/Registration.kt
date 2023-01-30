package com.doubleclick.pizzastation.android.model


/**
 * Created By Eslam Ghazy on 1/23/2023
 */
data class Registration(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

package com.doubleclick.pizzastation.android.model

data class LoginResponse(
    val device_token: String,
    val user: User
)
package com.doubleclick.pizzastation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
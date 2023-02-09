package com.doubleclick.pizzastation.android.model

data class SumExtra(val name: String, val price: Double) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SumExtra) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

package com.doubleclick.pizzastation.android.model

data class Extra(
    val name: String,
    val price: String,
    val image: String,
    val quantity: String,
    val size: String
) : java.io.Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Extra) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
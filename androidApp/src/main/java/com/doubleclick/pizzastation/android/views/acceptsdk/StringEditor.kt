package com.doubleclick.pizzastation.android.views.acceptsdk

internal object StringEditor {
    fun insertPeriodically(text: String, insert: String, period: Int): String {
        val builder = StringBuilder(text.length + insert.length * (text.length / period) + 1)
        var index = 0
        var prefix = ""
        while (index < text.length) {
            builder.append(prefix)
            prefix = insert
            builder.append(text.substring(index, Math.min(index + period, text.length)))
            index += period
        }
        return builder.toString()
    }

    fun monthString(month: Int): String? {
        return if (month in 1..12) {
            if (month < 10) "0$month" else month.toString()
        } else {
            null
        }
    }

    fun yearString(year: Int): String {
        return year.toString().substring(2, 4)
    }
}

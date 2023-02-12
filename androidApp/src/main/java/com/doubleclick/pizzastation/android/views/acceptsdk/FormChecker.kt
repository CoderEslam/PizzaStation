package com.doubleclick.pizzastation.android.views.acceptsdk

import java.text.SimpleDateFormat
import java.util.*

internal object FormChecker {
    fun checkCVV(cvvString: String?): Boolean {
        return cvvString != null && cvvString.length == 3
    }

    fun checkCardName(nameString: String?): Boolean {
        return nameString != null && nameString.isNotEmpty()
    }

    fun checkCardNumber(numberString: String?): Boolean {
        return numberString != null && numberString.length == 16
    }

    fun checkDate(monthString: String?, yearString: String?): Boolean {
        return if (monthString != null && monthString.length == 2 && yearString != null && yearString.length == 2) {
            val yearDiff = yearString.toInt() - SimpleDateFormat("yy", Locale.GERMANY)
                .format(Date()).toInt()
            val monthDiff = monthString.toInt() - SimpleDateFormat("MM", Locale.GERMANY)
                .format(Date()).toInt()
            yearDiff > 0 || yearDiff == 0 && monthDiff >= 0
        } else {
            false
        }
    }
}

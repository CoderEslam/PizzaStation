package com.doubleclick.pizzastation.android.views.acceptsdk

import android.text.InputFilter
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*

internal class ExpiryYearInputFilter : InputFilter {
    private val currentYearLastTwoDigits: String

    init {
        currentYearLastTwoDigits = SimpleDateFormat("yy", Locale.GERMANY).format(Date())
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        return if (source == "") {
            (if (dstart + 1 < dest.length) dest.subSequence(dstart, dend) else "")
        } else if (dest != null && dest.toString().length >= 2) {
            ""
        } else if (source.length > 1) {
            source
        } else if (dest != null && source.length > 1 && dstart != dest.length) {
            ""
        } else if (source == " ") {
            ""
        } else {
            val inputChar = source[0]
            if (dstart == 0) {
                val currYearFirstChar = currentYearLastTwoDigits[0]
                (if (inputChar < currYearFirstChar) "" else source)
            } else {
                if (dstart == 1) {
                    val inputYear = "" + dest[dest.length - 1] + source.toString()
                    if (inputYear.compareTo(currentYearLastTwoDigits) < 0) {
                        return ""
                    }
                }
                source
            }
        }
    }
}

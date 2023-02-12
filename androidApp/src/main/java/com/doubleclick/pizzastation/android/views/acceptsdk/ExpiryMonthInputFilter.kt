package com.doubleclick.pizzastation.android.views.acceptsdk

import android.text.InputFilter
import android.text.Spanned
import android.util.Log

internal class ExpiryMonthInputFilter : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        return if (source == "") {
            (if (dstart + 1 < dest!!.length) dest.subSequence(dstart, dend) else "")
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
                if (inputChar <= '1') {
                    return source
                }
                try {
                    return "0$inputChar"
                } catch (var9: Exception) {
                    Log.d("exception", "filter: " + var9.message)
                }
            } else if (dstart == 1) {
                val firstMonthChar = dest!![0]
                if (firstMonthChar == '0' && inputChar == '0') {
                    return ""
                }
                if (firstMonthChar == '1' && inputChar > '2') {
                    return ""
                }
            }
            source
        }
    }
}

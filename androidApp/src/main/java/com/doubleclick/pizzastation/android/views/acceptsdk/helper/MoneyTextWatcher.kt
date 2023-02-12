package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.text.NumberFormat

class MoneyTextWatcher(mEditText: EditText?) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText?>

    init {
        editTextWeakReference = WeakReference<EditText?>(mEditText)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val editTex = editTextWeakReference.get()
        if (s.toString() != editTex!!.text.toString()) {
            editTex.removeTextChangedListener(this)
            val cleanString = s.toString().replace("[$,.]".toRegex(), "")
            val parsed = cleanString.replace("[^\\d]".toRegex(), "").toDouble()
            var formatted = NumberFormat.getCurrencyInstance().format(parsed / 100.0)
            formatted = formatted.replace("$".toRegex(), "")
            formatted = formatted.substring(1)
            if (formatted.indexOf(".") == formatted.length - 2) {
                formatted = formatted + "0"
            }
            editTex.setText(formatted)
            editTex.setSelection(formatted.length)
            editTex.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(s: Editable) {}
}

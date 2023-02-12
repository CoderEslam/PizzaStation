package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import android.view.View
import android.widget.EditText

class MoneyClickListener(var editText: EditText) : View.OnClickListener {
    override fun onClick(v: View) {
        editText.setSelection(editText.text.length)
    }
}

package com.doubleclick.pizzastation.android.views.acceptsdk

import android.app.Activity
import android.content.Context
import android.widget.Toast

object ToastMaker {
    fun displayLongToast(activity: Activity, msg: String) {
        displayToast(activity.applicationContext, msg, 1)
    }

    fun displayShortToast(activity: Activity, msg: String) {
        displayToast(activity.applicationContext, msg, 0)
    }

    private fun displayToast(c: Context, msg: String, length: Int) {
        Toast.makeText(c, msg, length).show()
    }
}

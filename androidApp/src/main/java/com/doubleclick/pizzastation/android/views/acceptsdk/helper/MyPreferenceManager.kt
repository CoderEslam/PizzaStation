package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import android.content.Context
import android.content.SharedPreferences

class MyPreferenceManager(var _context: Context) {
    private val TAG = MyPreferenceManager::class.java.simpleName
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    init {
        pref = _context.getSharedPreferences("androidhive_gcm", PRIVATE_MODE)
        editor = pref.edit()
    }

    fun addNotification(notification: String) {
        var oldNotifications = notifications
        oldNotifications = if (oldNotifications != null) {
            "$oldNotifications|$notification"
        } else {
            notification
        }
        editor.putString("notifications", oldNotifications)
        editor.commit()
    }

    val notifications: String?
        get() = pref.getString("notifications", null as String?)

    fun clear() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "androidhive_gcm"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_NOTIFICATIONS = "notifications"
    }
}

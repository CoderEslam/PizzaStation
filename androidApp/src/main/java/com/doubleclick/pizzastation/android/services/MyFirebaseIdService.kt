package com.doubleclick.pizzastation.android.services

import com.google.firebase.messaging.FirebaseMessagingService

/**
 * Created By Eslam Ghazy on 1/1/2023
 */
class MyFirebaseIdService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

}
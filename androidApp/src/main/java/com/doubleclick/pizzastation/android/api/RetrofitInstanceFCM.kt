package com.doubleclick.pizzastation.android.api

import com.doubleclick.pizzastation.android.utils.Constants.FCM_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceFCM {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FCM_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FCMAPI by lazy {
        retrofit.create(FCMAPI::class.java)
    }

}
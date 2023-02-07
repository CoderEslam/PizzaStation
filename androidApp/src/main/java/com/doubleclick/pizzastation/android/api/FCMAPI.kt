package com.doubleclick.pizzastation.android.api

import com.doubleclick.pizzastation.android.fcm.model.MyResponse
import com.doubleclick.pizzastation.android.fcm.model.Sender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMAPI {


    @Headers(
        "Content-Type:application/json",
        "Authorization: key=AAAAIysnmL4:APA91bGKXVJ_VtVrKmiXIJwhvwDRWWLlYj6GFH_UM5NM48AqVSsLOF8S1UhN9wey6JiOLZa91Aar9rfElTgVIKKfHaCQfCErI_jNV2kCemiHdz1ivK4-m5K2LYO1sATiqV-8vuH37bw8"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): Call<MyResponse>


}
package com.doubleclick.pizzastation.android.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class SandImage(
    val image: MultipartBody.Part,
    val desc: RequestBody,
    val phone_number: String,
    val government_id: String
)
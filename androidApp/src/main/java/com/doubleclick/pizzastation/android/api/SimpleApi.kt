package com.doubleclick.pizzastation.android.api

import com.doubleclick.pizzastation.android.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface SimpleApi {

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body login: Login
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("register")
    fun createAccount(
        @Body registration: Registration
    ): Call<RegistrationResponse>

    @Headers("Content-Type: application/json")
    @GET("menu")
    fun getMenu(): Call<MenuList>

    @Headers("Content-Type: application/json")
    @GET("menu/{category}/filter")
    fun getMenuFilter(@Path("category") category: String): Call<MenuList>

    @Headers("Content-Type: application/json")
    @GET("menu/categories")
    fun getCategory(): Call<CategoryList>

    @Headers("Content-Type: application/json")
    @POST("menu/{item}/search")
    fun getSearchMenu(@Path("item") item: String): Call<MenuList>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("cart")
    fun setCart(
        @Header("Authorization") token: String,
        @Body cardModel: JsonObject
    ): Call<CartCallback>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<CartModelList>

    @Headers("Content-Type: application/json")
    @GET("offers")
    fun getOffers(): Call<OffersList>

    @Headers("Content-Type: application/json")
    @GET("menu/Extra/filter")
    fun getExtraFilters(): Call<MenuList>


}
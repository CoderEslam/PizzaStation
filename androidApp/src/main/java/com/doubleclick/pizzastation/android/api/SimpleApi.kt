package com.doubleclick.pizzastation.android.api

import com.doubleclick.pizzastation.android.model.*
import com.google.gson.JsonObject
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
    @POST("orders")
    fun setOrderComplete(
        @Header("Authorization") token: String,
        @Body orderModelList: JsonObject
    ): Call<OrderModelList>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("cart")
    fun setCart(
        @Header("Authorization") token: String,
        @Body cardModel: JsonObject
    ): Call<CartCallback>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("cart")
    fun getCart(@Header("Authorization") token: String): Call<CartModelList>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @DELETE("cart/{id}")
    fun deleteCartById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<MessageCallback>

    @Headers("Content-Type: application/json")
    @GET("offers")
    fun getOffers(): Call<OffersList>

    @Headers("Content-Type: application/json")
    @GET("menu/Extra/filter")
    fun getExtraFilters(): Call<MenuList>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("favorite")
    fun setFavorite(
        @Header("Authorization") token: String,
        @Body menu_id: String
    ): Call<MessageCallback>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("favorite")
    fun getFavorite(@Header("Authorization") token: String): Call<FavoriteModelList>

}
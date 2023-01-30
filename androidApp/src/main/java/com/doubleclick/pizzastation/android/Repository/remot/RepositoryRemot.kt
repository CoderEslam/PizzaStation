package com.doubleclick.pizzastation.android.Repository.remot

import com.doubleclick.pizzastation.android.api.RetrofitInstance
import com.doubleclick.pizzastation.android.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call

class RepositoryRemot {

    fun loginAccount(login: Login): Call<LoginResponse> {
        return RetrofitInstance.api.login(login)
    }

    fun createAccount(registration: Registration): Call<RegistrationResponse> {
        return RetrofitInstance.api.createAccount(registration)
    }

    fun getMenu(): Call<MenuList> {
        return RetrofitInstance.api.getMenu()
    }

    fun getMenuFilter(category: String): Call<MenuList> {
        return RetrofitInstance.api.getMenuFilter(category = category)
    }

    fun getCategory(): Call<CategoryList> {
        return RetrofitInstance.api.getCategory()
    }

    fun getSearchMenu(item: String): Call<MenuList> {
        return RetrofitInstance.api.getSearchMenu(item)
    }

    fun getOffers(): Call<OffersList> {
        return RetrofitInstance.api.getOffers()
    }

    fun getCart(token: String): Call<CartModelList> {
        return RetrofitInstance.api.getCart(token)
    }
    fun setOrder(token: String): Call<OrderModelList> {
        return RetrofitInstance.api.getCart(token)
    }

    fun setCart(token: String, cardModel: JsonObject): Call<CartCallback> {
        return RetrofitInstance.api.setCart(token, cardModel)
    }

    fun deleteCartById(token: String, id: String): Call<CardDeleteCallbackById> {
        return RetrofitInstance.api.deleteCartById(token, id)
    }

    fun getExtraFilters(): Call<MenuList> {
        return RetrofitInstance.api.getExtraFilters()
    }

}
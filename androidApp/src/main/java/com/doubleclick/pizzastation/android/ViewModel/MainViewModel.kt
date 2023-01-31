package com.doubleclick.pizzastation.android.ViewModel

import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.api.RetrofitInstance
import com.doubleclick.pizzastation.android.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call

class MainViewModel(private val repository: RepositoryRemot) : ViewModel() {

    private val loginResponseMutableLiveData: MutableLiveData<Call<LoginResponse>> =
        MutableLiveData()
    private val registerResponseMutableLiveData: MutableLiveData<Call<RegistrationResponse>> =
        MutableLiveData()
    private val menuMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val categoryMutableLiveData: MutableLiveData<Call<CategoryList>> = MutableLiveData()
    private val menuSearchMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val offersMutableLiveData: MutableLiveData<Call<OffersList>> = MutableLiveData()
    private val extrasFilterMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val cardGetMutableLiveData: MutableLiveData<Call<CartModelList>> = MutableLiveData()
    private val cardSetMutableLiveData: MutableLiveData<Call<CartCallback>> = MutableLiveData()
    private val orderSetMutableLiveData: MutableLiveData<Call<OrderModelList>> = MutableLiveData()
    private val cardDeleteByIdMutableLiveData: MutableLiveData<Call<CardDeleteCallbackById>> =
        MutableLiveData()


    fun getLoginResponse(login: Login): LiveData<Call<LoginResponse>> {
        loginResponseMutableLiveData.value = repository.loginAccount(login = login);
        return loginResponseMutableLiveData;
    }

    fun getRegisterResponse(registration: Registration): LiveData<Call<RegistrationResponse>> {
        registerResponseMutableLiveData.value =
            repository.createAccount(registration = registration);
        return registerResponseMutableLiveData;
    }

    fun getMenu(): LiveData<Call<MenuList>> {
        menuMutableLiveData.value = repository.getMenu();
        return menuMutableLiveData;
    }

    fun getMenuFilter(category: String): LiveData<Call<MenuList>> {
        menuMutableLiveData.value = repository.getMenuFilter(category);
        return menuMutableLiveData;
    }

    fun getCategory(): LiveData<Call<CategoryList>> {
        categoryMutableLiveData.value = repository.getCategory();
        return categoryMutableLiveData;
    }

    fun getSearchMenu(item: String): LiveData<Call<MenuList>> {
        menuSearchMutableLiveData.value = repository.getSearchMenu(item);
        return menuSearchMutableLiveData;
    }

    fun getOffers(): LiveData<Call<OffersList>> {
        offersMutableLiveData.value = repository.getOffers();
        return offersMutableLiveData;
    }

    fun getCart(token: String): LiveData<Call<CartModelList>> {
        cardGetMutableLiveData.value = repository.getCart(token);
        return cardGetMutableLiveData;
    }


    fun setOrderComplete(token: String, orderModelList: JsonObject): LiveData<Call<OrderModelList>> {
        orderSetMutableLiveData.value = repository.setOrderComplete(token, orderModelList);
        return orderSetMutableLiveData;
    }

    fun setCart(token: String, cardModel: JsonObject): LiveData<Call<CartCallback>> {
        cardSetMutableLiveData.value = repository.setCart(token, cardModel);
        return cardSetMutableLiveData;
    }

    fun deleteCartById(
        token: String,
        id: String
    ): LiveData<Call<CardDeleteCallbackById>> {
        cardDeleteByIdMutableLiveData.value = repository.deleteCartById(token, id);
        return cardDeleteByIdMutableLiveData;
    }

    fun getExtraFilters(): LiveData<Call<MenuList>> {
        extrasFilterMutableLiveData.value = repository.getExtraFilters();
        return extrasFilterMutableLiveData;
    }


}
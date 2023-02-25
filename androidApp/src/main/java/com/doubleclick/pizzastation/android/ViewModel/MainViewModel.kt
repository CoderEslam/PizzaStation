package com.doubleclick.pizzastation.android.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.api.RetrofitInstance
import com.doubleclick.pizzastation.android.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class MainViewModel(private val repository: RepositoryRemot) : ViewModel() {

    private val loginResponseMutableLiveData: MutableLiveData<Call<LoginResponse>> =
        MutableLiveData()
    private val registerResponseMutableLiveData: MutableLiveData<Call<RegistrationResponse>> =
        MutableLiveData()
    private val menuMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val CustomMenuMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val categoryMutableLiveData: MutableLiveData<Call<CategoryList>> = MutableLiveData()
    private val menuSearchMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val offersMutableLiveData: MutableLiveData<Call<OffersList>> = MutableLiveData()
    private val extrasFilterMutableLiveData: MutableLiveData<Call<MenuList>> = MutableLiveData()
    private val cardGetMutableLiveData: MutableLiveData<Call<CartModelList>> = MutableLiveData()
    private val cardSetMutableLiveData: MutableLiveData<Call<CartCallback>> = MutableLiveData()
    private val orderSetMutableLiveData: MutableLiveData<Call<OrderModelList>> = MutableLiveData()
    private val cardDeleteByIdMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val setFavoriteMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val deleteFavoriteMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()

    private val updateCardMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val uploadImageMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val editPhoneMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val editGovernmentMutableLiveData: MutableLiveData<Call<MessageCallback>> =
        MutableLiveData()
    private val getFavoriteMutableLiveData: MutableLiveData<Call<FavoriteModelList>> =
        MutableLiveData()
    private val getGovernorateMutableLiveData: MutableLiveData<Call<GovernorateList>> =
        MutableLiveData()
    private val getBranchesMutableLiveData: MutableLiveData<Call<BranchesList>> =
        MutableLiveData()
    private val getImageResponseCallbackMutableLiveData: MutableLiveData<Call<ImageResponseCallback>> =
        MutableLiveData()
    private val getPizzaInOfferCallbackMutableLiveData: MutableLiveData<Call<MenuList>> =
        MutableLiveData()
    private val getURLPayCallbackMutableLiveData: MutableLiveData<Call<URLPayment>> =
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

    fun getCustomMenu(): LiveData<Call<MenuList>> {
        CustomMenuMutableLiveData.value = repository.getCustomMenu();
        return CustomMenuMutableLiveData;
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


    fun setOrderComplete(
        token: String,
        orderModelList: JsonObject
    ): LiveData<Call<OrderModelList>> {
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
    ): LiveData<Call<MessageCallback>> {
        cardDeleteByIdMutableLiveData.value = repository.deleteCartById(token, id);
        return cardDeleteByIdMutableLiveData;
    }

    fun getExtraFilters(): LiveData<Call<MenuList>> {
        extrasFilterMutableLiveData.value = repository.getExtraFilters();
        return extrasFilterMutableLiveData;
    }


    fun setFavorite(token: String, menu_id: MenuId): LiveData<Call<MessageCallback>> {
        setFavoriteMutableLiveData.value = repository.setFavorite(token, menu_id);
        return setFavoriteMutableLiveData;
    }

    fun deleteFavorite(token: String, id: String): LiveData<Call<MessageCallback>> {
        deleteFavoriteMutableLiveData.value = repository.deleteFavorite(token, id);
        return deleteFavoriteMutableLiveData;
    }

    fun getFavorite(token: String): LiveData<Call<FavoriteModelList>> {
        getFavoriteMutableLiveData.value = repository.getFavorite(token);
        return getFavoriteMutableLiveData;
    }

    fun updateCart(
        token: String,
        id: String,
        jsonObject: JsonObject
    ): LiveData<Call<MessageCallback>> {
        updateCardMutableLiveData.value = repository.updateCart(token, id, jsonObject);
        return updateCardMutableLiveData;
    }

    fun getGovernorate(): LiveData<Call<GovernorateList>> {
        getGovernorateMutableLiveData.value = repository.getGovernorate();
        return getGovernorateMutableLiveData;
    }

    fun getBranches(): LiveData<Call<BranchesList>> {
        getBranchesMutableLiveData.value = repository.getBranches();
        return getBranchesMutableLiveData;
    }

    fun uploadImage(
        token: String,
        id: String,
        image: MultipartBody.Part
    ): LiveData<Call<MessageCallback>> {
        uploadImageMutableLiveData.value = repository.uploadImage(token, id, image);
        return uploadImageMutableLiveData;
    }

    fun editPhone(
        token: String,
        id: String,
        phone_number: PhoneNumber
    ): LiveData<Call<MessageCallback>> {
        editPhoneMutableLiveData.value = repository.editPhone(token, id, phone_number);
        return editPhoneMutableLiveData;
    }

    fun editGovernment(
        token: String,
        id: String,
        government_id: GovernmentId
    ): LiveData<Call<MessageCallback>> {
        editGovernmentMutableLiveData.value = repository.editGovernment(token, id, government_id);
        return editGovernmentMutableLiveData;
    }

    fun getImageResponseModel(token: String): LiveData<Call<ImageResponseCallback>> {
        getImageResponseCallbackMutableLiveData.value = repository.getImageResponseModel(token);
        return getImageResponseCallbackMutableLiveData;
    }

    fun getPizzaInOffer(): LiveData<Call<MenuList>> {
        getPizzaInOfferCallbackMutableLiveData.value = repository.getPizzaInOffer();
        return getPizzaInOfferCallbackMutableLiveData;
    }

    fun getURLPay(amount: AmountPayment, token: String): LiveData<Call<URLPayment>> {
        getURLPayCallbackMutableLiveData.value = repository.getURLPay(amount, token);
        return getURLPayCallbackMutableLiveData;
    }


}
package com.doubleclick.pizzastation.android.Repository.local

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * Created By Eslam Ghazy on 12/16/2022
 */
class ViewModelDatabase(application: Application) : AndroidViewModel(application) {

    //https://stackoverflow.com/questions/65035103/sqldelight-database-schema-not-generated
    private var mutableLiveData: MutableLiveData<List<Unit>> = MutableLiveData()


    private fun getQuery() {
        viewModelScope.launch {

        }
    }

    fun getList(): LiveData<List<Unit>?> {
        return mutableLiveData;
    }


    init {
        getQuery();
    }

}
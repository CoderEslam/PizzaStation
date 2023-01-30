package com.doubleclick.pizzastation.android.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Created By Eslam Ghazy on 1/21/2023
 */
object DataStore {
    /*
    *  dataStore -> extension function
    * */
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session_manager")

}
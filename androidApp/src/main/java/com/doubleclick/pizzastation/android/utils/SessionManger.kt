package com.doubleclick.pizzastation.android.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.doubleclick.pizzastation.android.utils.Constants.EMAIL_KEY
import com.doubleclick.pizzastation.android.utils.Constants.ID_KEY
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_KEY
import com.doubleclick.pizzastation.android.utils.Constants.NAME_KEY
import com.doubleclick.pizzastation.android.utils.Constants.PASSWORD_KEY
import com.doubleclick.pizzastation.android.utils.Constants.TOKEN_KEY
import com.doubleclick.pizzastation.android.utils.DataStore.dataStore
import kotlinx.coroutines.flow.first

/**
 * Created By Eslam Ghazy on 1/21/2023
 */
object SessionManger {
    private val TAG = "SessionManger"

    suspend fun updateSession(
        context: Context,
        token: String,
        id: String,
        password: String,
        email: String,
        name: String,
    ) {
        val TokenKey = stringPreferencesKey(TOKEN_KEY)
        val passwordKey = stringPreferencesKey(PASSWORD_KEY)
        val emailKey = stringPreferencesKey(EMAIL_KEY)
        val nameKey = stringPreferencesKey(NAME_KEY)
        val idKey = stringPreferencesKey(ID_KEY)
        /*
        * to save name , email , token in local phone by preferences
        * */

        /*updateSession: token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJOb3RlQXV0aCIsImlzcyI6Im5vdGVTZXJ2ZXIiLCJlbWFpbCI6ImVzbGFtZ2hhenk1NTVAZ21haWwuY29tIn0.pcy21quPrOSmzJ_RcbdP55e_gaTGANgH8SeZBt7kRlO4wO3zPP5GBRp671LcugASvzUbzu6eQrNuflRfva1jWA name: Eslam Ghazy email: eslamghazy555@gmail.com*/
        Log.e(
            TAG,
            "updateSession: " + "token: " + token + " id: " + id + " password: " + password
        );
        context.dataStore.edit { preferences ->
            preferences[TokenKey] = token
            preferences[passwordKey] = password
            preferences[idKey] = id
            preferences[emailKey] = email
            preferences[nameKey] = name
        }


    }


    /*
    * to get token stored in preferences by (key name)
    * */
    suspend fun getToken(context: Context): String? {
        val TokenKey = stringPreferencesKey(TOKEN_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[TokenKey]
    }

    suspend fun getName(context: Context): String? {
        val nameKey = stringPreferencesKey(NAME_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[nameKey]
    }

    suspend fun getImage(context: Context): String? {
        val imageKey = stringPreferencesKey(IMAGE_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[imageKey]
    }

    suspend fun setImage(context: Context, image: String) {
        val imageKey = stringPreferencesKey(IMAGE_KEY)
        context.dataStore.edit { preferences ->
            preferences[imageKey] = image
        }
    }


    /*
    * to get password stored in preferences by (key name)
    * */
    suspend fun getCurrentPassword(context: Context): String? {
        val passwordKey = stringPreferencesKey(PASSWORD_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[passwordKey]
    }

    /*
    * to get id stored in preferences by (key name)
    * */
    suspend fun getCurrentUserId(context: Context): String? {
        val idKey = stringPreferencesKey(ID_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[idKey]
    }

    suspend fun getCurrentEmail(context: Context): String? {
        val emailKay = stringPreferencesKey(EMAIL_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[emailKay]
    }

    /*
    * delete all data by logout
    * */
    suspend fun logout(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }
}
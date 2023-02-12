package com.doubleclick.pizzastation.android.views.acceptsdk

import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION
import java.util.*

class LocaleManager(context: Context?) {
    fun setLocale(c: Context): Context {
        return c
    }

    fun setNewLocale(c: Context, language: String, country: String): Context {
        return updateResources(c, language, country)
    }

    private fun updateResources(context: Context, language: String, country: String): Context {
        var context = context
        val locale = Locale(language, country)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        if (isAtLeastVersion(17)) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

    fun getLocale(context: Context): Locale {
        val config = context.resources.configuration
        return if (isAtLeastVersion(24)) config.locales[0] else config.locale
    }

    companion object {
        const val LANGUAGE_ENGLISH = "en"
        const val LANGUAGE_ENGLISH_US = "US"
        fun isAtLeastVersion(version: Int): Boolean {
            return VERSION.SDK_INT >= version
        }
    }
}

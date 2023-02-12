package com.doubleclick.pizzastation.android.views.acceptsdk

import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

internal object QueryParamsExtractor {
    fun getQueryParams3(url: String): Map<String?, String?>? {
        return try {
            val params: MutableMap<String?, String?> = HashMap<String?, String?>()
            val urlParts = url.split("\\?".toRegex()).toTypedArray()
            if (urlParts.size > 1) {
                val query = urlParts[1]
                val var4 = query.split("&".toRegex()).toTypedArray()
                val var5 = var4.size
                for (var6 in 0 until var5) {
                    val param = var4[var6]
                    val pair = param.split("=".toRegex()).toTypedArray()
                    val key = URLDecoder.decode(pair[0], "UTF-8")
                    var value: String? = ""
                    if (pair.size > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8")
                        params[key] = value
                    }
                }
            }
            params
        } catch (var11: UnsupportedEncodingException) {
            null
        }
    }

    fun getQueryParams(url: String): String? {
        return try {
            val JSON = JSONObject()
            HashMap<Any?, Any?>()
            val urlParts = url.split("\\?".toRegex()).toTypedArray()
            if (urlParts.size > 1) {
                val query = urlParts[1]
                val var5 = query.split("&".toRegex()).toTypedArray()
                val var6 = var5.size
                for (var7 in 0 until var6) {
                    val param = var5[var7]
                    val pair = param.split("=".toRegex()).toTypedArray()
                    val key = URLDecoder.decode(pair[0], "UTF-8")
                    var value: String? = ""
                    if (pair.size > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8")
                        JSON.put(key, value)
                    }
                }
            }
            JSON.toString()
        } catch (var12: Exception) {
            null
        }
    }
}

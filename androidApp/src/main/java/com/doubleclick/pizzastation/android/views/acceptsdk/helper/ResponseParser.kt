package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ResponseParser(response: String?) {
    var returnedMap: HashMap<String?, String?>
    var responseObject: JSONObject? = null

    init {
        try {
            responseObject = JSONObject(response)
        } catch (var3: JSONException) {
            var3.printStackTrace()
        }
        returnedMap = HashMap<String?, String?>()
    }

    val isStatusOK: Boolean
        get() {
            var status = ""
            try {
                status = responseObject!!.getString("status")
            } catch (var3: JSONException) {
                var3.printStackTrace()
            }
            return status.compareTo("OK") == 0
        }
    val details: HashMap<String?, String?>
        get() {
            var detailsObject: JSONObject? = null
            var detailsArray: JSONArray? = null
            try {
                detailsArray = responseObject!!.getJSONArray("details")
                detailsObject = detailsArray.getJSONObject(0)
                val objectIterator: Iterator<*> = detailsObject.keys()
                while (objectIterator.hasNext()) {
                    val nextKey = objectIterator.next() as String
                    returnedMap[nextKey] = detailsObject.getString(nextKey)
                }
            } catch (var5: JSONException) {
                var5.printStackTrace()
            }
            return returnedMap
        }
    val failureMessage: String?
        get() {
            val responseMap = details
            return if (responseMap.containsKey("failure_message")) responseMap["failure_message"] else null
        }
}

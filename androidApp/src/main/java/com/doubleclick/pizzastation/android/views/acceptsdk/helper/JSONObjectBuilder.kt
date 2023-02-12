package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import android.content.Context
import com.doubleclick.pizzastation.android.R
import org.json.JSONException
import org.json.JSONObject

class JSONObjectBuilder(context: Context) {
    private val app_id: String?
    var returnedJSON: JSONObject

    init {
        val sharedPref = context.getSharedPreferences(context.getString(R.string.pref_file_name), 0)
        app_id = sharedPref.getString(context.getString(R.string.app_id), "")
        returnedJSON = JSONObject()
        returnedJSON.put("APP_ID", app_id)
        returnedJSON.put("LOGIN", "test")
        returnedJSON.put("PASSWORD", "test")
        returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
        returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
    }

    fun buildREFUNDJSONObject(
        msisdn: String?,
        pin: String?,
        language: String?,
        msisdn2: String?,
        amount: String?,
        CID: String?
    ): JSONObject {
        try {
            returnedJSON.put("TYPE", "PREREQ")
            returnedJSON.put("MSISDN", msisdn)
            returnedJSON.put("PIN", pin)
            returnedJSON.put("LANGUAGE1", language)
            returnedJSON.put("MSISDN2", msisdn2)
            returnedJSON.put("AMOUNT", amount)
            returnedJSON.put("CID", CID)
        } catch (var8: JSONException) {
            var8.printStackTrace()
        }
        return returnedJSON
    }

    fun buildPurchaseJSONObject(
        msisdn: String?,
        pin: String?,
        language: String?,
        mercode: String?,
        amount: String?,
        orderno: String?,
        CID: String?
    ): JSONObject {
        try {
            returnedJSON.put("TYPE", "CMPREQ")
            returnedJSON.put("MSISDN", msisdn)
            returnedJSON.put("PIN", pin)
            returnedJSON.put("LANGUAGE1", language)
            returnedJSON.put("MERCODE", mercode)
            returnedJSON.put("AMOUNT", amount)
            returnedJSON.put("ORDERNO", orderno)
            returnedJSON.put("CID", CID)
        } catch (var9: JSONException) {
            var9.printStackTrace()
        }
        return returnedJSON
    }

    fun buildCashinJSONObject(
        msisdn: String?,
        pin: String?,
        language: String?,
        msisdn2: String?,
        amount: String?,
        CID: String?
    ): JSONObject {
        try {
            returnedJSON.put("TYPE", "RCIREQ")
            returnedJSON.put("MSISDN", msisdn)
            returnedJSON.put("PIN", pin)
            returnedJSON.put("LANGUAGE1", language)
            returnedJSON.put("AMOUNT", amount)
            returnedJSON.put("MSISDN2", msisdn2)
            returnedJSON.put("CID", CID)
        } catch (var8: JSONException) {
            var8.printStackTrace()
        }
        return returnedJSON
    }

    fun buildBalanceInquiryJSONObject(
        msisdn: String?,
        pin: String?,
        language: String?
    ): JSONObject {
        try {
            returnedJSON.put("TYPE", "CBEREQ")
            returnedJSON.put("MSISDN", msisdn)
            returnedJSON.put("PIN", pin)
            returnedJSON.put("LANGUAGE1", language)
        } catch (var5: JSONException) {
            var5.printStackTrace()
        }
        return returnedJSON
    }

    fun buildUserJSONObject(username: String?, password: String?): JSONObject {
        try {
            returnedJSON.put("TYPE", "MPOSINQREQ")
            returnedJSON.put("MSISDN", username)
            returnedJSON.put("PIN", password)
        } catch (var4: JSONException) {
            var4.printStackTrace()
        }
        return returnedJSON
    }

    fun buildRSAKeyJSONObject(androidID: String?, key: String?): JSONObject {
        try {
            returnedJSON.put("TYPE", "MPOSKEYREQ")
            returnedJSON.put("LOGIN", "test")
            returnedJSON.put("PASSWORD", "test")
            returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
            returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
            returnedJSON.put("LANGUAGE1", "2")
            returnedJSON.put("SNO", androidID)
            returnedJSON.put("PUB", key)
        } catch (var4: JSONException) {
            var4.printStackTrace()
        }
        return returnedJSON
    }

    fun buildAESKeyJSONObject(androidID: String?, key: String?): JSONObject {
        try {
            returnedJSON.put("TYPE", "MPOSKEYREQ")
            returnedJSON.put("LOGIN", "test")
            returnedJSON.put("PASSWORD", "test")
            returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
            returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
            returnedJSON.put("LANGUAGE1", "2")
            returnedJSON.put("SNO", androidID)
            returnedJSON.put("KEY", key)
        } catch (var4: JSONException) {
            var4.printStackTrace()
        }
        return returnedJSON
    }

    fun buildHistoryJSONObject(username: String?): JSONObject {
        try {
            returnedJSON.put("TYPE", "CLTREQ")
            returnedJSON.put("MSISDN", username)
            returnedJSON.put("PIN", "")
            returnedJSON.put("LOGIN", "test")
            returnedJSON.put("PASSWORD", "test")
            returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
            returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
            returnedJSON.put("LANGUAGE1", "2")
            returnedJSON.put("CID", "")
        } catch (var3: JSONException) {
            var3.printStackTrace()
        }
        return returnedJSON
    }

    fun buildShiftJSONObject(username: String?, password: String?, Date: String?): JSONObject {
        try {
            returnedJSON.put("TYPE", "MPOSINQREQ")
            returnedJSON.put("MSISDN", username)
            returnedJSON.put("PIN", password)
            returnedJSON.put("TIME", Date)
        } catch (var5: JSONException) {
            var5.printStackTrace()
        }
        return returnedJSON
    }

    fun buildTxnJSONObject(msi: String?, trx_id: String?): JSONObject {
        try {
            returnedJSON.put("LOGIN", "test")
            returnedJSON.put("PASSWORD", "test")
            returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
            returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
            returnedJSON.put("TYPE", "CLTXNRREQ")
            returnedJSON.put("MSISDN", msi)
            returnedJSON.put("CID", "")
            returnedJSON.put("TXNREFID", trx_id)
        } catch (var4: JSONException) {
            var4.printStackTrace()
        }
        return returnedJSON
    }

    fun buildreverseJSONObject(
        msi: String?,
        msi2: String?,
        cid: String?,
        pin: String?,
        trx_id: String?
    ): JSONObject {
        try {
            returnedJSON.put("LOGIN", "test")
            returnedJSON.put("PASSWORD", "test")
            returnedJSON.put("REQUEST_GATEWAY_CODE", "W")
            returnedJSON.put("REQUEST_GATEWAY_TYPE", "W")
            returnedJSON.put("TYPE", "PREVREQ")
            returnedJSON.put("MSISDN", msi)
            returnedJSON.put("MSISDN2", msi2)
            returnedJSON.put("PIN", pin)
            returnedJSON.put("CID", cid)
            returnedJSON.put("PurchID", trx_id)
        } catch (var7: JSONException) {
            var7.printStackTrace()
        }
        return returnedJSON
    }

    companion object {
        private const val login = "test"
        private const val password = "test"
        private const val requestGatewayCode = "W"
        private const val requestGatewayType = "W"
    }
}

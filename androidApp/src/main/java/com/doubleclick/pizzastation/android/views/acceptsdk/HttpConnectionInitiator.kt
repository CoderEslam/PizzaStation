package com.doubleclick.pizzastation.android.views.acceptsdk

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.net.HttpURLConnection
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils


internal class HttpConnectionInitiator(var context: Context) :
    AsyncTask<String?, Void?, String?>() {
    var http: HttpURLConnection? = null
    var delegate: AsyncResponse? = null
    var apiName: String? = null
    var status_code: String? = null
    protected override fun doInBackground(vararg params: String?): String? {
        apiName = params[0]
        var link: String? = null
        var apiCallResult: String? = null
        val var4 = apiName
        var var5: Int = -1
        when (var4.hashCode()) {
            -1303360142 -> if (var4 == "USER_PAYMENT") {
                var5 = 0
            }
            97030280 -> if (var4 == "CARD_TOKENIZER") {
                var5 = 1
            }
        }
        when (var5) {
            0 -> link = "https://accept.paymobsolutions.com/api/acceptance/payments/pay"
            1 -> link =
                "https://accept.paymobsolutions.com/api/acceptance/tokenization?payment_token=" + params[2]
        }
        try {
            val httpPost = HttpPost(link)
            httpPost.setEntity(StringEntity(params[1]))
            httpPost.setHeader("Accept", "application/json")
            httpPost.setHeader("Content-type", "application/json")
            val httpResponse: HttpResponse = DefaultHttpClient().execute(httpPost)
            Log.d("notice", "the link is $link")
            status_code = httpResponse.statusLine.statusCode.toString()
            apiCallResult = EntityUtils.toString(httpResponse.entity)
        } catch (var6: Exception) {
            Log.e(HttpConnectionInitiator::class.java.name, var6.message!!)
        }
        return apiCallResult
    }

    override fun onPostExecute(apiCallResult: String?) {
        super.onPostExecute(apiCallResult)
        delegate?.processFinish(apiName, apiCallResult, status_code)
    }
}

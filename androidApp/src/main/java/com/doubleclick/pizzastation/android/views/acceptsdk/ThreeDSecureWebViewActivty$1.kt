package com.doubleclick.pizzastation.android.views.acceptsdk

import android.content.Intent
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.doubleclick.pizzastation.android.views.acceptsdk.QueryParamsExtractor.getQueryParams


/*
internal class ThreeDSecureWebViewActivty1(this0: ThreeDSecureWebViewActivty) :
    WebViewClient() {
    init {
        this.this0 = this0
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.d("Accept - NEW URL: ", url)
        return if (url.startsWith("https://accept.paymobsolutions.com/api/acceptance/post_pay")) {
            Log.d("Accept - SUCCESS_URL", url)
            val intent = Intent()
            try {
                intent.putExtra("raw_pay_response", getQueryParams(url))
                this.this0.setResult(17, intent)
            } catch (var5: Exception) {
            }
            this.this0.finish()
            true
        } else {
            false
        }
    }
}
*/

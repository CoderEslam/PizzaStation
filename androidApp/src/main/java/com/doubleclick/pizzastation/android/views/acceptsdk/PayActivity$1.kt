package com.doubleclick.pizzastation.android.views.acceptsdk

import android.util.Log;
import com.android.volley.Response.Listener;
import org.json.JSONObject;


/*
internal class `PayActivity$1`(`this$0`: PayActivity) :
    Listener<String?> {
    init {
        `this$0` = `this$0`
    }

    override fun onResponse(response: String?) {
        PayActivity.`access$000`(this.`this$0`)
        try {
            val jsonResult = JSONObject(response)
            Log.d("notice", "json output: $jsonResult")
            val direct3dSecure = jsonResult.getString("is_3d_secure")
            if (direct3dSecure != null) {
                this.`this$0`.payDict = jsonResult
                if (direct3dSecure == "true") {
                    val redirectionURL = jsonResult.getString("redirection_url")
                    if (redirectionURL != null) {
                        PayActivity.`access$100`(this.`this$0`, redirectionURL)
                    } else {
                        PayActivity.`access$000`(this.`this$0`)
                        PayActivity.`access$200`(
                            this.`this$0`,
                            "An error occured while reading the 3dsecure redirection URL"
                        )
                    }
                } else {
                    PayActivity.`access$300`(this.`this$0`)
                }
            } else {
                PayActivity.`access$000`(this.`this$0`)
                PayActivity.`access$200`(
                    this.`this$0`,
                    "An error occured while checking if the card is 3d secure"
                )
            }
        } catch (var5: Exception) {
            PayActivity.`access$000`(this.`this$0`)
            Log.d("notice", "exception caught " + var5.message)
            PayActivity.`access$200`(this.`this$0`, var5.message)
        }
    }
}
*/

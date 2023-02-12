package com.doubleclick.pizzastation.android.views.acceptsdk.helper

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class StringPOSTRequest(
    url: String?,
    postContents: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener?
) :
    StringRequest(1, url, listener, errorListener) {
    private val postContents: String

    init {
        setShouldCache(false)
        this.postContents = postContents
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        return postContents.toByteArray()
    }

    override fun getBodyContentType(): String {
        return "application/json"
    }
}

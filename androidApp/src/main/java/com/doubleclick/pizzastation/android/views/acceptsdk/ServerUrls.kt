package com.doubleclick.pizzastation.android.views.acceptsdk

internal object ServerUrls {
    private const val PAYMOB_SERVER_IP = "https://accept.paymobsolutions.com/api/acceptance/"
    const val API_URL_USER_PAYMENT =
        "https://accept.paymobsolutions.com/api/acceptance/payments/pay"
    const val API_URL_TOKENIZE_CARD =
        "https://accept.paymobsolutions.com/api/acceptance/tokenization?payment_token="
    const val API_NAME_USER_PAYMENT = "USER_PAYMENT"
    const val API_NAME_TOKENIZE_CARD = "CARD_TOKENIZER"
    const val PAYMOB_SUCCESS_URL = "https://accept.paymobsolutions.com/api/acceptance/post_pay"
}

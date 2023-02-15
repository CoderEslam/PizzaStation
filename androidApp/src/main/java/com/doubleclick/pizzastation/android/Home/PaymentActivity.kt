package com.doubleclick.pizzastation.android.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pizzastation.android.MainActivity
import com.doubleclick.pizzastation.android.R
import com.google.gson.Gson
import com.myfatoorah.sdk.entity.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.entity.executepayment_cardinfo.MFCardInfo
import com.myfatoorah.sdk.entity.executepayment_cardinfo.MFDirectPaymentResponse
import com.myfatoorah.sdk.entity.initiatepayment.MFInitiatePaymentRequest
import com.myfatoorah.sdk.entity.initiatepayment.MFInitiatePaymentResponse
import com.myfatoorah.sdk.entity.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.enums.MFAPILanguage
import com.myfatoorah.sdk.enums.MFCurrencyISO
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
import com.paymob.acceptsdk.*


class PaymentActivity : AppCompatActivity() {

    private val TAG = "PaymentActivity"
    val ACCEPT_PAYMENT_REQUEST = 10

    // Replace this with your actual payment key
//    val paymentKey =
//        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMk5Ea3dPREUzTkRNc0ltOXlaR1Z5WDJsa0lqbzBNREkxTXpneE5pd2lZM1Z5Y21WdVkza2lPaUpGUjFBaUxDSnNiMk5yWDI5eVpHVnlYM2RvWlc1ZmNHRnBaQ0k2Wm1Gc2MyVXNJbUpwYkd4cGJtZGZaR0YwWVNJNmV5Sm1hWEp6ZEY5dVlXMWxJam9pUTJ4cFptWnZjbVFpTENKc1lYTjBYMjVoYldVaU9pSk9hV052YkdGeklpd2ljM1J5WldWMElqb2lSWFJvWVc0Z1RHRnVaQ0lzSW1KMWFXeGthVzVuSWpvaU9EQXlPQ0lzSW1ac2IyOXlJam9pTkRJaUxDSmhjR0Z5ZEcxbGJuUWlPaUk0TURNaUxDSmphWFI1SWpvaVNtRnphMjlzYzJ0cFluVnlaMmdpTENKemRHRjBaU0k2SWxWMFlXZ2lMQ0pqYjNWdWRISjVJam9pUTFJaUxDSmxiV0ZwYkNJNkltTnNZWFZrWlhSMFpUQTVRR1Y0WVM1amIyMGlMQ0p3YUc5dVpWOXVkVzFpWlhJaU9pSXJPRFlvT0NrNU1UTTFNakV3TkRnM0lpd2ljRzl6ZEdGc1gyTnZaR1VpT2lJd01UZzVPQ0lzSW1WNGRISmhYMlJsYzJOeWFYQjBhVzl1SWpvaVRrRWlmU3dpZFhObGNsOXBaQ0k2TVRJNU16WXNJbUZ0YjNWdWRGOWpaVzUwY3lJNk1UQXdMQ0p3Yld0ZmFYQWlPaUl4T1RZdU1UVXpMak0wTGpFNU5DSXNJbWx1ZEdWbmNtRjBhVzl1WDJsa0lqb3hPRFk0TlgwLkFzazlYa0U0a1c5VnBOa0NuR1BZekpWaGc4NTFfRjg2a3JabzMyU05ael8xSGlNNVZ6RVBBVC1ScjNjOUs1bHlHNXpsczVPQjhTeUxiVWZPWGNtNjRR"
//    val paymentKey =
//        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SndjbTltYVd4bFgzQnJJam8yT1RNeE9ESXNJbU5zWVhOeklqb2lUV1Z5WTJoaGJuUWlMQ0p1WVcxbElqb2lNVFkzTmpJM09UYzBNeTQ0TnpBeU5qRWlmUS5nVlN3Nnc1NXBCbXhwNXFrX05HLUg3TUlJN0pXS3Z6NXA0eTFQcHdEYjF2LXFsNFFHTHNVWFNVTE1Gal9ZUk40akNJZThMcjNuMGppV1VWSTBnc2NMQQ=="

    val paymentKey: String? =
        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMk5Ea3dPREUzTkRNc0ltOXlaR1Z5WDJsa0lqbzBNREkxTXpneE5pd2lZM1Z5Y21WdVkza2lPaUpGUjFBaUxDSnNiMk5yWDI5eVpHVnlYM2RvWlc1ZmNHRnBaQ0k2Wm1Gc2MyVXNJbUpwYkd4cGJtZGZaR0YwWVNJNmV5Sm1hWEp6ZEY5dVlXMWxJam9pUTJ4cFptWnZjbVFpTENKc1lYTjBYMjVoYldVaU9pSk9hV052YkdGeklpd2ljM1J5WldWMElqb2lSWFJvWVc0Z1RHRnVaQ0lzSW1KMWFXeGthVzVuSWpvaU9EQXlPQ0lzSW1ac2IyOXlJam9pTkRJaUxDSmhjR0Z5ZEcxbGJuUWlPaUk0TURNaUxDSmphWFI1SWpvaVNtRnphMjlzYzJ0cFluVnlaMmdpTENKemRHRjBaU0k2SWxWMFlXZ2lMQ0pqYjNWdWRISjVJam9pUTFJaUxDSmxiV0ZwYkNJNkltTnNZWFZrWlhSMFpUQTVRR1Y0WVM1amIyMGlMQ0p3YUc5dVpWOXVkVzFpWlhJaU9pSXJPRFlvT0NrNU1UTTFNakV3TkRnM0lpd2ljRzl6ZEdGc1gyTnZaR1VpT2lJd01UZzVPQ0lzSW1WNGRISmhYMlJsYzJOeWFYQjBhVzl1SWpvaVRrRWlmU3dpZFhObGNsOXBaQ0k2TVRJNU16WXNJbUZ0YjNWdWRGOWpaVzUwY3lJNk1UQXdMQ0p3Yld0ZmFYQWlPaUl4T1RZdU1UVXpMak0wTGpFNU5DSXNJbWx1ZEdWbmNtRjBhVzl1WDJsa0lqb3hPRFk0TlgwLkFzazlYa0U0a1c5VnBOa0NuR1BZekpWaGc4NTFfRjg2a3JabzMyU05ael8xSGlNNVZ6RVBBVC1ScjNjOUs1bHlHNXpsczVPQjhTeUxiVWZPWGNtNjRR"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        MFSDK.setUpActionBar(isShowToolBar = false)
// initiatePayment
        val request = MFInitiatePaymentRequest(0.100, MFCurrencyISO.UNITED_STATE_USD)
        MFSDK.initiatePayment(
            request,
            MFAPILanguage.EN
        ) { result: MFResult<MFInitiatePaymentResponse> ->
            when (result) {
                is MFResult.Success ->
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                is MFResult.Fail ->
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                else -> {}
            }
        }


// executePayment

        val request1 = MFExecutePaymentRequest(6, 0.100)
        val mfCardInfo = MFCardInfo("5454545454545454", "09", "23", "100", "test test", true, true)
        /* If you want to execute a payment through a saved token you have use MFCardInfo(cardToken: "put your token here")*/
        MFSDK.executeDirectPayment(
            this@PaymentActivity,
            request1,
            mfCardInfo,
            MFAPILanguage.EN,
            onInvoiceCreated = {
                Log.d("MFCardInfo", "invoiceId: $it")
            }
        ) { invoiceId: String,
            result: MFResult<MFDirectPaymentResponse> ->
            when (result) {
                is MFResult.Success ->
                    Log.d("MFCardInfo", "Response => : " + Gson().toJson(result.response))
                is MFResult.Fail ->
                    Log.d("MFCardInfo", "Fail => : " + Gson().toJson(result.error))
                else -> {}
            }
        }
        MFSDK.executePayment(
            this,
            request1,
            MFAPILanguage.EN,
            onInvoiceCreated = {
                Log.d(TAG, "invoiceId: $it")
            }
        ) { invoiceId: String, result: MFResult<MFGetPaymentStatusResponse> ->
            when (result) {
                is MFResult.Success ->
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                is MFResult.Fail ->
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                else -> {}
            }
        }
    }

}



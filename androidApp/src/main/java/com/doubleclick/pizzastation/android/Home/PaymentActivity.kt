package com.doubleclick.pizzastation.android.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pizzastation.android.MainActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.acceptsdk.*

//import com.paymob.acceptsdk.*


class PaymentActivity : AppCompatActivity() {

    val ACCEPT_PAYMENT_REQUEST = 10

    // Replace this with your actual payment key
    val paymentKey =
        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMk5Ea3dPREUzTkRNc0ltOXlaR1Z5WDJsa0lqbzBNREkxTXpneE5pd2lZM1Z5Y21WdVkza2lPaUpGUjFBaUxDSnNiMk5yWDI5eVpHVnlYM2RvWlc1ZmNHRnBaQ0k2Wm1Gc2MyVXNJbUpwYkd4cGJtZGZaR0YwWVNJNmV5Sm1hWEp6ZEY5dVlXMWxJam9pUTJ4cFptWnZjbVFpTENKc1lYTjBYMjVoYldVaU9pSk9hV052YkdGeklpd2ljM1J5WldWMElqb2lSWFJvWVc0Z1RHRnVaQ0lzSW1KMWFXeGthVzVuSWpvaU9EQXlPQ0lzSW1ac2IyOXlJam9pTkRJaUxDSmhjR0Z5ZEcxbGJuUWlPaUk0TURNaUxDSmphWFI1SWpvaVNtRnphMjlzYzJ0cFluVnlaMmdpTENKemRHRjBaU0k2SWxWMFlXZ2lMQ0pqYjNWdWRISjVJam9pUTFJaUxDSmxiV0ZwYkNJNkltTnNZWFZrWlhSMFpUQTVRR1Y0WVM1amIyMGlMQ0p3YUc5dVpWOXVkVzFpWlhJaU9pSXJPRFlvT0NrNU1UTTFNakV3TkRnM0lpd2ljRzl6ZEdGc1gyTnZaR1VpT2lJd01UZzVPQ0lzSW1WNGRISmhYMlJsYzJOeWFYQjBhVzl1SWpvaVRrRWlmU3dpZFhObGNsOXBaQ0k2TVRJNU16WXNJbUZ0YjNWdWRGOWpaVzUwY3lJNk1UQXdMQ0p3Yld0ZmFYQWlPaUl4T1RZdU1UVXpMak0wTGpFNU5DSXNJbWx1ZEdWbmNtRjBhVzl1WDJsa0lqb3hPRFk0TlgwLkFzazlYa0U0a1c5VnBOa0NuR1BZekpWaGc4NTFfRjg2a3JabzMyU05ael8xSGlNNVZ6RVBBVC1ScjNjOUs1bHlHNXpsczVPQjhTeUxiVWZPWGNtNjRR"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        /*val myWebView:WebView = findViewById(R.id.web)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://demo.MyFatoorah.com/KWT/ie/01072201794541")*/


        /* MFSDK.setUpActionBar(
             "MyFatoorah Payment",
             com.doubleclick.pizzastation.android.R.color.red,
             com.doubleclick.pizzastation.android.R.color.yellow,
             true
         )*/

        findViewById<Button>(R.id.Button1).setOnClickListener {
            startPayActivityNoToken(true)
        }
    }

    private fun putNormalExtras(intent: Intent) {
        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, paymentKey)
        //   intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val extras = data?.extras
        if (requestCode == ACCEPT_PAYMENT_REQUEST) {
            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(this, "User canceled!!")
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(
                    this,
                    "Missing Argument == " + extras!!.getString(IntentConstants.MISSING_ARGUMENT_VALUE)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(
                    this,
                    "Reason == " + extras!!.getString(IntentConstants.TRANSACTION_ERROR_REASON)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras!!.getString(PayResponseKeys.DATA_MESSAGE)!!)
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(
                    this,
                    extras!!.getString(IntentConstants.RAW_PAY_RESPONSE)!!
                )
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {

                // User finished their payment successfully

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras!!.getString(PayResponseKeys.DATA_MESSAGE)!!)
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(
                    this,
                    "TRANSACTION_SUCCESSFUL - Parsing Issue" + extras!!.getString(
                        SaveCardResponseKeys.TOKEN
                    )
                )
                // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
                Log.d("result", "onActivityResult: $data")
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(
                    this,
                    "Token == " + extras!!.getString(SaveCardResponseKeys.TOKEN)
                )
                ToastMaker.displayLongToast(
                    this,
                    "data " + extras.getString(PayResponseKeys.DATA_MESSAGE)
                )
                Log.d("token", "onActivityResult: " + extras[SaveCardResponseKeys.TOKEN])
                //   Log.d("message", "onActivityResult: "+extras.get(PayResponseKeys.MERCHANT_ORDER_ID));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayLongToast(this, "User canceled 3-d scure verification!!")

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras!!.getString(PayResponseKeys.PENDING)!!)
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(
                    this,
                    "User canceled 3-d scure verification - Parsing Issue!!"
                )

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(
                    this,
                    extras!!.getString(IntentConstants.RAW_PAY_RESPONSE)!!
                )
            }
        }
    }

    private fun startPayActivityNoToken(showSaveCard: Boolean) {
        val pay_intent = Intent(this, PayActivity::class.java)
        putNormalExtras(pay_intent)
        // this key is used to save the card by deafult.
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, false)
        // this key is used to display the savecard checkbox.
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, showSaveCard)
        //this key is used to set the theme color(Actionbar, statusBar, button).
        pay_intent.putExtra(
            PayActivityIntentKeys.THEME_COLOR,
            resources.getColor(R.color.black)
        )
        // this key is to whether display the Actionbar or not.
        pay_intent.putExtra("ActionBar", true)
        // this key is used to define the language. takes for ex ("ar", "en") as inputs.
        pay_intent.putExtra("language", "ar")
        // this Key is used to set text to Pay confirm button.
        pay_intent.putExtra("PAY_BUTTON_TEXT", "SAVE")
        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST)
    }

}
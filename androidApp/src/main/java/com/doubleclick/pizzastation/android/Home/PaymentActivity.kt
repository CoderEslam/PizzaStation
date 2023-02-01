package com.doubleclick.pizzastation.android.Home

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.doubleclick.pizzastation.android.R


class PaymentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.doubleclick.pizzastation.android.R.layout.activity_payment)


        val myWebView:WebView = findViewById(R.id.web)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("https://demo.MyFatoorah.com/KWT/ie/01072201794541")


        /* MFSDK.setUpActionBar(
             "MyFatoorah Payment",
             com.doubleclick.pizzastation.android.R.color.red,
             com.doubleclick.pizzastation.android.R.color.yellow,
             true
         )*/

    }
}
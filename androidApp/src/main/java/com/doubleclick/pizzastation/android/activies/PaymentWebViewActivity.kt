package com.doubleclick.pizzastation.android.activies

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityHomeBinding
import com.doubleclick.pizzastation.android.databinding.ActivityPaymentWebViewBinding
import com.doubleclick.pizzastation.android.databinding.NoInternetConnectionBinding
import com.doubleclick.pizzastation.android.model.AmountPayment
import com.doubleclick.pizzastation.android.model.URLPayment
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import com.doubleclick.pizzastation.android.utils.isNetworkConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentWebViewBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var networkBinding: NoInternetConnectionBinding

    @RequiresApi(Build.VERSION_CODES.M)
    private var net: Boolean = false
    private val TAG = "PaymentWebViewActivity"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        net = isNetworkConnected(this)
        if (net) {
            binding = ActivityPaymentWebViewBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } else {
            networkBinding = NoInternetConnectionBinding.inflate(layoutInflater)
            setContentView(networkBinding.root)
            networkBinding.tryAgain.setOnClickListener {
                //https://stackoverflow.com/questions/3053761/reload-activity-in-android
                finish()
                startActivity(intent)
            }
        }
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webView.webViewClient = WebViewClient()

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getURLPay(
                AmountPayment("230"),
                "Bearer " + getToken(this@PaymentWebViewActivity).toString()
            )
                .observe(this@PaymentWebViewActivity) {
                    it.enqueue(object : Callback<URLPayment> {
                        override fun onResponse(
                            call: Call<URLPayment>,
                            response: Response<URLPayment>
                        ) {
                            if (response.body()?.url.toString() == "null") {
                                networkBinding = NoInternetConnectionBinding.inflate(layoutInflater)
                                setContentView(networkBinding.root)
                                networkBinding.tryAgain.setOnClickListener {
                                    //https://stackoverflow.com/questions/3053761/reload-activity-in-android
                                    finish()
                                    startActivity(intent)
                                }
                            } else
                                binding.webView.loadUrl(response.body()?.url.toString())
                        }

                        override fun onFailure(call: Call<URLPayment>, t: Throwable) {

                        }

                    })
                }

        }
        // this will load the url of the website

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)

        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java", ReplaceWith("true"))
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Insert your code here
                splitLink(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
            }
        }
        Log.e(
            "webView",
            "onCreate: ${
                binding.webView.originalUrl
            }"
        )
    }

    private fun splitLink(url: String) {
        val list: MutableList<String> = url.split("&") as MutableList<String>
//        [https://www.google.com/?paymentStatus=SUCCESS, cardDataToken=41753522-212b-4ce4-b0fb-3f3b5f3ccef6, maskedCard=512345******2346, merchantOrderId=1677341817, orderId=cb07689c-c01d-4f0b-b693-a2d041b39f30, cardBrand=Mastercard, orderReference=TEST-ORD-193333697, transactionId=TX-17523958166, amount=230, currency=EGP, signature=7bebf48b16db225c4e3a67dd16fbe0a9fe3e91e2705615e54374fb024fcf830e, mode=test]
        val paymentStatus =
            list[0].replace("https://www.google.com/?", "").replace("paymentStatus=", "")
//        val cardDataToken =
        Log.e(TAG, "sparteLink: $list")

    }

    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}
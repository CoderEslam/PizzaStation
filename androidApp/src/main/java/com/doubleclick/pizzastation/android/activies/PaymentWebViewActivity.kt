package com.doubleclick.pizzastation.android.activies

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityHomeBinding
import com.doubleclick.pizzastation.android.databinding.ActivityPaymentWebViewBinding
import com.doubleclick.pizzastation.android.databinding.NoInternetConnectionBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.Constants.NORMAL_ORDER
import com.doubleclick.pizzastation.android.utils.Constants.OFFER
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import com.doubleclick.pizzastation.android.utils.isNetworkConnected
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
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
    private lateinit var menuOptionItemSelectedGovernorateModel: GovernorateModel
    private lateinit var menuOptionItemSelectedBranchesModel: AreasModel
    private lateinit var carts: ArrayList<CartModel>

    @RequiresApi(Build.VERSION_CODES.M)
    private var net: Boolean = false
    private val TAG = "PaymentWebViewActivity"

    @OptIn(DelicateCoroutinesApi::class)
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
        menuOptionItemSelectedGovernorateModel =
            intent.extras?.getSerializable("governorateModel") as GovernorateModel
        menuOptionItemSelectedBranchesModel =
            intent.extras?.getSerializable("branchesModel") as AreasModel
        carts = intent.extras?.getSerializable("carts") as ArrayList<CartModel>
        Log.d(TAG, "onCreate: $carts")
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webView.webViewClient = WebViewClient()

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getURLPay(
                AmountPayment(
                    intent.extras?.getDouble("total")
                        ?.plus(menuOptionItemSelectedBranchesModel.delivery.toDouble()).toString()
                ),
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
    }

    private fun splitLink(url: String) {
        val list: MutableList<String> = url.split("&") as MutableList<String>
//        [https://www.google.com/?paymentStatus=SUCCESS, cardDataToken=41753522-212b-4ce4-b0fb-3f3b5f3ccef6, maskedCard=512345******2346, merchantOrderId=1677341817, orderId=cb07689c-c01d-4f0b-b693-a2d041b39f30, cardBrand=Mastercard, orderReference=TEST-ORD-193333697, transactionId=TX-17523958166, amount=230,
//        currency=EGP, signature=7bebf48b16db225c4e3a67dd16fbe0a9fe3e91e2705615e54374fb024fcf830e, mode=test]
        val paymentStatus =
            list[0].replace("https://www.google.com/?paymentStatus=", "")
        val cardDataToken =
            list[1].replace("cardDataToken=", "")
        val maskedCard = list[2].replace("maskedCard=", "")
        val merchantOrderId = list[3].replace("merchantOrderId=", "")
        val orderId = list[4].replace("orderId=", "")
        val cardBrand = list[5].replace("cardBrand=", "")
        val orderReference = list[6].replace("orderReference=", "")
        val transactionId = list[7].replace("transactionId=", "")
        val amount_payment = list[8].replace("amount=", "")
        val currency = list[9].replace("currency=", "")
        val signature = list[10].replace("signature=", "")
        val mode = list[10].replace("mode=", "")
        Log.e(TAG, "sparteLink: $list")

        sendOrder(
            paymentStatus,
            cardDataToken,
            maskedCard,
            merchantOrderId,
            orderId,
            cardBrand,
            orderReference,
            transactionId,
            amount_payment,
            currency,
            signature,
            mode
        );
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

    private fun sendOrder(
        paymentStatus: String,
        cardDataToken: String,
        maskedCard: String,
        merchantOrderId: String,
        orderId: String,
        cardBrand: String,
        orderReference: String,
        transactionId: String,
        amount_payment: String,
        currency: String,
        signature: String,
        mode: String
    ) {
        val parentJsonObject = JsonObject();
        parentJsonObject.addProperty("total", intent.extras?.getDouble("total"))
        parentJsonObject.addProperty("delivery", menuOptionItemSelectedBranchesModel.delivery)
        parentJsonObject.addProperty("amount", intent.extras?.getInt("amount"))
        parentJsonObject.addProperty("status", "mobile_app")
        parentJsonObject.addProperty("notes", intent.extras?.getInt("notes"))
        parentJsonObject.addProperty(
            "area_id",
            menuOptionItemSelectedGovernorateModel.id.toString()
        )
        parentJsonObject.addProperty(
            "branch_id",
            menuOptionItemSelectedBranchesModel.id.toString()
        )
        val jsonArrayChildItems = JsonArray();
        for (cart in carts) {
            val jsonObjectItemsChild = JsonObject();
            jsonObjectItemsChild.addProperty("name", cart.name)
            jsonObjectItemsChild.addProperty("price", cart.price)
            jsonObjectItemsChild.addProperty("size", cart.size)
            jsonObjectItemsChild.addProperty("quantity", cart.quantity)
            jsonObjectItemsChild.addProperty("category", cart.menuModel?.get(0)?.category)
            val jsonArrayChildExtras = JsonArray();
            try {
                if (cart.extra?.isNotEmpty() == true && cart.type == NORMAL_ORDER && cart.menuModel?.isNotEmpty() == true) {
                    for (extra in cart.extra) {
                        val jsonObjectChildExtra = JsonObject();
                        jsonObjectChildExtra.addProperty("name", extra.name)
                        jsonObjectChildExtra.addProperty("price", extra.price)
                        jsonObjectChildExtra.addProperty("size", extra.size)
                        jsonObjectChildExtra.addProperty("quantity", extra.quantity)
                        Log.e("JSONResponse", "sendOrder: ${jsonObjectChildExtra}")
                        jsonArrayChildExtras.add(jsonObjectChildExtra)
                        jsonObjectItemsChild.add("extra", jsonArrayChildExtras)
                    }
                }
                Log.e(
                    TAG,
                    "sendOrder:   ${cart.menuModel?.isNotEmpty()}  ${cart.type == OFFER}",
                )
                if (cart.type == OFFER && cart.menuModel?.isNotEmpty() == true) {
                    for (pizza in cart.menuModel) {
                        val jsonObjectChildExtra = JsonObject();
                        jsonObjectChildExtra.addProperty("name", pizza?.name)
                        jsonObjectChildExtra.addProperty("price", "0")
                        jsonObjectChildExtra.addProperty("size", "0")
                        jsonObjectChildExtra.addProperty("quantity", "1")
                        jsonArrayChildExtras.add(jsonObjectChildExtra)
                        Log.e("JSONResponse", "sendOrder: ${jsonObjectChildExtra}")
                        jsonObjectItemsChild.add("extra", jsonArrayChildExtras)
                    }
                }
                jsonArrayChildItems.add(jsonObjectItemsChild);
            } catch (e: NullPointerException) {
                Log.e(TAG, "Error : ${e.message}")
            }
        }
        parentJsonObject.add("items", jsonArrayChildItems);
        parentJsonObject.addProperty("paymentStatus", paymentStatus)
        parentJsonObject.addProperty("cardDataToken", cardDataToken)
        parentJsonObject.addProperty("maskedCard", maskedCard)
        parentJsonObject.addProperty("merchantOrderId", merchantOrderId)
        parentJsonObject.addProperty("orderId", orderId)
        parentJsonObject.addProperty("cardBrand", cardBrand)
        parentJsonObject.addProperty("orderReference", orderReference)
        parentJsonObject.addProperty("transactionId", transactionId)
        parentJsonObject.addProperty("amount_payment", amount_payment)
        parentJsonObject.addProperty("currency", currency)
        parentJsonObject.addProperty("signature", signature)
        parentJsonObject.addProperty("mode", mode)
        parentJsonObject.addProperty("delivery", menuOptionItemSelectedBranchesModel.delivery)
        parentJsonObject.addProperty("payment_type", "pay online")
        Log.d(TAG, "onViewCreated: $parentJsonObject")
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.setOrderComplete(
                "Bearer " + getToken(this@PaymentWebViewActivity).toString(),
                parentJsonObject
            ).observe(this@PaymentWebViewActivity) {
                it.enqueue(object : Callback<OrderModelList> {
                    override fun onResponse(
                        call: Call<OrderModelList>,
                        response: Response<OrderModelList>
                    ) {
                        Toast.makeText(
                            this@PaymentWebViewActivity,
                            response.body()?.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<OrderModelList>, t: Throwable) {
                        Toast.makeText(
                            this@PaymentWebViewActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }

    }
}
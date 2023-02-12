package com.doubleclick.pizzastation.android.views.acceptsdk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.*

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import com.android.volley.*
import com.android.volley.toolbox.HttpStack
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import com.doubleclick.pizzastation.android.views.acceptsdk.helper.StringPOSTRequest
import com.doubleclick.pizzastation.android.views.acceptsdk.helper.TLSSocketFactory
import com.doubleclick.pizzastation.android.views.editcard.EditCard
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import com.doubleclick.pizzastation.android.R


class PayActivity : AppCompatActivity(), View.OnClickListener,
    AsyncResponse {
    private var hasBilling = false
    var billingData: JSONObject? = null
    var paymentKey: String? = null
    var token: String? = null
    var maskedPanNumber: String? = null
    var saveCardDefault: Boolean? = null
    var showSaveCard: Boolean? = null
    var themeColor = 0
    var language: String? = "en"
    var nameText: EditText? = null
    var cardNumberText: EditCard? = null
    var monthText: EditText? = null
    var yearText: EditText? = null
    var cvvText: EditText? = null
    var saveCardCheckBox: AppCompatCheckBox? = null
    var saveCardText: TextView? = null
    var payBtn: Button? = null
    var cardName_linearLayout: LinearLayout? = null
    var expiration_linearLayout: LinearLayout? = null
    var saveCard_linearLayout: LinearLayout? = null
    var mProgressDialog: ProgressDialog? = null
    var verificationActivity_title: String? = null
    var status: Status? = null
    var payDict: JSONObject? = null

    @RequiresApi(api = 23)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetVariables()
        initUiTheme()
        this.setContentView(R.layout.activity_card_information)
        init()
    }

    fun initUiTheme() {
        val intent = this.intent
        if (intent.hasExtra("language")) {
            language = intent.getStringExtra("language")
            setLocale(language)
        }
        themeColor = intent.getIntExtra(
            "theme_color",
            this.applicationContext.resources.getColor(R.color.colorPrimaryDark)
        )
    }

    @RequiresApi(api = 23)
    private fun init() {
        acceptParameters
        val intent = this.intent
        language = intent.getStringExtra("language")
        linkViews(language)
        updateLayout()
        Log.d("test", "onCreate: " + themeColor)
    }

    fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        onConfigurationChanged(conf)
    }

    @RequiresApi(api = 17)
    override fun onBackPressed() {
        onCancelPress()
    }

    @RequiresApi(api = 22)
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == 4) {
            onCancelPress()
        }
        return true
    }

    override fun onClick(v: View) {
        if (v.id == R.id.pay) {
            handlePayment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 16908332) {
            onCancelPress()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onCancelPress() {
        if (status == Status.IDLE) {
            val canceledIntent = Intent()
            this.setResult(1, canceledIntent)
            finish()
        }
    }

    private fun handlePayment() {
        val nameString = nameText!!.text.toString()
        val numberString: String = cardNumberText!!.cardNumber
        val monthString = monthText!!.text.toString()
        val yearString = yearText!!.text.toString()
        val cvvString = cvvText!!.text.toString()
        val cardData = JSONObject()
        if (token != null) {
            try {
                cardData.put("identifier", token)
                cardData.put("subtype", "TOKEN")
                cardData.put("cvn", cvvString)
            } catch (var10: JSONException) {
                return
            }
        } else {
            if (!FormChecker.checkCardName(nameString)) {
                Toast.makeText(this, this.getString(R.string.Empty_name_check), Toast.LENGTH_LONG)
                    .show()
                return
            }
            if (!FormChecker.checkCardNumber(numberString)) {
                Toast.makeText(this, this.getString(R.string.Card_Number_check), Toast.LENGTH_LONG)
                    .show()
                return
            }
            if (!FormChecker.checkDate(monthString, yearString)) {
                Toast.makeText(this, this.getString(R.string.Date_check), Toast.LENGTH_LONG).show()
                return
            }
            if (!FormChecker.checkCVV(cvvString)) {
                Toast.makeText(this, this.getString(R.string.Cvv_check), Toast.LENGTH_LONG).show()
                return
            }
            try {
                cardData.put("identifier", numberString)
                cardData.put("sourceholder_name", nameString)
                cardData.put("subtype", "CARD")
                cardData.put("expiry_month", monthString)
                cardData.put("expiry_year", yearString)
                cardData.put("cvn", cvvString)
            } catch (var9: JSONException) {
                return
            }
        }
        try {
            payAPIRequest(cardData)
        } catch (var8: JSONException) {
            notifyErrorTransaction("An error occured while handling payment response")
        }
    }

    @Throws(JSONException::class)
    fun payAPIRequest(cardData: JSONObject?) {
        val params = JSONObject()
        params.put("source", cardData)
        params.put("api_source", "SDK")
        if (hasBilling) {
            params.put("billing", billingData)
        }
        params.put("payment_token", paymentKey)
        val jsons = params.toString()
        var queue = Volley.newRequestQueue(this.applicationContext)
        var stack: HttpStack? = null
        if (VERSION.SDK_INT >= 9) {
            try {
                if (VERSION.SDK_INT <= 19) {
                    stack = HurlStack(null as HurlStack.UrlRewriter?, TLSSocketFactory())
                    queue = Volley.newRequestQueue(this.applicationContext, stack)
                }
            } catch (var7: Exception) {
                Log.i("NetworkClient", "can no create custom socket factory")
            }
        }
        val request =
            StringPOSTRequest("https://accept.paymobsolutions.com/api/acceptance/payments/pay",
                jsons,
                { response ->
                    dismissProgressDialog()
                    try {
                        val jsonResult = JSONObject(response)
                        Log.d("notice", "json output: $jsonResult")
                        val direct3dSecure = jsonResult.getString("is_3d_secure")
                        if (direct3dSecure != null) {
                            payDict = jsonResult
                            if (direct3dSecure == "true") {
                                val redirectionURL = jsonResult.getString("redirection_url")
                                if (redirectionURL != null) {
                                    open3DSecureView(redirectionURL)
                                } else {
                                    dismissProgressDialog()
                                    notifyErrorTransaction("An error occured while reading the 3dsecure redirection URL")
                                }
                            } else {
                                paymentInquiry()
                            }
                        } else {
                            dismissProgressDialog()
                            notifyErrorTransaction("An error occured while checking if the card is 3d secure")
                        }
                    } catch (var5: Exception) {
                        dismissProgressDialog()
                        Log.d("notice", "exception caught " + var5.message)
                        notifyErrorTransaction(var5.message)
                    }
                },
                { error ->
                    Log.d("notice", "json error output: $error")
                    val networkResponse = error.networkResponse
                    dismissProgressDialog()
                    if (networkResponse != null && networkResponse.statusCode == 401) {
                        notifyErrorTransaction("Invalid or Expired Payment Key")
                    }
                })
        request.retryPolicy = DefaultRetryPolicy(30000, 1, 1.0f)
        request.tag = 0
        queue.add(request)
        showProgressDialog()
    }

    private fun open3DSecureView(url: String) {
        val intent = this.intent
        themeColor = intent.getIntExtra(
            "theme_color",
            this.applicationContext.resources.getColor(R.color.colorPrimaryDark)
        )
        val actionBar = intent.getBooleanExtra("ActionBar", false)
        val threeDSecureViewIntent = Intent(this, ThreeDSecureWebViewActivty::class.java)
        threeDSecureViewIntent.putExtra("three_d_secure_url", url)
        threeDSecureViewIntent.putExtra("ActionBar", actionBar)
        threeDSecureViewIntent.putExtra("theme_color", themeColor)
        threeDSecureViewIntent.putExtra("three_d_secure_activity_title", verificationActivity_title)
        this.startActivityForResult(
            threeDSecureViewIntent,
            IntentConstants.THREE_D_SECURE_VERIFICATION_REQUEST
        )
    }

    override fun processFinish(apiName: String?, output: String?, status_code: String?) {
        dismissProgressDialog()
        Log.d("notice", "output: $output")
        Log.d("notice", "status code: $status_code")
        if (Integer.valueOf(status_code) == 401) {
            notifyErrorTransaction("Invalid or Expired Payment Key")
        }
        if (apiName.equals("USER_PAYMENT", ignoreCase = true)) {
            try {
                val jsonResult = JSONObject(output)
                Log.d("notice", "json output: $jsonResult")
                val direct3dSecure = jsonResult.getString("is_3d_secure")
                if (direct3dSecure != null) {
                    payDict = jsonResult
                    if (direct3dSecure == "true") {
                        val redirectionURL = jsonResult.getString("redirection_url")
                        if (redirectionURL != null) {
                            open3DSecureView(redirectionURL)
                        } else {
                            dismissProgressDialog()
                            notifyErrorTransaction("An error occured while reading the 3dsecure redirection URL")
                        }
                    } else {
                        paymentInquiry()
                    }
                } else {
                    dismissProgressDialog()
                    notifyErrorTransaction("An error occured while checking if the card is 3d secure")
                }
            } catch (var7: Exception) {
                dismissProgressDialog()
                Log.d("notice", "exception caught " + var7.message)
                notifyErrorTransaction(var7.message)
            }
        } else if (apiName.equals("CARD_TOKENIZER", ignoreCase = true)) {
            dismissProgressDialog()
            notifySuccessfulTransactionSaveCard(output!!)
        }
    }

    private fun paymentInquiry() {
        try {
            Log.d("notice", payDict.toString())
            val success = payDict!!.getString("success")
            Log.d("notice", "txn_response_code is " + payDict!!.getInt("txn_response_code"))
            if (payDict!!.getInt("txn_response_code") == 1) {
                notifyErrorTransaction("There was an error processing the transaction")
            }
            if (payDict!!.getInt("txn_response_code") == 2) {
                notifyErrorTransaction("Contact card issuing bank")
            }
            if (payDict!!.getInt("txn_response_code") == 4) {
                notifyErrorTransaction("Expired Card")
            }
            if (payDict!!.getInt("txn_response_code") == 5) {
                notifyErrorTransaction("Insufficient Funds")
            }
            if (success == "true") {
                if (saveCardCheckBox!!.isChecked) {
                    val cardData = JSONObject()
                    cardData.put("pan", cardNumberText!!.cardNumber)
                    cardData.put("cardholder_name", nameText!!.text.toString())
                    cardData.put("expiry_month", monthText!!.text.toString())
                    cardData.put("expiry_year", yearText!!.text.toString())
                    cardData.put("cvn", cvvText!!.text.toString())
                    val jsons = cardData.toString()
                    var queue = Volley.newRequestQueue(this.applicationContext)
                    var stack: HttpStack? = null
                    if (VERSION.SDK_INT >= 9) {
                        try {
                            if (VERSION.SDK_INT <= 19) {
                                stack =
                                    HurlStack(null as HurlStack.UrlRewriter?, TLSSocketFactory())
                                queue = Volley.newRequestQueue(this.applicationContext, stack)
                            }
                        } catch (var7: Exception) {
                            Log.i("NetworkClient", "can no create custom socket factory")
                        }
                    }
                    val request =
                        StringPOSTRequest("https://accept.paymobsolutions.com/api/acceptance/tokenization?payment_token=" + paymentKey,
                            jsons,
                            { response ->
                                Log.d("notice", "tokenize response $response")
                                dismissProgressDialog()
                                notifySuccessfulTransactionSaveCard(response)
                            },
                            { error ->
                                val networkResponse = error.networkResponse
                                dismissProgressDialog()
                                Log.d("notice", "tokenize error response $error")
                                if (networkResponse != null && networkResponse.statusCode == 401) {
                                    notifyErrorTransaction("Invalid or Expired Payment Key")
                                }
                            })
                    request.retryPolicy = DefaultRetryPolicy(30000, 1, 1.0f)
                    request.tag = 0
                    queue.add(request)
                } else {
                    dismissProgressDialog()
                    notifySuccesfulTransaction()
                }
            } else {
                dismissProgressDialog()
                notifyRejectedTransaction()
            }
        } catch (var8: JSONException) {
            notifyErrorTransaction("An error occured while reading returned message")
        }
    }

    private fun notifySuccessfulTransactionSaveCard(saveCardData: String) {
        val successIntent = Intent()
        try {
            val savedCardDict = JSONObject(saveCardData)
            putSaveCardData(successIntent, savedCardDict)
            putPayDataInIntent(successIntent)
        } catch (var6: JSONException) {
            payDict!!.remove("merchant_order_id")
            try {
                payDict!!.put("merchant_order_id", "null")
                putPayDataInIntent(successIntent)
            } catch (var5: JSONException) {
                var5.printStackTrace()
            }
        }
        this.setResult(8, successIntent)
        finish()
    }

    private fun putSaveCardData(intent: Intent, saveCardData: JSONObject) {
        for (i in 0 until SaveCardResponseKeys.SAVE_CARD_DICT_KEYS.size) {
            try {
                intent.putExtra(
                    SaveCardResponseKeys.SAVE_CARD_DICT_KEYS[i],
                    saveCardData.getString(SaveCardResponseKeys.SAVE_CARD_DICT_KEYS[i])
                )
            } catch (var5: JSONException) {
            }
        }
    }

    private fun notifyErrorTransaction(reason: String?) {
        val errorIntent = Intent()
        errorIntent.putExtra("transaction_error_reason", reason)
        this.setResult(3, errorIntent)
        finish()
    }

    private fun notifyCancel3dSecure() {
        val cancel3dSecureIntent = Intent()
        try {
            putPayDataInIntent(cancel3dSecureIntent)
            this.setResult(9, cancel3dSecureIntent)
        } catch (var3: JSONException) {
            cancel3dSecureIntent.putExtra("raw_pay_response", payDict.toString())
            this.setResult(10, cancel3dSecureIntent)
        }
        finish()
    }

    private fun notifyRejectedTransaction() {
        val rejectIntent = Intent()
        try {
            putPayDataInIntent(rejectIntent)
            this.setResult(4, rejectIntent)
        } catch (var3: JSONException) {
            rejectIntent.putExtra("raw_pay_response", payDict.toString())
            this.setResult(5, rejectIntent)
        }
        finish()
    }

    private fun notifySuccesfulTransaction() {
        val successIntent = Intent()
        try {
            putPayDataInIntent(successIntent)
            this.setResult(6, successIntent)
            finish()
        } catch (var3: JSONException) {
            notifySuccesfulTransactionParsingIssue(payDict.toString())
        }
    }

    private fun notifySuccesfulTransactionParsingIssue(raw_pay_response: String?) {
        val successIntent = Intent()
        successIntent.putExtra("raw_pay_response", raw_pay_response)
        this.setResult(7, successIntent)
        finish()
    }

    @Throws(JSONException::class)
    private fun putPayDataInIntent(intent: Intent) {
        for (i in 0 until PayResponseKeys.PAY_DICT_KEYS.size) {
            intent.putExtra(
                PayResponseKeys.PAY_DICT_KEYS[i],
                payDict!!.getString(PayResponseKeys.PAY_DICT_KEYS[i])
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentConstants.THREE_D_SECURE_VERIFICATION_REQUEST) {
            if (resultCode == 2) {
                notifyCancel3dSecure()
            } else if (resultCode == 1) {
                notifyCancel3dSecure()
            } else if (resultCode == 17) {
                val raw_pay_response = data!!.getStringExtra("raw_pay_response")
                try {
                    payDict = JSONObject(raw_pay_response)
                    paymentInquiry()
                } catch (var6: Exception) {
                    notifySuccesfulTransactionParsingIssue(raw_pay_response)
                }
            }
        }
    }

    private fun showProgressDialog() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setTitle(this.getString(R.string.processing))
        mProgressDialog!!.setMessage(this.getString(R.string.wait))
        mProgressDialog!!.setCancelable(false)
        status = Status.PROCESSING
        mProgressDialog!!.show()
        this.window.setFlags(16, 16)
    }

    private fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
        status = Status.IDLE
        this.window.clearFlags(16)
    }

    @RequiresApi(api = 21)
    private fun linkViews(language: String?) {
        val intent = this.intent
        val showActionBar = intent.getBooleanExtra("ActionBar", false)
        if (/*showActionBar*/false) {
            val actionBar = this.supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            val colorDrawable = ColorDrawable(themeColor)
            assert(actionBar != null)
            actionBar.setBackgroundDrawable(colorDrawable)
        } else {
            Log.d("actionBar", "no actionBar")
        }
        val window = this.window
        window.addFlags(-2147483648)
        window.clearFlags(67108864)
        window.statusBarColor = themeColor
        nameText = findViewById<View>(R.id.cardName) as EditText
        cardNumberText = findViewById<View>(R.id.cardNumber) as EditCard
        monthText = findViewById<View>(R.id.expiryMonth) as EditText
        yearText = findViewById<View>(R.id.expiryYear) as EditText
        cvvText = findViewById<View>(R.id.cvv) as EditText
        if (language == "ar") {
            nameText!!.gravity = 5
            cardNumberText!!.setGravity(5)
            monthText!!.gravity = 5
            yearText!!.gravity = 5
            cvvText!!.gravity = 5
        }
        saveCardCheckBox = findViewById<View>(R.id.saveCardCheckBox) as AppCompatCheckBox
        saveCardText = findViewById<View>(R.id.saveCardText) as TextView
        payBtn = findViewById<View>(R.id.pay) as Button
        if (intent.getStringExtra("PAY_BUTTON_TEXT") != null && !intent.getStringExtra("PAY_BUTTON_TEXT")!!
                .isEmpty()
        ) {
            payBtn!!.text = intent.getStringExtra("PAY_BUTTON_TEXT")
        }
        payBtn!!.setBackgroundColor(themeColor)
        payBtn!!.setOnClickListener(this)
        cardName_linearLayout = findViewById<View>(R.id.cardName_linearLayout) as LinearLayout
        expiration_linearLayout = findViewById<View>(R.id.expiration_linearLayout) as LinearLayout
        saveCard_linearLayout = findViewById<View>(R.id.saveCard_linearLayout) as LinearLayout
    }

    private val acceptParameters: Unit
        private get() {
            val intent = this.intent
            if (intent.hasExtra("email")) {
                billingData = JSONObject()
                readBillingData(intent)
                hasBilling = true
            }
            paymentKey = intent.getStringExtra("payment_key")
            checkIfPassed("payment_key", paymentKey)
            token = intent.getStringExtra("token")
            maskedPanNumber = intent.getStringExtra("masked_pan_number")
            if (token != null) {
                checkIfPassed("masked_pan_number", maskedPanNumber)
            }
            saveCardDefault = intent.getBooleanExtra("save_card_default", false)
            showSaveCard = intent.getBooleanExtra("show_save_card", true)
            verificationActivity_title = intent.getStringExtra("three_d_secure_activity_title")
            if (verificationActivity_title == null) {
                verificationActivity_title = ""
            }
        }

    private fun readBillingData(intent: Intent) {
        try {
            readBillingValue(intent, "first_name")
            readBillingValue(intent, "last_name")
            readBillingValue(intent, "building")
            readBillingValue(intent, "floor")
            readBillingValue(intent, "apartment")
            readBillingValue(intent, "city")
            readBillingValue(intent, "state")
            readBillingValue(intent, "country")
            readBillingValue(intent, "email")
            readBillingValue(intent, "phone_number")
            readBillingValue(intent, "postal_code")
            Log.d("notice", "finished reading billing data")
        } catch (var3: JSONException) {
        }
    }

    @Throws(JSONException::class)
    private fun readBillingValue(intent: Intent, key: String) {
        val value = intent.getStringExtra(key)
        checkIfPassed(key, value)
        billingData!!.put(key, value)
    }

    private fun checkIfPassed(key: String, value: String?) {
        if (value == null) {
            abortForNotPassed(key)
        }
    }

    private fun abortForNotPassed(key: String) {
        val errorIntent = Intent()
        errorIntent.putExtra("missing_argument_value", key)
        this.setResult(2, errorIntent)
        finish()
    }

    private fun resetVariables() {
        billingData = null
        paymentKey = null
        token = null
        maskedPanNumber = null
        showSaveCard = true
        saveCardDefault = false
        mProgressDialog = null
        payDict = null
        verificationActivity_title = null
        status = Status.IDLE
        hasBilling = false
    }

    private fun updateLayout() {
        saveCardCheckBox!!.isChecked = saveCardDefault!!
        saveCardCheckBox!!.isClickable = showSaveCard!!
        ColorEditor.setAppCompatCheckBoxColors(
            saveCardCheckBox!!,
            -2139062144,
            themeColor.toString().toInt()
        )
        if (!showSaveCard!!) {
            saveCardCheckBox!!.visibility = View.GONE
            if (saveCardDefault!!) {
                saveCardText!!.text = "Your card will be saved for future use"
            } else {
                saveCard_linearLayout!!.visibility = View.GONE
            }
        }
        if (token != null) {
            invalidateOptionsMenu()
            cardName_linearLayout!!.visibility = View.GONE
            expiration_linearLayout!!.visibility = View.GONE
            saveCardCheckBox!!.isChecked = false
            saveCard_linearLayout!!.visibility = View.GONE
            cardNumberText!!.setHint(maskedPanNumber)
            cardNumberText!!.setHintTextColor(this.resources.getColor(R.color.colorText))
            cardNumberText!!.setEnabled(false)
            cardNumberText!!.setFocusable(false)
            val intent = this.intent
            themeColor = intent.getIntExtra(
                "theme_color",
                this.applicationContext.resources.getColor(R.color.colorPrimaryDark)
            )
        }
    }

    @RequiresApi(api = 17)
    private fun setApplicationLanguage(newLanguage: String) {
        val activityRes = this.resources
        val activityConf = activityRes.configuration
        val newLocale = Locale(newLanguage)
        if (VERSION.SDK_INT >= 17) {
            activityConf.setLocale(newLocale)
        }
        activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
        val applicationRes = this.applicationContext.resources
        val applicationConf = applicationRes.configuration
        applicationConf.setLocale(newLocale)
        applicationRes.updateConfiguration(applicationConf, applicationRes.displayMetrics)
    }

    override fun attachBaseContext(newBase: Context) {
        localeManager = LocaleManager(newBase)
        super.attachBaseContext(localeManager!!.setLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager!!.setLocale(this)
        super.onConfigurationChanged(newConfig)
        if (newConfig.locale === Locale.ENGLISH) {
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show()
        } else if (newConfig.locale === Locale.FRENCH) {
            Toast.makeText(this, "French", Toast.LENGTH_SHORT).show()
        }
    }

    enum class Status {
        IDLE, PROCESSING
    }

    companion object {
        var localeManager: LocaleManager? = null
    }

//    override fun processFinish(var1: String?, var2: String?, var3: String?) {
//
//    }
}

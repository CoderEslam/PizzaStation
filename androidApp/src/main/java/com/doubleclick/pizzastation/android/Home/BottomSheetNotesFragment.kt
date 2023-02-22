package com.doubleclick.pizzastation.android.Home

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.doubleclick.pizzastation.android.*
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterBranches
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterGoverorate
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.databinding.FragmentNotesBottomSheetBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.views.superbottomsheet.SuperBottomSheetFragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.paymob.acceptsdk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Predicate
import java.util.stream.Collectors
import com.doubleclick.pizzastation.android.R


/**
 * Created By Eslam Ghazy on 1/25/2023
 */
class BottomSheetNotesFragment(
    private val carts: ArrayList<CartModel>,
    private val total: Double,
    private val amount: Int
) : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentNotesBottomSheetBinding
    private val TAG = "BottomSheetFragment"
    private lateinit var viewModel: MainViewModel
    private lateinit var menuOptionItemSelectedGovernorateModel: GovernorateModel
    private lateinit var menuOptionItemSelectedBranchesModel: BranchesModel
    private var governorateModelList: List<GovernorateModel> = ArrayList()
    private var branchesModelList: List<BranchesModel> = ArrayList()
    val ACCEPT_PAYMENT_REQUEST = 10
    val paymentKey: String =
        "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFMk5Ea3dPREUzTkRNc0ltOXlaR1Z5WDJsa0lqbzBNREkxTXpneE5pd2lZM1Z5Y21WdVkza2lPaUpGUjFBaUxDSnNiMk5yWDI5eVpHVnlYM2RvWlc1ZmNHRnBaQ0k2Wm1Gc2MyVXNJbUpwYkd4cGJtZGZaR0YwWVNJNmV5Sm1hWEp6ZEY5dVlXMWxJam9pUTJ4cFptWnZjbVFpTENKc1lYTjBYMjVoYldVaU9pSk9hV052YkdGeklpd2ljM1J5WldWMElqb2lSWFJvWVc0Z1RHRnVaQ0lzSW1KMWFXeGthVzVuSWpvaU9EQXlPQ0lzSW1ac2IyOXlJam9pTkRJaUxDSmhjR0Z5ZEcxbGJuUWlPaUk0TURNaUxDSmphWFI1SWpvaVNtRnphMjlzYzJ0cFluVnlaMmdpTENKemRHRjBaU0k2SWxWMFlXZ2lMQ0pqYjNWdWRISjVJam9pUTFJaUxDSmxiV0ZwYkNJNkltTnNZWFZrWlhSMFpUQTVRR1Y0WVM1amIyMGlMQ0p3YUc5dVpWOXVkVzFpWlhJaU9pSXJPRFlvT0NrNU1UTTFNakV3TkRnM0lpd2ljRzl6ZEdGc1gyTnZaR1VpT2lJd01UZzVPQ0lzSW1WNGRISmhYMlJsYzJOeWFYQjBhVzl1SWpvaVRrRWlmU3dpZFhObGNsOXBaQ0k2TVRJNU16WXNJbUZ0YjNWdWRGOWpaVzUwY3lJNk1UQXdMQ0p3Yld0ZmFYQWlPaUl4T1RZdU1UVXpMak0wTGpFNU5DSXNJbWx1ZEdWbmNtRjBhVzl1WDJsa0lqb3hPRFk0TlgwLkFzazlYa0U0a1c5VnBOa0NuR1BZekpWaGc4NTFfRjg2a3JabzMyU05ael8xSGlNNVZ6RVBBVC1ScjNjOUs1bHlHNXpsczVPQjhTeUxiVWZPWGNtNjRR"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNotesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.getBranches().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<BranchesList> {
                override fun onResponse(
                    call: Call<BranchesList>,
                    response: Response<BranchesList>
                ) {
                    branchesModelList = response.body()!!.data
                }

                override fun onFailure(call: Call<BranchesList>, t: Throwable) {

                }

            })
        }

        binding.spinnerArea.setSpinnerEventsListener(object :
            OnSpinnerEventsListener {
            override fun onPopupWindowOpened(spinner: Spinner?) {
                binding.spinnerArea.background =
                    resources.getDrawable(R.drawable.bg_spinner_down)
            }

            override fun onPopupWindowClosed(spinner: Spinner?) {
                binding.spinnerArea.background = resources.getDrawable(R.drawable.bg_spinner_up)
            }

        })
        binding.spinnerGovernorate.setSpinnerEventsListener(object :
            OnSpinnerEventsListener {
            override fun onPopupWindowOpened(spinner: Spinner?) {
                binding.spinnerGovernorate.background =
                    resources.getDrawable(R.drawable.bg_spinner_down)
            }

            override fun onPopupWindowClosed(spinner: Spinner?) {
                binding.spinnerGovernorate.background =
                    resources.getDrawable(R.drawable.bg_spinner_up)
            }

        })


        viewModel.getGovernorate().observe(viewLifecycleOwner) {
            it.enqueue(object : Callback<GovernorateList> {
                override fun onResponse(
                    call: Call<GovernorateList>,
                    response: Response<GovernorateList>
                ) {
                    governorateModelList = response.body()!!.data

                    val adapter = SpinnerAdapterGoverorate(
                        requireActivity(),
                        governorateModelList
                    )
                    binding.spinnerGovernorate.adapter = adapter
                    binding.spinnerGovernorate.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View,
                            i: Int,
                            l: Long
                        ) {
                            menuOptionItemSelectedGovernorateModel = governorateModelList[i]
                            selectArea();
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>?) {
                            menuOptionItemSelectedGovernorateModel = governorateModelList[0]
                        }
                    }

                }

                override fun onFailure(call: Call<GovernorateList>, t: Throwable) {

                }

            })
        }


        binding.completeOrder.setOnClickListener {
            startPayActivityNoToken(true)
        }

    }

    private fun sendOrder() {
        val parentJsonObject = JsonObject();
        parentJsonObject.addProperty("total", total)
        parentJsonObject.addProperty("delivery", menuOptionItemSelectedBranchesModel.delivery)
        parentJsonObject.addProperty("amount", amount)
        parentJsonObject.addProperty("status", "mobile_app")
        parentJsonObject.addProperty("notes", binding.notes.text.toString())
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
            jsonObjectItemsChild.addProperty("category", cart.menuModel?.category)
            val jsonArrayChildExtras = JsonArray();
            try {
                if (cart.extra?.isNotEmpty() == true) {
                    for (extra in cart.extra) {
                        val jsonObjectChildExtra = JsonObject();
                        jsonObjectChildExtra.addProperty("name", extra.name)
                        jsonObjectChildExtra.addProperty("price", extra.price)
                        jsonObjectChildExtra.addProperty("size", extra.size)
                        jsonObjectChildExtra.addProperty("quantity", extra.quantity)
                        jsonArrayChildExtras.add(jsonObjectChildExtra)
                        jsonObjectItemsChild.add("extra", jsonArrayChildExtras)
                    }
                } else {

                }
                jsonArrayChildItems.add(jsonObjectItemsChild);
            } catch (e: NullPointerException) {
                Log.e(TAG, "onViewCreated: ${e.message}")
            }
        }
        parentJsonObject.add("items", jsonArrayChildItems);
        Log.d(TAG, "onViewCreated: $parentJsonObject")
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.setOrderComplete(
                "Bearer " + SessionManger.getToken(requireActivity()).toString(),
                parentJsonObject
            ).observe(viewLifecycleOwner) {
                it.enqueue(object : Callback<OrderModelList> {
                    override fun onResponse(
                        call: Call<OrderModelList>,
                        response: Response<OrderModelList>
                    ) {
                        Toast.makeText(
                            requireActivity(),
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onFailure(call: Call<OrderModelList>, t: Throwable) {
                        Toast.makeText(
                            requireActivity(),
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectArea() {

        val byGovernorate: Predicate<BranchesModel> =
            Predicate<BranchesModel> { branch -> branch.government_id == menuOptionItemSelectedGovernorateModel.id }

        binding.spinnerArea.adapter = SpinnerAdapterBranches(
            requireActivity(),
            /*https://zetcode.com/java/filterlist*/
            branchesModelList.stream().filter(byGovernorate)
                .collect(Collectors.toList())
        )
        binding.spinnerArea.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ) {
                menuOptionItemSelectedBranchesModel = branchesModelList[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                menuOptionItemSelectedBranchesModel = branchesModelList[0]
            }
        }
    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED

    private fun startPayActivityNoToken(showSaveCard: Boolean) {
        val pay_intent = Intent(requireActivity(), PayActivity::class.java)
        putNormalExtras(pay_intent)
        // this key is used to save the card by deafult.
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, false)
        // this key is used to display the savecard checkbox.
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, showSaveCard)
        //this key is used to set the theme color(Actionbar, statusBar, button).
        pay_intent.putExtra(
            PayActivityIntentKeys.THEME_COLOR,
            resources.getColor(R.color.colorText)
        )
        // this key is to whether display the Actionbar or not.
        pay_intent.putExtra("ActionBar", true)
        // this key is used to define the language. takes for ex ("ar", "en") as inputs.
        pay_intent.putExtra("language", "ar")
        // this Key is used to set text to Pay confirm button.
        pay_intent.putExtra("PAY_BUTTON_TEXT", "SAVE")
        startActivityForResult(pay_intent, ACCEPT_PAYMENT_REQUEST)
    }

    private fun putNormalExtras(intent: Intent) {
        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, paymentKey)
        //   intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val extras = data!!.extras
        if (requestCode == ACCEPT_PAYMENT_REQUEST) {
            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(requireActivity(), "User canceled!!")
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(
                    requireActivity(), "Missing Argument == " + extras!!.getString(
                        IntentConstants.MISSING_ARGUMENT_VALUE
                    )
                )
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(
                    requireActivity(),
                    "Reason == " + extras!!.getString(IntentConstants.TRANSACTION_ERROR_REASON)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(
                    requireActivity(),
                    extras!!.getString(PayResponseKeys.DATA_MESSAGE)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(
                    requireActivity(),
                    extras!!.getString(IntentConstants.RAW_PAY_RESPONSE)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {

                // User finished their payment successfully

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(
                    requireActivity(),
                    extras!!.getString(PayResponseKeys.DATA_MESSAGE)
                )
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(
                    requireActivity(),
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
                    requireActivity(),
                    "Token == " + extras!!.getString(SaveCardResponseKeys.TOKEN)
                )
                ToastMaker.displayLongToast(
                    requireActivity(),
                    "data " + extras.getString(PayResponseKeys.DATA_MESSAGE)
                )
                Log.d("token", "onActivityResult: " + extras[SaveCardResponseKeys.TOKEN])
                //   Log.d("message", "onActivityResult: "+extras.get(PayResponseKeys.MERCHANT_ORDER_ID));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayLongToast(
                    requireActivity(),
                    "User canceled 3-d scure verification!!"
                )

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(
                    requireActivity(),
                    extras!!.getString(PayResponseKeys.PENDING)
                )
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(
                    requireActivity(),
                    "User canceled 3-d scure verification - Parsing Issue!!"
                )

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(
                    requireActivity(),
                    extras!!.getString(IntentConstants.RAW_PAY_RESPONSE)
                )
            }
        }
    }
}
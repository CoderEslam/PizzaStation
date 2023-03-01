package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.ItemSizeAdapter
import com.doubleclick.pizzastation.android.Adapter.MenuDealAdapter
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterSlice
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.DeletedSliceListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeListener
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.databinding.ActivityDealOfferBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.utils.Constants.OFFERS_URL
import com.doubleclick.pizzastation.android.utils.ItemDecoration
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DealOfferActivity : AppCompatActivity(), ItemSizeListener, DeletedSliceListener {

    private val TAG = "DealOfferActivity"
    private lateinit var binding: ActivityDealOfferBinding
    private var menusAdded: ArrayList<MenuModel> = ArrayList();
    private lateinit var viewModel: MainViewModel
    private var menus: ArrayList<MenuModel> = ArrayList();
    private lateinit var adapter: SpinnerAdapterSlice
    private lateinit var menuDealAdapter: MenuDealAdapter
    private lateinit var offersModel: OffersModel
    private var amount: Int = 1
    private var limit: Int = 0
    private var priceTotal: Double = 0.0
    private var sizePrice: Double = 0.0

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDealOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        offersModel = intent.getSerializableExtra("offersModel") as OffersModel
        limit = offersModel.items_limit.toInt()
        sizePrice = offersModel.offer_price.toDouble()
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        menuDealAdapter = MenuDealAdapter(menusAdded, this@DealOfferActivity)
        binding.rvSlices.adapter = menuDealAdapter
        binding.nameItem.text = offersModel.offer_name
        binding.details.text = offersModel.offer_details
        Glide.with(this).load(OFFERS_URL + offersModel.offer_image)
            .into(binding.imageItem)
        GlobalScope.launch(Dispatchers.Main) {
            async {
                viewModel.getPizzaInOffer().observe(this@DealOfferActivity) {
                    it.enqueue(object : Callback<MenuList> {
                        override fun onResponse(
                            call: Call<MenuList>,
                            response: Response<MenuList>
                        ) {

                            menus = response.body()?.data as ArrayList<MenuModel>

                            adapter = SpinnerAdapterSlice(
                                this@DealOfferActivity,
                                menus
                            )

                            binding.spinnerPizzas.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<MenuList>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.message}")
                        }

                    })
                }
            }.await()

        }

        binding.spinnerPizzas.setSpinnerEventsListener(object :
            OnSpinnerEventsListener {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPopupWindowOpened(spinner: Spinner?) {
                binding.spinnerPizzas.background =
                    resources.getDrawable(R.drawable.bg_spinner_down)
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onPopupWindowClosed(spinner: Spinner?) {
                binding.spinnerPizzas.background = resources.getDrawable(R.drawable.bg_spinner_up)
            }

        })
        binding.spinnerPizzas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                if (menusAdded.size <= limit - 1) {
                    menusAdded.add(menus[i])
                    menuDealAdapter.notifyItemRangeChanged(0, menusAdded.size)
                } else {
                    menusAdded[limit - 1] = menus[i]
                    menuDealAdapter.notifyItemRangeChanged(0, menusAdded.size)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.add.setOnClickListener {
            try {
                amount++
                setTotalPrice()
            } catch (e: NumberFormatException) {
                Log.e(TAG, "add btn: ${e.message}")
            }
        }

        binding.mins.setOnClickListener {
            if (amount <= 1) {
                amount = 1
                Toast.makeText(this, "Can't order less than one ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                amount--
                setTotalPrice()
            } catch (e: NumberFormatException) {
                Log.e(TAG, "mins btn: ${e.message}")
            }
        }

        binding.addToCard.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val jsonObjectParent = JsonObject();
                jsonObjectParent.addProperty("price", priceTotal.toString())
                jsonObjectParent.addProperty("name", offersModel.offer_name)
                jsonObjectParent.addProperty("quantity", amount.toString())
                jsonObjectParent.addProperty("size", offersModel.offer_name)
                jsonObjectParent.addProperty("image", offersModel.offer_image)
                jsonObjectParent.addProperty("id", offersModel.id.toString())
                val jsonArrayMenuModel = JsonArray();
                for (menuModel in menusAdded) {
                    val jsonObjectMenuModel = JsonObject();
                    jsonObjectMenuModel.addProperty("FB", menuModel?.FB.toString());
                    jsonObjectMenuModel.addProperty("FB", menuModel?.FB.toString());
                    jsonObjectMenuModel.addProperty("L", menuModel?.L.toString());
                    jsonObjectMenuModel.addProperty("M", menuModel?.M.toString());
                    jsonObjectMenuModel.addProperty("Slice", menuModel?.Slice.toString());
                    jsonObjectMenuModel.addProperty("XXL", menuModel?.XXL.toString());
                    jsonObjectMenuModel.addProperty("category", menuModel?.category.toString());
                    jsonObjectMenuModel.addProperty("half_L", menuModel?.half_L.toString());
                    jsonObjectMenuModel.addProperty(
                        "half_stuffed_crust_L",
                        menuModel?.half_stuffed_crust_L.toString()
                    );
                    jsonObjectMenuModel.addProperty("id", menuModel?.id.toString());
                    jsonObjectMenuModel.addProperty("image", menuModel?.image.toString());
                    jsonObjectMenuModel.addProperty("name", menuModel?.name.toString());
                    jsonObjectMenuModel.addProperty(
                        "quarter_XXL",
                        menuModel?.quarter_XXL.toString()
                    );
                    jsonObjectMenuModel.addProperty("status", menuModel?.status.toString());
                    jsonObjectMenuModel.addProperty(
                        "stuffed_crust_L",
                        menuModel?.stuffed_crust_L.toString()
                    );
                    jsonObjectMenuModel.addProperty(
                        "stuffed_crust_M",
                        menuModel?.stuffed_crust_M.toString()
                    );
                    jsonArrayMenuModel.add(jsonObjectMenuModel)
                }
                jsonObjectParent.add("menuModel", jsonArrayMenuModel)
                val jsonArray = JsonArray();
                jsonObjectParent.add("extra", null)
                Log.e("jsonObjectParent", "onCreate: $jsonObjectParent")
                viewModel.setCart(
                    "Bearer " + getToken(this@DealOfferActivity),
                    jsonObjectParent
                ).observe(this@DealOfferActivity) {
                    it.enqueue(object : Callback<CartCallback> {
                        override fun onResponse(
                            call: Call<CartCallback>,
                            response: Response<CartCallback>
                        ) {
                            try {
                                Toast.makeText(
                                    this@DealOfferActivity,
                                    response.body()?.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                                GlobalScope.launch(Dispatchers.Main) {
                                    binding.animationView.visibility = View.VISIBLE
                                    binding.addToCard.isEnabled = false
                                    binding.tvAddToCard.setTextColor(resources.getColor(R.color.grey_600))
                                    delay(1000)
                                    startActivity(
                                        Intent(
                                            this@DealOfferActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            } catch (_: NullPointerException) {
                            }
                        }

                        override fun onFailure(call: Call<CartCallback>, t: Throwable) {
                            Toast.makeText(
                                this@DealOfferActivity,
                                "Error " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun setTotalPrice() {
        try {
            priceTotal = (this.sizePrice * amount)
            binding.count.text = amount.toString()
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeListener: ${e.message}")
        }
    }

    override fun onItemSizeListener(sizeName: String, sizePrice: String) {
        binding.priceTotal.text = sizePrice
    }

    override fun deleteSlice(pizza: MenuModel, position: Int) {
        try {
            menusAdded.remove(pizza)
            limit--
            menuDealAdapter.notifyItemRemoved(position)
            menuDealAdapter.notifyItemRangeChanged(0, menusAdded.size)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "deleteSlice: ${e.message}")
        }
    }
}
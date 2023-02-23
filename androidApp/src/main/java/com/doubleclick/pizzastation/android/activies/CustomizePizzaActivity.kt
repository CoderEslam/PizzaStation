package com.doubleclick.pizzastation.android.activies

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.CustomPizzaItemAdapter
import com.doubleclick.pizzastation.android.Adapter.onPizzaClicked
import com.doubleclick.pizzastation.android.Home.BottomSheetPopUpMenuFragment
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityCustomizePizzaBinding
import com.doubleclick.pizzastation.android.model.CartCallback
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.model.OffersModel
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_customize_pizza.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomizePizzaActivity : AppCompatActivity(), BottomSheetPopUpMenuFragment.setImagePizza {

    private lateinit var binding: ActivityCustomizePizzaBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "CustomizePizzaActivity"
    private var menus: ArrayList<MenuModel> = ArrayList();
    private lateinit var offersModel: OffersModel
    private var amount: Int = 1
    private var priceTotal: Double = 0.0
    private var sizePrice: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        offersModel = intent.getSerializableExtra("offersModel") as OffersModel
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getPizzaInOffer().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(
                    call: Call<MenuList>,
                    response: Response<MenuList>
                ) {
                    menus.addAll(response.body()!!.data)
                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
        }
        binding.name.text = offersModel.offer_name
        try {
            sizePrice = offersModel.offer_price.toDouble()
            setTotalPrice()
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onCreate: ${e.message}")
        }
        binding.quarter1.setOnClickListener {
            binding.addPlus1.visibility = View.GONE
            openPopUpMenu(binding.quarter1)
        }
        binding.quarter2.setOnClickListener {
            binding.addPlus2.visibility = View.GONE
            openPopUpMenu(quarter2)
        }
        binding.quarter3.setOnClickListener {
            binding.addPlus3.visibility = View.GONE
            openPopUpMenu(quarter3)
        }
        binding.quarter4.setOnClickListener {
            binding.addPlus4.visibility = View.GONE
            openPopUpMenu(quarter4)
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
                val jsonObjectMenuModel = JsonObject();
                jsonObjectParent.addProperty("price", priceTotal.toString())
                jsonObjectParent.addProperty("name", offersModel.offer_name)
                jsonObjectParent.addProperty("quantity", amount.toString())
                jsonObjectParent.addProperty("size", offersModel.offer_name)
                jsonObjectParent.addProperty("image", offersModel.offer_image)
                jsonObjectParent.addProperty("id", offersModel.id.toString())
                jsonObjectParent.add("menuModel", null)
                val jsonArray = JsonArray();
                jsonObjectParent.add("extra", null)
                Log.e("jsonObjectParent", "onCreate: $jsonObjectParent")
                viewModel.setCart(
                    "Bearer " + SessionManger.getToken(this@CustomizePizzaActivity),
                    jsonObjectParent
                ).observe(this@CustomizePizzaActivity) {
                    it.enqueue(object : Callback<CartCallback> {
                        override fun onResponse(
                            call: Call<CartCallback>,
                            response: Response<CartCallback>
                        ) {
                            try {
                                Toast.makeText(
                                    this@CustomizePizzaActivity,
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
                                            this@CustomizePizzaActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            } catch (e: NullPointerException) {
                            }
                        }

                        override fun onFailure(call: Call<CartCallback>, t: Throwable) {
                            Toast.makeText(
                                this@CustomizePizzaActivity,
                                "Error " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
            }
        }
    }

    override fun setImage(menu: MenuModel, image: ImageView) {
        Glide.with(this).load(IMAGE_URL + menu.image)
            .into(image)
    }

    private fun openPopUpMenu(image: ImageView) {
        BottomSheetPopUpMenuFragment(
            this@CustomizePizzaActivity,
            image,
            menus
        ).show(
            supportFragmentManager,
            ""
        )
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


}
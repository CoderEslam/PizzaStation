package com.doubleclick.pizzastation.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.ExtrasAdapter
import com.doubleclick.pizzastation.android.Adapter.ItemExtraSizeAdapter
import com.doubleclick.pizzastation.android.Adapter.ItemSizeAdapter
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.ItemExtraListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeExtraListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeListener
import com.doubleclick.pizzastation.android.databinding.ActivityFoodItemBinding
import com.doubleclick.pizzastation.android.model.*
import org.json.JSONObject
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodItemActivity : AppCompatActivity(), ItemSizeListener, ItemExtraListener,
    ItemSizeExtraListener {

    private lateinit var binding: ActivityFoodItemBinding
    private var menuModel: MenuModel? = null
    private var amount: Int = 1
    private var sizePrice: Double = 0.0
    private var nameSize: String = ""
    private var priceTotal: Double = 0.0
    private var sizePriceExtras: String = "0"
    private var sizeNameExtra: String = ""
    private val TAG = "FoodItemActivity"
    private val extraList: ArrayList<Extra> = ArrayList()
    private lateinit var viewModel: MainViewModel

    private lateinit var itemSizeAdapter: ItemSizeAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuModel = intent?.getParcelableExtra<MenuModel>("menu");
        binding.nameItem.text = menuModel?.name
        Glide.with(this).load(IMAGE_URL + menuModel?.image).into(binding.imageItem)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        val sizes: ArrayList<Sizes> = ArrayList();
        if (menuModel?.FB.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "FB",
                    menuModel?.image ?: "",
                    menuModel?.FB ?: "0",
                    0
                )
            )
        }
        if (menuModel?.L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "L",
                    menuModel?.image ?: "",
                    menuModel?.L ?: "0",
                    1
                )
            )
        }
        if (menuModel?.M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "M",
                    menuModel?.image ?: "",
                    menuModel?.M ?: "0", 2
                )
            )
        }
        if (menuModel?.Slice.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "Slice",
                    menuModel?.image ?: "",
                    menuModel?.Slice ?: "0", 3
                )
            )
        }
        if (menuModel?.XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "XXL",
                    menuModel?.image ?: "",
                    menuModel?.XXL ?: "0", 4
                )
            )
        }
        if (menuModel?.half_L != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "Half L",
                    menuModel?.image ?: "",
                    menuModel?.half_L ?: "0", 5
                )
            )
        }
        if (menuModel?.half_stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.half_stuffed_crust_L ?: "0",
                    6
                )
            )
        }
        if (menuModel?.quarter_XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Quarter XXL",
                    menuModel?.image ?: "",
                    menuModel?.quarter_XXL ?: "0",
                    7
                )
            )
        }
        if (menuModel?.stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_L ?: "0",
                    8
                )
            )
        }
        if (menuModel?.stuffed_crust_M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust M",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_M ?: "0",
                    9
                )
            )
        }
        itemSizeAdapter = ItemSizeAdapter(this, sizes)
        binding.rvSizes.adapter = itemSizeAdapter


        viewModel.getExtraFilters().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    binding.extrasRv.apply {
                        adapter = ExtrasAdapter(this@FoodItemActivity, response.body()!!.data)
                    }
                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {

                }

            })
        }

        binding.add.setOnClickListener {
            try {
                amount++
                binding.count.text = amount.toString()
                priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
                binding.priceTotal.text = priceTotal.toString() + " ج.م "
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
                binding.count.text = amount.toString()
                priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
                binding.priceTotal.text = priceTotal.toString() + " ج.م "
            } catch (e: NumberFormatException) {
                Log.e(TAG, "mins btn: ${e.message}")
            }

        }

        binding.addToCard.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val jsonObjectParent = JsonObject();
                jsonObjectParent.addProperty("price", priceTotal.toString())
                jsonObjectParent.addProperty("name", menuModel!!.name!!)
                jsonObjectParent.addProperty("quantity", amount.toString())
                jsonObjectParent.addProperty("size", nameSize)
                jsonObjectParent.addProperty("image", menuModel?.image.toString())
                val jsonArray = JsonArray();
                for (extraItem in extraList) {
                    val jsonObjectChild = JsonObject();
                    jsonObjectChild.addProperty("name", extraItem.name)
                    jsonObjectChild.addProperty("price", extraItem.price)
                    jsonObjectChild.addProperty("size", extraItem.size)
                    jsonObjectChild.addProperty("image", "")
                    jsonObjectChild.addProperty("quantity", "1")
                    jsonArray.add(jsonObjectChild)
                    jsonObjectParent.add("extra", jsonArray)
                }
                viewModel.setCart(
                    "Bearer " + SessionManger.getToken(this@FoodItemActivity),
                    jsonObjectParent
                ).observe(this@FoodItemActivity) {
                    it.enqueue(object : Callback<CartCallback> {
                        override fun onResponse(
                            call: Call<CartCallback>,
                            response: Response<CartCallback>
                        ) {
                            Toast.makeText(
                                this@FoodItemActivity,
                                "Response = " + response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            GlobalScope.launch(Dispatchers.Main) {
                                binding.animationView.visibility = View.VISIBLE
                                binding.addToCard.isEnabled = false
                                binding.tvAddToCard.setTextColor(resources.getColor(R.color.grey_600))
                                delay(2000)
                                startActivity(
                                    Intent(
                                        this@FoodItemActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }

                        }

                        override fun onFailure(call: Call<CartCallback>, t: Throwable) {
                            Toast.makeText(
                                this@FoodItemActivity,
                                "Error " + t.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
            }


        }


    }


    override fun onItemSizeListener(sizeName: String, sizePrice: String) {
        try {
            this.sizePrice = sizePrice.toDouble()
            this.nameSize = sizeName
            priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeListener: ${e.message}")
        }
    }


    override fun onItemExtraListener(menuModel: MenuModel?) {
        val sizes: ArrayList<Sizes> = ArrayList();
        if (menuModel?.FB.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "FB",
                    menuModel?.image ?: "",
                    menuModel?.FB ?: "0",
                    0
                )
            )
        }
        if (menuModel?.L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "L",
                    menuModel?.image ?: "",
                    menuModel?.L ?: "0",
                    1
                )
            )
        }
        if (menuModel?.M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "M",
                    menuModel?.image ?: "",
                    menuModel?.M ?: "0",
                    2
                )
            )
        }
        if (menuModel?.Slice.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Slice",
                    menuModel?.image ?: "",
                    menuModel?.Slice ?: "0",
                    3
                )
            )
        }
        if (menuModel?.XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "XXL",
                    menuModel?.image ?: "",
                    menuModel?.XXL ?: "0",
                    4
                )
            )
        }
        if (menuModel?.half_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half L",
                    menuModel?.image ?: "",
                    menuModel?.half_L ?: "0",
                    5
                )
            )
        }
        if (menuModel?.half_stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.half_stuffed_crust_L ?: "0",
                    6
                )
            )
        }
        if (menuModel?.quarter_XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Quarter XXL", menuModel?.image ?: "",
                    menuModel?.quarter_XXL ?: "0",
                    7
                )
            )
        }
        if (menuModel?.stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_L ?: "0",
                    8
                )
            )
        }
        if (menuModel?.stuffed_crust_M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust M",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_M ?: "0",
                    9
                )
            )
        }
        try {
            priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeListener: ${e.message}")
        }

        val builder = AlertDialog.Builder(this@FoodItemActivity)
        builder.setTitle(resources.getString(R.string.add_extras));
        val view: View = LayoutInflater.from(this@FoodItemActivity)
            .inflate(R.layout.alert_extras_layout, null, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setPadding(30, 5, 30, 5)
        val rv_extra_sizes: RecyclerView = view.findViewById(R.id.rv_extra_sizes);
        val image_item: ImageView = view.findViewById(R.id.image_item);
        Glide.with(this@FoodItemActivity).load(IMAGE_URL + menuModel?.image).into(image_item)
        rv_extra_sizes.adapter = ItemExtraSizeAdapter(this, sizes)
        builder.setCancelable(true);
        builder.setView(view);
        builder.show()


    }

    override fun onItemSizeExtraListener(
        sizeSosTypeName: String,
        sizePriceExtra: String,
        sizeNameExtra: String,
        image: String
    ) {
        this.sizePriceExtras = sizePriceExtra;
        this.sizeNameExtra = sizeNameExtra;
        try {
            priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
            extraList.add(Extra(sizeSosTypeName, sizePriceExtra, image, "1", sizeNameExtra))
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeExtraListener btn: ${e.message}")
        }
    }


}
package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.ExtrasAdapter
import com.doubleclick.pizzastation.android.Adapter.ItemExtraSizeAdapter
import com.doubleclick.pizzastation.android.Adapter.ItemSizeAdapter
import com.doubleclick.pizzastation.android.Home.BottomSheetPopUpExtrasFragment
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.ItemExtraListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeExtraListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeListener
import com.doubleclick.pizzastation.android.databinding.ActivityFoodItemBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import com.doubleclick.pizzastation.android.utils.Constants.NORMAL_ORDER
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_customize_pizza.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodItemActivity : AppCompatActivity(), ItemSizeListener, ItemExtraListener,
    ItemSizeExtraListener {

    private lateinit var binding: ActivityFoodItemBinding
    private var menuModel: MenuModel? = null
    private var cartModel: CartModel? = null
    private var amount: Int = 1
    private var sizePrice: Double = 0.0
    private var nameSize: String = ""
    private var priceTotal: Double = 0.0
    private var sizePriceExtras: String = "0"
    private var sizeNameExtra: String = ""
    private val TAG = "FoodItemActivity"
    private val extraList: ArrayList<Extra> = ArrayList()
    private var sumExtraList: ArrayList<SumExtra> = ArrayList()
    private var favoriteModelList: ArrayList<FavoriteModel> = ArrayList();
    private var menuModelList: ArrayList<MenuModel> = ArrayList();
    private lateinit var viewModel: MainViewModel
    private var isFavorite: Boolean = false
    private lateinit var extrasAdapter: ExtrasAdapter

    private lateinit var itemSizeAdapter: ItemSizeAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuModel = intent?.getSerializableExtra("menu") as MenuModel?;
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
                    menuModel?.FB ?: "0"
                )
            )
        }
        if (menuModel?.L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "L",
                    menuModel?.image ?: "",
                    menuModel?.L ?: "0"
                )
            )
        }
        if (menuModel?.M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "M",
                    menuModel?.image ?: "",
                    menuModel?.M ?: "0"
                )
            )
        }
        if (menuModel?.Slice.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "Slice",
                    menuModel?.image ?: "",
                    menuModel?.Slice ?: "0"
                )
            )
        }
        if (menuModel?.XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "XXL",
                    menuModel?.image ?: "",
                    menuModel?.XXL ?: "0"
                )
            )
        }
        if (menuModel?.half_L != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(), "Half L",
                    menuModel?.image ?: "",
                    menuModel?.half_L ?: "0"
                )
            )
        }
        if (menuModel?.half_stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.half_stuffed_crust_L ?: "0"
                )
            )
        }
        if (menuModel?.quarter_XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Quarter XXL",
                    menuModel?.image ?: "",
                    menuModel?.quarter_XXL ?: "0"
                )
            )
        }
        if (menuModel?.stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_L ?: "0"
                )
            )
        }
        if (menuModel?.stuffed_crust_M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust M",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_M ?: "0"
                )
            )
        }
        itemSizeAdapter = ItemSizeAdapter(this, sizes, "")
        binding.rvSizes.adapter = itemSizeAdapter


        viewModel.getExtraFilters().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    menuModelList = response.body()!!.data as ArrayList<MenuModel>
                    extrasAdapter =
                        ExtrasAdapter(this@FoodItemActivity, response.body()!!.data, extraList)
                    binding.extrasRv.adapter = extrasAdapter
                    extrasAdapter.notifyItemRangeChanged(0, menuModelList.size)

                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {

                }

            })
        }

        binding.add.setOnClickListener {
            try {
                amount++
                binding.count.text = amount.toString()
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
                binding.count.text = amount.toString()
                setTotalPrice()
            } catch (e: NumberFormatException) {
                Log.e(TAG, "mins btn: ${e.message}")
            }

        }

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getFavorite("Bearer " + SessionManger.getToken(this@FoodItemActivity))
                .observe(this@FoodItemActivity) {
                    it.enqueue(object : Callback<FavoriteModelList> {
                        override fun onResponse(
                            call: Call<FavoriteModelList>,
                            response: Response<FavoriteModelList>
                        ) {
                            favoriteModelList = response.body()!!.data as ArrayList<FavoriteModel>
                            for (id in favoriteModelList) {
                                if (id.menu.id == menuModel?.id) {
                                    binding.favorite.setChecked(true)
                                    isFavorite = true;
                                    return
                                }
                            }
                            Log.i(TAG, "onResponse: ${response.body().toString()}")
                        }

                        override fun onFailure(call: Call<FavoriteModelList>, t: Throwable) {

                        }

                    });
                }
        }

        binding.favorite.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    if (!isFavorite) {
                        Log.i(TAG, "onCreate: " + SessionManger.getToken(this@FoodItemActivity))
                        viewModel.setFavorite(
                            "Bearer " + SessionManger.getToken(this@FoodItemActivity),
                            MenuId(menuModel?.id.toString())
                        ).observe(this@FoodItemActivity) {
                            it.enqueue(object : Callback<MessageCallback> {
                                override fun onResponse(
                                    call: Call<MessageCallback>,
                                    response: Response<MessageCallback>
                                ) {
                                    for (id in favoriteModelList) {
                                        if (id.menu.id == menuModel?.id) {
                                            binding.favorite.setChecked(true)
                                            isFavorite = true;
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<MessageCallback>, t: Throwable) {

                                }

                            })
                        }
                    } else {
                        for (id in favoriteModelList) {
                            if (id.menu.id == menuModel?.id) {
                                viewModel.deleteFavorite(
                                    "Bearer " + SessionManger.getToken(this@FoodItemActivity),
                                    id.id.toString()
                                ).observe(this@FoodItemActivity) {
                                    it.enqueue(object : Callback<MessageCallback> {
                                        override fun onResponse(
                                            call: Call<MessageCallback>,
                                            response: Response<MessageCallback>
                                        ) {
                                            for (favoritemodel in favoriteModelList) {
                                                if (favoritemodel.menu.id == menuModel?.id) {
                                                    binding.favorite.setChecked(false)
                                                    isFavorite = false;
                                                }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<MessageCallback>,
                                            t: Throwable
                                        ) {

                                        }

                                    })
                                }
                            }
                        }
                    }
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "onCreate: ${e.message}")
                }

            }
        }
        binding.addToCard.setOnClickListener {
            if (nameSize == "") {
                Toast.makeText(this@FoodItemActivity, "select size first", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    val jsonObjectParent = JsonObject();
                    val jsonObjectMenuModel = JsonObject();
                    val jsonArray = JsonArray();
                    val jsonArrayMenuModel = JsonArray();
                    jsonObjectParent.addProperty("price", priceTotal.toString())
                    jsonObjectParent.addProperty("name", menuModel!!.name!!)
                    jsonObjectParent.addProperty("quantity", amount.toString())
                    jsonObjectParent.addProperty("size", nameSize)
                    jsonObjectParent.addProperty("image", menuModel?.image.toString())
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
                    jsonObjectParent.add("menuModel", jsonArrayMenuModel)
                    jsonObjectParent.addProperty("type", NORMAL_ORDER)
                    if (extraList.isNotEmpty()) {
                        for (extraItem in extraList) {
                            val jsonObjectChild = JsonObject();
                            jsonObjectChild.addProperty("name", extraItem.name)
                            jsonObjectChild.addProperty("price", extraItem.price)
                            jsonObjectChild.addProperty("size", extraItem.size)
                            jsonObjectChild.addProperty("image", extraItem.image)
                            jsonObjectChild.addProperty("quantity", "1")
                            jsonArray.add(jsonObjectChild)
                            jsonObjectParent.add("extra", jsonArray)
                        }
                    } else {
                        jsonObjectParent.add("extra", null)
                    }
                    Log.d("jsonObjectParent", "onCreate: $jsonObjectParent")
                    viewModel.setCart(
                        "Bearer " + SessionManger.getToken(this@FoodItemActivity),
                        jsonObjectParent
                    ).observe(this@FoodItemActivity) {
                        it.enqueue(object : Callback<CartCallback> {
                            override fun onResponse(
                                call: Call<CartCallback>,
                                response: Response<CartCallback>
                            ) {
                                try {
                                    Toast.makeText(
                                        this@FoodItemActivity,
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
                                                this@FoodItemActivity,
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
                    menuModel?.FB ?: "0"
                )
            )
        }
        if (menuModel?.L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "L",
                    menuModel?.image ?: "",
                    menuModel?.L ?: "0"
                )
            )
        }
        if (menuModel?.M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "M",
                    menuModel?.image ?: "",
                    menuModel?.M ?: "0"
                )
            )
        }
        if (menuModel?.Slice.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Slice",
                    menuModel?.image ?: "",
                    menuModel?.Slice ?: "0"
                )
            )
        }
        if (menuModel?.XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "XXL",
                    menuModel?.image ?: "",
                    menuModel?.XXL ?: "0"
                )
            )
        }
        if (menuModel?.half_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half L",
                    menuModel?.image ?: "",
                    menuModel?.half_L ?: "0"
                )
            )
        }
        if (menuModel?.half_stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Half stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.half_stuffed_crust_L ?: "0"
                )
            )
        }
        if (menuModel?.quarter_XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Quarter XXL", menuModel?.image ?: "",
                    menuModel?.quarter_XXL ?: "0"
                )
            )
        }
        if (menuModel?.stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust L",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_L ?: "0"
                )
            )
        }
        if (menuModel?.stuffed_crust_M.toString() != "null") {
            sizes.add(
                Sizes(
                    menuModel?.name.toString(),
                    "Stuffed crust M",
                    menuModel?.image ?: "",
                    menuModel?.stuffed_crust_M ?: "0"
                )
            )
        }
        setTotalPrice()
        BottomSheetPopUpExtrasFragment(
            this,
            sizes,
            menuModel
        ).show(
            supportFragmentManager,
            ""
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemExtraListenerDeleted(menuModel: MenuModel?, pos: Int) {
        val e = Extra(menuModel!!.name, "", "", "", "")
        val sumExtra = SumExtra(menuModel!!.name, 0.0)
        if (extraList.contains(e)) {
            extraList.remove(e)
            sumExtraList.remove(sumExtra)
            Log.e(TAG, "onItemSizeExtraListener: ${sumExtras()}")
            setTotalPrice()
            extrasAdapter.notifyItemRangeChanged(0, menuModelList.size)
            extrasAdapter.notifyItemChanged(pos)
            extrasAdapter.notifyDataSetChanged()
        }

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
            val extra = Extra(sizeSosTypeName, sizePriceExtra, image, "1", sizeNameExtra)
            val sumExtra = SumExtra(sizeSosTypeName, sizePriceExtra.toDouble())
            if (extraList.contains(extra)) {
                extraList[extraList.indexOf(extra)] = extra
                sumExtraList[sumExtraList.indexOf(sumExtra)] = sumExtra
                extrasAdapter.notifyItemRangeChanged(0, menuModelList.size)
                Log.e(TAG, "onItemSizeExtraListener: ${sumExtras()}")
                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()
                setTotalPrice()
            } else {
                extraList.add(extra)
                sumExtraList.add(sumExtra)
                extrasAdapter.notifyItemRangeChanged(0, menuModelList.size)
                Log.e(TAG, "onItemSizeExtraListener: ${sumExtras()}")
                Toast.makeText(this, "Added", Toast.LENGTH_LONG).show()
                setTotalPrice()
            }
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeExtraListener btn: ${e.message}")
        }
    }

    private fun sumExtras(): Double {
        var sum = 0.0
        for (price in sumExtraList) {
            sum += price.price
        }
        return sum * amount
    }

    private fun setTotalPrice() {
        try {
            priceTotal = (this.sizePrice * amount).plus(sumExtras())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeListener: ${e.message}")
        }
    }
}
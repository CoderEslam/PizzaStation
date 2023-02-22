package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.doubleclick.pizzastation.android.databinding.ActivityFoodItemEditBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodItemEditActivity : AppCompatActivity(), ItemSizeListener, ItemExtraListener,
    ItemSizeExtraListener {

    private lateinit var binding: ActivityFoodItemEditBinding
    private var cartModel: CartModel? = null
    private var amount: Int = 1
    private var sizePrice: Double = 0.0
    private var nameSize: String = ""
    private var priceTotal: Double = 0.0
    private var sizePriceExtras: String = "0"
    private var sumExtraList: ArrayList<SumExtra> = ArrayList()
    private var sizeNameExtra: String = ""
    private val TAG = "FoodItemActivity"
    private val extraList: ArrayList<Extra> = ArrayList()
    private var extraMenuModelList: ArrayList<MenuModel> = ArrayList()
    private var favoriteModelList: ArrayList<FavoriteModel> = ArrayList();
    private lateinit var viewModel: MainViewModel
    private var isFavorite: Boolean = false
    private lateinit var itemSizeAdapter: ItemSizeAdapter
    private lateinit var extrasAdapter: ExtrasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodItemEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        cartModel = intent?.getSerializableExtra("cartModel") as CartModel?;
        cartModel?.extra?.let { extra ->
            extra.forEach {
                val sumExtra = SumExtra(it.name, it.price.toDouble())
                extraList.add(it)
                sumExtraList.add(sumExtra)
            }
        }
        onEdit(cartModel);
        putSizes();

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

        viewModel.getExtraFilters().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    extraMenuModelList = response.body()!!.data as ArrayList<MenuModel>
                    extrasAdapter =
                        ExtrasAdapter(this@FoodItemEditActivity, extraMenuModelList, extraList)
                    binding.extrasRv.adapter = extrasAdapter
                    extrasAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {

                }

            })
        }

        binding.favorite.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    if (!isFavorite) {
                        Log.i(TAG, "onCreate: " + getToken(this@FoodItemEditActivity))
                        viewModel.setFavorite(
                            "Bearer " + getToken(this@FoodItemEditActivity),
                            MenuId(cartModel?.menuModel?.id.toString())
                        ).observe(this@FoodItemEditActivity) {
                            it.enqueue(object : Callback<MessageCallback> {
                                override fun onResponse(
                                    call: Call<MessageCallback>,
                                    response: Response<MessageCallback>
                                ) {
                                    for (id in favoriteModelList) {
                                        if (id.menu.id == cartModel?.menuModel?.id) {
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
                            if (id.menu.id == cartModel?.menuModel?.id) {
                                viewModel.deleteFavorite(
                                    "Bearer " + getToken(this@FoodItemEditActivity),
                                    id.id.toString()
                                ).observe(this@FoodItemEditActivity) {
                                    it.enqueue(object : Callback<MessageCallback> {
                                        override fun onResponse(
                                            call: Call<MessageCallback>,
                                            response: Response<MessageCallback>
                                        ) {
                                            for (favoritemodel in favoriteModelList) {
                                                if (favoritemodel.menu.id == cartModel?.menuModel?.id) {
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
    }

    private fun putSizes() {
        val sizes: ArrayList<Sizes> = ArrayList();
        if (cartModel?.menuModel?.FB.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "FB",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.FB ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.L.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "L",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.L ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.M.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(), "M",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.M ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.Slice.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(), "Slice",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.Slice ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(), "XXL",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.XXL ?: "0",
                )
            )
        }
        if (cartModel?.menuModel?.half_L != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(), "Half L",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.half_L ?: "0",
                )
            )
        }
        if (cartModel?.menuModel?.half_stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "Half stuffed crust L",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.half_stuffed_crust_L ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.quarter_XXL.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "Quarter XXL",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.quarter_XXL ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.stuffed_crust_L.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "Stuffed crust L",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.stuffed_crust_L ?: "0"
                )
            )
        }
        if (cartModel?.menuModel?.stuffed_crust_M.toString() != "null") {
            sizes.add(
                Sizes(
                    cartModel?.menuModel?.name.toString(),
                    "Stuffed crust M",
                    cartModel?.menuModel?.image ?: "",
                    cartModel?.menuModel?.stuffed_crust_M ?: "0"
                )
            )
        }
        itemSizeAdapter = ItemSizeAdapter(this, sizes, cartModel?.size.toString())
        binding.rvSizes.adapter = itemSizeAdapter
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onEdit(cartModel: CartModel?) {
        if (cartModel != null) {
            Picasso.get().load(Constants.IMAGE_URL + cartModel.image).into(binding.imageItem)
            amount = cartModel.quantity.toInt()
            binding.count.text = amount.toString()
            binding.priceTotal.text = cartModel.price
            binding.nameItem.text = cartModel.name
            setTotalPrice()
            binding.addToCard.setOnClickListener {
                val jsonObjectParent = JsonObject();
                val jsonObjectMenuModel = JsonObject();
                jsonObjectParent.addProperty("price", priceTotal.toString())
                jsonObjectParent.addProperty("name", cartModel!!.name!!)
                jsonObjectParent.addProperty("quantity", amount.toString())
                jsonObjectParent.addProperty("size", nameSize)
                jsonObjectParent.addProperty("image", cartModel.image.toString())
                jsonObjectMenuModel.addProperty("FB", cartModel.menuModel?.FB.toString());
                jsonObjectMenuModel.addProperty("FB", cartModel.menuModel?.FB.toString());
                jsonObjectMenuModel.addProperty("L", cartModel.menuModel?.L.toString());
                jsonObjectMenuModel.addProperty("M", cartModel.menuModel?.M.toString());
                jsonObjectMenuModel.addProperty("Slice", cartModel.menuModel?.Slice.toString());
                jsonObjectMenuModel.addProperty("XXL", cartModel.menuModel?.XXL.toString());
                jsonObjectMenuModel.addProperty(
                    "category",
                    cartModel.menuModel?.category.toString()
                );
                jsonObjectMenuModel.addProperty("half_L", cartModel.menuModel?.half_L.toString());
                jsonObjectMenuModel.addProperty(
                    "half_stuffed_crust_L",
                    cartModel.menuModel?.half_stuffed_crust_L.toString()
                );
                jsonObjectMenuModel.addProperty("id", cartModel.menuModel?.id.toString());
                jsonObjectMenuModel.addProperty("image", cartModel.menuModel?.image.toString());
                jsonObjectMenuModel.addProperty("name", cartModel.menuModel?.name.toString());
                jsonObjectMenuModel.addProperty(
                    "quarter_XXL",
                    cartModel.menuModel?.quarter_XXL.toString()
                );
                jsonObjectMenuModel.addProperty("status", cartModel.menuModel?.status.toString());
                jsonObjectMenuModel.addProperty(
                    "stuffed_crust_L",
                    cartModel.menuModel?.stuffed_crust_L.toString()
                );
                jsonObjectMenuModel.addProperty(
                    "stuffed_crust_M",
                    cartModel.menuModel?.stuffed_crust_M.toString()
                );
                jsonObjectParent.add("menuModel", jsonObjectMenuModel)
                val jsonArray = JsonArray();
                if (extraList.isNotEmpty()) {
                    for (extraItem in extraList) {
                        val jsonObjectChild = JsonObject();
                        jsonObjectChild.addProperty("name", extraItem.name)
                        jsonObjectChild.addProperty("price", extraItem.price)
                        jsonObjectChild.addProperty("size", extraItem.size)
                        jsonObjectChild.addProperty("image", extraItem.image)
                        jsonObjectChild.addProperty("quantity", "1")
                        jsonArray.add(jsonObjectChild)
                        Log.e(TAG, "onEditextraItem: $extraItem")
                        jsonObjectParent.add("extra", jsonArray)
                    }
                } else {
                    jsonObjectParent.add("extra", null)
                }
                binding.animationView.visibility = View.VISIBLE
                binding.addToCard.isEnabled = false
                binding.tvAddToCard.setTextColor(resources.getColor(R.color.grey_600))
                Log.d(TAG, "onEdit: $jsonObjectParent")
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateCart(
                        "Bearer " + getToken(this@FoodItemEditActivity),
                        cartModel.id.toString(),
                        jsonObjectParent
                    ).observe(
                        this@FoodItemEditActivity
                    ) {
                        it.enqueue(object : Callback<MessageCallback> {
                            override fun onResponse(
                                call: Call<MessageCallback>,
                                response: Response<MessageCallback>
                            ) {
                                Log.d(TAG, "onResponse: ${response.body()?.message.toString()}")
                                startActivity(
                                    Intent(
                                        this@FoodItemEditActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()

                            }

                            override fun onFailure(call: Call<MessageCallback>, t: Throwable) {
                                Log.d(TAG, "onResponse: ${t.message.toString()}")
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
            setTotalPrice()
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
            menuModel,
            extraList
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
            extrasAdapter.notifyItemRangeChanged(0, extraMenuModelList.size)
            extrasAdapter.notifyItemChanged(pos)
            extrasAdapter.notifyDataSetChanged()
        }

    }

    override fun onItemSizeExtraListener(
        sizeSosTypeName: String,/*FB, XXL, L, M*/
        sizePriceExtra: String,  /* price of sos added on pizza*/
        sizeNameExtra: String, /*name of sos added on pizza*/
        imageExtra: String
    ) {
        this.sizePriceExtras = sizePriceExtra;
        this.sizeNameExtra = sizeNameExtra;
        try {
            val extra = Extra(sizeSosTypeName, sizePriceExtra, imageExtra, "1", sizeNameExtra)
            val sumExtra = SumExtra(sizeSosTypeName, sizePriceExtra.toDouble())
            if (extraList.contains(extra)) {
                extraList[extraList.indexOf(extra)] = extra
                sumExtraList[sumExtraList.indexOf(sumExtra)] = sumExtra
                extrasAdapter.notifyItemRangeChanged(0, extraMenuModelList.size)
                Log.e(TAG, "onItemSizeExtraListener: ${sumExtras()}")
                Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()
                setTotalPrice()
            } else {
                extraList.add(extra)
                sumExtraList.add(sumExtra)
                extrasAdapter.notifyItemRangeChanged(0, extraMenuModelList.size)
                Log.e(TAG, "onItemSizeExtraListener: ${sumExtras()}")
                setTotalPrice()
                Toast.makeText(this, "Added", Toast.LENGTH_LONG).show()
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
            Log.e(TAG, "setTotalPrice: ${e.message}")
        }
    }
}
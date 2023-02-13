package com.doubleclick.pizzastation.android

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
    private val extraMenuModelList: ArrayList<MenuModel> = ArrayList()
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
        cartModel?.extra?.let {
            extraList.addAll(it)
        }
        onEdit(cartModel);
        putSizes();

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

        viewModel.getExtraFilters().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<MenuList>, response: Response<MenuList>) {
                    extraMenuModelList.addAll(response.body()!!.data)
                    extrasAdapter =
                        ExtrasAdapter(this@FoodItemEditActivity, extraMenuModelList, extraList)
                    binding.extrasRv.adapter = extrasAdapter
                    extrasAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<MenuList>, t: Throwable) {

                }

            })
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
            Log.e(TAG, "onEdit: ${cartModel}")
            Picasso.get().load(Constants.IMAGE_URL + cartModel.image).into(binding.imageItem)
            amount = cartModel.quantity.toInt()
            binding.count.text = amount.toString()
            binding.priceTotal.text = cartModel.price
            binding.nameItem.text = cartModel.name
            binding.addToCard.setOnClickListener {
                val jsonObjectParent = JsonObject();
                val jsonObjectMenuModel = JsonObject();
                jsonObjectParent.addProperty("price", priceTotal.toString())
                jsonObjectParent.addProperty("name", cartModel!!.name!!)
                jsonObjectParent.addProperty("quantity", amount.toString())
                jsonObjectParent.addProperty("size", nameSize)
                jsonObjectParent.addProperty("image", cartModel.image.toString())
                jsonObjectMenuModel.addProperty("FB", cartModel.menuModel.FB.toString());
                jsonObjectMenuModel.addProperty("FB", cartModel.menuModel.FB.toString());
                jsonObjectMenuModel.addProperty("L", cartModel.menuModel.L.toString());
                jsonObjectMenuModel.addProperty("M", cartModel.menuModel.M.toString());
                jsonObjectMenuModel.addProperty("Slice", cartModel.menuModel.Slice.toString());
                jsonObjectMenuModel.addProperty("XXL", cartModel.menuModel.XXL.toString());
                jsonObjectMenuModel.addProperty(
                    "category",
                    cartModel.menuModel.category.toString()
                );
                jsonObjectMenuModel.addProperty("half_L", cartModel.menuModel.half_L.toString());
                jsonObjectMenuModel.addProperty(
                    "half_stuffed_crust_L",
                    cartModel.menuModel.half_stuffed_crust_L.toString()
                );
                jsonObjectMenuModel.addProperty("id", cartModel.menuModel.id.toString());
                jsonObjectMenuModel.addProperty("image", cartModel.menuModel.image.toString());
                jsonObjectMenuModel.addProperty("name", cartModel.menuModel.name.toString());
                jsonObjectMenuModel.addProperty(
                    "quarter_XXL",
                    cartModel.menuModel.quarter_XXL.toString()
                );
                jsonObjectMenuModel.addProperty("status", cartModel.menuModel.status.toString());
                jsonObjectMenuModel.addProperty(
                    "stuffed_crust_L",
                    cartModel.menuModel.stuffed_crust_L.toString()
                );
                jsonObjectMenuModel.addProperty(
                    "stuffed_crust_M",
                    cartModel.menuModel.stuffed_crust_M.toString()
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
                    val jsonObjectChild = JsonObject();
                    jsonObjectChild.addProperty("name", "")
                    jsonObjectChild.addProperty("price", "")
                    jsonObjectChild.addProperty("size", "")
                    jsonObjectChild.addProperty("image", "")
                    jsonObjectChild.addProperty("quantity", "")
                    jsonArray.add(jsonObjectChild)
                    jsonObjectParent.add("extra", null)
                }
                binding.animationView.visibility = View.VISIBLE
                binding.addToCard.isEnabled = false
                binding.tvAddToCard.setTextColor(resources.getColor(R.color.grey_600))
                Log.d(TAG, "onEdit: $jsonObjectParent")
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateCart(
                        "Bearer " + SessionManger.getToken(this@FoodItemEditActivity),
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
        try {
            priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
        } catch (e: NumberFormatException) {
            Log.e(TAG, "onItemSizeListener: ${e.message}")
        }
        val builder = AlertDialog.Builder(this@FoodItemEditActivity)
        builder.setTitle(resources.getString(R.string.add_extras));
        val view: View = LayoutInflater.from(this@FoodItemEditActivity)
            .inflate(R.layout.alert_extras_layout, null, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setPadding(30, 5, 30, 5)
        val rv_extra_sizes: RecyclerView = view.findViewById(R.id.rv_extra_sizes);
        val image_item: ImageView = view.findViewById(R.id.image_item);
        val name: TextView = view.findViewById(R.id.name);
        Log.e(TAG, "onItemExtraListener: ${menuModel?.name}")
        name.text = menuModel?.name
        Glide.with(this@FoodItemEditActivity).load(Constants.IMAGE_URL + menuModel?.image)
            .into(image_item)
        rv_extra_sizes.adapter = ItemExtraSizeAdapter(this, sizes, cartModel?.extra)
        builder.setPositiveButton(
            "Close"
        ) { dialog, _ ->
            run {
                dialog?.dismiss()
            }
        }
        builder.setCancelable(true);
        builder.setView(view);
        builder.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemExtraListenerDeleted(menuModel: MenuModel?, pos: Int) {
        val e = Extra(menuModel!!.name, "", "", "", "")
        if (extraList.contains(e)) {
            extraList.remove(e)
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
            priceTotal = (this.sizePrice * amount).plus(sizePriceExtras.toDouble())
            binding.priceTotal.text = priceTotal.toString() + " ج.م "
            val extra = Extra(sizeSosTypeName, sizePriceExtra, imageExtra, "1", sizeNameExtra)
            val sum = SumExtra(sizeSosTypeName, sizePriceExtra.toDouble())
            if (extraList.contains(extra)) {
                extraList[extraList.indexOf(extra)] = extra
                Toast.makeText(this, "Exist", Toast.LENGTH_LONG).show()

            } else {
                extraList.add(extra)
//                totalSizePriceExtras += sizePriceExtras.toDouble()
//                Log.e(TAG, "onItemSizeExtraListener: $totalSizePriceExtras")
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
        return sum
    }
}
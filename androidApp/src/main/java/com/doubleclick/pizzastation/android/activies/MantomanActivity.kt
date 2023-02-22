package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
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
import com.doubleclick.pizzastation.android.databinding.ActivityMantomanBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.model.Sizes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MantomanActivity : AppCompatActivity(), ItemSizeListener, DeletedSliceListener {

    private val TAG = "MantomanActivity"
    private lateinit var binding: ActivityMantomanBinding
    private var menusAdded: ArrayList<MenuModel> = ArrayList();
    private lateinit var viewModel: MainViewModel
    private var menus: ArrayList<MenuModel> = ArrayList();
    private lateinit var adapter: SpinnerAdapterSlice
    private lateinit var menuDealAdapter: MenuDealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMantomanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        menuDealAdapter = MenuDealAdapter(menusAdded, this@MantomanActivity)
        binding.rvSlices.adapter = menuDealAdapter
        viewModel.getPizzaInOffer().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(
                    call: Call<MenuList>,
                    response: Response<MenuList>
                ) {
                    Log.e(TAG, "onResponse: ${response.body()!!.data}")

                    for (menu in response.body()!!.data) {
                        menus.add(menu)
                    }

                    adapter = SpinnerAdapterSlice(
                        this@MantomanActivity,
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

        val sizes: ArrayList<Sizes> = ArrayList();
        sizes.add(
            Sizes(
                "FB",
                "FB",
                "",
                "245"
            )
        )
        binding.rvSizes.adapter = ItemSizeAdapter(this@MantomanActivity, sizes, "FB")
        binding.addToCard.setOnClickListener {

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
                menusAdded.add(menus[i])
                menuDealAdapter.notifyItemRangeChanged(0, menusAdded.size)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onItemSizeListener(sizeName: String, sizePrice: String) {
        binding.priceTotal.text = sizePrice
    }

    override fun deleteSlice(pizza: MenuModel, position: Int) {
        try {
            menusAdded.remove(pizza)
            menuDealAdapter.notifyItemRemoved(position)
            menuDealAdapter.notifyItemRangeChanged(0, menusAdded.size)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "deleteSlice: ${e.message}")
        }
    }
}
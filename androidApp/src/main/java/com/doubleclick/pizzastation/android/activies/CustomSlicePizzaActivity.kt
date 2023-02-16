package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.MenuSliceAdapter
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterSlice
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.DeletedSliceListener
import com.doubleclick.pizzastation.android.`interface`.OnSpinnerEventsListener
import com.doubleclick.pizzastation.android.databinding.ActivityCustomSlicePizzaBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomSlicePizzaActivity : AppCompatActivity(), DeletedSliceListener {

    private lateinit var binding: ActivityCustomSlicePizzaBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "CustomSlicePizzaActivit"
    private var menus: ArrayList<MenuModel> = ArrayList();
    private var menusAdded: ArrayList<MenuModel> = ArrayList();
    private lateinit var adapter: SpinnerAdapterSlice
    private lateinit var menuSliceAdapter: MenuSliceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomSlicePizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        menuSliceAdapter = MenuSliceAdapter(menusAdded, this@CustomSlicePizzaActivity)
        binding.rvSlices.adapter = menuSliceAdapter
        viewModel.getPizzaInOffer().observe(this) {
            it.enqueue(object : Callback<MenuList> {
                override fun onResponse(
                    call: Call<MenuList>,
                    response: Response<MenuList>
                ) {
                    Log.e(TAG, "onResponse: ${response.body()!!.data}")
                    menus = response.body()!!.data as ArrayList
                    adapter = SpinnerAdapterSlice(
                        this@CustomSlicePizzaActivity,
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
                menuSliceAdapter.notifyItemRangeChanged(0, menusAdded.size)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun deleteSlice(pizza: MenuModel, position: Int) {
        try {
            menusAdded.remove(pizza)
            menuSliceAdapter.notifyItemRemoved(position)
            menuSliceAdapter.notifyItemRangeChanged(0, menusAdded.size)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "deleteSlice: ${e.message}")
        }

    }
}
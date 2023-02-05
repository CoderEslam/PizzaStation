package com.doubleclick.pizzastation.android

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.Adapter.CustomPizzaItemAdapter
import com.doubleclick.pizzastation.android.Adapter.onPizzaClicked
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityCustomizePizzaBinding
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import kotlinx.android.synthetic.main.activity_customize_pizza.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomizePizzaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomizePizzaBinding
    private lateinit var viewModel: MainViewModel
    private val TAG = "CustomizePizzaActivity"
    private var menus: ArrayList<MenuModel> = ArrayList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizePizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getMenu().observe(this) {
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
        binding.quarter1.setOnClickListener {
            selectQurter(quarter1)
        }
        binding.quarter2.setOnClickListener {
            selectQurter(quarter2)
        }
        binding.quarter3.setOnClickListener {
            selectQurter(quarter3)
        }
        binding.quarter4.setOnClickListener {
            selectQurter(quarter4)
        }

    }

    private fun selectQurter(image: ImageView) {
        val builder = AlertDialog.Builder(this@CustomizePizzaActivity);
        builder.setTitle("select")
        val view: View = LayoutInflater.from(this@CustomizePizzaActivity)
            .inflate(R.layout.rv_custom_pizza, null, false)
        val items: RecyclerView = view.findViewById(R.id.rv_pizza);
        val adapter = CustomPizzaItemAdapter(menus)
        items.adapter = adapter
        adapter.callBack(object : onPizzaClicked {
            override fun onPizza(menu: MenuModel) {
                Glide.with(this@CustomizePizzaActivity).load(IMAGE_URL + menu.image).into(image)
            }
        })
        builder.setCancelable(true);
        builder.setView(view);
        builder.show()
    }


}
package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.OldOrderAdapter
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityOrdersBinding
import com.doubleclick.pizzastation.android.model.OldOrderList
import com.doubleclick.pizzastation.android.model.OldOrderModel
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var viewModel: MainViewModel
    private var oldOrderList: ArrayList<OldOrderModel> = ArrayList();
    private lateinit var oldOrderAdapter: OldOrderAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(RepositoryRemot())
        )[MainViewModel::class.java]
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getOrderInProgressList(
                "Bearer " + SessionManger.getToken(this@OrdersActivity).toString()
            )
                .observe(this@OrdersActivity) {
                    it.enqueue(object : Callback<JsonObject> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            Log.e("OldOrderList", "onResponse: ${response.body()}")
                            val gson = Gson()
                            val old = gson.fromJson(response.body(), OldOrderList::class.java)
                            Log.e("OldOrderList", "onResponse: $old")
                            oldOrderList = gson.fromJson(response.body(), OldOrderList::class.java)!! as ArrayList<OldOrderModel>
                            oldOrderAdapter = OldOrderAdapter(
                                oldOrderList,
                            )
//                            binding.oldOrderRv.adapter = oldOrderAdapter
//                            binding.animationView.visibility = View.GONE
//                            oldOrderAdapter.notifyItemRangeChanged(0, oldOrderList.size)
//                            oldOrderAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        }

                    })
                }
        }

    }
}
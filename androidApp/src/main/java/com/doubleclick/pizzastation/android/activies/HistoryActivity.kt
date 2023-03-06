package com.doubleclick.pizzastation.android.activies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.CartAdapter
import com.doubleclick.pizzastation.android.Adapter.OldOrderAdapter
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.databinding.ActivityHistoryBinding
import com.doubleclick.pizzastation.android.databinding.ActivityHomeBinding
import com.doubleclick.pizzastation.android.model.CartModel
import com.doubleclick.pizzastation.android.model.OldOrderList
import com.doubleclick.pizzastation.android.model.OldOrderModel
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: MainViewModel
    private var oldOrderList: ArrayList<OldOrderModel> = ArrayList();
    private lateinit var oldOrderAdapter: OldOrderAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MainViewModelFactory(RepositoryRemot()))[MainViewModel::class.java]
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getOldOrderList("Bearer "+getToken(this@HistoryActivity).toString())
                .observe(this@HistoryActivity) {
                    it.enqueue(object : Callback<OldOrderList> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<OldOrderList>,
                            response: Response<OldOrderList>
                        ) {
//                            oldOrderList = response.body()!!.data as ArrayList<OldOrderModel>
//                            oldOrderAdapter = OldOrderAdapter(
//                                oldOrderList,
//                            )
//                            binding.oldOrderRv.adapter = oldOrderAdapter
//                            binding.animationView.visibility = View.GONE
//                            oldOrderAdapter.notifyItemRangeChanged(0, oldOrderList.size)
//                            oldOrderAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<OldOrderList>, t: Throwable) {

                        }

                    })
                }
        }

    }
}
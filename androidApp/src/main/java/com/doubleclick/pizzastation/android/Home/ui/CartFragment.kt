package com.doubleclick.pizzastation.android.Home.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.doubleclick.pizzastation.android.Adapter.CartAdapter
import com.doubleclick.pizzastation.android.Adapter.SpinnerAdapterGoverorate
import com.doubleclick.pizzastation.android.Home.BottomSheetNotesFragment
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.ExtraDeleteListener
import com.doubleclick.pizzastation.android.databinding.FragmentCartBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.SessionManger.getToken
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), ExtraDeleteListener {

    private lateinit var binding: FragmentCartBinding
    private var carts: ArrayList<CartModel> = ArrayList();
    lateinit var cartAdapter: CartAdapter
    private lateinit var viewModel: MainViewModel
    private var governorateModelList: List<GovernorateModel> = ArrayList()
    private var branchesModelList: List<AreasModel> = ArrayList()

    private val TAG = "CartFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory(RepositoryRemot())
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                async {
                    viewModel.getAreas().observe(viewLifecycleOwner) {
                        it.clone().enqueue(object : Callback<AreasList> {
                            override fun onResponse(
                                call: Call<AreasList>,
                                response: Response<AreasList>
                            ) {
                                branchesModelList = response.body()!!.data
                            }

                            override fun onFailure(call: Call<AreasList>, t: Throwable) {

                            }

                        })
                    }
                }.await()
                async {
                    viewModel.getGovernorate().observe(viewLifecycleOwner) {
                        it.clone().enqueue(object : Callback<GovernorateList> {
                            override fun onResponse(
                                call: Call<GovernorateList>,
                                response: Response<GovernorateList>
                            ) {
                                governorateModelList = response.body()!!.data
                            }

                            override fun onFailure(call: Call<GovernorateList>, t: Throwable) {

                            }

                        })
                    }
                }.await()

                viewModel.getCart("Bearer " + getToken(requireActivity()))
                    .observe(requireActivity()) {
                        it.clone().enqueue(object : Callback<CartModelList> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<CartModelList>,
                                response: Response<CartModelList>
                            ) {
                                try {
                                    carts = response.body()!!.data as ArrayList<CartModel>
                                    cartAdapter = CartAdapter(
                                        carts,
                                        ::Counter,
                                        ::OnActionClicked,
                                        this@CartFragment
                                    )
                                    binding.rvCart.adapter = cartAdapter
                                    binding.animationViewLoading.visibility = View.GONE
                                    cartAdapter.notifyItemRangeChanged(0, carts.size)
                                    cartAdapter.notifyDataSetChanged()
                                } catch (e: NullPointerException) {
                                    Log.e(TAG, "onResponse getCart: ${e.message}")
                                }

                            }

                            override fun onFailure(call: Call<CartModelList>, t: Throwable) {
                                Log.e(TAG, "onFailure: " + t.message)
                            }

                        })
                    }
            }
        }



        binding.selectLocation.setOnClickListener {
            if (carts.size == 0) {
                binding.selectLocation.setTextColor(resources.getColor(R.color.grey_600))
                Toast.makeText(requireActivity(), "Choose item first", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                var total = 0.0
                var amount = 0
                for (cart in carts) {
                    total += cart.price.toDouble()
                    amount++
                }
                val sheet = BottomSheetNotesFragment(
                    carts,
                    total,
                    amount,
                    governorateModelList,
                    branchesModelList
                )
                sheet.show(
                    requireActivity().supportFragmentManager,
                    "BottomSheetNotesFragment"
                )
            }
        }


    }

    private fun OnActionClicked(cartModel: CartModel, action: SwipeAction, pos: Int) {
        when (action.actionId) {
            R.id.delete -> {
                try {
                    lifecycleScope.launch {
                        viewModel.deleteCartById(
                            "Bearer " + getToken(requireActivity()).toString(),
                            cartModel.id.toString()
                        ).observe(viewLifecycleOwner) { it ->
                            try {
                                it.clone().enqueue(object : Callback<MessageCallback> {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onResponse(
                                        call: Call<MessageCallback>,
                                        response: Response<MessageCallback>
                                    ) {
                                        try {
                                            cartAdapter.notifyItemRemoved(pos)
                                            cartAdapter.notifyItemRangeChanged(0, carts.size)
                                            cartAdapter.notifyDataSetChanged()
                                            carts.removeAt(pos);
                                            Toast.makeText(
                                                requireActivity(),
                                                "Deleted",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (e: IndexOutOfBoundsException) {
                                            Log.e(TAG, "onResponse deleted: ${e.message}")

                                        }

                                    }

                                    override fun onFailure(
                                        call: Call<MessageCallback>,
                                        t: Throwable
                                    ) {

                                    }

                                })
                            } catch (e: IllegalStateException) {
                                Log.e(TAG, "OnActionClicked: ${e.message}")
                            }
                        }

                    }
                } catch (e: NullPointerException) {
                    Log.d(TAG, "onResponseNullPointerException: ${e.message}")
                }
            }
        }
    }

    private fun Counter(input: Int) {
        Log.e("TAG", "Counter: $input")
    }

    override fun onExtraDeleteListener(pos: Int, extra: Extra, posParent: Int) {
        carts[posParent].extra?.removeAt(pos)
        cartAdapter.notifyItemRangeChanged(0, carts.size);
        Log.e(TAG, "onExtraDeleteListener: ${extra.name}   -  $posParent")
    }

}

package com.doubleclick.pizzastation.android.Home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.doubleclick.pizzastation.android.Adapter.CartAdapter
import com.doubleclick.pizzastation.android.Home.BottomSheetNotesFragment
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.Repository.remot.RepositoryRemot
import com.doubleclick.pizzastation.android.ViewModel.MainViewModel
import com.doubleclick.pizzastation.android.ViewModel.MainViewModelFactory
import com.doubleclick.pizzastation.android.`interface`.ExtraDeleteListener
import com.doubleclick.pizzastation.android.`interface`.SendNotes
import com.doubleclick.pizzastation.android.databinding.FragmentCartBinding
import com.doubleclick.pizzastation.android.model.*
import com.doubleclick.pizzastation.android.utils.SessionManger
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CartFragment : Fragment(), ExtraDeleteListener, SendNotes {

    private lateinit var binding: FragmentCartBinding
    private var carts: ArrayList<CartModel> = ArrayList();
    lateinit var cartAdapter: CartAdapter
    private lateinit var viewModel: MainViewModel
    private var notes: String = ""

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
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.getCart("Bearer " + SessionManger.getToken(requireActivity()))
                .observe(requireActivity()) {
                    it.enqueue(object : Callback<CartModelList> {
                        override fun onResponse(
                            call: Call<CartModelList>,
                            response: Response<CartModelList>
                        ) {
                            carts.addAll(response.body()!!.data)
                            Log.e(TAG, "onResponse: $carts")
                            cartAdapter =
                                CartAdapter(carts, ::Counter, ::OnActionClicked, this@CartFragment)
                            binding.rvCart.adapter = cartAdapter
                        }

                        override fun onFailure(call: Call<CartModelList>, t: Throwable) {
                            Log.e(TAG, "onFailure: " + t.message)
                        }

                    })
                }
        }
        binding.selectLocation.setOnClickListener {
            val sheet = BottomSheetNotesFragment(this@CartFragment)
            sheet.show(
                requireActivity().supportFragmentManager,
                "BottomSheetNotesFragment"
            )
            val parentJsonObject = JsonObject();
            parentJsonObject.addProperty("total", "100")
            parentJsonObject.addProperty("delivery", "120")
            parentJsonObject.addProperty("amount", "25")
            parentJsonObject.addProperty("status", "mobile_app")
            parentJsonObject.addProperty("notes", notes)
            parentJsonObject.addProperty("area_id", "1")
            parentJsonObject.addProperty("branch_id", "1")
            val jsonArrayChildItems = JsonArray();
            val os = ByteArrayOutputStream()
            for (cart in carts) {
                val jsonObjectItemsChild = JsonObject();
                jsonObjectItemsChild.addProperty("name", cart.name)
                jsonObjectItemsChild.addProperty("price", cart.price)
                jsonObjectItemsChild.addProperty("size", cart.size)
                jsonObjectItemsChild.addProperty("quantity", cart.quantity)
                val jsonArrayChildExtras = JsonArray();
                try {
                    if (cart.extra?.isNotEmpty() == true) {
                        for (extra in cart.extra) {
                            val jsonObjectChildExtra = JsonObject();
                            jsonObjectChildExtra.addProperty("name", extra.name)
                            jsonObjectChildExtra.addProperty("price", extra.price)
                            jsonObjectChildExtra.addProperty("size", extra.size)
                            jsonObjectChildExtra.addProperty("quantity", extra.quantity)
                            jsonArrayChildExtras.add(jsonObjectChildExtra)
                            jsonObjectItemsChild.add("extra", jsonArrayChildExtras)
                        }
                    } else {

                    }
                    jsonArrayChildItems.add(jsonObjectItemsChild);
                } catch (e: NullPointerException) {
                    Log.e(TAG, "onViewCreated: ${e.message}")
                }
            }
            parentJsonObject.add("items", jsonArrayChildItems);
            Log.d(TAG, "onViewCreated: $parentJsonObject")
            /* viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                 viewModel.setOrderComplete(
                     "Bearer " + SessionManger.getToken(requireActivity()).toString(),
                     parentJsonObject
                 ).observe(viewLifecycleOwner) {
                     it.enqueue(object : Callback<OrderModelList> {
                         override fun onResponse(
                             call: Call<OrderModelList>,
                             response: Response<OrderModelList>
                         ) {
                             Toast.makeText(
                                 requireActivity(),
                                 response.body()!!.message.toString(),
                                 Toast.LENGTH_LONG
                             ).show()
                         }

                         override fun onFailure(call: Call<OrderModelList>, t: Throwable) {
                             Toast.makeText(
                                 requireActivity(),
                                 t.message,
                                 Toast.LENGTH_LONG
                             ).show()
                         }
                     })
                 }
             }*/
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun OnActionClicked(cartModel: CartModel, action: SwipeAction, pos: Int) {
        when (action.actionId) {
            R.id.delete -> {
                try {
                    GlobalScope.launch(Dispatchers.Main) {
                        viewModel.deleteCartById(
                            "Bearer " + SessionManger.getToken(requireActivity()).toString(),
                            cartModel.id.toString()
                        ).observe(viewLifecycleOwner) { it ->
                            try {
                                it.enqueue(object : Callback<MessageCallback> {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun onResponse(
                                        call: Call<MessageCallback>,
                                        response: Response<MessageCallback>
                                    ) {
                                        try {
                                            if (carts.size == 1) {
                                                carts.removeAt(0);
                                                cartAdapter.notifyItemRemoved(0)
                                                cartAdapter.notifyItemRangeChanged(0, carts.size)
                                                cartAdapter.notifyDataSetChanged()
                                            }
                                            cartAdapter.notifyItemRemoved(pos)
                                            cartAdapter.notifyItemRangeChanged(0, carts.size)
                                            cartAdapter.notifyDataSetChanged()
                                            carts.removeAt(pos);
                                            Toast.makeText(
                                                requireActivity(),
                                                "" + response.body()?.message.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } catch (e: IndexOutOfBoundsException) {
                                            Log.e(TAG, "onResponse: ${e.message}")

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

    override fun onTextNote(text: String) {
        notes = text;
        Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
    }


}

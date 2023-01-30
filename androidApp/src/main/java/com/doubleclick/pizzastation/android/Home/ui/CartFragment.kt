package com.doubleclick.pizzastation.android.Home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.doubleclick.pizzastation.android.Adapter.CartAdapter
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.databinding.FragmentCartBinding
import com.doubleclick.pizzastation.android.model.Cart
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private var count: Int = 0
    lateinit var cart: MutableList<Cart>
    lateinit var cartAdapter: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cart = mutableListOf(
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
            Cart(1, "name"),
        )
        cartAdapter = CartAdapter(cart, ::Counter, ::OnActionClicked)
        binding.rvCart.adapter = cartAdapter
    }

    private fun OnActionClicked(contact: Cart, action: SwipeAction, pos: Int) {
        when (action.actionId) {
            R.id.delete -> {
                Toast.makeText(requireActivity(), "deleted", Toast.LENGTH_LONG)
                    .show()
                cart.removeAt(pos);
                cartAdapter.notifyItemRemoved(pos)
            }
        }
    }

    private fun Counter(input: Int) {
        Log.e("TAG", "Counter: $input")
    }


}
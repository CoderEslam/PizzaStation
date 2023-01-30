package com.doubleclick.pizzastation.android.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.Cart
import com.doubleclick.pizzastation.android.model.CartModel
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.ActionBindHelper
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeMenuListener
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeToActionLayout

/**
 * Created By Eslam Ghazy on 1/3/2023
 */
private const val TAG = "CartAdapter"

//lambda function
typealias OnActionClicked = (contact: CartModel, action: SwipeAction, pos: Int) -> Unit
typealias Block = (input: Int) -> Unit

class CartAdapter(
    private val carts: List<CartModel>,
    private val block: Block,
    private val actionClicked: OnActionClicked
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val actionsBindHelper = ActionBindHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_swipe_to_action,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        actionsBindHelper.bind("", holder.swipeToActionLayout)
        holder.bind()
        holder.rv_item_extra_cart.adapter = ItemExtraAdapter(carts[position].extra);
        holder.add.setOnClickListener {
            carts[position].count = carts[position].count + 1
            holder.count.text = carts[position].count.toString()
            Log.e(TAG, "onBindViewHolder: " + carts[position].count)
            notifyItemChanged(position)
        }
        holder.mins.setOnClickListener {
            carts[position].count = carts[position].count - 1
            holder.count.text = carts[position].count.toString()
            notifyItemChanged(position)
        }
        block(carts[position].count)
    }


    override fun getItemCount(): Int {
        return carts.size;
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SwipeMenuListener {
        var add: ImageView = itemView.findViewById(R.id.add)
        var mins: ImageView = itemView.findViewById(R.id.mins)
        var count: TextView = itemView.findViewById(R.id.count)
        var rv_item_extra_cart: RecyclerView = itemView.findViewById(R.id.rv_item_extra_cart)
        val swipeToActionLayout: SwipeToActionLayout =
            itemView.findViewById(R.id.swipe_to_action_layout)

        fun bind() {
            swipeToActionLayout.menuListener = this
        }

        override fun onClosed(view: View) {
            try {
                val cart = carts[adapterPosition]
                actionsBindHelper.closeOtherThan(cart.name)
            } catch (e: IndexOutOfBoundsException) {
                Log.e(TAG, "onClosed: ${e.message}")
            }
        }

        override fun onOpened(view: View) {

        }

        override fun onFullyOpened(view: View, quickAction: SwipeAction) {

        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            actionClicked(carts[adapterPosition], action, adapterPosition)
            swipeToActionLayout.close()
        }
    }


}
package com.doubleclick.pizzastation.android.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.FoodItemActivity
import com.doubleclick.pizzastation.android.FoodItemEditActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.`interface`.ExtraDeleteListener
import com.doubleclick.pizzastation.android.model.Cart
import com.doubleclick.pizzastation.android.model.CartModel
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.ActionBindHelper
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeMenuListener
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeToActionLayout
import de.hdodenhof.circleimageview.CircleImageView

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
    private val actionClicked: OnActionClicked,
    private val extraDeleteListener: ExtraDeleteListener
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
        try {
            holder.rv_item_extra_cart.adapter =
                carts[position].extra?.let { ItemExtraAdapter(it, extraDeleteListener, position) };
        } catch (e: NullPointerException) {
            Log.e(TAG, "onBindViewHolder: ${e.message}")
        }

        Glide.with(holder.itemView.context).load(IMAGE_URL + carts[position].image)
            .into(holder.image_item)
        holder.name_item.text = carts[position].name
        holder.price_total.text = carts[position].price
        holder.count.text = carts[position].quantity
        block(carts[position].count)
        holder.edit.setOnClickListener {
            val intent = Intent(
                holder.itemView.context,
                FoodItemEditActivity::class.java
            )
            intent.putExtra("cartModel", carts[position])
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return carts.size;
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        SwipeMenuListener {
        var count: TextView = itemView.findViewById(R.id.count)
        var image_item: CircleImageView = itemView.findViewById(R.id.image_item)
        var edit: ImageView = itemView.findViewById(R.id.edit)
        var name_item: TextView = itemView.findViewById(R.id.name_item)
        var price_total: TextView = itemView.findViewById(R.id.price_total)
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
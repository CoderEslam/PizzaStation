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
import com.doubleclick.pizzastation.android.activies.FoodItemEditActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.`interface`.ExtraDeleteListener
import com.doubleclick.pizzastation.android.model.CartModel
import com.doubleclick.pizzastation.android.model.OldOrderList
import com.doubleclick.pizzastation.android.model.OldOrderModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import com.doubleclick.pizzastation.android.utils.Constants.OFFER
import com.doubleclick.pizzastation.android.utils.Constants.OFFERS_URL
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.ActionBindHelper
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeAction
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeMenuListener
import com.doubleclick.pizzastation.android.views.swipetoactionlayout.SwipeToActionLayout
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created By Eslam Ghazy on 1/3/2023
 */
private const val TAG = "CartAdapter"

class OldOrderAdapter(
    private val oldOrderModel: List<OldOrderModel>,
) : RecyclerView.Adapter<OldOrderAdapter.OldOrderViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OldOrderViewHolder {
        return OldOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recent_order, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OldOrderViewHolder, position: Int) {


    }


    override fun getItemCount(): Int = oldOrderModel.size;


    class OldOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var count: TextView = itemView.findViewById(R.id.count)
        var image_item: CircleImageView = itemView.findViewById(R.id.image_item)
        var name_item: TextView = itemView.findViewById(R.id.name_item)
        var price_total: TextView = itemView.findViewById(R.id.price_total)
        var item_size: TextView = itemView.findViewById(R.id.item_size)
        var time: TextView = itemView.findViewById(R.id.time)
        var rv_item_pizza_selected: RecyclerView =
            itemView.findViewById(R.id.rv_item_pizza_selected)
        val swipeToActionLayout: SwipeToActionLayout =
            itemView.findViewById(R.id.swipe_to_action_layout)
    }


}
package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import de.hdodenhof.circleimageview.CircleImageView

class ItemInOfferAdapter(val pizzas: List<MenuModel?>?) :
    RecyclerView.Adapter<ItemInOfferAdapter.ItemInOfferViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemInOfferViewHolder {
        return ItemInOfferViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pizza_selected, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemInOfferViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(IMAGE_URL + pizzas?.get(position)?.image)
            .into(holder.image_item)
        holder.name_item.text = pizzas?.get(position)?.name
    }

    override fun getItemCount(): Int {
        if (pizzas?.isEmpty() == true) {
            return 0;
        } else {
            return pizzas!!.size
        }
    }

    class ItemInOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image_item: CircleImageView = itemView.findViewById(R.id.image_item);
        val name_item: TextView = itemView.findViewById(R.id.name_item);
    }

}
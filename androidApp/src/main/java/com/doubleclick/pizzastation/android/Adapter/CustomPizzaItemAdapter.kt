package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import de.hdodenhof.circleimageview.CircleImageView

class CustomPizzaItemAdapter(val menus: ArrayList<MenuModel>) :
    RecyclerView.Adapter<CustomPizzaItemAdapter.CustomPizzaItemViewHolder>() {

    private lateinit var onPizzaClicked: onPizzaClicked

    fun callBack(onPizzaClicked: onPizzaClicked) {
        this.onPizzaClicked = onPizzaClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomPizzaItemViewHolder {
        return CustomPizzaItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_pizza, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomPizzaItemViewHolder, position: Int) {
        holder.name.text = menus[position].name
        Glide.with(holder.itemView.context).load(IMAGE_URL + menus[position].image).into(holder.image)
        holder.itemView.setOnClickListener {
            onPizzaClicked.onPizza(menus[position])
        }
    }

    override fun getItemCount(): Int = menus.size

    inner class CustomPizzaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: CircleImageView = itemView.findViewById(R.id.image);
        val name: TextView = itemView.findViewById(R.id.name);
    }


}

interface onPizzaClicked {
    fun onPizza(menu: MenuModel)
}
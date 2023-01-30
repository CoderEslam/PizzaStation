package com.doubleclick.pizzastation.android.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Scroller
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.FoodItemActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.MenuViewHolder
import com.doubleclick.pizzastation.android.model.MenuList
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL

/**
 * Created By Eslam Ghazy on 1/23/2023
 */
class MenuAdapter(private var menus: List<MenuModel>) :
    RecyclerView.Adapter<MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.name.text = menus[position].name
        holder.name.isSelected = true
        Glide.with(holder.itemView.context)
            .load(IMAGE_URL + menus[position].image).into(holder.imageItem)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodItemActivity::class.java);
            intent.putExtra("menu", menus[position])
            holder.itemView.context.startActivity(intent);
        }
    }


}
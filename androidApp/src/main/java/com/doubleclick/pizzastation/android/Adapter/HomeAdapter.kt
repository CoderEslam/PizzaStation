package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.HomeViewHolder
import com.doubleclick.pizzastation.android.model.CategoricalMenu

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class HomeAdapter(private val menu: ArrayList<CategoricalMenu>) :
    RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.rv_category.adapter = MenuAdapter(menu[position].menus)
        holder.catrgory_name.text = menu[position].CategoryModel.category
    }
}
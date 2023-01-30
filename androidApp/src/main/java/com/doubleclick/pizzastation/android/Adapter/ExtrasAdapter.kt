package com.doubleclick.pizzastation.android.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.ItemExtrasViewHolder
import com.doubleclick.pizzastation.android.`interface`.ItemExtraListener
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL


/**
 * Created By Eslam Ghazy on 1/21/2023
 */

class ExtrasAdapter(itemListener: ItemExtraListener, menuModel: List<MenuModel>) :
    RecyclerView.Adapter<ItemExtrasViewHolder>() {

    private var itemListener: ItemExtraListener
    private var menuModel: List<MenuModel> = ArrayList();

    init {
        this.itemListener = itemListener
        this.menuModel = menuModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemExtrasViewHolder {
        return ItemExtrasViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_menu,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return menuModel.size;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemExtrasViewHolder, position: Int) {

        holder.name.text = menuModel[position].name
        Glide.with(holder.itemView.context).load(IMAGE_URL + menuModel[position].image)
            .into(holder.imageItem)

        holder.itemView.setOnClickListener {
            itemListener.onItemExtraListener(menuModel[position])
        }

    }


}

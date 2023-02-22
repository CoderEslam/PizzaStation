package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.SliceViewHolder
import com.doubleclick.pizzastation.android.`interface`.DeletedSliceListener
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL

/**
 * Created By Eslam Ghazy on 1/23/2023
 */
class MenuDealAdapter(
    private var menus: List<MenuModel>,
    val deletedSliceListener: DeletedSliceListener
) :
    RecyclerView.Adapter<SliceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliceViewHolder {
        return SliceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_deal_item, parent, false)
        )
    }

    override fun getItemCount(): Int = menus.size


    override fun onBindViewHolder(holder: SliceViewHolder, position: Int) {
        holder.name_slice.text = menus[position].name
        holder.name_slice.isSelected = true
        Glide.with(holder.itemView.context).load(IMAGE_URL + menus[position].image)
            .into(holder.slice_image)
        holder.itemView.setOnClickListener {
            deletedSliceListener.deleteSlice(menus[position] ,position)
        }
    }


}
package com.doubleclick.pizzastation.android.Adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.ItemCategoryViewHolder
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.model.CategoryList
import com.doubleclick.pizzastation.android.model.CategoryModel
import com.doubleclick.pizzastation.android.model.MenuModel


/**
 * Created By Eslam Ghazy on 1/21/2023
 */
class ItemCategoryAdapter(itemListener: itemListener, itemSize: List<MenuModel>) :
    RecyclerView.Adapter<ItemCategoryViewHolder>() {

    private var lastCheckedPosition = ""
    private var itemListener: itemListener
    private var itemSize: List<MenuModel>

    init {
        this.itemListener = itemListener
        this.itemSize = itemSize
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCategoryViewHolder {
        return ItemCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_size,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return itemSize.size;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {

        holder.tv_size.text = itemSize[position].category
        holder.tv_size.text = itemSize[position].category
        if (itemSize[position].category == lastCheckedPosition) {
            val img: Drawable =
                holder.itemView.context.resources.getDrawable(R.drawable.ic_close_black_24dp)
            img.setBounds(0, 0, 24, 24)
            holder.tv_size.setCompoundDrawables(
                img,
                null,
                null,
                null
            )
            holder.tv_size.setTextColor(
                holder.itemView.context.resources.getColor(
                    R.color.white
                )
            )
            holder.card.background = holder.itemView.context.resources.getDrawable(R.color.red)

        } else {
            val img: Drawable =
                holder.itemView.context.resources.getDrawable(R.drawable.ic_close_black_24dp)
            img.setBounds(0, 0, 0, 0)
            (holder as ItemCategoryViewHolder).tv_size.setCompoundDrawables(
                null,
                null,
                null,
                null
            )
            holder.card.background = holder.itemView.context.resources.getDrawable(R.color.yellow)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemListener.mListener(itemSize[position].category!!)
            lastCheckedPosition = itemSize[position].category.toString()
            notifyItemRangeChanged(0, itemSize.size)
        })
    }

}
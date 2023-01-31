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
import com.doubleclick.pizzastation.android.`interface`.ItemSizeListener
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.model.CategoryList
import com.doubleclick.pizzastation.android.model.CategoryModel
import com.doubleclick.pizzastation.android.model.Sizes

/**
 * Created By Eslam Ghazy on 1/21/2023
 */

class ItemSizeAdapter(itemListener: ItemSizeListener, sizes: ArrayList<Sizes>) :
    RecyclerView.Adapter<ItemCategoryViewHolder>() {

    private var lastCheckedPosition = -1
    var itemListener: ItemSizeListener
    var sizes: ArrayList<Sizes>

    init {
        this.itemListener = itemListener
        this.sizes = sizes
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
        return sizes.size;
    }

    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {

        initializeViews(sizes[position], holder);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initializeViews(model: Sizes, holder: ItemCategoryViewHolder) {
        holder.tv_size.text = model.sizeName
        holder.tv_price.text = model.sizePrice
        holder.tv_size.isSelected = true
        if (model.id == lastCheckedPosition) {
            holder.tv_size.setTextColor(
                holder.itemView.context.resources.getColor(
                    R.color.yellow
                )
            )
            holder.card
                .setCardBackgroundColor(
                    holder.itemView.context.resources.getColor(
                        R.color.red
                    )
                )
        } else {
            holder.card.setCardBackgroundColor(
                holder.itemView.context.resources.getColor(
                    R.color.white
                )
            )

            holder.tv_size.setTextColor(
                holder.itemView.context.resources.getColor(
                    R.color.white
                )
            )
        }
        holder.itemView.setOnClickListener {
            itemListener.onItemSizeListener(model.sizeName, model.sizePrice)
            lastCheckedPosition = model.id
            notifyItemRangeChanged(0, sizes.size)
        }
    }

}

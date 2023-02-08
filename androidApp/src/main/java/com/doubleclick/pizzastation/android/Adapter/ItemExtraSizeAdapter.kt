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
import com.doubleclick.pizzastation.android.`interface`.ItemSizeExtraListener
import com.doubleclick.pizzastation.android.`interface`.ItemSizeListener
import com.doubleclick.pizzastation.android.`interface`.itemListener
import com.doubleclick.pizzastation.android.model.CategoryList
import com.doubleclick.pizzastation.android.model.CategoryModel
import com.doubleclick.pizzastation.android.model.Extra
import com.doubleclick.pizzastation.android.model.Sizes

/**
 * Created By Eslam Ghazy on 1/21/2023
 */

class ItemExtraSizeAdapter(
    itemListener: ItemSizeExtraListener,
    sizes: ArrayList<Sizes>,
    lastCheckedPosition: String = ""
) :
    RecyclerView.Adapter<ItemCategoryViewHolder>() {

    private var lastCheckedPosition: String
    var itemListener: ItemSizeExtraListener
    var sizes: ArrayList<Sizes>


    init {
        this.lastCheckedPosition = lastCheckedPosition
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemCategoryViewHolder, position: Int) {
        holder.tv_size.text = sizes[position].sizeName
        holder.tv_price.text = sizes[position].sizePrice
        holder.tv_size.isSelected = true
        if (sizes[position].sizeName == lastCheckedPosition) {
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
            itemListener.onItemSizeExtraListener(
                sizes[position].sizeSosTypeName,
                sizes[position].sizePrice,
                sizes[position].sizeName,
                sizes[position].image
            )
            lastCheckedPosition = sizes[position].sizeName
            notifyItemRangeChanged(0, sizes.size)
        }
    }


}

package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.neumorph.NeumorphCardView

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class ItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_size: TextView = itemView.findViewById(R.id.tv_size);
    val tv_price: TextView =
        itemView.findViewById(R.id.tv_price);
    val card: CardView = itemView.findViewById(R.id.card);
}

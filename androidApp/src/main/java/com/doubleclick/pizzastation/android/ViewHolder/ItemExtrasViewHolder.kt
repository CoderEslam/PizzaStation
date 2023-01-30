package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.neumorph.NeumorphCardView

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class ItemExtrasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name);
    val price: TextView = itemView.findViewById(R.id.price);
    val imageItem: ImageView = itemView.findViewById(R.id.image_item);
    val card_item_menu: CardView = itemView.findViewById(R.id.card_item_menu);
}

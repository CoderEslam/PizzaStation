package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.name);
    val imageItem: ImageView = itemView.findViewById(R.id.image_item);
}
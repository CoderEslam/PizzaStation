package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val catrgory_name: TextView = itemView.findViewById(R.id.catrgory_name)
    val rv_category: RecyclerView = itemView.findViewById(R.id.rv_category)
}
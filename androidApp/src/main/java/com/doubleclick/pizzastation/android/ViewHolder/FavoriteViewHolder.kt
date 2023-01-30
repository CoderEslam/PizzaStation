package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.shinebutton.ShineButton

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val shineButton: ShineButton = itemView.findViewById(R.id.favorite)
}
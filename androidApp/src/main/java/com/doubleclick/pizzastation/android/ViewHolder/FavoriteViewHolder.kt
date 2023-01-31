package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.shinebutton.ShineButton
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val shineButton: ShineButton = itemView.findViewById(R.id.favorite)
    val image_food: CircleImageView = itemView.findViewById(R.id.image_food)
    val name_food: TextView = itemView.findViewById(R.id.name_food)
}
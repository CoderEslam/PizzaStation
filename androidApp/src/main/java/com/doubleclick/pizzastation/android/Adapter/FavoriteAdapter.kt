package com.doubleclick.pizzastation.android.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.activies.FoodItemActivity
import com.doubleclick.pizzastation.android.activies.HomeActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.FavoriteViewHolder
import com.doubleclick.pizzastation.android.`interface`.OnFavoriteCheckedItem
import com.doubleclick.pizzastation.android.model.FavoriteModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL

/**
 * Created By Eslam Ghazy on 1/14/2023
 */
class FavoriteAdapter(
    val favoriteModel: List<FavoriteModel>,
    val activity: HomeActivity,
    val onFavoriteCheckedItem: OnFavoriteCheckedItem
) :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.shineButton.init(activity = activity)
        holder.shineButton.setChecked(true)
        Glide.with(holder.itemView.context).load(IMAGE_URL + favoriteModel[position].menu.image)
            .into(holder.image_food)
        holder.name_food.text = favoriteModel[position].menu.name
        holder.shineButton.setOnClickListener {
            onFavoriteCheckedItem.onFavoriteChecked(position, favoriteModel[position])
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, FoodItemActivity::class.java)
            intent.putExtra("menu", favoriteModel[position].menu)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return favoriteModel.size
    }


}
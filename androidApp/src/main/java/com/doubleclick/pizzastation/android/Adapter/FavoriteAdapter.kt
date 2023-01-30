package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.HomeActivity
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.FavoriteViewHolder
import com.doubleclick.pizzastation.android.views.shinebutton.ShineButton

/**
 * Created By Eslam Ghazy on 1/14/2023
 */
class FavoriteAdapter(activity: HomeActivity) :
    RecyclerView.Adapter<FavoriteViewHolder>() {

    private val activity: HomeActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.shineButton.init(activity = activity)
    }

    override fun getItemCount(): Int {
        return 20
    }


    init {
        this.activity = activity
    }

}
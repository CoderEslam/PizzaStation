package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.AddressViewHolder

/**
 * Created By Eslam Ghazy on 1/14/2023
 */
class AddressAdapter : RecyclerView.Adapter<AddressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {

        return AddressViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_location, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }


}
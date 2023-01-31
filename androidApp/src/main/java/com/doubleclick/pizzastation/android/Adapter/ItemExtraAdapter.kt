package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.`interface`.ExtraDeleteListener
import com.doubleclick.pizzastation.android.model.Extra


class ItemExtraAdapter(
    private val extra: List<Extra>,
    private val extraDeleteListener: ExtraDeleteListener,
    private val pos: Int
) :
    RecyclerView.Adapter<ItemExtraAdapter.ItemExtraViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemExtraViewHolder {
        return ItemExtraViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_extra_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemExtraViewHolder, position: Int) {
        holder.name_extra.text = extra[position].name
        holder.size_extra.text = extra[position].size
        holder.delete_item_extra.setOnClickListener {
            extraDeleteListener.onExtraDeleteListener(position, extra[position], pos)
        }
    }

    override fun getItemCount(): Int {
        return extra.size
    }

    class ItemExtraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name_extra: TextView = itemView.findViewById(R.id.name_extra);
        val size_extra: TextView = itemView.findViewById(R.id.size_extra);
        val delete_item_extra: ImageView =
            itemView.findViewById(R.id.delete_item_extra);
    }
}
package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R

class SliceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name_slice: TextView = itemView.findViewById(R.id.name_slice);
    val slice_image: ImageView = itemView.findViewById(R.id.slice_image);
    val delete: ImageView = itemView.findViewById(R.id.delete);
}
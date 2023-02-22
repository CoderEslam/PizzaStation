package com.doubleclick.pizzastation.android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.MenuModel
import com.doubleclick.pizzastation.android.utils.Constants.IMAGE_URL
import de.hdodenhof.circleimageview.CircleImageView


class SpinnerAdapterSlice(
    var context: Context,
    val menuModel: List<MenuModel>
) : BaseAdapter() {


    override fun getCount(): Int {
        return menuModel.size
    }

    override fun getItem(i: Int): Any {
        return i;
    }

    override fun getItemId(i: Int): Long {
        return i.toLong();
    }

    override fun getView(i: Int, p1: View?, viewGroup: ViewGroup?): View {
        val rootView: View =
            LayoutInflater.from(context)
                .inflate(R.layout.text_spinner_slice_layout, viewGroup, false)
        val text: TextView = rootView.findViewById(R.id.text_spinner);
        val image_pizza: CircleImageView = rootView.findViewById(R.id.image_pizza);
        text.text = menuModel[i].name
        Glide.with(context).load(IMAGE_URL + menuModel[i].image).into(image_pizza)
        return rootView;
    }
}
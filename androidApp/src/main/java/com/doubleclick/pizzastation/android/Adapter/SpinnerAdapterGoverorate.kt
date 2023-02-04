package com.doubleclick.pizzastation.android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.GovernorateModel


class SpinnerAdapterGoverorate(
    var context: Context,
    val governorateModelList: List<GovernorateModel>
) : BaseAdapter() {


    override fun getCount(): Int {
        return governorateModelList.size
    }

    override fun getItem(i: Int): Any {
        return i;
    }

    override fun getItemId(i: Int): Long {
        return i.toLong();
    }

    override fun getView(i: Int, p1: View?, viewGroup: ViewGroup?): View {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.text_spinner_layout, viewGroup, false)
        val text: TextView = rootView.findViewById(R.id.text_spinner);
        text.text = governorateModelList[i].name
        return rootView;
    }
}
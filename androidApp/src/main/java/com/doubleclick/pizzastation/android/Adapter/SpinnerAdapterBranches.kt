package com.doubleclick.pizzastation.android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.AreasModel
import com.doubleclick.pizzastation.android.model.BranchesModel
import org.apache.commons.lang3.StringUtils.trim


class SpinnerAdapterBranches(val context: Context, val branchesModelList: List<AreasModel>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return branchesModelList.size
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
        text.text = branchesModelList[i].name.trim()
        return rootView;
    }

}
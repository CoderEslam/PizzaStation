package com.doubleclick.pizzastation.android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.BranchesModel


class SpinnerAdapterBranches(val context: Context, val branchesModelList: List<BranchesModel>) :
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
        text.text = branchesModelList[i].branch_name.trim()
        return rootView;
    }

}
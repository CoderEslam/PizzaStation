package com.doubleclick.pizzastation.android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.ViewHolder.LocationViewHolder
import com.doubleclick.pizzastation.android.model.BranchesModel
import com.doubleclick.pizzastation.android.model.GovernorateModel

class OurLocationAdapter(
    private val branchesModelList: List<BranchesModel>
) :
    RecyclerView.Adapter<LocationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_our_brensh_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.initLocation(branchesModel = branchesModelList[position])
        holder.location.text = branchesModelList[position].location
        holder.name.text = branchesModelList[position].branch_name
        holder.numbers.text = branchesModelList[position].branch_number
    }

    override fun getItemCount(): Int {
        return branchesModelList.size;
    }


}
package com.doubleclick.pizzastation.android.Adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
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
        holder.call_us.setOnClickListener {
            try {
                val callUri = Uri.parse("tel:${branchesModelList[position].branch_number}")
                val intentCall = Intent(Intent.ACTION_DIAL, callUri)
                holder.itemView.context.startActivity(intentCall)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    holder.itemView.context,
                    "You don't have call app!",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    override fun getItemCount(): Int {
        return branchesModelList.size;
    }


}
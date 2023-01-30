package com.doubleclick.pizzastation.android.Adapter

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import com.doubleclick.pizzastation.android.databinding.InfoWindowLayoutBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil

/**
 * Created By Eslam Ghazy on 1/15/2023
 */
class InfoWindowAdapter(location: Location, context: Context) :
    GoogleMap.InfoWindowAdapter {
    private val binding: InfoWindowLayoutBinding
    private val location: Location
    private val context: Context

    init {
        this.location = location
        this.context = context
        binding = InfoWindowLayoutBinding.inflate(LayoutInflater.from(context), null, false)
    }

    override fun getInfoWindow(marker: Marker): View {
        binding.txtLocationName.text = marker.title
        val distance = SphericalUtil.computeDistanceBetween(
            LatLng(location.latitude, location.longitude),
            marker.position
        )
        if (distance > 1000) {
            val kilometers = distance / 1000
            binding.txtLocationDistance.text = "$distance KM"
        } else {
            binding.txtLocationDistance.text = "$distance Meters"
        }
        val speed = location.speed
        if (speed > 0) {
            val time = distance / speed
            binding.txtLocationTime.text = "$time sec"
        } else {
            binding.txtLocationTime.text = "N/A"
        }
        return binding.root
    }

    override fun getInfoContents(marker: Marker): View {
        binding.txtLocationName.text = marker.title
        val distance = SphericalUtil.computeDistanceBetween(
            LatLng(location.latitude, location.longitude),
            marker.position
        )
        if (distance > 1000) {
            val kilometers = distance / 1000
            binding.txtLocationDistance.text = "$distance KM"
        } else {
            binding.txtLocationDistance.text = "$distance Meters"
        }
        val speed = location.speed
        if (speed > 0) {
            val time = distance / speed
            binding.txtLocationTime.text = "$time sec"
        } else {
            binding.txtLocationTime.text = "N/A"
        }
        return binding.root
    }
}

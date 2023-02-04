package com.doubleclick.pizzastation.android.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.model.BranchesModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnMapReadyCallback {

    val map: MapView = itemView.findViewById(R.id.map)
    val name: TextView = itemView.findViewById(R.id.name)
    val location: TextView = itemView.findViewById(R.id.location)
    val numbers: TextView = itemView.findViewById(R.id.numbers)
    var googleMap: GoogleMap? = null
    var mMapLocation: LatLng? = null


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        MapsInitializer.initialize(itemView.context);
        googleMap.uiSettings.isMapToolbarEnabled = false;
        googleMap.uiSettings.isZoomControlsEnabled = true;
        googleMap.uiSettings.isMyLocationButtonEnabled = true;
        googleMap.uiSettings.isRotateGesturesEnabled = true;
        googleMap.uiSettings.isCompassEnabled = true;
        // If we have mapView data, update the mapView content.
        if (mMapLocation != null) {
            updateMapContents();
        }
    }

    fun initLocation(branchesModel: BranchesModel) {
        val list: List<String> = branchesModel.latlng.replace("[", "")
            .replace("]", "")
            .replace(" ", "")
            .split(",")
        val latLng = LatLng(list[0].toDouble(), list[1].toDouble())
        setMapLocation(latLng)
    }


    private fun updateMapContents() {
        googleMap?.clear()
        mMapLocation?.let { MarkerOptions().position(it).title("location of you friend") }
            ?.let { googleMap?.addMarker(it) }
        mMapLocation?.let { CameraUpdateFactory.newLatLngZoom(it, 17f) }
            ?.let { googleMap?.moveCamera(it) }
    }

    private fun setMapLocation(latLng: LatLng) {
        mMapLocation = latLng
        // If the mapView is ready, update its content.
        if (googleMap != null) {
            updateMapContents()
        }
    }

    init {
        map.onCreate(null);
        map.getMapAsync(this);
    }
}
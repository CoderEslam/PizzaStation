package com.doubleclick.pizzastation.android.Home.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.doubleclick.pizzastation.android.Adapter.AddressAdapter
import com.doubleclick.pizzastation.android.Adapter.InfoWindowAdapter
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.databinding.FragmentLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationBinding
    private lateinit var mGoogleMap: GoogleMap
    private var isLocationPermissionOk = false
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private var currentMarker: Marker? = null
    private var infoWindowAdapter: InfoWindowAdapter? = null
    private var typeMap: Boolean = true


    private val TAG = "LocationFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPreviousSearch.apply {
            adapter = AddressAdapter()
        }

        binding.currentLocation.setOnClickListener {
            getCurrentLocation()
        }

        binding.btnMapType.setOnClickListener {
            changeMapType()
        }

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var location = query.toString();
                var addressList: List<Address>? = null
                try {
                    if (location.isNotEmpty()) {
                        var geocoder = Geocoder(requireActivity());
                        try {
                            addressList = geocoder.getFromLocationName(location, 1)
                            Log.e(TAG, "onQueryTextSubmit: $addressList")
                        } catch (e: IOException) {
                            Log.e(TAG, "beforeTextChanged: ${e.message}")
                        }
                        var address = addressList!![0];
                        var latlang = LatLng(address.latitude, address.longitude)
                        mGoogleMap.addMarker(MarkerOptions().position(latlang))
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 17f))
                    }
                } catch (e: IndexOutOfBoundsException) {
                    Log.e(TAG, "beforeTextChanged: ${e.message}")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun changeMapType() {
        if (typeMap) {
            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            typeMap = false
        } else {
            mGoogleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            typeMap = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap;
        changeMapType();
        mGoogleMap.setOnMapClickListener {
            // When clicked on map
            // Initialize marker options
            val markerOptions = MarkerOptions()
            // Set position of marker
            markerOptions.position(it)
            // Set title of marker
            markerOptions.title("${it.latitude} : ${it.longitude}")
            // Remove all marker
            mGoogleMap.clear()
            // Animating to zoom the marker
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 17f))
            // Add marker on map
            mGoogleMap.addMarker(markerOptions)
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                try {
                    currentLocation = location
                    infoWindowAdapter = null
                    infoWindowAdapter = InfoWindowAdapter(currentLocation, requireActivity())
                    mGoogleMap.setInfoWindowAdapter(infoWindowAdapter)
                    moveCameraToLocation(location)
                } catch (e: NullPointerException) {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.open_your_location),
                        Toast.LENGTH_LONG
                    ).show();
                }
            }
    }

    private fun moveCameraToLocation(location: Location) {
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17f)
        val markerOptions = MarkerOptions()
            .position(LatLng(location.latitude, location.longitude))
            .title("Current Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .snippet("this is string text")
        if (currentMarker != null) {
            currentMarker!!.remove()
        }
        currentMarker = mGoogleMap.addMarker(markerOptions)!!
        currentMarker!!.tag = 703
        mGoogleMap.animateCamera(cameraUpdate)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("type-map", !typeMap);
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        typeMap = savedInstanceState?.getBoolean("type-map", true) ?: true;
    }
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=47.8069962,-103.6187714&radius=5000&type=Egypt&key=AIzaSyCsNE4JNKvA6uR-TBaNXuw6NKkDv-JiAYQ

    /*
    *  private void getPlaces(String placeName) {

        if (isLocationPermissionOk) {


            loadingDialog.startLoading();
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + currentLocation.getLatitude() + "," + currentLocation.getLongitude()
                    + "&radius=" + radius + "&type=" + placeName + "&key=" +
                    getResources().getString(R.string.API_KEY);

            if (currentLocation != null) {


                retrofitAPI.getNearByPlaces(url).enqueue(new Callback<GoogleResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GoogleResponseModel> call, @NonNull Response<GoogleResponseModel> response) {
                        Gson gson = new Gson();
                        String res = gson.toJson(response.body());
                        Log.d("TAG", "onResponse: " + res);
                        if (response.errorBody() == null) {
                            if (response.body() != null) {
                                if (response.body().getGooglePlaceModelList() != null && response.body().getGooglePlaceModelList().size() > 0) {

                                    googlePlaceModelList.clear();
                                    mGoogleMap.clear();
                                    for (int i = 0; i < response.body().getGooglePlaceModelList().size(); i++) {

                                        if (userSavedLocationId.contains(response.body().getGooglePlaceModelList().get(i).getPlaceId())) {
                                            response.body().getGooglePlaceModelList().get(i).setSaved(true);
                                        }
                                        googlePlaceModelList.add(response.body().getGooglePlaceModelList().get(i));
                                        addMarker(response.body().getGooglePlaceModelList().get(i), i);
                                    }

                                    googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);

                                } else if (response.body().getError() != null) {
                                    Snackbar.make(binding.getRoot(),
                                            response.body().getError(),
                                            Snackbar.LENGTH_LONG).show();
                                } else {

                                    mGoogleMap.clear();
                                    googlePlaceModelList.clear();
                                    googlePlaceAdapter.setGooglePlaceModels(googlePlaceModelList);
                                    radius += 1000;
                                    Log.d("TAG", "onResponse: " + radius);
                                    getPlaces(placeName);

                                }
                            }

                        } else {
                            Log.d("TAG", "onResponse: " + response.errorBody());
                            Toast.makeText(requireContext(), "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }

                        loadingDialog.stopLoading();

                    }

                    @Override
                    public void onFailure(Call<GoogleResponseModel> call, Throwable t) {

                        Log.d("TAG", "onFailure: " + t);
                        loadingDialog.stopLoading();

                    }
                });
            }
        }

    }
    *
    *  private void addMarker(GooglePlaceModel googlePlaceModel, int position) {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(googlePlaceModel.getGeometry().getLocation().getLat(),
                        googlePlaceModel.getGeometry().getLocation().getLng()))
                .title(googlePlaceModel.getName())
                .snippet(googlePlaceModel.getVicinity());
        markerOptions.icon(getCustomIcon());
        mGoogleMap.addMarker(markerOptions).setTag(position);
    }
    *
    *  private BitmapDescriptor getCustomIcon() {

        Drawable background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location);
        background.setTint(getResources().getColor(R.color.quantum_googred900, null));
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    * */

}
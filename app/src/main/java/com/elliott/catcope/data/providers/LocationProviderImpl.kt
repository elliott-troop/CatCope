package com.elliott.catcope.data.providers

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliott.catcope.internal.NoConnectivityException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationProviderImpl(
    private var fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProvider {

    private val _latitude = MutableLiveData<Float>()
    private val _longitude = MutableLiveData<Float>()

    override val latitude: LiveData<Float>
        get() = _latitude

    override val longitude: LiveData<Float>
        get() = _longitude

    @SuppressLint("MissingPermission")
    override fun fetchCoordinates(context: Context) {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        _latitude.postValue(location.latitude as Float)
                        _longitude.postValue(location.longitude as Float)

                    } else {
                        Log.e("MainActivity", "Location is null")
                    }
                }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}
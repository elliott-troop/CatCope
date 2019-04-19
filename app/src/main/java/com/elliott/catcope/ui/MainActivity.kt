package com.elliott.catcope.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.elliott.catcope.R
import com.elliott.catcope.utilities.InjectorUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var viewModel: CatCopeViewModel
    private lateinit var viewModelFactory: CatCopeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = InjectorUtils.provideCatCopeViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CatCopeViewModel::class.java)

        requestLocationPermission()

        if (hasLocationPermission()) {
            initializeUi()
        }
        else
            requestLocationPermission()

    }

    fun initializeUi() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        if(googleApiClient.isConnected) googleApiClient.disconnect()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if(!hasLocationPermission()) {
            requestLocationPermission()
        }
        else {
            /*fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        *//*latitude = location.latitude
                        longitude = location.longitude*//*
                        viewModel

                    } else {
                        Log.e("MainActivity", "Location is null")
                    }
                }*/
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("MainActivity", "Connection Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("MainActivity", "Connection Failed")
    }

}

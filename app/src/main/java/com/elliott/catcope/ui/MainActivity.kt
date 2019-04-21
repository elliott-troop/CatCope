package com.elliott.catcope.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.elliott.catcope.R
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.utilities.InjectorUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

// class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    override fun onLocationChanged(p0: Location) {
        Log.e("MainActivity", "Latitude from onLocationChanged(): " + p0.latitude)
        Log.e("MainActivity", "Longitude from onLocationChanged(): " + p0.longitude)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var viewModel: CatCopeViewModel
    private lateinit var viewModelFactory: CatCopeViewModelFactory
    private var latitude: Double = 36.7201600
    private var longitude: Double = -4.4203400
    private var i: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = InjectorUtils.provideCatCopeViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CatCopeViewModel::class.java)

        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()

        if (hasLocationPermission()) {
            getDeviceCurrentLocation()
            initializeUi()
        } else
            Log.e("MainActivity", "1 hasLocationPermission() failed and went to else " + i++)
            requestLocationPermission()

        val apiService = SunriseSunsetApiService()
        val sunriseSunsetNetworkDataSource = SunriseSunsetNetworkDataSourceImpl(apiService)
        sunriseSunsetNetworkDataSource.downloadedSolarEvents.observe(this, Observer {
            next_solar_event.text = it.solarEventsEntry.sunset.toString()
        })

        GlobalScope.launch {
            sunriseSunsetNetworkDataSource.fetchSolarEvents(latitude, longitude)
        }


    }

    private fun initializeUi() {
        Log.e("MainActivity", "2 initializeUi() just called " + i++)
        /*runBlocking {
            viewModel.getSolarEvents(latitude!!, longitude!!).observe(
                this@MainActivity,
                Observer {
                    next_solar_event.text = it.solarEventsEntry.sunrise
                }
            )
        }*/
    }

    override fun onStart() {
        Log.e("MainActivity", "3 onStart() just called " + i++)
        super.onStart();
        googleApiClient.connect()
    }

    override fun onStop() {
        Log.e("MainActivity", "4 onStop() just called " + i++)
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private fun hasLocationPermission(): Boolean {
        Log.e("MainActivity", "5 hasLocationPermission() just called " + i++)
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        Log.e("MainActivity", "6 requestLocationPermission() just called " + i++)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
        if(hasLocationPermission()) {
            getDeviceCurrentLocation()
            initializeUi()
        }
    }

    override fun onConnected(p0: Bundle?) {
        Log.e("MainActivity", "7 onConnected() just called " + i++)
        if (!hasLocationPermission()) {
            Log.e("MainActivity", "8 !hasLocationPermission() just called " + i++)
            requestLocationPermission()
        } else {
            getDeviceCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceCurrentLocation() {
        Log.e("MainActivity", "9 getDeviceCurrentLocation() just called " + i++)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.e("MainActivity", "10 Latitude: " + latitude + " counter: " + i++)
                    Log.e("MainActivity", "11 Longitude: " + longitude + " counter: " + i++)
                    latitude_value.text = latitude.toString()
                    longitude_value.text = longitude.toString()
                    /*viewModel.setLatitude(location.latitude as Float)
                    viewModel.setLongitude(location.longitude as Float)*/
                } else {
                    Log.e("MainActivity", "Location is null")
                }
            }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("MainActivity", "Connection Suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("MainActivity", "Connection Failed")
    }

}

package com.elliott.catcope.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.elliott.catcope.R
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.RandomDogApi.RandomDogApiService
import com.elliott.catcope.data.network.RandomDogApi.RandomDogNetworkDataSourceImpl
import com.elliott.catcope.ui.base.ScopedActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.ZonedDateTime
//import android.R
import java.util.*


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : ScopedActivity(), KodeinAware, SeekBar.OnSeekBarChangeListener {

    override val kodein by closestKodein()

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var viewModel: CatCopeViewModel
    private val viewModelFactory: CatCopeViewModelFactory by instance()

    private var latitude: Double = 36.7201600
    private var longitude: Double = -4.4203400

    private val seekBarMinVal: Long = 1
    private var refresh: Long = 5

    private var timer: Timer? = null

    //remove later
    private var i: Int = 0
    private var j: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CatCopeViewModel::class.java)


        seekbar!!.setOnSeekBarChangeListener(this)

        requestLocationPermission()

        if (hasLocationPermission()) {
            runApp()
        }
        else
            requestLocationPermission()

    }

    override fun onStart() {
        startLocationUpdate()
        super.onStart()
    }

    override fun onResume() {
        startLocationUpdate()
        super.onResume()
    }

    override fun onPause() {
        removeLocationUpdates()
        super.onPause()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //do nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        timer!!.cancel()
        runApp()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        refresh = progress.toLong() + seekBarMinVal
        progress_view.text = (progress.toLong() + seekBarMinVal).toString()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    bindUI()
                }
                else {
                    Log.d("MainActivity", "Location is null")
                    latitude = 36.7201600
                    longitude = -4.4203400
                }
            }
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun runApp() {
        timer = Timer()

        timer!!.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    bindUI()
                }
            },
            0, refresh * 1000
        )
    }

    private val locationRequest = LocationRequest().apply {
        interval = 5000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    private fun bindUI() = launch{
        latitude_value.text = latitude.toString()
        longitude_value.text = longitude.toString()

        val solarEvent = viewModel.getSolarTimes(latitude, longitude)
            solarEvent.observe(this@MainActivity, Observer {
            if(it == null) {
                return@Observer
            }

            sunrise_value.text = it.sunrise
            sunset_value.text = it.sunset
        })

        j++

        if(j % 2 == 0) {
            var dogImage = viewModel.getDogUrl.await()
            dogImage.observe(this@MainActivity, Observer {
                if(it == null) {
                    return@Observer
                }

                Log.e("MainActivity", "Dog URL: " + it.imageOfDogUrl)
                Glide.with(this@MainActivity)
                    .load(it.imageOfDogUrl)
                    .into(pet_image_view)
            })

        }

        else {
            var catImage = viewModel.getCatUrl.await()
            catImage.observe(this@MainActivity, Observer {
                if(it == null) {
                    return@Observer
                }

                Log.e("MainActivity", "Cat URL: " + it.imageOfCatUrl)
                Glide.with(this@MainActivity)
                    .load(it.imageOfCatUrl)
                    .into(pet_image_view)
            })
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }
}


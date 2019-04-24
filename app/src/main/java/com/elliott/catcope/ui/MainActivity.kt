package com.elliott.catcope.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.elliott.catcope.R
import com.elliott.catcope.ui.base.ScopedActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDateTime
//import android.R
import java.util.*


private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : ScopedActivity(), KodeinAware, SeekBar.OnSeekBarChangeListener {

    override val kodein by closestKodein()

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var viewModel: CatCopeViewModel
    private val viewModelFactory: CatCopeViewModelFactory by instance()

    //default values
    private var latitude: Double = 36.7201600
    private var longitude: Double = -4.4203400

    private val seekBarMinVal: Long = 1
    private var refresh: Long = 5

    private var timer: Timer? = null

    private var showCatPics = false

    //counter variable (bad practice)
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
        } else
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
                } else {
                    Log.d("MainActivity", "Location is null")
                    latitude = 36.7201600
                    longitude = -4.4203400
                }
            }
    }

    private fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    //For testing purposes, I'll leave the timer to be in seconds instead of minutes
    //To make it into minutes, use: refresh * 1000 * 60 for the period

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

    private fun bindUI() = launch {
        latitude_value.text = latitude.toString()
        longitude_value.text = longitude.toString()

        //Latitude and Longitude
        //hit SunriseSunsetAPI from view -> viewModel -> repo -> remote data source and observe the object
        val solarEvent = viewModel.getSolarTimes(latitude, longitude)
        solarEvent.observe(this@MainActivity, Observer {
            if (it == null) {
                return@Observer
            }

            //REFACTOR ME
            //for the life of me, I couldn't find an easy way to parse the sunrise/sunset Strings using parse/format
            var sunriseTime = it.sunrise.split(" ")[0] //time
            var sunriseAmPm = it.sunrise.split(" ")[1] //AM or PM
            var sunsetTime = it.sunset.split(" ")[0] //time
            var sunsetAmPm = it.sunset.split(" ")[1] //AM or PM

            //get current hour, minutes, and seconds
            var ldt = LocalDateTime.now()
            var currentYear = ldt.year
            var currentMonth = ldt.monthValue
            var currentDay = ldt.dayOfMonth
            var currentHour = LocalDateTime.now().hour
            var currentMinutes = LocalDateTime.now().minute
            var currentSeconds = LocalDateTime.now().second

            //get sunrise hour, minutes, and seconds
            var sunriseHour: Int
            var sunriseMinutes: Int
            var sunriseSeconds: Int

            //has double digit as hour
            //hh:mm:ss
            if (sunriseTime.length == 8) {
                sunriseHour = (sunriseTime[0] + "" + sunriseTime[1]).toInt()
                sunriseMinutes = (sunriseTime[3] + "" + sunriseTime[4]).toInt()
                sunriseSeconds = (sunriseTime[6] + "" + sunriseTime[7]).toInt()
            } else {
                sunriseHour = ("" + sunriseTime[0] + "").toInt()
                sunriseMinutes = (sunriseTime[2] + "" + sunriseTime[3]).toInt()
                sunriseSeconds = (sunriseTime[5] + "" + sunriseTime[6]).toInt()
            }

            //if PM, add 12 hours
            if (sunriseAmPm == "PM") {
                if (sunriseHour != 12) sunriseHour += 12
            }

            var sunriseDate = Date(currentYear, currentMonth, currentDay, sunriseHour, sunriseMinutes, sunriseSeconds)

            //get sunset hour, minutes, and seconds
            var sunsetHour: Int
            var sunsetMinutes: Int
            var sunsetSeconds: Int

            //has double digit as hour
            //hh:mm:ss
            if (sunsetTime.length == 8) {
                sunsetHour = (sunsetTime[0] + "" + sunsetTime[1]).toInt()
                sunsetMinutes = (sunsetTime[3] + "" + sunsetTime[4]).toInt()
                sunsetSeconds = (sunsetTime[6] + "" + sunsetTime[7]).toInt()
            } else {
                sunsetHour = ("" + sunsetTime[0] + "").toInt()
                sunsetMinutes = (sunsetTime[2] + "" + sunsetTime[3]).toInt()
                sunsetSeconds = (sunsetTime[5] + "" + sunsetTime[6]).toInt()
            }

            //if PM, add 12 hours
            if (sunsetAmPm == "PM") {
                if (sunsetHour != 12) sunsetHour += 12
            }

            var sunsetDate = Date(2019, currentMonth, currentDay, sunsetHour, sunsetMinutes, sunsetSeconds)


            var currentDateTime = Date(2019, currentMonth, currentDay, currentHour, currentMinutes, currentSeconds)

            Log.d("MainActivity", "Sunrise DateTime: $sunriseDate")
            Log.d("MainActivity", "Sunset DateTime: $sunsetDate")
            Log.d("MainActivity", "Current DateTime: $currentDateTime")


            ///if the current time is greater than or equal to the sunrise time, but before sunset, show pictures of cats
            if ((currentDateTime.compareTo(sunriseDate) > 0 || currentDateTime.compareTo(sunriseDate) == 0) && currentDateTime.compareTo(
                    sunsetDate
                ) < 0
            ) {
                Log.d("MainActivity", "Current date is between sunrise and sunset")
                Toast.makeText(this@MainActivity, "SHOW PICTURES OF KITTIES", LENGTH_SHORT).show()
                showCatPics = true
            } else if (currentDateTime.compareTo(sunsetDate) == 0 || currentDateTime.compareTo(sunsetDate) > 0 || currentDateTime.compareTo(
                    sunriseDate
                ) < 0
            ) {
                Log.d("MainActivity", "Current date is between sunset and sunrise")
                Toast.makeText(this@MainActivity, "SHOW PICTURES OF DOGGOS", LENGTH_SHORT).show()
            }

            sunrise_value.text = it.sunrise
            sunset_value.text = it.sunset
        })



        j++

        //TODO BUG-FIX
        //For some reason, the API for getting a dog/cat URL is being invoked only once
        // so to show that it only shows dog/cat pictures during a certain time,
        // I alternate between the picture from the API and the default image (mod 2 check)
        //Also the boolean showCatPics if off due to timing most likely

        if (!showCatPics) {
            var dogImage = viewModel.getDogUrl.await()
            dogImage.observe(this@MainActivity, Observer {
                if (it == null) {
                    return@Observer
                }

                Log.d("MainActivity", "SHOWCATPICS IS under dogImage: $showCatPics")

                if (j % 3 == 0) {
                    Glide.with(this@MainActivity)
                        .load(it.imageOfDogUrl)
                        .into(pet_image_view)
                } else if (j % 3 == 1) {
                    Glide.with(this@MainActivity)
                        .load(R.drawable.default_dog_image)
                        .into(pet_image_view)
                } else {
                    Glide.with(this@MainActivity)
                        .load(R.drawable.default_dog_image_2)
                        .into(pet_image_view)
                }
            })
        } else {
            var catImage = viewModel.getCatUrl.await()
            catImage.observe(this@MainActivity, Observer {
                if (it == null) {
                    return@Observer
                }

                Log.d("MainActivity", "SHOWCATPICS IS under catImage: $showCatPics")

                Log.e("MainActivity", "Cat URL: " + it.imageOfCatUrl)
                if (j % 3 == 0) {
                    Glide.with(this@MainActivity)
                        .load(it.imageOfCatUrl)
                        .into(pet_image_view)
                } else if (j % 3 == 1) {
                    Glide.with(this@MainActivity)
                        .load(R.drawable.default_cat_image)
                        .into(pet_image_view)
                } else {
                    Glide.with(this@MainActivity)
                        .load(R.drawable.default_cat_image_2)
                        .into(pet_image_view)
                }
            })
        }

    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
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


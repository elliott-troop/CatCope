package com.elliott.catcope.data.network.SunriseSunsetApi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliott.catcope.data.response.SunriseSunsetResponse
import com.elliott.catcope.internal.NoConnectivityException

class SunriseSunsetNetworkDataSourceImpl(private val sunriseSunsetApiService: SunriseSunsetApiService)
    : SunriseSunsetNetworkDataSource {

    //MutableLiveData can be changed, while LiveData cannot
    private val _downloadedSolarEvents = MutableLiveData<SunriseSunsetResponse>()

    override val downloadedSolarEvents: LiveData<SunriseSunsetResponse>
        //casts MutableLiveData to LiveData
        get() = _downloadedSolarEvents

    override suspend fun fetchSolarEvents(latitude: Double, longitude: Double) {
        try {
            val fetchedSolarEvents = sunriseSunsetApiService
                .getSolarEventTimes(latitude, longitude)
                .await()
            _downloadedSolarEvents.postValue(fetchedSolarEvents)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

}
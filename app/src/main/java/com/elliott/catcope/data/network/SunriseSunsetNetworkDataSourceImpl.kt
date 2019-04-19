package com.elliott.catcope.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.response.SunriseSunsetResponse
import com.elliott.catcope.internal.NoConnectivityException

class SunriseSunsetNetworkDataSourceImpl(
    private val sunriseSunsetApiService: SunriseSunsetApiService
) : SunriseSunsetNetworkDataSource {

    private val _downloadedSolarEvents = MutableLiveData<SunriseSunsetResponse>()

    override val downloadedSolarEvents: LiveData<SunriseSunsetResponse>
        get() = _downloadedSolarEvents

    override suspend fun fetchSolarEvents(latitude: Float, longitude: Float) {
        try {
            val fetchSolarEvents = sunriseSunsetApiService
                .getSolarEventTimes(latitude, longitude)
                .await()
            _downloadedSolarEvents.postValue(fetchSolarEvents)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}
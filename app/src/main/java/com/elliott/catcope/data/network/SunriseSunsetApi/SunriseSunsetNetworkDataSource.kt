package com.elliott.catcope.data.network.SunriseSunsetApi

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.SunriseSunsetResponse

interface SunriseSunsetNetworkDataSource {

    val downloadedSolarEvents : LiveData<SunriseSunsetResponse>

    suspend fun fetchSolarEvents(
        latitude: Double,
        longitude: Double
    )
}
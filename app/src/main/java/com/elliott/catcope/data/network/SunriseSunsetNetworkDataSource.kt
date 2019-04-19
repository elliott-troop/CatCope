package com.elliott.catcope.data.network

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.SunriseSunsetResponse

interface SunriseSunsetNetworkDataSource {

    val downloadedSolarEvents : LiveData<SunriseSunsetResponse>

    suspend fun fetchSolarEvents(
        latitude: Float,
        longitude: Float
    )
}
package com.elliott.catcope.data.network.SunriseSunsetApi

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.SunriseSunsetResponse

//an abstraction on top of the API service, so when requesting data, you won't have to work with API service directly
//all of the exception handling will be done here
interface SunriseSunsetNetworkDataSource {

    val downloadedSolarEvents : LiveData<SunriseSunsetResponse>

    //updates downloaded solar events and then observed later for changes
    suspend fun fetchSolarEvents(
        latitude: Double,
        longitude: Double
    )
}
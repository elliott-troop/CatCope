package com.elliott.catcope.data.network.RandomDogApi

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.RandomDogResponse

interface RandomDogNetworkDataSource {

    val downloadedDogUrl: LiveData<RandomDogResponse>

    //    Used to update the live data to later be observed
    suspend fun fetchRandomDogUrl(
    )
}
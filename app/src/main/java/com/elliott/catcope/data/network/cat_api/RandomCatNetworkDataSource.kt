package com.elliott.catcope.data.network.cat_api

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.RandomCatResponse

interface RandomCatNetworkDataSource {

    val downloadedCatUrl: LiveData<RandomCatResponse>

    //    Used to update the live data to later be observed
    suspend fun fetchRandomCatUrl()
}
package com.elliott.catcope.data.network.RandomCatApi

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.response.RandomCatResponse

interface RandomCatNetworkDataSource {

    val downloadedCatUrl: LiveData<RandomCatResponse>

    suspend fun fetchRandomCatUrl()
}
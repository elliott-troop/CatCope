package com.elliott.catcope.data.network.RandomDogApi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliott.catcope.data.response.RandomDogResponse
import com.elliott.catcope.internal.NoConnectivityException

class RandomDogNetworkDataSourceImpl(
    private val randomDogApiService: RandomDogApiService
) : RandomDogNetworkDataSource {

    private val _downloadedDogUrl = MutableLiveData<RandomDogResponse>()

    override val downloadedDogUrl: LiveData<RandomDogResponse>
        get() = _downloadedDogUrl

    override suspend fun fetchRandomDogUrl() {
        try {
            val fetchRandomDogUrl = randomDogApiService
                .getDogImageUrl()
                .await()
            _downloadedDogUrl.postValue(fetchRandomDogUrl)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}
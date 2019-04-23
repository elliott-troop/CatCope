package com.elliott.catcope.data.network.RandomCatApi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elliott.catcope.data.response.RandomCatResponse
import com.elliott.catcope.internal.NoConnectivityException

class RandomCatNetworkDataSourceImpl(
    private val randomCatApiService: RandomCatApiService
) : RandomCatNetworkDataSource {

    private val _downloadedCatUrl = MutableLiveData<RandomCatResponse>()

    override val downloadedCatUrl: LiveData<RandomCatResponse>
        get() = _downloadedCatUrl

    override suspend fun fetchRandomCatUrl() {
        try {
            val fetchRandomCatUrl = randomCatApiService
                .getCatImageUrl()
                .await()
            _downloadedCatUrl.postValue(fetchRandomCatUrl)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}
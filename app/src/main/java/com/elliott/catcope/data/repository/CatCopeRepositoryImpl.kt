package com.elliott.catcope.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.elliott.catcope.data.db.SunriseSunsetDao
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.network.RandomDogApi.RandomDogNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetApi.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.RandomCatApi.RandomCatNetworkDataSource
import com.elliott.catcope.data.response.RandomCatResponse
import com.elliott.catcope.data.response.RandomDogResponse
import com.elliott.catcope.data.response.SunriseSunsetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CatCopeRepositoryImpl internal constructor(
    private val sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource,
    private val sunriseSunsetDao: SunriseSunsetDao,
    private val randomDogNetworkDataSource: RandomDogNetworkDataSource,
    private val randomCatNetworkDataSource: RandomCatNetworkDataSource
) : CatCopeRepository {

    override suspend fun getSolarTimes(latitude: Double, longitude: Double) : LiveData<SolarEventsEntry> {
        sunriseSunsetNetworkDataSource.fetchSolarEvents(latitude, longitude)
        return withContext(Dispatchers.IO) {
            return@withContext sunriseSunsetDao.getSolarEventEntry()
        }
    }

    override suspend fun getDogUrl() : LiveData<RandomDogResponse> {
        randomDogNetworkDataSource.fetchRandomDogUrl()
        Log.e("getDogUrl", randomDogNetworkDataSource.dogUrl)
        return withContext(Dispatchers.IO) {
            return@withContext randomDogNetworkDataSource.downloadedDogUrl
        }
    }

    override suspend fun getCatUrl() : LiveData<RandomCatResponse> {
        randomCatNetworkDataSource.fetchRandomCatUrl()
        return withContext(Dispatchers.IO) {
            return@withContext randomCatNetworkDataSource.downloadedCatUrl
        }
    }
}
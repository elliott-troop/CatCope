package com.elliott.catcope.data.repository

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.db.SunriseSunsetDao
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.network.RandomDogApi.RandomDogNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetApi.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.cat_api.RandomCatNetworkDataSource
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

    init {
        sunriseSunsetNetworkDataSource.downloadedSolarEvents.observeForever{ newSolarEvents ->
            //persist the new solar events
            persistFetchedSolarEvent(newSolarEvents)
        }
    }

    override suspend fun getSolarEvents() : LiveData<SolarEventsEntry> {
        initSolarEventData()
        return withContext(Dispatchers.IO) {
            return@withContext sunriseSunsetDao.getSolarEventEntry()
        }
    }

    //get the current time, compare with sunrise and sunset times, return url based on comparison
    override suspend fun getPetUrl() : LiveData<RandomDogResponse> {
        //val currentTime = ZonedDateTime.now()

        randomDogNetworkDataSource.fetchRandomDogUrl()
        //randomCatNetworkDataSource.fetchRandomCatUrl()

        return withContext(Dispatchers.IO) {
            return@withContext randomDogNetworkDataSource.downloadedDogUrl
        }
    }

    private fun persistFetchedSolarEvent(fetchedSolarEvents : SunriseSunsetResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            sunriseSunsetDao.upsert(fetchedSolarEvents.solarEventsEntry)
        }
    }

    private suspend fun initSolarEventData() {
        if(isFetchNeeded(ZonedDateTime.now().minusMinutes(10))) {
            fetchCurrentSolarEvents()
        }
    }

    private suspend fun fetchCurrentSolarEvents() {
        sunriseSunsetNetworkDataSource.fetchSolarEvents(36.7201600, -4.4203400)
    }

    private fun isFetchNeeded(lastFetchTime: ZonedDateTime) : Boolean {
        val refreshTime = ZonedDateTime.now().minusMinutes(5)
        return lastFetchTime.isBefore(refreshTime)
    }
}
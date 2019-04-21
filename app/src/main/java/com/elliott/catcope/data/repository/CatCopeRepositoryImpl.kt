package com.elliott.catcope.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.elliott.catcope.data.db.SunriseSunsetDao
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.data.providers.LocationProvider
import com.elliott.catcope.data.providers.LocationProviderImpl
import com.elliott.catcope.data.response.SunriseSunsetResponse
import com.elliott.catcope.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CatCopeRepositoryImpl internal constructor(
    private val sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource,
    private val sunriseSunsetDao: SunriseSunsetDao
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

    //CatCopeRepositoryImpl will be a singleton
    /*companion object {

        //writes to this field are immediately made visible to other threads
        @Volatile
        private var instance: CatCopeRepositoryImpl? = null

        fun getInstance(sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource) =
            instance ?: synchronized(this) {
                instance ?: CatCopeRepositoryImpl(sunriseSunsetNetworkDataSource).also { instance = it }
            }
    }*/

    /*suspend fun getSolarEvents(latitude: Double, longitude: Double) : LiveData<SunriseSunsetResponse> {
        val apiService = SunriseSunsetApiService()
        sunriseSunsetNetworkDataSource = SunriseSunsetNetworkDataSourceImpl(apiService)
        sunriseSunsetNetworkDataSource.fetchSolarEvents(latitude, longitude)

        return sunriseSunsetNetworkDataSource.downloadedSolarEvents
    }*/

    /*override suspend fun getSolarEvents(): LiveData<SunriseSunsetResponse> {
        *//*val sunriseSunsetApiService = SunriseSunsetApiService(ConnectivityInterceptorImpl(context = MainActivity))
        sunriseSunsetNetworkDataSource = SunriseSunsetNetworkDataSourceImpl(sunriseSunsetApiService)*//*
    }

    override suspend fun getLatitude(): LiveData<Float> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLongitude(): LiveData<Float> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override suspend fun getNextSolarEvent(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPetUrl(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/
}
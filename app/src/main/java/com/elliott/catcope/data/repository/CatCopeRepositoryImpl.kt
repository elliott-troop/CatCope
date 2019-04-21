package com.elliott.catcope.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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

class CatCopeRepositoryImpl private constructor(
    private var sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource
) {

    //CatCopeRepositoryImpl will be a singleton
    companion object {

        //writes to this field are immediately made visible to other threads
        @Volatile
        private var instance: CatCopeRepositoryImpl? = null

        fun getInstance(sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource) =
            instance ?: synchronized(this) {
                instance ?: CatCopeRepositoryImpl(sunriseSunsetNetworkDataSource).also { instance = it }
            }
    }

    suspend fun getSolarEvents(latitude: Double, longitude: Double) : LiveData<SunriseSunsetResponse> {
        val apiService = SunriseSunsetApiService()
        sunriseSunsetNetworkDataSource = SunriseSunsetNetworkDataSourceImpl(apiService)
        sunriseSunsetNetworkDataSource.fetchSolarEvents(latitude, longitude)

        return sunriseSunsetNetworkDataSource.downloadedSolarEvents
    }

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
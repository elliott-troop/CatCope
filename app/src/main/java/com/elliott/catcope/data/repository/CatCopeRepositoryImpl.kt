package com.elliott.catcope.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.data.providers.LocationProvider
import com.elliott.catcope.data.response.SunriseSunsetResponse
import com.elliott.catcope.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient

class CatCopeRepositoryImpl private constructor(
    private var sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource,
    private val locationProvider: LocationProvider,
    context: Context
    ) {

    //CatCopeRepository will be a singleton
    companion object {

        //writes to this field are immediately made visible to other threads
        @Volatile private var instance: CatCopeRepository? = null

        fun getInstance(sunriseSunsetNetworkDataSource: SunriseSunsetNetworkDataSource,
                        locationProvider: LocationProvider,
                        context: Context) =
            instance ?: synchronized(this) {
                instance ?: CatCopeRepository(sunriseSunsetNetworkDataSource, locationProvider, context).also { instance = it }
            }
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
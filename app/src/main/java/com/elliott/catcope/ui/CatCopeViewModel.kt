package com.elliott.catcope.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.elliott.catcope.data.network.ConnectivityInterceptor
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.data.repository.CatCopeRepositoryImpl

class CatCopeViewModel(
    private val catCopeRepository: CatCopeRepositoryImpl
) : ViewModel() {

    /*fun getLatitude() = catCopeRepository.getLatitude()
    fun getLongitude() = catCopeRepository.getLongitude()*/

    //fun getDeviceCurrentLocation(context: Context) = catCopeRepository.getDeviceCurrentLocation(context)


    suspend fun getSolarEvents(latitude: Double, longitude: Double) = catCopeRepository.getSolarEvents(latitude, longitude)

    //val sunsetApiService = SunriseSunsetApiService(ConnectivityInterceptorImpl())
}
package com.elliott.catcope.ui

import androidx.lifecycle.ViewModel
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.internal.lazyDeferred

class CatCopeViewModel(
    private val catCopeRepository: CatCopeRepository
) : ViewModel() {

//    private val refreshTime = //get from seekbar


    val getSolarEvent by lazyDeferred() {
        catCopeRepository.getSolarEvents()
    }

    val getPetUrl by lazyDeferred() {
        catCopeRepository.getPetUrl()
    }

    /*fun getLatitude() = catCopeRepository.getLatitude()
    fun getLongitude() = catCopeRepository.getLongitude()*/

    //fun getDeviceCurrentLocation(context: Context) = catCopeRepository.getDeviceCurrentLocation(context)


    //suspend fun getSolarEvents(latitude: Double, longitude: Double) = catCopeRepository.getSolarEvents(latitude, longitude)

    //val sunsetApiService = SunriseSunsetApiService(ConnectivityInterceptorImpl())
}
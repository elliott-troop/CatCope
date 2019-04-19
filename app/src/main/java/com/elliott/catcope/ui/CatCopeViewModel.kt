package com.elliott.catcope.ui

import androidx.lifecycle.ViewModel
import com.elliott.catcope.data.network.ConnectivityInterceptor
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.repository.CatCopeRepository

class CatCopeViewModel(
    private val catCopeRepository: CatCopeRepository) : ViewModel() {

    suspend fun getSolarEvents(latitude: Float, longitude: Float) = catCopeRepository.getSolarEvents()

    //val sunsetApiService = SunriseSunsetApiService(ConnectivityInterceptorImpl())
}
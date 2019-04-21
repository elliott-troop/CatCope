package com.elliott.catcope.utilities

import android.content.Context
import com.elliott.catcope.data.network.ConnectivityInterceptorImpl
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.data.providers.LocationProvider
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.data.repository.CatCopeRepositoryImpl
import com.elliott.catcope.ui.CatCopeViewModelFactory

object InjectorUtils {

    fun provideCatCopeViewModelFactory(context: Context) : CatCopeViewModelFactory {
        val sunriseSunsetNetworkDataSource = SunriseSunsetNetworkDataSourceImpl(SunriseSunsetApiService.invoke())
        val catCopeRepository = CatCopeRepositoryImpl.getInstance(sunriseSunsetNetworkDataSource)
        return CatCopeViewModelFactory(catCopeRepository)
    }
}
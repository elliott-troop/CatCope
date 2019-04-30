package com.elliott.catcope.ui

import androidx.lifecycle.ViewModel
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.internal.lazyDeferred
import kotlinx.coroutines.Deferred

class CatCopeViewModel(
    private val catCopeRepository: CatCopeRepository
) : ViewModel() {

    val getDogUrl by lazyDeferred {
        catCopeRepository.getDogUrl()
    }

    val getCatUrl by lazyDeferred {
        catCopeRepository.getCatUrl()
    }

    suspend fun getSolarTimes(latitude: Double, longitude: Double) = catCopeRepository.getSolarTimes(latitude, longitude)
    /*suspend fun getDogUrl() = catCopeRepository.getDogUrl()
    suspend fun getCatUrl() = catCopeRepository.getCatUrl()*/
}
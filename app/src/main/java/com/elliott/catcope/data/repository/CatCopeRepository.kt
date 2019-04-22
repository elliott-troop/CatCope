package com.elliott.catcope.data.repository

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.response.RandomDogResponse
import com.elliott.catcope.data.response.SunriseSunsetResponse

interface CatCopeRepository {
    suspend fun getSolarEvents() : LiveData<SolarEventsEntry>
    suspend fun getPetUrl(): LiveData<RandomDogResponse>

}
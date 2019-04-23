package com.elliott.catcope.data.repository

import androidx.lifecycle.LiveData
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.response.RandomCatResponse
import com.elliott.catcope.data.response.RandomDogResponse
import com.elliott.catcope.data.response.SunriseSunsetResponse

interface CatCopeRepository {
    suspend fun getSolarEvents() : LiveData<SolarEventsEntry>
    suspend fun getDogUrl(): LiveData<RandomDogResponse>
    suspend fun getCatUrl() : LiveData<RandomCatResponse>
    suspend fun getSolarTimes(latitude: Double, longitude: Double) : LiveData<SolarEventsEntry>
}
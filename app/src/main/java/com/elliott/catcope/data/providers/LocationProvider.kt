package com.elliott.catcope.data.providers

import android.content.Context
import androidx.lifecycle.LiveData

interface LocationProvider {
    val latitude: LiveData<Float>
    val longitude: LiveData<Float>

    fun fetchCoordinates(context: Context)
}
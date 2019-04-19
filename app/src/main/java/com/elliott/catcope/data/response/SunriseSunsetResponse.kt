package com.elliott.catcope.data.response

import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.google.gson.annotations.SerializedName

data class SunriseSunsetResponse(
    @SerializedName("results")
    val solarEventsEntry: SolarEventsEntry,
    val status: String
)
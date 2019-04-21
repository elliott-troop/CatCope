package com.elliott.catcope.data.db.entity

import com.google.gson.annotations.SerializedName

data class SolarEventsEntry(
    @SerializedName("astronomical_twilight_begin")
    val astronomicalTwilightBegin: String,
    @SerializedName("astronomical_twilight_end")
    val astronomicalTwilightEnd: String,
    @SerializedName("civil_twilight_begin")
    val civilTwilightBegin: String,
    @SerializedName("civil_twilight_end")
    val civilTwilightEnd: String,
    @SerializedName("day_length")
    val dayLength: String,
    @SerializedName("nautical_twilight_begin")
    val nauticalTwilightBegin: String,
    @SerializedName("nautical_twilight_end")
    val nauticalTwilightEnd: String,
    @SerializedName("solar_noon")
    val solarNoon: String,
    val sunrise: String,
    val sunset: String

)
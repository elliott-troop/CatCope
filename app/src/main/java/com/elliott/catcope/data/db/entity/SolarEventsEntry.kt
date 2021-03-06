package com.elliott.catcope.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_SOLAR_EVENT_ID = 0


@Entity (tableName = "current_solar_events")
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
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_SOLAR_EVENT_ID
}
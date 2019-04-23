package com.elliott.catcope.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_DOG_URL_ID = 0

@Entity(tableName = "current_dog_url")
data class RandomDogResponse(
    @SerializedName("url")
    val imageOfDogUrl: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_DOG_URL_ID
}
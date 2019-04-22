package com.elliott.catcope.data.response

import com.google.gson.annotations.SerializedName

data class RandomDogResponse(
    @SerializedName("url")
    val imageOfDogUrl: String
)
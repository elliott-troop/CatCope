package com.elliott.catcope.data.response

import com.google.gson.annotations.SerializedName

data class RandomCatResponse(
    @SerializedName("file")
    val imageOfCatUrl: String
)
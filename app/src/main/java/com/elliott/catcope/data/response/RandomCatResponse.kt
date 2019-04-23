package com.elliott.catcope.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val CURRENT_CAT_URL_ID = 0

@Entity(tableName = "current_cat_url")
data class RandomCatResponse(
    @SerializedName("file")
    val imageOfCatUrl: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_CAT_URL_ID
}
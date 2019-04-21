package com.elliott.catcope.data.network

import com.elliott.catcope.data.response.SunriseSunsetResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.sunrise-sunset.org/json?lat=40.7027020&lng=-74.058672

interface SunriseSunsetApiService {

    @GET("json")
    fun getSolarEventTimes(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Deferred<SunriseSunsetResponse>

    companion object {
        operator fun invoke(): SunriseSunsetApiService {
            return Retrofit.Builder()
                .baseUrl("https://api.sunrise-sunset.org/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SunriseSunsetApiService::class.java)
        }
    }
}
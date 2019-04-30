package com.elliott.catcope.data.network.SunriseSunsetApi

import com.elliott.catcope.data.network.ConnectivityInterceptor
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

    //GET Request
    //Returns a Deferred SunriseSunsetResponse
    @GET("json")
    fun getSolarEventTimes(
        //Query parameters
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Deferred<SunriseSunsetResponse>

    //static method
    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): SunriseSunsetApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            //CallAdapterFactory is used because Deferred is being returned from the getSolarEventTimes
            //Tell Retrofit to use Gson to parse JSON into Kotlin object
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.sunrise-sunset.org/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SunriseSunsetApiService::class.java)
        }
    }
}
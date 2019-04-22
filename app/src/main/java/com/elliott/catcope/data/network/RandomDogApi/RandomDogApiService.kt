package com.elliott.catcope.data.network.RandomDogApi

import com.elliott.catcope.data.network.ConnectivityInterceptor
import com.elliott.catcope.data.response.RandomDogResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RandomDogApiService {

    @GET("woof.json")
    fun getDogImageUrl() : Deferred<RandomDogResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): RandomDogApiService {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(connectivityInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://random.dog/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RandomDogApiService::class.java)
        }
    }

}
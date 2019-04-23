package com.elliott.catcope.data.network.RandomCatApi

import com.elliott.catcope.data.network.ConnectivityInterceptor
import com.elliott.catcope.data.response.RandomCatResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RandomCatApiService {

    @GET("meow")
    fun getCatImageUrl() : Deferred<RandomCatResponse>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): RandomCatApiService {
            val okHttpClient = OkHttpClient.Builder().addInterceptor(connectivityInterceptor).build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://aws.random.cat/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RandomCatApiService::class.java)
        }
    }
}
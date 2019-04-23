package com.elliott.catcope

import android.app.Application
import android.content.Context
import com.elliott.catcope.data.db.SunriseSunsetDatabase
import com.elliott.catcope.data.network.*
import com.elliott.catcope.data.network.RandomDogApi.RandomDogApiService
import com.elliott.catcope.data.network.RandomDogApi.RandomDogNetworkDataSource
import com.elliott.catcope.data.network.RandomDogApi.RandomDogNetworkDataSourceImpl
import com.elliott.catcope.data.network.SunriseSunsetApi.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetApi.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetApi.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.data.network.RandomCatApi.RandomCatApiService
import com.elliott.catcope.data.network.RandomCatApi.RandomCatNetworkDataSource
import com.elliott.catcope.data.network.RandomCatApi.RandomCatNetworkDataSourceImpl
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.data.repository.CatCopeRepositoryImpl
import com.elliott.catcope.ui.CatCopeViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CatCopeApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CatCopeApplication))

        bind() from singleton { SunriseSunsetDatabase(instance()) }
        bind() from singleton { instance<SunriseSunsetDatabase>().currentSunriseSunsetDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { SunriseSunsetApiService(instance()) }
        bind() from singleton { RandomDogApiService(instance()) }
        bind() from singleton { RandomCatApiService(instance()) }
        bind<SunriseSunsetNetworkDataSource>() with singleton {
            SunriseSunsetNetworkDataSourceImpl(
                instance()
            )
        }
        bind<RandomDogNetworkDataSource>() with singleton {
            RandomDogNetworkDataSourceImpl(instance())
        }
        bind<RandomCatNetworkDataSource>() with singleton {
            RandomCatNetworkDataSourceImpl(instance())
        }
        bind<CatCopeRepository>() with singleton {
            CatCopeRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind() from provider { CatCopeViewModelFactory(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
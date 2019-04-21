package com.elliott.catcope

import android.app.Application
import com.elliott.catcope.data.db.SunriseSunsetDatabase
import com.elliott.catcope.data.network.SunriseSunsetApiService
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSource
import com.elliott.catcope.data.network.SunriseSunsetNetworkDataSourceImpl
import com.elliott.catcope.data.repository.CatCopeRepository
import com.elliott.catcope.data.repository.CatCopeRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class CatCopeApplication : Application(), KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@CatCopeApplication))

        bind() from singleton {SunriseSunsetDatabase(instance())}
        bind() from singleton {instance<SunriseSunsetDatabase>().currentSunriseSunsetDao()}
        bind() from singleton {SunriseSunsetApiService()}
        bind<SunriseSunsetNetworkDataSource>() with singleton { SunriseSunsetNetworkDataSourceImpl(instance()) }
        bind<CatCopeRepository>() with singleton { CatCopeRepositoryImpl(instance(), instance()) }
    }
}
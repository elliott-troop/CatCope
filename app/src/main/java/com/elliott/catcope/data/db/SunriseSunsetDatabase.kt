package com.elliott.catcope.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elliott.catcope.data.db.entity.SolarEventsEntry
import com.elliott.catcope.data.response.RandomCatResponse
import com.elliott.catcope.data.response.RandomDogResponse

@Database(
    entities = [SolarEventsEntry::class],
    version = 1
)
abstract class SunriseSunsetDatabase : RoomDatabase() {
    abstract fun currentSunriseSunsetDao() : SunriseSunsetDao

    companion object {
        @Volatile private var instance: SunriseSunsetDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    SunriseSunsetDatabase::class.java, "solar_events.db")
                    .fallbackToDestructiveMigration()
                    .build()
    }
}
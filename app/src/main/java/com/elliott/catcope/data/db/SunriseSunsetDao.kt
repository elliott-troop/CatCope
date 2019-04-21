package com.elliott.catcope.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elliott.catcope.data.db.entity.CURRENT_SOLAR_EVENT_ID
import com.elliott.catcope.data.db.entity.SolarEventsEntry

@Dao
interface SunriseSunsetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(solarEventsEntry: SolarEventsEntry)

    @Query("select * from current_solar_events where id = $CURRENT_SOLAR_EVENT_ID")
    fun getSolarEventEntry() : LiveData<SolarEventsEntry>
}
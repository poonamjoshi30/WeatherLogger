package com.example.weatherlogger.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDao {
    @get:Query(value = "Select * from WeatherEntity order by date desc")
    val allWeather: LiveData<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertMsg(weatherEntity: WeatherEntity)

    @Query(value = "Update WeatherEntity set temp =:temp where date < :weatherTime")
     fun updateMsg(temp:String,weatherTime:Long)

    @Delete
     fun deleteMsg(weatherEntity: WeatherEntity)
}
package com.example.weatherlogger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ WeatherEntity::class], version = 1)
abstract class WeatherLoggerDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var weatherLoggerDatabaseInstance: WeatherLoggerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) =
            weatherLoggerDatabaseInstance ?: synchronized(LOCK) {
                weatherLoggerDatabaseInstance ?: bindDatabase(context).also {
                    weatherLoggerDatabaseInstance = it
                }
            }

        private fun bindDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WeatherLoggerDatabase::class.java,
            "WeatherLogger.db"
        ).allowMainThreadQueries().build()
    }

    abstract val weatherDao: WeatherDao?

}
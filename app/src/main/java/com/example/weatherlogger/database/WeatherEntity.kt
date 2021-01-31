package com.example.weatherlogger.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize



@Entity
data class WeatherEntity (val temp:String?,
                          val description:String?,
                          val name:String?,
                          @PrimaryKey var date:String)
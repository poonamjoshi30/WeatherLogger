package com.example.weatherlogger.DataModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse (
        @SerializedName("main")
        val main: MainResponse,
         @SerializedName("weather")
        val weather: List<WeatherResponse>,
         @SerializedName("dt")
        val dt: String,
  @SerializedName("name")
        val name: String



):Serializable
class MainResponse(@SerializedName("temp") var temp: String="",@SerializedName("temp_min") var temp_min: String="",@SerializedName("temp_max") var temp_max: String="")
class WeatherResponse(@SerializedName("main") var main: String="",@SerializedName("description") var description: String="")
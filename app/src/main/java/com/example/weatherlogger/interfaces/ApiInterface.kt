package com.example.weatherlogger.interfaces

import com.example.weatherlogger.DataModel.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {



    @GET("weather")
 fun getWeather(@Query("lat") lat:String,@Query("lon") lon:String,@Query ("appid") appid:String): Call<LoginResponse>
 @GET("weather")
 fun getWeatherByCityName(@Query("q") q:String,@Query ("appid") appid:String): Call<LoginResponse>

}
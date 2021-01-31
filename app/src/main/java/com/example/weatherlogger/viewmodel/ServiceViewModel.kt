package com.example.weatherlogger.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherlogger.CommonService.RetrofitService
import com.example.weatherlogger.DataModel.LoginResponse


class ServiceViewModel:ViewModel(){

    private val mService  =  RetrofitService()

    fun WeatherData(lat: String,lon:String) : MutableLiveData<LoginResponse>? {
        return mService.WeatherData(lat,lon)
    }

 fun WeatherDataByCity(city:String) : MutableLiveData<LoginResponse>? {
        return mService.WeatherDataByCityName(city)
    }







}
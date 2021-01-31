package com.example.weatherlogger.viewmodel


import androidx.lifecycle.ViewModel
import android.widget.RadioGroup
import com.example.weatherlogger.database.WeatherEntity
import com.example.weatherlogger.util.SingleLiveEvent


class MainViewModel:ViewModel(){
    var save: SingleLiveEvent<Boolean>? = null
    var delete: SingleLiveEvent<WeatherEntity>? = null

    init {
        save = SingleLiveEvent<Boolean>()
        delete = SingleLiveEvent<WeatherEntity>()

    }
    fun Save() {
        save?.value = true
    }

fun Delete(item:WeatherEntity) {
    delete?.value = item
    }





}
package com.example.weatherlogger.CommonService


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherlogger.DataModel.LoginResponse
import com.example.weatherlogger.interfaces.ApiInterface
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    var WeatherResponse: MutableLiveData<LoginResponse> = MutableLiveData()

    companion object Factory {
        var gson = GsonBuilder().setLenient().create()
        fun create(): ApiInterface {
            Log.e("retrofit","create")
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS)
                .build()

            val url="https://api.openweathermap.org/data/2.5/"
            val retrofit = Retrofit.Builder().client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(url)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }

    fun WeatherData(lat: String,lon:String): MutableLiveData<LoginResponse>? {


        val retrofitCall  = create().getWeather(lat,lon,"ef84a61da9d07d207a13e502451ab04a")

        retrofitCall.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable?) {
                Log.e("on Failure :", "retrofit error")
                WeatherResponse?.value=null
            }

            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {

if(response.code()==200) {
    WeatherResponse?.value = response.body()
}else{
    WeatherResponse?.value=null
}



            }

        })

        return WeatherResponse
    }
    fun WeatherDataByCityName(city: String): MutableLiveData<LoginResponse>? {


        val retrofitCall  = create().getWeatherByCityName(city,"ef84a61da9d07d207a13e502451ab04a")

        retrofitCall.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable?) {
                Log.e("on Failure :", "retrofit error")
                WeatherResponse?.value=null
            }

            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {

if(response.code()==200) {
    WeatherResponse?.value = response.body()
}else{
    WeatherResponse?.value=null
}



            }

        })

        return WeatherResponse
    }
   }

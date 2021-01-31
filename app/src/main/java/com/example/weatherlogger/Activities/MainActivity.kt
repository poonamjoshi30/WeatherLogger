package com.example.weatherlogger.Activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherlogger.DataModel.LoginResponse
import com.example.weatherlogger.R
import com.example.weatherlogger.adapter.WeatherAdapter
import com.example.weatherlogger.database.WeatherDao
import com.example.weatherlogger.database.WeatherEntity
import com.example.weatherlogger.database.WeatherLoggerDatabase
import com.example.weatherlogger.util.CustomeProgressDialog
import com.example.weatherlogger.util.Util
import com.example.weatherlogger.viewmodel.MainViewModel
import com.example.weatherlogger.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.activity_main.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), View.OnClickListener {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    var Action=""
    private val REQUEST_LOCATION = 1
    var viewmodel: MainViewModel? = null
    var serviceViewModel: ServiceViewModel? = null
    var customeProgressDialog: CustomeProgressDialog? = null
    private lateinit var recyclerAdapter: WeatherAdapter
    private lateinit var weatherList: MutableList<WeatherEntity>
    lateinit var weatherDao: WeatherDao
    lateinit var weatherEntity: WeatherEntity
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView( R.layout.activity_main)
       viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)
       serviceViewModel = ViewModelProviders.of(this).get(ServiceViewModel::class.java)
        weatherDao = WeatherLoggerDatabase.invoke(this).weatherDao!!
        weatherList = mutableListOf()
        customeProgressDialog = CustomeProgressDialog(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if(!query.isNullOrBlank()){
                    GetWeatherDataByCityName(query)
                }

                return false
            }
        })

        checkAndRequestPermissions()

        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                Action = "null"
            } else {
                Action = extras.getString("Data").toString()
            }
        }
        if(Action.equals("View",true)) {
            setupRecyclerView("View")
            weatherDao.allWeather.observe(this, Observer {
                weatherList.clear()
                weatherList.addAll(it)
                recyclerAdapter.notifyDataSetChanged()
            })
            ImgSave.visibility=View.GONE
            ImgView.visibility=View.GONE
            searchView.visibility=View.GONE
             }else{
            setupRecyclerView("")
            if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS()
            } else {
                getLocation()

            }
            ShowIntro(ImgSave,"Save","You can save data by clicking save button",this)

        }

        ImgSave.setOnClickListener(this)
        ImgView.setOnClickListener(this)

initObservables()
    }

fun  initObservables(){
    viewmodel?.delete!!.observe(this, Observer {
        weatherDao.deleteMsg(it)
    })
}

    //By lat lon get weather
    fun GetWeatherData() {
        if (Util.isOnline(this)) {
            customeProgressDialog?.show()


            serviceViewModel?.WeatherData(latitude.toString(), longitude.toString())
                ?.observe(this, Observer<LoginResponse> { response ->
                    customeProgressDialog?.dismiss()
                    try {
                        if (response != null) {
                            weatherList.clear()
                            var temp=""
                            var date=""
                            var name=""
                            var description=""
                            if (response?.main != null) {
                                temp=response?.main.temp
//                                tvTemperature!!.setText(temp)

                            }

                            if (!response.dt.isNullOrBlank()) {
                                date=getDateTime(response.dt)
//                                tvDate?.setText(date)
                            }
                            if(response.weather.size>0 && !response.weather.get(response.weather.size-1).description.isNullOrBlank()){
                                description=response.weather.get(response.weather.size-1).description
//                               tvDescription!!.setText(description)

                            }
                            if (!response.name.isNullOrBlank()) {
                                name=response.name
//                               tvName!!.setText(name)
                            }
                            weatherEntity= WeatherEntity(temp,description,name,date)


                                weatherList.add(weatherEntity)
                            recyclerAdapter.notifyDataSetChanged()

                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()

                    }

                })
        } else {
            Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_LONG)
                .show()
        }

    }
    //By city name get weather
    fun GetWeatherDataByCityName(city:String) {
        if (Util.isOnline(this)) {
            customeProgressDialog?.show()


            serviceViewModel?.WeatherDataByCity(city)
                ?.observe(this, Observer<LoginResponse> { response ->
                    customeProgressDialog?.dismiss()
                    try {
                        if (response != null) {
                            weatherList.clear()
                            var temp=""
                            var date=""
                            var name=""
                            var description=""
                            if (response?.main != null) {
                                temp=response?.main.temp
//                                tvTemperature!!.setText(temp)

                            }

                            if (!response.dt.isNullOrBlank()) {
                                date=getDateTime(response.dt)
//                                tvDate?.setText(date)
                            }
                            if(response.weather.size>0 && !response.weather.get(response.weather.size-1).description.isNullOrBlank()){
                                description=response.weather.get(response.weather.size-1).description
//                               tvDescription!!.setText(description)

                            }
                            if (!response.name.isNullOrBlank()) {
                                name=response.name
//                               tvName!!.setText(name)
                            }
                            weatherEntity= WeatherEntity(temp,description,name,date)

                                weatherList.add(weatherEntity)
                            recyclerAdapter.notifyDataSetChanged()


                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()

                    }

                })
        } else {
            Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_LONG)
                .show()
        }

    }

    //convert timezone
    fun getDateTime(dt: String): String {
        val calendar = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currenTimeZone = Date(dt.toLong() * 1000)
        return sdf.format(currenTimeZone)
    }


    private fun checkAndRequestPermissions(): Boolean {
        val camerapermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    private fun OnGPS() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialog, which ->
                startActivity(
                    Intent(
                        ACTION_LOCATION_SOURCE_SETTINGS
                    )
                )
            }).setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    //setup Recycleview
    private fun setupRecyclerView(flag:String) {

        val linearLayoutManager =
            LinearLayoutManager(this)
        rvWeather.layoutManager = linearLayoutManager
        recyclerAdapter = WeatherAdapter(this,  weatherList,viewmodel!!,flag)
        rvWeather.adapter = recyclerAdapter
        rvWeather.isNestedScrollingEnabled = false
    }

    //setup Tooltip
    fun ShowIntro(view: View?, title: String?, text: String?, context: Context) {

        GuideView.Builder(context)
            .setTitle(title)
            .setContentText(text)
            .setTargetView(view)
            .setIndicatorHeight(0F)
            .setGravity(Gravity.center)
            .setContentTextSize(12) //optional
            .setTitleTextSize(18) //optional
            .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
            .setGuideListener(object : GuideListener {
                override fun onDismiss(view: View?) {

                }
            })
            .build()
            .show()

    }

    //get location
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            try {
                val locationGPS: Location =
                    locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                if (locationGPS != null) {
                    val lat: Double = locationGPS.getLatitude()
                    val longi: Double = locationGPS.getLongitude()
                    latitude = lat.toString()
                    longitude = longi.toString()
                    Toast.makeText(
                        this,
                        "Your Location: \nLatitude: $latitude\nLongitude: $longitude",
                        Toast.LENGTH_SHORT
                    ).show()
                    GetWeatherData()
                } else {
                    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                GetWeatherDataByCityName("Mountain View")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d("tag", "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED

                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]

                    if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
                } else {

                }
            }
        }

    }


    //click listener
    override fun onClick(p0: View?) {
     when(p0!!.id){
         R.id.ImgSave->{
             try {
                 weatherDao.insertMsg(weatherEntity)
                 Util.showToast(this,"Saved Successfully")
             }catch (e:Exception){
                 Util.showToast(this,"Some error occurred")
             }
         }
         R.id.ImgView->{
             try {
                 val i = Intent(this, MainActivity::class.java)

                 i.putExtra("Data", "View")
                 startActivity(i)
                 overridePendingTransition(R.anim.animation_enter,R.anim.animation_leave)
             }catch (e:Exception){

             }

         }
     }
    }
}
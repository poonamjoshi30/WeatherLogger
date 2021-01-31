package com.example.weatherlogger.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import java.util.regex.Pattern
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar


class Util {

    companion object {

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }








        fun showToast(context: Context, msg: String) {
            val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
            toast.show()
        }



       }
}






package com.example.weatherlogger.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.database.WeatherEntity
import com.example.weatherlogger.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.weather_adapter.view.*


class WeatherAdapter(
    private val mContext: Context,
    private val weatherList: MutableList<WeatherEntity>,viewModel: MainViewModel,flag:String
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    var viewModel=viewModel
    var flag=flag
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.weather_adapter, parent, false),viewModel
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (weatherList.size > 0) {

            val itemList = weatherList[position]
            holder.bindItem(itemList)
           if(flag.equals("View",true)){
               holder.itemView.ImgDelete.visibility=View.VISIBLE
           }else{
               holder.itemView.ImgDelete.visibility=View.GONE
           }
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }


    inner class ViewHolder(itemView: View,viewModel:MainViewModel) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
var viewModel=viewModel
     lateinit   var item:WeatherEntity
        fun bindItem(itemList: WeatherEntity) {
            item=itemList
            itemView.tvName.text = itemList.name
            itemView.tvDate.text = itemList.date
            itemView.tvdate.text = itemList.date
            itemView.tvDescription.text = itemList.description
            itemView.tvTemp.text = itemList.temp
            itemView.tvTemperature.text = itemList.temp

            itemView.tvLessDetails.setOnClickListener(this)
            itemView.tvMoreDetails.setOnClickListener(this)
            itemView.ImgDelete.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
           when(p0!!.id){
               R.id.tvLessDetails,R.id.tvMoreDetails->{
                   if (itemView.cvLessDetails!!.isVisible) {
                       val slideUp: Animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_enter)

                       itemView.cvDetails!!.visibility = View.VISIBLE
                       itemView.cvLessDetails!!.visibility = View.GONE
                       itemView.cvDetails!!.startAnimation(slideUp);
                   } else {
                       val slideUp: Animation = AnimationUtils.loadAnimation(mContext, R.anim.animation_leave)

                       itemView.cvDetails!!.visibility = View.GONE
                       itemView.cvLessDetails!!.visibility = View.VISIBLE
                       itemView.cvLessDetails!!.startAnimation(slideUp);
                   }

               }
               R.id.ImgDelete->{
                   viewModel.delete!!.value=item
               }
           }
        }
    }



}
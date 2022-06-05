package com.mhdrk.openweather.lists.daily

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhdrk.openweather.R
import com.mhdrk.openweather.data.onecall.Daily
import kotlinx.android.synthetic.main.item_daily_list.view.*

class DailyLIstAdapter(val dailyList: List<Daily>):RecyclerView.Adapter<DailyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyListViewHolder {
        return DailyListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daily_list, parent,false)
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: DailyListViewHolder, position: Int) {
        val day = dailyList[position]
        val sdf = java.text.SimpleDateFormat("EEEE")
        val date = java.util.Date(day.dt.toLong() * 1000)
        val result = sdf.format(date)

        when(position){
            0 -> holder.itemView.DayNameTV.text = "Today"
            1 -> holder.itemView.DayNameTV.text = "Tomorrow"
            else ->  holder.itemView.DayNameTV.text = result.toString()
        }
        holder.itemView.DailyTempTV.text = day.temp.max.toInt().toString() + "°  /  " + day.temp.min.toInt().toString() + "°"
        when( day.weather[0].description){
            "broken clouds" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_broken_cloud)
            "clear sky" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_clear_sky_morning)
            "few clouds" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_few_cloud_morning)
            "rain" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_rain)
            "showers" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_shower_raint)
            "snow" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_snow)
            "thunder" -> holder.itemView.DailyWeatherIconIV.setImageResource(R.drawable.ic_thunderstorm)
        }
        holder.itemView.DailyWeatherIconIV

    }

    override fun getItemCount(): Int {
        return 7
    }
}
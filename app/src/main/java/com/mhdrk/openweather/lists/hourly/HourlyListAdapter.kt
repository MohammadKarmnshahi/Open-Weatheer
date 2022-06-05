package com.mhdrk.openweather.lists.hourly

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mhdrk.openweather.R
import com.mhdrk.openweather.data.onecall.Hourly
import kotlinx.android.synthetic.main.item_hourly_list.view.*

class HourlyListAdapter(val hourlyList: List<Hourly>): RecyclerView.Adapter<HourlyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyListViewHolder {
        return HourlyListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hourly_list,parent,false)
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: HourlyListViewHolder, position: Int) {
        val hour = hourlyList[position]
        val sdf = java.text.SimpleDateFormat("h aaa")
        val date = java.util.Date(hour.dt.toLong() * 1000)
        val result = sdf.format(date)
        holder.itemView.HourlyTempTV.text = hour.temp.toInt().toString()+ "°"
        holder.itemView.HourlyTimeTV.text = result.toString()
        when( hour.weather[0].description){
            "Clouds" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_broken_cloud)
            "clear sky" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_clear_sky_morning)
            "Few Clouds" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_few_cloud_morning)
            "Rain" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_rain)
            "Showers" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_shower_raint)
            "Snow" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_snow)
            "Thunder" -> holder.itemView.HourlyIconIV.setImageResource(R.drawable.ic_thunderstorm)
        }
        holder.itemView.HourlyIconIV
    }

    override fun getItemCount(): Int {
        return 7
    }
}
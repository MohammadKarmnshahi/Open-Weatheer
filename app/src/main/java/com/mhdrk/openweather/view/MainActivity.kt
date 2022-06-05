package com.mhdrk.openweather.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mhdrk.openweather.R
import com.mhdrk.openweather.data.current.CurrentWeatherResponse
import com.mhdrk.openweather.data.onecall.Daily
import com.mhdrk.openweather.data.onecall.Hourly
import com.mhdrk.openweather.data.onecall.OneCallResponse
import com.mhdrk.openweather.lists.daily.DailyLIstAdapter
import com.mhdrk.openweather.lists.hourly.HourlyListAdapter
import com.mhdrk.openweather.server.WeatherEndpoint
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat=intent.getStringExtra("lat")
        val lon=intent.getStringExtra("lon")

        transparentStatusBar()
        changeBackground()
        getJsonData(lat, lon)
        getDay()
    }

    private fun getJsonData(lat:String?,lon:String?) {
        val apiKey = "7a4c256217d454cee80c725e84457ee0"
        val lang = "en"
        val units = "metric"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val create = retrofit.create(WeatherEndpoint::class.java)

        if (lat != null && lon != null) {
            create.getCurrentWeather(apiKey, lang, lon, lat, units).enqueue(object: Callback<CurrentWeatherResponse>{
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    if (response.isSuccessful){
                        val currentBody = response.body()
                        val temp = currentBody?.main?.temp
                        CurrentWeatherTempTV.text = temp?.toInt().toString()+"°"
                        val tempMax = currentBody?.main?.tempMax
                        val tempMin = currentBody?.main?.tempMin
                        WeatherRangeTV.text = tempMin?.toInt().toString() + "°"+ " / " + tempMax?.toInt().toString()+ "°"
                        when(currentBody?.weather?.get(0)?.main){
                            "Clouds" -> CurrentWeatherIV.setImageResource(R.drawable.ic_broken_cloud)
                            "Clear" -> CurrentWeatherIV.setImageResource(R.drawable.ic_clear_sky_morning)
                            "Few Clouds" -> CurrentWeatherIV.setImageResource(R.drawable.ic_few_cloud_morning)
                            "Rain" -> CurrentWeatherIV.setImageResource(R.drawable.ic_rain)
                            "Showers" -> CurrentWeatherIV.setImageResource(R.drawable.ic_shower_raint)
                            "Snow" -> CurrentWeatherIV.setImageResource(R.drawable.ic_snow)
                            "Thunder" -> CurrentWeatherIV.setImageResource(R.drawable.ic_thunderstorm)
                        }

                        val cityName = currentBody?.name
                        CityTV.text = cityName

                    }else{
                        Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }

        if (lat != null && lon != null) {
            create.getOneCallWeather(apiKey, lang, lon, lat, units).enqueue(object: Callback<OneCallResponse>{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<OneCallResponse>,
                    response: Response<OneCallResponse>
                ) {
                    if (response.isSuccessful){
                        val hourlyBody= response.body()?.hourly
                        hourlyBody?.let {
                            getHourlyView(it)
                        }

                        val dailyBody = response.body()?.daily
                        dailyBody?.let {
                            getDailyView(it)
                        }
                    }else{
                        Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<OneCallResponse>, t: Throwable) {
                    Toast.makeText(applicationContext,t.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun getTime(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    private fun getDay(): Int{
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    }

    private fun changeBackground(){
        when (getTime()) {
            in 0..6 -> MainPage.setBackgroundResource(R.drawable.ic_night_background)
            in 6..19 -> MainPage.setBackgroundResource(R.drawable.ic_morning_background)
            in 18..19 -> MainPage.setBackgroundResource(R.drawable.ic_noon_background)
            in 19..21 -> MainPage.setBackgroundResource(R.drawable.ic_dawn_background)
            in 21..23 -> MainPage.setBackgroundResource(R.drawable.ic_evening_background)
        }
    }

    private fun getHourlyView(hourly: List<Hourly>){
        HourlyListRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        HourlyListRV.adapter = HourlyListAdapter(hourly)
        HourlyListRV.isNestedScrollingEnabled = false
    }

    private fun getDailyView(daily: List<Daily>){
        DailyListRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        DailyListRV.adapter = DailyLIstAdapter(daily)
        HourlyListRV.isNestedScrollingEnabled = false
    }

    private fun transparentStatusBar(){
        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}
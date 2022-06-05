package com.mhdrk.openweather.server

import com.mhdrk.openweather.data.current.CurrentWeatherResponse
import com.mhdrk.openweather.data.onecall.Daily
import com.mhdrk.openweather.data.onecall.Hourly
import com.mhdrk.openweather.data.onecall.OneCallResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndpoint {

    @GET("weather")
    fun getCurrentWeather(
        @Query("appid") apiKey: String = "7a4c256217d454cee80c725e84457ee0",
        @Query("lang") lang: String= "en",
        @Query("lon") lon: String,
        @Query("lat") lat: String,
        @Query("units") units : String="metric"
    ): Call<CurrentWeatherResponse>

    @GET("onecall")
    fun getOneCallWeather(
        @Query("appid") apiKey: String = "7a4c256217d454cee80c725e84457ee0",
        @Query("lang") lang: String= "en",
        @Query("lon") lon: String ,
        @Query("lat") lat: String,
        @Query("units") units : String="metric"
    ): Call<OneCallResponse>
}
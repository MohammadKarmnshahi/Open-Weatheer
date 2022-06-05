package com.mhdrk.openweather.data.onecall

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
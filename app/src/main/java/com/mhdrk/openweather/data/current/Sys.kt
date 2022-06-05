package com.mhdrk.openweather.data.current


import com.google.gson.annotations.SerializedName

data class Sys(
    val country: String?,
    val sunrise: Int?,
    val sunset: Int?
)
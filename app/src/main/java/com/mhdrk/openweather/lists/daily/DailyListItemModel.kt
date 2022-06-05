package com.mhdrk.openweather.lists.daily

data class DailyListItemModel(
    val dayName: String,
    val dailyIcon: Int,
    val dayTempMax: String,
    val dayTempMin: String
)
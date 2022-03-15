package com.example.android_2_sem.data.mappers

import com.example.android_2_sem.data.response.WeatherResponse
import com.example.android_2_sem.domain.entity.Weather

class WeatherMapper {
    fun map(response: WeatherResponse): Weather = Weather(
        id = response.id,
        city = response.name,
        temp = response.main.temp.toString() + "°C",
        feels_like = response.main.feels_like.toString(),
        humidity = response.main.humidity,
        windDirection = response.wind.deg,
    )
}
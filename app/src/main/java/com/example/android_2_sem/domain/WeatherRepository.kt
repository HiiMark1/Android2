package com.example.android_2_sem.domain

import com.example.android_2_sem.data.response.cities_list_response.CitiesResponse
import com.example.android_2_sem.domain.entity.Weather

interface WeatherRepository {
    suspend fun getWeatherByName(city: String): Weather
    suspend fun getCities(lat: String, lon: String): CitiesResponse
    suspend fun getWeatherById(id: String): Weather
}
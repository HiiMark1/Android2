package com.example.android_2_sem.data

import com.example.android_2_sem.data.api.Api
import com.example.android_2_sem.data.mappers.WeatherMapper
import com.example.android_2_sem.data.response.cities_list_response.CitiesResponse
import com.example.android_2_sem.domain.WeatherRepository
import com.example.android_2_sem.domain.entity.Weather

class WeatherRepositoryImpl(
    private val api: Api,
    private val mapper: WeatherMapper,
) : WeatherRepository {
    override suspend fun getWeatherByName(city: String): Weather {
        return mapper.map(api.getWeatherByName(city))
    }

    override suspend fun getCities(lat: String, lon: String): CitiesResponse {
        return api.getCities(lat, lon)
    }

    override suspend fun getWeatherById(id: String): Weather {
        return mapper.map(api.getWeatherById(id))
    }
}
package com.example.android_2_sem.domain.usecases

import com.example.android_2_sem.domain.WeatherRepository
import com.example.android_2_sem.domain.entity.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherByNameUseCase (
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Weather {
        return withContext(Dispatchers.Main) {
            weatherRepository.getWeatherByName(cityName)
        }
    }
}
package com.example.android_2_sem.domain.usecases

import com.example.android_2_sem.domain.WeatherRepository
import com.example.android_2_sem.domain.entity.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWeatherByIdUseCase (
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(id: String): Weather {
        return withContext(Dispatchers.Main) {
            weatherRepository.getWeatherById(id)
        }
    }
}
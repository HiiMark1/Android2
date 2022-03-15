package com.example.android_2_sem.domain.usecases

import com.example.android_2_sem.data.response.cities_list_response.CitiesResponse
import com.example.android_2_sem.domain.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCitiesListUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: String, lon: String): CitiesResponse {
        return withContext(Dispatchers.Main) {
            weatherRepository.getCities(lat, lon)
        }
    }
}
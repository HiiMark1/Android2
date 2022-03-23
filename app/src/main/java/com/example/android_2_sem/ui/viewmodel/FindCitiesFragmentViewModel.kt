package com.example.android_2_sem.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2_sem.data.response.cities_list_response.CitiesResponse
import com.example.android_2_sem.domain.entity.Weather
import com.example.android_2_sem.domain.usecases.GetCitiesListUseCase
import com.example.android_2_sem.domain.usecases.GetWeatherByNameUseCase
import kotlinx.coroutines.launch

class FindCitiesFragmentViewModel(
    private val getCitiesListUseCase: GetCitiesListUseCase,
    private val getWeatherByCityUseCase: GetWeatherByNameUseCase
) : ViewModel() {
    private val _citiesList: MutableLiveData<Result<CitiesResponse>> = MutableLiveData()
    var citiesList: LiveData<Result<CitiesResponse>> = _citiesList

    private var _weather: MutableLiveData<Result<Weather>> = MutableLiveData()
    var weather: LiveData<Result<Weather>> = _weather

    fun getCitiesList(lat: String, lon: String) {
        viewModelScope.launch {
            try {
                val citiesList = getCitiesListUseCase(lat, lon)
                _citiesList.value = Result.success(citiesList)
            } catch (ex: Exception) {
                _citiesList.value = Result.failure(ex)
            }
        }
    }

    fun getCityByName(city: String) {
        viewModelScope.launch {
            try {
                val weather = getWeatherByCityUseCase(city)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
            }
        }
    }
}
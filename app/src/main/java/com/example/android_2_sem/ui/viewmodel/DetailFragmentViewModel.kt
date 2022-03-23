package com.example.android_2_sem.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_2_sem.domain.entity.Weather
import com.example.android_2_sem.domain.usecases.GetWeatherByIdUseCase
import kotlinx.coroutines.launch

class DetailFragmentViewModel(
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase
) : ViewModel() {
    private var _weather: MutableLiveData<Result<Weather>> = MutableLiveData()
    var weather: LiveData<Result<Weather>> = _weather

    fun getWeatherByName(id: String) {
        viewModelScope.launch {
            try {
                val weather = getWeatherByIdUseCase(id)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
            }
        }
    }
}
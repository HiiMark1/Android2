package com.example.android_2_sem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_2_sem.di.DIContainer
import com.example.android_2_sem.ui.viewmodel.DetailFragmentViewModel
import com.example.android_2_sem.ui.viewmodel.FindCitiesFragmentViewModel

class ViewModelFactory(
    private val di: DIContainer,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(FindCitiesFragmentViewModel::class.java) ->
                FindCitiesFragmentViewModel(di.getCitiesListUseCase, di.getWeatherByNameUseCase)
                        as? T ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(DetailFragmentViewModel::class.java) ->
                DetailFragmentViewModel(di.getWeatherByIdUseCase)
                        as? T ?: throw IllegalArgumentException("Unknown ViewModel class")
            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
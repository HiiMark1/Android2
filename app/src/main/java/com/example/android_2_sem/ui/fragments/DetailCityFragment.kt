package com.example.android_2_sem.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android_2_sem.R
import com.example.android_2_sem.data.WeatherRepositoryImpl
import com.example.android_2_sem.data.mappers.WeatherMapper
import com.example.android_2_sem.databinding.FragmentDetailCityBinding
import com.example.android_2_sem.di.DIContainer
import com.example.android_2_sem.domain.usecases.GetWeatherByIdUseCase
import kotlinx.coroutines.launch

class DetailCityFragment: Fragment(R.layout.fragment_detail_city) {
    private lateinit var binding: FragmentDetailCityBinding
    private lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObjects()
        binding = FragmentDetailCityBinding.bind(view)

        lifecycleScope.launch {
            val id = arguments?.getInt("id").toString()

            with(binding) {
                val cityWeather = getWeatherByIdUseCase(id)
                val temp = cityWeather.temp + "°C"
                tvTemperature.text = temp
                tvFeeling.text = cityWeather.feels_like
                tvHumidity1.text = cityWeather.humidity.toString()
                when(cityWeather.windDirection){
                    in 0..25 -> tvWindDirection.text = "С"
                    in 25..70 -> tvWindDirection.text = "СВ"
                    in 70..115 -> tvWindDirection.text = "В"
                    in 115..160 -> tvWindDirection.text = "ЮВ"
                    in 160..205 -> tvWindDirection.text = "Ю"
                    in 205..250 -> tvWindDirection.text = "ЮЗ"
                    in 250..295 -> tvWindDirection.text = "З"
                    in 295..340 -> tvWindDirection.text = "СЗ"
                    in 340..360 -> tvWindDirection.text = "С"
                }
            }
        }
    }

    private fun initObjects(){
        getWeatherByIdUseCase= GetWeatherByIdUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api= DIContainer.api,
                mapper = WeatherMapper()
            )
        )
    }
}
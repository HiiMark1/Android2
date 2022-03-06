package com.example.android_2_sem.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android_2_sem.R
import com.example.android_2_sem.data.WeatherRepository
import com.example.android_2_sem.databinding.FragmentDetailCityBinding
import kotlinx.coroutines.launch

class DetailCityFragment: Fragment(R.layout.fragment_detail_city) {
    private lateinit var binding: FragmentDetailCityBinding

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailCityBinding.bind(view)

        lifecycleScope.launch {
            val id = arguments?.getInt("id").toString()

            with(binding) {
                val weatherResponse = repository.getWeatherById(id)
                val temp = weatherResponse.main.temp.toString() + "°C"
                tvTemperature.text = temp
                tvFeeling.text = weatherResponse.main.feels_like.toString()
                tvHumidity1.text = weatherResponse.main.humidity.toString()
                when(weatherResponse.wind.deg){
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
}
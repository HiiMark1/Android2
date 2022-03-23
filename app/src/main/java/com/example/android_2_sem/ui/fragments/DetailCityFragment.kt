package com.example.android_2_sem.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android_2_sem.R
import com.example.android_2_sem.databinding.FragmentDetailCityBinding
import com.example.android_2_sem.di.DIContainer
import com.example.android_2_sem.domain.entity.Weather
import com.example.android_2_sem.ui.viewmodel.DetailFragmentViewModel
import com.example.android_2_sem.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DetailCityFragment : Fragment(R.layout.fragment_detail_city) {
    private lateinit var binding: FragmentDetailCityBinding
    private lateinit var viewModel: DetailFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFactory()
        initObservers()

        binding = FragmentDetailCityBinding.bind(view)

        val id = arguments?.getInt("id").toString()
        viewModel.getWeatherByName(id)
    }

    private fun initObservers() {
        viewModel.weather.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                setWeather(it)
            }, onFailure = {
                Snackbar.make(
                    binding.root,
                    "Не удалось найти город",
                    Snackbar.LENGTH_LONG
                ).show()
            })
        }
    }

    private fun initFactory() {
        val factory = ViewModelFactory(DIContainer)
        viewModel = ViewModelProvider(
            this,
            factory
        )[DetailFragmentViewModel::class.java]
    }

    private fun setWeather(weather: Weather) {
        with(binding) {
            val temp = weather.temp + "°C"
            tvTemperature.text = temp
            tvFeeling.text = weather.feels_like
            tvHumidity1.text = weather.humidity.toString()
            when (weather.windDirection) {
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
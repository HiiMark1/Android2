package com.example.android_2_sem.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android_2_sem.R
import com.example.android_2_sem.adapters.CityListAdapter
import com.example.android_2_sem.data.WeatherRepositoryImpl
import com.example.android_2_sem.data.mappers.WeatherMapper
import com.example.android_2_sem.data.response.cities_list_response.City
import com.example.android_2_sem.databinding.FragmentFindCitiesBinding
import com.example.android_2_sem.di.DIContainer
import com.example.android_2_sem.domain.usecases.GetCitiesListUseCase
import com.example.android_2_sem.domain.usecases.GetWeatherByNameUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.lang.Exception

class FindCitiesFragment : Fragment(R.layout.fragment_find_cities) {
    private var cityListAdapter: CityListAdapter? = null
    private lateinit var binding: FragmentFindCitiesBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = "51.509865"
    private var lon = "-0.118092"
    private lateinit var getCitiesListUseCase: GetCitiesListUseCase
    private lateinit var getWeatherByNameUseCase: GetWeatherByNameUseCase

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLastLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLastLocation()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObjects()
        binding = FragmentFindCitiesBinding.bind(view)

        lifecycleScope.launch {
            requestLocationAccess()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            val cities = getCitiesListUseCase(lat, lon).list as MutableList<City>

            cityListAdapter = CityListAdapter {
                lifecycleScope.launch {
                    try {
                        val bundle = Bundle()
                        bundle.putInt("id", it)
                        val detailWeatherFragment = DetailCityFragment()
                        detailWeatherFragment.arguments = bundle
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, detailWeatherFragment)
                            ?.addToBackStack("weather")
                            ?.commit()
                    } catch (e: Exception) {
                        println(e)
                    }
                }
                cityListAdapter?.submitList(cities)
            }

            binding.cities.run {
                adapter = cityListAdapter
            }

            cityListAdapter?.submitList(cities)
        }

        with(binding) {
            svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    lifecycleScope.launch {
                        try {
                            val bundle = Bundle()
                            val id = getWeatherByNameUseCase(query).id
                            bundle.putInt("id", id)
                            val detailWeatherFragment = DetailCityFragment()
                            detailWeatherFragment.arguments = bundle
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.container, detailWeatherFragment)
                                ?.addToBackStack("weather")
                                ?.commit()
                        } catch (e: Exception) {
                            println(e)
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
    }

    private fun initObjects() {
        getWeatherByNameUseCase= GetWeatherByNameUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api= DIContainer.api,
                mapper = WeatherMapper()
            )
        )
        getCitiesListUseCase= GetCitiesListUseCase(
            weatherRepository = WeatherRepositoryImpl(
                api= DIContainer.api,
                mapper = WeatherMapper()
            )
        )
    }

    private fun requestLocationAccess() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun getLastLocation() {
        if (checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location == null) {
                        Snackbar.make(
                            binding.root,
                            "Не удалось получить данные о местоположение",
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        lat = location.latitude.toString()
                        lon = location.longitude.toString()
                    }
                }
        }
    }

    private fun checkPermission(vararg perm: String): Boolean {
        val havePermissions = perm.toList().all {
            ContextCompat.checkSelfPermission(requireContext(), it) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (!havePermissions) {
            return false
        }
        return true
    }
}
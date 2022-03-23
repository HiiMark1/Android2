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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.android_2_sem.R
import com.example.android_2_sem.adapters.CityListAdapter
import com.example.android_2_sem.databinding.FragmentFindCitiesBinding
import com.example.android_2_sem.di.DIContainer
import com.example.android_2_sem.ui.viewmodel.FindCitiesFragmentViewModel
import com.example.android_2_sem.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.lang.Exception

class FindCitiesFragment : Fragment(R.layout.fragment_find_cities) {
    private var citiesListAdapter: CityListAdapter? = null
    private lateinit var binding: FragmentFindCitiesBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = "51.509865"
    private var lon = "-0.118092"
    private lateinit var viewModel: FindCitiesFragmentViewModel
    val bundle = Bundle()


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

        initFactory()
        initObservers()

        binding = FragmentFindCitiesBinding.bind(view)
        viewModel.getCitiesList(lat, lon)

        requestLocationAccess()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        with(binding) {
            svCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    lifecycleScope.launch {
                        try {
                            val id = DIContainer.getWeatherByNameUseCase(query).id
                            bundle.putInt("id", id)
                            val detailWeatherFragment = DetailCityFragment()
                            detailWeatherFragment.arguments = bundle
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.container, detailWeatherFragment)
                                ?.addToBackStack("city")
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

    private fun initFactory() {
        val factory = ViewModelFactory(DIContainer)
        viewModel = ViewModelProvider(
            this,
            factory
        )[FindCitiesFragmentViewModel::class.java]
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

    private fun goToDetailWeatherFragmentById(id: Int) {
        lifecycleScope.launch {
            try {
                val bundle = Bundle()
                bundle.putInt("id", id)
                val detailCityFragment = DetailCityFragment()
                detailCityFragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, detailCityFragment)
                    ?.addToBackStack("city")
                    ?.commit()
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun initObservers() {
        viewModel.citiesList.observe(viewLifecycleOwner) { list ->
            list.fold(onSuccess = {
                val cities = it.list as MutableList

                citiesListAdapter = CityListAdapter { clickedCity ->
                    goToDetailWeatherFragmentById(clickedCity)
                    citiesListAdapter?.submitList(cities)
                }

                binding.cities.run {
                    adapter = citiesListAdapter
                }

                citiesListAdapter?.submitList(cities)
            }, onFailure = {
                Snackbar.make(
                    binding.root,
                    "Не удалось найти город",
                    Snackbar.LENGTH_LONG
                ).show()
            })
        }
    }
}
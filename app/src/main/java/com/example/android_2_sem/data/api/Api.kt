package com.example.android_2_sem.data.api

import com.example.android_2_sem.data.response.WeatherResponse
import com.example.android_2_sem.data.response.cities_list_response.CitiesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather?")
    suspend fun getWeatherByName(@Query("q") city: String): WeatherResponse

    @GET("find?cnt=10")
    suspend fun getCities(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): CitiesResponse

    @GET("weather?")
    suspend fun getWeatherById(@Query("id") id: String): WeatherResponse
}
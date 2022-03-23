package com.example.android_2_sem.di

import com.example.android_2_sem.data.WeatherRepositoryImpl
import com.example.android_2_sem.data.api.Api
import com.example.android_2_sem.data.mappers.WeatherMapper
import com.example.android_2_sem.domain.WeatherRepository
import com.example.android_2_sem.domain.usecases.GetCitiesListUseCase
import com.example.android_2_sem.domain.usecases.GetWeatherByIdUseCase
import com.example.android_2_sem.domain.usecases.GetWeatherByNameUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val QUERY_API_KEY = "appid"
private const val API_KEY = "03a004c6d9ac174fdc05238823f5bd23"
private const val QUERY_UNITS = "units"
private const val QUERY_METRIC = "metric"
private const val QUERY_LANG = "lang"
private const val QUERY_RU = "ru"

object DIContainer {

    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val metricInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_UNITS, QUERY_METRIC)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val langInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_LANG, QUERY_RU)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(metricInterceptor)
            .addInterceptor(langInterceptor)
            .build()
    }

    val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        api = api,
        mapper = WeatherMapper()
    )

    val getWeatherByNameUseCase: GetWeatherByNameUseCase = GetWeatherByNameUseCase(
        weatherRepository = weatherRepository
    )

    val getCitiesListUseCase: GetCitiesListUseCase = GetCitiesListUseCase(
        weatherRepository = weatherRepository
    )

    val getWeatherByIdUseCase: GetWeatherByIdUseCase = GetWeatherByIdUseCase(
        weatherRepository = weatherRepository
    )
}
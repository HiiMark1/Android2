package com.example.android_2_sem.domain.entity

data class Weather(
    val id: Int,
    val city: String,
    val temp: String,
    val feels_like: String,
    val humidity: Int,
    val windDirection: Int,
)
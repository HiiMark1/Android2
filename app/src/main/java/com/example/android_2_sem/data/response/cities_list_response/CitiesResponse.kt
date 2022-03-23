package com.example.android_2_sem.data.response.cities_list_response

data class CitiesResponse(
    val cod: String,
    val count: Int,
    val list: List<City>,
    val message: String
)
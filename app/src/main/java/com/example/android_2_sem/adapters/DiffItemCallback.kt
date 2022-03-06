package com.example.android_2_sem.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.android_2_sem.data.response.cities_list_response.City

class DiffItemCallback : DiffUtil.ItemCallback<City>() {

    override fun areItemsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(
        oldItem: City,
        newItem: City
    ): Boolean = oldItem == newItem
}
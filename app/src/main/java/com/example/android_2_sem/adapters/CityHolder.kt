package com.example.android_2_sem.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_2_sem.data.response.cities_list_response.City
import com.example.android_2_sem.databinding.ItemCityBinding

class CityHolder(
    private val binding: ItemCityBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var city: City? = null

    init {
        itemView.setOnClickListener {
            city?.id?.also(action)
        }
    }

    fun bind(item: City) {
        this.city = item
        with(binding) {
            tvCity.text = item.name
            tvTemperature.text = item.main.temp.toString()
            when(item.main.temp){
                in 20.0..55.0 -> tvTemperature.setTextColor(Color.rgb(128, 0, 0))
                in 0.0..20.0 -> tvTemperature.setTextColor(Color.rgb(255, 0, 0))
                in -10.0..10.0-> tvTemperature.setTextColor(Color.rgb(0, 255, 0))
                in -25.0..-10.0 -> tvTemperature.setTextColor(Color.rgb(0, 0, 128))
                in -100.0..-25.0 -> tvTemperature.setTextColor(Color.rgb(0, 0, 255))
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            action: (Int) -> Unit
        ) = CityHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}
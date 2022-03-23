package com.example.android_2_sem.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.android_2_sem.data.response.cities_list_response.City

class CityListAdapter(
    private val action: (Int) -> Unit,
) : ListAdapter<City, CityHolder>(DiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CityHolder = CityHolder.create(parent, action)

    override fun onBindViewHolder(holder: CityHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<City>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
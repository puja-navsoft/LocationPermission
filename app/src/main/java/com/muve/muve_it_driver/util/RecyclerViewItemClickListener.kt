package com.muve.muve_it_driver.util

import com.muve.muve_it_driver.model.countrymodel.DataItem

interface RecyclerViewItemClickListener {

    fun onItemClick(position: Int , countryList: ArrayList<DataItem>)
}
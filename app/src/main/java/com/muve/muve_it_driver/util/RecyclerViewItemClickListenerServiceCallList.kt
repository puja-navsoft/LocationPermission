package com.muve.muve_it_driver.util

import com.muve.muve_it_driver.model.servicecalllisting.DetailItem

interface RecyclerViewItemClickListenerServiceCallList {

    fun onItemClick(position: Int , countryList: ArrayList<DetailItem>)
}
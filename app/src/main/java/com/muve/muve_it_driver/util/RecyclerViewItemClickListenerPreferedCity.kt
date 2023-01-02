package com.muve.muve_it_driver.util

import com.muve.muve_it_driver.model.aboutusmodel.cityDataItemAbout

interface RecyclerViewItemClickListenerPreferedCity {

    fun onItemClick(position: Int , countryList: ArrayList<cityDataItemAbout>)
}
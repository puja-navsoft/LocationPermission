package com.muve.muve_it_driver.util

import com.muve.muve_it_driver.model.aboutusmodel.DataItemAbout

interface RecyclerViewItemClickListenerAboutUS {

    fun onItemClick(position: Int , countryList: ArrayList<DataItemAbout>)
}
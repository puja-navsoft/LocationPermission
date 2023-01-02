package com.muve.muve_it_driver.util

import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem


interface RecyclerViewItemClickListenerFavPlace {

    fun onItemClick(position: Int , countryList: List<FavouritePlacesItem>)
}
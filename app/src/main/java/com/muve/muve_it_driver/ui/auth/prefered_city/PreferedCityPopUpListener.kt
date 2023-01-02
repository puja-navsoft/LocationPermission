package com.muve.muve_it_driver.ui.auth.prefered_city

interface PreferedCityPopUpListener {

    fun onStarting()
    fun onSuccess()
    fun onFailure(message:String)
    fun onCountryPopUpCancel()
}
package com.muve.muve_it_driver.ui.auth.choose_country

interface CountryPopUpListener {

    fun onStarting()
    fun onSuccess()
    fun onFailure(message:String)
    fun onCountryPopUpCancel()
}
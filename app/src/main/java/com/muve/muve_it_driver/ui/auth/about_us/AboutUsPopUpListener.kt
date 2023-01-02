package com.muve.muve_it_driver.ui.auth.about_us

interface AboutUsPopUpListener {

    fun onStarting()
    fun onSuccess()
    fun onFailure(message:String)
    fun onCountryPopUpCancel()
}
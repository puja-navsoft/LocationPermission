package com.muve.muve_it_driver.ui.auth.register

interface RegistrationListener {


    fun onStarting()
    fun onSuccess()
    fun onFailure(message:String)
    fun onBackButtonPressed()
    fun onCountryOpen()
    fun clickAboutUsButton()
    fun clickPreferedCityButton()
    fun clickAlternateCityButton()
    fun showPassword()
    fun showPasswordConfirm()
   // fun showGender()
   // fun setDate(date:String)

}
package com.muve.muve_it_driver.ui.auth.forgetpassword

interface ForgetListener {

    fun onStarting()
    fun onSuccess()
    fun onFailure(message:String)
    fun onBackButtonPressed()
    fun onCountryOpen()
    fun showHidePassword()
}
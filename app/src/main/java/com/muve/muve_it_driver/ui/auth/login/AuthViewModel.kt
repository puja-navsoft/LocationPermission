package com.muve.muve_it_driver.ui.auth.login

import android.view.View
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var phoneNumber : String? = null
    var password : String? = null

    var myAuthListener : AuthListener? = null

    fun onCountryListOpen(view: View){

        myAuthListener?.onCountryOpen()

    }

    fun onLogInButton(view: View){

        myAuthListener?.onSuccess()


    }

    fun onBackButton(view: View){

        myAuthListener?.onBackButtonPressed()
    }

    fun showHidePassword(view: View){


        myAuthListener?.showHidePassword()

    }



}
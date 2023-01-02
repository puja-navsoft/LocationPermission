package com.muve.muve_it_driver.ui.auth.forgetpassword

import android.view.View
import androidx.lifecycle.ViewModel

class ForgetViewModel : ViewModel() {

    var phoneNumber : String? = null
    var password : String? = null

    var myAuthListener : ForgetListener? = null

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
package com.muve.muve_it_driver.ui.auth.choose_country

import android.view.View
import androidx.lifecycle.ViewModel

class SelectCountryCodeViewModel : ViewModel() {

    var countryPopUpListener : CountryPopUpListener? = null

    fun onCountryPopUpCancelled(view: View){

        countryPopUpListener?.onCountryPopUpCancel()
    }

}

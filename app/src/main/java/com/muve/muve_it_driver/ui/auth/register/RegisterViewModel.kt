package com.muve.muve_it_driver.ui.auth.register

import android.view.View
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel(){

    var regListener : RegistrationListener? = null
    var birthday : String? = null


    fun clickRegistrationButton(view : View){

        regListener?.onSuccess()
    }

    fun clickBackButton(view: View){

        regListener?.onBackButtonPressed()
    }

    fun clickCountryCodebutton(view: View){

        regListener?.onCountryOpen()

    }

    fun clickAboutUsButton(view: View){

        regListener?.clickAboutUsButton()

    }

    fun clickPreferedCityButton(view: View){

        regListener?.clickPreferedCityButton()

    }
    fun clickAlternateCityButton(view: View){

        regListener?.clickAlternateCityButton()

    }



    fun showHidePassword(view: View){


        regListener?.showPassword()

    }

    fun showHidePasswordConfirm(view: View){


        regListener?.showPasswordConfirm()

    }



    /* fun ClickShowGender(view: View){

         regListener?.showGender()
     }*/

/*
    fun clickDatePicker(view: View){

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            regListener?.setDate("$dayOfMonth/${monthOfYear+1}/$year")



        }, year, month, day)
        dpd.show()
    }
*/
}
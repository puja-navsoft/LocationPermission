package com.muve.muve_it_driver.ui.auth.create_password

import android.view.View
import androidx.lifecycle.ViewModel


class CreatePasswordViewModel : ViewModel() {

    var myCreatePasswordListener : CreatePasswordListener? = null

    var isPasswordHide : Boolean = true

    var password : String = ""
    var confirmPassword : String = ""

    fun clickCreatePasswordButton(view: View){

      /*  if(isValidPassword(password)){

            if(password != confirmPassword){

                view.context.toast("Password mismatches")
            }else{

                *//*Intent(view.context,HomeActivity::class.java).also {

                    view.context.startActivity(it)
                }*//*
            }
        }else{

            view.context.toast("Password must be 6 or more characters with a mix of letters, numbers and symbols")
        }
*/

    }

    fun showHidePassword(view: View){


       myCreatePasswordListener?.showPassword()

    }

    fun showHidePasswordConfirm(view: View){


        myCreatePasswordListener?.showPasswordConfirm()

    }




}
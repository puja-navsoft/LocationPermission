package com.muve.muve_it_driver.ui.auth.welcome

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.util.isLogInButtonClicked

class WelcomeViewModel : ViewModel() {

    fun clickLogInButton(view : View){

        isLogInButtonClicked = true

        Intent(view.context, LogInActivity/*DriverInformationScreen*/::class.java).also {

            view.context.startActivity(it)
        }
    }

    fun clickRegisterButton(view: View){

        isLogInButtonClicked = false

        Intent(view.context, RegisterActivity::class.java).also {

            view.context.startActivity(it)
        }


    }


}
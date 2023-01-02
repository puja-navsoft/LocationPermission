package com.muve.muve_it_driver.ui.auth.create_password

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.application.Muve
import com.muve.muve_it_driver.databinding.ActivityCreatePasswordBinding
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.clickOnPinViewCreate
import kotlinx.android.synthetic.main.activity_create_password.*
import kotlinx.android.synthetic.main.activity_create_password.img_back_arrow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class CreatePasswordActivity : AppCompatActivity(), CreatePasswordListener {


    var isPasswordHide : Boolean = true
    var isPasswordHideconfirm : Boolean = true
    var response1: Call<VerifyOtpPojo>? = null
    var sharedPreferences: SharedPreferences? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val cBinding: ActivityCreatePasswordBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_password)
        val viewModel = ViewModelProviders.of(this).get(CreatePasswordViewModel::class.java)
        cBinding.createpassword = viewModel
        viewModel.myCreatePasswordListener = this
        sharedPreferenceManager = SharedPreferenceManager(this)

        if (intent.getBooleanExtra("changepassword",false)==true){
            cBinding.txtLogin.setText("Change Password")

        }else{
            cBinding.txtLogin.setText("Create Password")

        }

        img_back_arrow.setOnClickListener {


            finish()

        }

        cBinding.cMain.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)
        }

        cBinding.btnLogIn.setOnClickListener {


            if(cBinding.edtPassword1.text.toString().equals("")|| cBinding.edtPassword2.text.toString().equals("")||
                cBinding.edtPassword3.text.toString().equals("") || cBinding.edtPassword4.text.toString().equals("")
            /* || cBinding.edtPassword.text.toString().equals("") || cBinding.edtConfirmPassword.text.toString().equals("")*/){

                Toast.makeText(this, "Please insert valid OTP", Toast.LENGTH_LONG).show()


            }
            else if (cBinding.edtPassword.text.toString() .equals("")){

                Toast.makeText(this, "Please insert password", Toast.LENGTH_LONG).show()

            }
            else if (cBinding.edtPassword.text.toString().trim()!!.length < 6){

                Toast.makeText(this, "Please give your password minimum 6 input", Toast.LENGTH_LONG).show()

            }

            /* else if (cBinding.edtPassword.text.toString().trim()!!.length >20 ){

                 Toast.makeText(this, "Please give your password maximum 20 input", Toast.LENGTH_LONG).show()

             }*/


            else if (cBinding.edtPassword.text.toString().contains(" ") ){

                Toast.makeText(this, "Please remove space from your password", Toast.LENGTH_LONG).show()

            }

            else if (!cBinding.edtPassword.text.toString().trim().matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"))){

                Toast.makeText(this, "Password should contain digit and charecter both", Toast.LENGTH_LONG).show()

            }


            else if (cBinding.edtConfirmPassword.text.toString().trim()!!.equals("")){

                Toast.makeText(this, "Please insert confirm password", Toast.LENGTH_LONG).show()


            }

            else if ( cBinding.edtConfirmPassword.text.toString().contains(" ")){

                Toast.makeText(this, "Please remove space from your confirm password", Toast.LENGTH_LONG).show()

            }

            /*  else if (!cBinding.edtConfirmPassword.text.toString().trim().matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"))){

                  Toast.makeText(this, "Confirm Password should contain digit and charecter both", Toast.LENGTH_LONG).show()

              }*/

            else if (cBinding.edtPassword.text.toString() != cBinding.edtConfirmPassword.text.toString()){



                Toast.makeText(this, "Password and confirm password  doesn't match", Toast.LENGTH_LONG).show()

            }


            /* else if (cBinding.edtPassword.text.toString().length >=6 ){

                 Toast.makeText(this, "Password minimum 6 charecter", Toast.LENGTH_LONG).show()

             }*/

            /*  else if (!cBinding.edtPassword.text.toString().matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"))){

                  Toast.makeText(this, "Password should contain digit and charecter both", Toast.LENGTH_LONG).show()

              }*/
            else {

                doCreateNewPassword(cBinding)
            }

        }

        clickOnPinViewCreate(cBinding!!)



    }


    private fun doCreateNewPassword(cBinding: ActivityCreatePasswordBinding) {

        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)


            req ["phone"] = sharedPreferences!!.getString("phonenumberSharedPref","")!!.trim()!!
            req["country_code"] = sharedPreferences!!.getString("phonecodeSharedPref","")!!.trim()
            req["otp"] = cBinding.edtPassword1.text.toString()+cBinding.edtPassword2.text.toString()+cBinding.edtPassword3.text.toString()+cBinding.edtPassword4.text.toString()
            req["password"] = cBinding.edtPassword.text.toString().trim()

            response1 = apiservice.doingPasswordChange(req)
            // Call<RejectorderModel> response1 = apiservice.rejectitemorder(kitchecn_order_id,item_id,reason_id_);
            response1!!.enqueue(object : Callback<VerifyOtpPojo?> {
                override fun onResponse(
                    call: Call<VerifyOtpPojo?>,
                    response: Response<VerifyOtpPojo?>
                ) {

                    // AppProgressBar.closeLoader();
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {


                            Toast.makeText(
                                this@CreatePasswordActivity,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                            finish()

                            val myIntent = Intent(this@CreatePasswordActivity, LogInActivity::class.java)
                            startActivity(myIntent)



                        }

                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.message!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true)
                                ) {

                                    val sharedPreferences =
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            this@CreatePasswordActivity,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                }
                                else {


                                    Toast.makeText(
                                        this@CreatePasswordActivity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }

                    } else {
                        try {

                            AppProgressBar.closeLoader();
                            if (response.body() != null) {

                                Toast.makeText(
                                    this@CreatePasswordActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(Muve.getInstance(), e.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CreatePasswordActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CreatePasswordActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showPassword() {


        if(isPasswordHide){

            isPasswordHide = false
            edt_password?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            // edt_confirm_password?.transformationMethod = HideReturnsTransformationMethod.getInstance()

            tv_showclick1.text = getString(R.string.tv_hide_password)


        }else{

            isPasswordHide = true
            edt_password?.transformationMethod = PasswordTransformationMethod.getInstance()
            // edt_confirm_password?.transformationMethod =  PasswordTransformationMethod.getInstance()

            tv_showclick1.text = getString(R.string.tv_show_password)


        }
    }
    override fun showPasswordConfirm() {


        if(isPasswordHideconfirm){

            isPasswordHideconfirm = false
            edt_confirm_password?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            // edt_confirm_password?.transformationMethod = HideReturnsTransformationMethod.getInstance()

            txt_show_password1.text = getString(R.string.tv_hide_password)

        }else{

            isPasswordHideconfirm = true
            edt_confirm_password?.transformationMethod = PasswordTransformationMethod.getInstance()
            // edt_confirm_password?.transformationMethod =  PasswordTransformationMethod.getInstance()

            txt_show_password1.text = getString(R.string.tv_show_password)


        }
    }


}

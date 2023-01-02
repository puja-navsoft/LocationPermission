package com.muve.muve_it_driver.ui.auth.forgetpassword

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.ActivityForgetPasswordFirstStepBinding
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment
import com.muve.muve_it_driver.ui.auth.create_password.CreatePasswordActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ForgetPasswordFirstStep : AppCompatActivity(),ForgetListener {

    var bottomSheetFragment: SelectCountryCodeFragment? = null
    lateinit var binding: ActivityForgetPasswordFirstStepBinding
    var response1: Call<VerifyOtpPojo>? = null
    var myEdit: SharedPreferences.Editor?=null
    var sharedPreferences: SharedPreferences?=null
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=
            DataBindingUtil.setContentView(this, R.layout.activity_forget_password_first_step)
        val viewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        binding.forgetpassword = viewModel
        viewModel.myAuthListener = this

        bottomSheetFragment = SelectCountryCodeFragment()
        sharedPreferenceManager = SharedPreferenceManager(this)

       /* if (intent.getBooleanExtra("clickfromeditprofile",false)==true){
            binding.txtLogin.setText("Change Password")

        }else{
            binding.txtLogin.setText("Forgot Password")
        }*/

    }

    override fun onStarting() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        if (binding.edtPhoneNumber.text.toString().equals("")){

            Toast.makeText(this, "Enter Phone number to continue", Toast.LENGTH_LONG).show()
        }
        else if ( binding.edtPhoneNumber.text.toString().trim().length <9){

            Toast.makeText(this, "Phone number should be minimum 9 digit", Toast.LENGTH_LONG).show()

        }

        else if (binding.edtPhoneNumber.text.toString().length > 15){

            Toast.makeText(this, "Phone number should be maximum 15 digit", Toast.LENGTH_LONG).show()

        }

        else {

            doingForgetPasswordPhoneForOTP()

        }
    }

    private fun doingForgetPasswordPhoneForOTP() {

        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req ["phone"] = binding.edtPhoneNumber.text.toString()
            req["country_code"] = binding.txtCountryCode.text.toString().replace("\"", "")


            response1 = apiservice.doingforgot_password_send_otp(req)
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
                                this@ForgetPasswordFirstStep,
                                "OTP sent successfully",
                                Toast.LENGTH_LONG
                            ).show()


                            val myIntent = Intent(this@ForgetPasswordFirstStep, CreatePasswordActivity::class.java)
                            startActivity(myIntent)


                            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            myEdit = sharedPreferences!!.edit()

                            sharedPreferences!!.getString("phonenumberSharedPref","")!!
                            sharedPreferences!!.getString("phonecodeSharedPref","")!!

                            myEdit!!.clear().apply()

                            myEdit!!.putString("phonenumberSharedPref", binding.edtPhoneNumber.text.toString())
                            myEdit!!.putString("phonecodeSharedPref", binding.txtCountryCode.text.toString())

                            myEdit!!.apply()

                        }else{
                            AppProgressBar.closeLoader();

                            Toast.makeText(
                                this@ForgetPasswordFirstStep,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
                                        this@ForgetPasswordFirstStep,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()

                            }
                            else {


                                Toast.makeText(
                                    this@ForgetPasswordFirstStep,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()


                            }

                        }
                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@ForgetPasswordFirstStep,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@ForgetPasswordFirstStep,
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

    override fun onFailure(message: String) {

    }

    override fun onBackButtonPressed() {
        finish()

    }

    override fun onCountryOpen() {

        showBottomSheetDialogFragment()


    }

    private fun showBottomSheetDialogFragment() {

        bottomSheetFragment!!.isCancelable=false
        val bundle = Bundle()
        bundle.putString("test", "ForgetPassword")
        bottomSheetFragment!!.arguments = bundle
        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)

    }

    override fun showHidePassword() {
        TODO("Not yet implemented")
    }

    fun cancelCountryPopup(countryCode: Int) {

        txt_country_code.text = "+"+countryCode.toString()
        bottomSheetFragment?.dismiss()
    }
}
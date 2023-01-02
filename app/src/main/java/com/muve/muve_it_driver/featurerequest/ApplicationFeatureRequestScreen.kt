package com.muve.muve_it_driver.featurerequest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.NetworkUtility

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ApplicationFeatureRequestScreen : AppCompatActivity() {

    private var tv_submit: TextView? = null
    private var et_subject: EditText? = null
    private var et_description: EditText? = null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    private var mainC: ConstraintLayout? = null
    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
    var response1: Call<VerifyOtpPojo>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_feature_request_screen)


        val iv_back = findViewById<ImageView>(R.id.iv_back)

        tv_submit = findViewById<TextView>(R.id.tv_submit)
        et_subject = findViewById<EditText>(R.id.et_subject)
        et_description = findViewById<EditText>(R.id.et_description)

        mainC = findViewById<ConstraintLayout>(R.id.mainC)

        et_subject!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        et_description!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)


        mainC!!.setOnClickListener {
            AppUtilities.hideSoftKeyboard(this)

        }
        iv_back!!.setOnClickListener {

            finish()
        }

        try {
            sharedPreferenceManager = SharedPreferenceManager(this)

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            /*  fnameSharedPrefAfterLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","").toString()
              lnameSharedPrefAfterLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","").toString()
              fnameSharedPrefAfterReg = sharedPreferences!!.getString("fnameSharedPref","").toString()
              lnameSharedPrefAfterReg = sharedPreferences!!.getString("lnameSharedPref","").toString()*/
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()

        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }

        tv_submit!!.setOnClickListener {

            if (et_subject!!.text.toString().equals("")){

                et_subject!!.setError("Enter Subject of your report")
                et_subject!!.requestFocus()
            }else if (et_description!!.text.toString().equals("")){
                et_description!!.setError("Enter description !")
                et_description!!.requestFocus()
            }else{

                if (NetworkUtility.isNetworkAvailable(this)) {
                    reportAPICall()
                }
                else{
                    Toast.makeText(
                        this,
                        getString(R.string.msg_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun reportAPICall() {



        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["title"] =   "Application feature request"
                req["subject"] = et_subject!!.text.toString().trim()
                req["description"] = et_description!!.text.toString().trim()

                response1 = apiservice.doingHelpFunctionality(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["title"] =   "Application feature request"
                req["subject"] = et_subject!!.text.toString().trim()
                req["description"] = et_description!!.text.toString().trim()
                response1 = apiservice.doingHelpFunctionality(token,req)

            }
            response1!!.enqueue(object : Callback<VerifyOtpPojo?> {
                override fun onResponse(
                    call: Call<VerifyOtpPojo?>,
                    response: Response<VerifyOtpPojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status==true) {


                            Toast.makeText(
                                this@ApplicationFeatureRequestScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                            finish()

                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.message.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true
                                    )
                                ) {

                                    val sharedPreferences =
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            this@ApplicationFeatureRequestScreen,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                } else {


                                    Toast.makeText(
                                        this@ApplicationFeatureRequestScreen,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()


                                }

                            }
                        }


                    } else {

                        AppProgressBar.closeLoader()

                        if (response.body() != null) {
                            Toast.makeText(
                                this@ApplicationFeatureRequestScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@ApplicationFeatureRequestScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@ApplicationFeatureRequestScreen,
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


}
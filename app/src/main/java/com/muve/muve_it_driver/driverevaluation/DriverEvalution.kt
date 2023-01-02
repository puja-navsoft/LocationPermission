package com.muve.muve_it_driver.driverevaluation

import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.muve.muve_it_driver.Driverstatusstoreresponse
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility.Companion.DRIVER_TEST_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DriverEvalution : AppCompatActivity() {

    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var fnameSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterLog: String = ""
    var emailSharedPrefAfterLog: String = ""
    var emailSharedPrefAfterReg: String = ""
    var phoneSharedPrefAfterLog: String = ""
    var phoneSharedPrefAfterReg: String = ""
    var codeSharedPrefAfterReg: String = ""
    var codeSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var fnameSharedPrefAfterReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var lickclick: Boolean = false
    var response1: Call<ResendOtpPojo>? = null
    var receiver: BroadcastReceiver? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_evalution)

      //  var tv_linkwatchvdo = findViewById<TextView>(R.id.tv_linkwatchvdo)
        var tv_linktest = findViewById<TextView>(R.id.tv_linktest)
      //  var iv_back = findViewById<ImageView>(R.id.iv_back)
        var okassisment = findViewById<TextView>(R.id.okassisment)

        try {
            sharedPreferenceManager = SharedPreferenceManager(this)

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            fnameSharedPrefAfterLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","").toString()
            lnameSharedPrefAfterLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","").toString()
            fnameSharedPrefAfterReg = sharedPreferences!!.getString("fnameSharedPref","").toString()
            lnameSharedPrefAfterReg = sharedPreferences!!.getString("lnameSharedPref","").toString()
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()
            emailSharedPrefAfterLog = sharedPreferences!!.getString("emailSharedPrefAfterLog","").toString()
            emailSharedPrefAfterReg = sharedPreferences!!.getString("emailSharedPref","").toString()
            phoneSharedPrefAfterLog = sharedPreferences!!.getString("phoneSharedPrefAfterLog","").toString()
            codeSharedPrefAfterLog = sharedPreferences!!.getString("codeSharedPrefAfterLog","").toString()
            phoneSharedPrefAfterReg = sharedPreferences!!.getString("phoneSharedPref","").toString()
            codeSharedPrefAfterReg = sharedPreferences!!.getString("codeSharedPref","").toString()


        }
        catch (e:java.lang.Exception){

            e.printStackTrace()
        }



       /* iv_back.setOnClickListener {

            finish()
        }*/



       /* tv_linkwatchvdo.setOnClickListener {

            //  finish()
            lickclick = true


        }*/

        tv_linktest.setOnClickListener {


            lickclick = true

            val uri: Uri = Uri.parse(DRIVER_TEST_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

            okassisment!!.setBackground(resources.getDrawable(R.drawable.round_background_2))

            okassisment.isEnabled=true

        }


        okassisment.setOnClickListener {

            getApiCallforokassisment()

        }

        if (lickclick==true){

            okassisment!!.setBackground(resources.getDrawable(R.drawable.round_background_2))

            okassisment.isEnabled=true

        }else{

            okassisment!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))

            okassisment.isEnabled=false
        }


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                try {

                    if (intent.getIntExtra(
                            "status_codeFromPushMultipleDeviceLogin",
                            0
                        ) == 204
                    ) {

                        Toast.makeText(this@DriverEvalution, "There is currently another device logged into your account.", Toast.LENGTH_SHORT).show()

                        getLoggedOut()


                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    fun getLoggedOut() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = java.util.HashMap<String, Any>()


            if (defaultIdLog.equals("")) {

                req["id"] = defaultIdReg
                req["driver_status"] = "Logged out"

                responseDriverStatusSend = apiservice.doingdriver_status_send(tokenReg, req)
            } else {

                req["id"] = defaultIdLog
                req["driver_status"] = "Logged out"
                responseDriverStatusSend = apiservice.doingdriver_status_send(token, req)

            }

            responseDriverStatusSend!!.enqueue(object : Callback<Driverstatusstoreresponse?> {
                override fun onResponse(
                    call: Call<Driverstatusstoreresponse?>,
                    response1: Response<Driverstatusstoreresponse?>
                ) {
                    if (response1.isSuccessful()) {

                        AppProgressBar.closeLoader()

                        val sharedPreferences = getSharedPreferences(
                            "MySharedPref",
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences.edit()
                        sharedPreferenceManager!!.logoutUser()
                        editor.clear()
                        editor.commit()


                        val myIntent = Intent(this@DriverEvalution, WelcomeActivity::class.java)
                        startActivity(myIntent)
                        finish()


                    } else {

                        AppProgressBar.closeLoader()

                        if (response1.body() != null) {

                            if (response1.body()!!.message.equals(
                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    ignoreCase = true
                                )
                            ) {
                                val sharedPreferences =
                                    getSharedPreferences(
                                        "MySharedPref",
                                        Context.MODE_PRIVATE
                                    )
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(
                                        this@DriverEvalution,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()


                            } else {

                                Toast.makeText(this@DriverEvalution, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()



                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if (t is NoConnectivityException) {
                        Toast.makeText(this@DriverEvalution, "" + t.message, Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@DriverEvalution, "" + t.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getApiCallforokassisment() {

        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")) {

                req["driver_id"] = defaultIdReg

                response1 = apiservice.doingattempt_status(tokenReg, req)
            } else {

                req["driver_id"] = defaultIdLog

                response1 = apiservice.doingattempt_status(token, req)

            }

            response1!!.enqueue(object : Callback<ResendOtpPojo?> {
                override fun onResponse(
                    call: Call<ResendOtpPojo?>,
                    response1: Response<ResendOtpPojo?>
                ) {
                    if (response1.isSuccessful()) {

                        AppProgressBar.closeLoader()

                        if (response1.body()!!.status == true) {


                            Toast.makeText(
                                this@DriverEvalution,
                                response1.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()


                            val dialog = Dialog(this@DriverEvalution)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.custom_layout_for_pending)
                            val btn_close = dialog.findViewById(R.id.btn_close) as Button
                            val tv_headettxt = dialog.findViewById(R.id.tv_headettxt) as TextView
                            val tv_entiremsg = dialog.findViewById(R.id.tv_entiremsg) as TextView

                            tv_headettxt.setText(getString(R.string.assessment_notification))
                            tv_entiremsg.setText(getString(R.string.complete_assessment_notification))

                            btn_close.setOnClickListener {
                                dialog.dismiss()
                                finishAffinity()
                              //  System.exit(0)
                            }
                            dialog.show()


                        }
                        else {

                            if (response1.body() != null) {

                                if (response1.body()!!.detail!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    )
                                ) {

                                    Toast.makeText(
                                        this@DriverEvalution,
                                        response1.body()!!.detail.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    getLoggedOut()

                                }
                                else {


                                    Toast.makeText(
                                        this@DriverEvalution,
                                        response1.body()!!.detail.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }


                        }


                    } else {

                        Toast.makeText(
                            this@DriverEvalution,
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<ResendOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DriverEvalution,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DriverEvalution,
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

    override fun onResume() {
        super.onResume()


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                try {

                    if (intent.getIntExtra(
                            "status_codeFromPushMultipleDeviceLogin",
                            0
                        ) == 204
                    ) {

                        Toast.makeText(this@DriverEvalution, "Your Account is Logged In from another Device", Toast.LENGTH_SHORT).show()

                        getLoggedOut()


                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver!!),
            IntentFilter("com.muve.muve_it_driver")
        )
    }

}
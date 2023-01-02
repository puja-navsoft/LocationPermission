package com.muve.muve_it_driver

import android.app.Activity
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.drivercapability.DriverCapability
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.minimumreq.MinimumReqScreen
import com.muve.muve_it_driver.model.forceupdate.ForceupdatePojo
import com.muve.muve_it_driver.model.servicecallaccept.ServicecallResponse
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.vehicleequipment.VehicleScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 4000

    var token: String = ""
    var btn_updateApp: Button ? = null
    var popupforce: ConstraintLayout ? = null


    var response1: Call<UserDetailPojo>? = null
    var responseforgetservicecallapi: Call<ServicecallResponse>? = null
    var response2: Call<ForceupdatePojo>? = null
    var response4: Call<ServiceCallStatuswiseetailsResponse>? = null
    var responseServiceCallListDetail: Call<ServiceCallStatuswiseetailsResponse>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences1 = this.getSharedPreferences("MySharedPref1", 0)
        var sharedPreferenceManager: SharedPreferenceManager? = null
        var sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        sharedPreferenceManager = SharedPreferenceManager(this)
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);


        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE)
        lock.disableKeyguard()

        val myEdit = sharedPreferences.edit()

        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        setContentView(R.layout.activity_main)

        try {

            if (NetworkUtility.isNetworkAvailable(this@SplashScreen)) {
                AppProgressBar.openLoader(
                    this,
                    this.getResources().getString(R.string.pleasewait)
                )

                val apiservice: ApiInterface =
                    ApiClient.getClient(this).create(ApiInterface::class.java)
                val req = HashMap<String, Any>()
                val pInfo: PackageInfo =
                    getPackageManager().getPackageInfo(getPackageName(), 0)
                val versionName: String = pInfo.versionName
                req["device_type"] = "android"
                req["device_version"] = versionName
                response2 = apiservice.getforceupdate(req)


                response2!!.enqueue(object : Callback<ForceupdatePojo?> {
                    override fun onResponse(
                        call: Call<ForceupdatePojo?>,
                        response: Response<ForceupdatePojo?>
                    ) {
                        if (response.body() == null) {
                            AppProgressBar.closeLoader()
                            return
                        } else if (response.isSuccessful()) {
                            AppProgressBar.closeLoader()

                            if (response.body()!!.status == true) {

                                Handler().postDelayed({

                                    Log.v(
                                        "isVehicleScreenVal",
                                        "isVehicleScreenVal" + sharedPreferences1!!.getBoolean(
                                            "isVehicleScreenVal",
                                            false
                                        )
                                    )


                                    if (sharedPreferences1!!.getBoolean(
                                            "isVehicleScreenVal",
                                            false
                                        ) == false
                                    ) {

                                        startActivity(
                                            Intent(
                                                this@SplashScreen,
                                                VehicleScreen::class.java
                                            )
                                        )
                                        finish()

                                    } else if (sharedPreferences1!!.getBoolean(
                                            "isMinimumReqVal",
                                            false
                                        ) == false
                                    ) {

                                        startActivity(
                                            Intent(
                                                this@SplashScreen,
                                                MinimumReqScreen::class.java
                                            )
                                        )
                                        finish()
                                    } else if (sharedPreferences1!!.getBoolean(
                                            "isDriverCapabilityVal",
                                            false
                                        ) == false
                                    ) {
                                        startActivity(
                                            Intent(
                                                this@SplashScreen,
                                                DriverCapability::class.java
                                            )
                                        )
                                        // close this activity
                                        finish()
                                    } else {

                                        if (sharedPreferenceManager!!.loginUserData != null) {

                                            if (sharedPreferenceManager!!.loginUserData.isRemember != null && sharedPreferenceManager!!.loginUserData.isRemember == true) {

                                                sharedPreferences = getSharedPreferences(
                                                    "MySharedPref",
                                                    MODE_PRIVATE
                                                )

                                                try {
                                                    if (sharedPreferences.getString(
                                                            "account_Status",
                                                            ""
                                                        )
                                                            .equals("Active", ignoreCase = true)
                                                    ) {

                                                        if (sharedPreferences.getString(
                                                                "idSharedPrefAfterLog",
                                                                ""
                                                            ) != null
                                                        ) {

                                                            if (NetworkUtility.isNetworkAvailable(
                                                                    this@SplashScreen
                                                                )
                                                            ) {
                                                                fetchFevPlac(
                                                                    sharedPreferences,
                                                                    sharedPreferences1
                                                                )
                                                            } else {
                                                                Toast.makeText(
                                                                    this@SplashScreen,
                                                                    getString(R.string.msg_no_internet),
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            }

                                                        }

                                                    } else {

                                                        startActivity(
                                                            Intent(
                                                                this@SplashScreen,
                                                                WelcomeActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    }
                                                } catch (e: Exception) {
                                                    e.printStackTrace()
                                                }


                                            } else {

                                                startActivity(
                                                    Intent(
                                                        this@SplashScreen,
                                                        WelcomeActivity::class.java
                                                    )
                                                )

                                                finish()
                                            }


                                        } else {

                                            startActivity(
                                                Intent(
                                                    this@SplashScreen,
                                                    WelcomeActivity::class.java
                                                )
                                            )

                                            finish()

                                        }

                                    }


                                }, SPLASH_TIME_OUT)


                            } else {

                                AppProgressBar.closeLoader()

                                val dialog = Dialog(this@SplashScreen)

                                dialog.setContentView(R.layout.forcelayout)
                                dialog.window!!.setLayout(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                dialog.setCancelable(false)

                                val btn_updateApp =
                                    dialog.findViewById(R.id.btn_updateApp) as Button

                                dialog.show()

                                btn_updateApp!!.setOnClickListener {

                                    try {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=$packageName")
                                            )
                                        )

                                    } catch (e: Exception) {
                                        startActivity(
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                            )
                                        )
                                    }

                                    startActivity(intent)

                                }
                            }
                        } else {
                            AppProgressBar.closeLoader()

                        }
                    }

                    override fun onFailure(call: Call<ForceupdatePojo?>, t: Throwable) {
                        AppProgressBar.closeLoader()
                        if (t is NoConnectivityException) {
                            Toast.makeText(
                                this@SplashScreen,
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            Toast.makeText(
                                this@SplashScreen,
                                "" + t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
            else{
                Toast.makeText(
                    this,
                    getString(R.string.msg_no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }




    }


    fun fetchFevPlac(sharedPreferences: SharedPreferences, sharedPreferences1: SharedPreferences) {

        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            if (sharedPreferences.getString("idSharedPrefAfterLog", "").equals("")) {

                req["driver_id"] = sharedPreferences.getString("idSharedPref", "")!!
                response1 = apiservice.getuserdetails(
                    sharedPreferences.getString("AuthSharedPref", ""),
                    req
                )

            } else {
                req["driver_id"] = sharedPreferences.getString("idSharedPrefAfterLog", "")!!
                response1 =
                    apiservice.getuserdetails(sharedPreferences.getString("Authorization", ""), req)

            }
            response1!!.enqueue(object : Callback<UserDetailPojo?> {
                override fun onResponse(
                    call: Call<UserDetailPojo?>,
                    response: Response<UserDetailPojo?>
                ) {
                    if (response.body() == null) {
                        AppProgressBar.closeLoader()
                        return
                    } else if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {


                            if (!response.body()!!.detail!!.service_id.equals("") || response.body()!!.detail!!.service_id.equals(
                                    "0"
                                )
                            ) {


                              //  getservicecallAPI(sharedPreferences,response.body()!!.detail!!.service_id)



                                getDriverDetails(
                                    sharedPreferences,
                                    response.body()!!.detail!!.service_id!!
                                )





                            } else if (response.body()!!.detail!!.account_status.equals("Active") && response.body()!!.detail!!.document_verify_status == true && response.body()!!.detail!!.is_training.equals(
                                    "Yes"
                                ) &&
                                response.body()!!.detail!!.attempt_status == true && sharedPreferences1!!.getBoolean(
                                    "isshowpopup",
                                    false
                                ) && sharedPreferences1!!.getBoolean(
                                    "locationallow",
                                    false
                                ) && sharedPreferences1!!.getBoolean(
                                    "cameraallow",
                                    false
                                ) && sharedPreferences1!!.getBoolean(
                                    "storageallow",
                                    false
                                ) && sharedPreferences1!!.getBoolean("phoneallow", false)
                            ) {

                                val myIntent_ = Intent(this@SplashScreen, HomeActivity::class.java)
                                startActivity(myIntent_)
                                finish()
                            } else {

                                val myIntent_ = Intent(this@SplashScreen, WelcomeActivity::class.java)
                                startActivity(myIntent_)
                                finish()

                            }


                        } else {

                            AppProgressBar.closeLoader()


                        }
                    } else {
                        AppProgressBar.closeLoader()

                    }
                }

                override fun onFailure(call: Call<UserDetailPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Toast.makeText(
                            this@SplashScreen,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Toast.makeText(
                            this@SplashScreen,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun getservicecallAPI(sharedPreferences: SharedPreferences,serviceId: String?) {


        try {

            val apiservice: ApiInterface =
                ApiClient.getClient(this@SplashScreen).create(ApiInterface::class.java)


            val req = HashMap<String, Any>()


            if (sharedPreferences.getString("idSharedPrefAfterLog", "").equals("")) {

                req["driver_id"] = sharedPreferences.getString("idSharedPref", "")!!
                responseforgetservicecallapi = apiservice.doingservicecall_accept(
                    sharedPreferences.getString("AuthSharedPref", ""),
                    req
                )

            } else {
                req["driver_id"] = sharedPreferences.getString("idSharedPrefAfterLog", "")!!
                responseforgetservicecallapi =
                    apiservice.doingservicecall_accept(sharedPreferences.getString("Authorization", ""), req)

            }

            responseforgetservicecallapi!!.enqueue(object : Callback<ServicecallResponse?> {
                override fun onResponse(
                    call: Call<ServicecallResponse?>,
                    response1: Response<ServicecallResponse?>
                ) {
                    if (response1.isSuccessful()) {

                        if (response1.body()!!.status == true) {


                            // service_id = response1.body()!!.detail!!.serviceId!!.toString().trim()

                            /* service_type =*/
                            response1.body()!!.detail!!.service_type!!.toString().trim()


                            getDriverDetails(
                                sharedPreferences,
                                serviceId!!
                            )

                        } else {

                            AppProgressBar.closeLoader()

/*                                                        if (response1.body() != null) {

                                                            if (response1.body()!!.detail!!.equals(
                                                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice)
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
                                                                        this@SplashScreen,
                                                                        WelcomeActivity::class.java
                                                                    )
                                                                startActivity(myIntent)

                                                                finish()

                                                            } else {


                                                                Toast.makeText(this@SplashScreen, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()


                                                            }

                                                        }*/
                        }


                    } else {

                        AppProgressBar.closeLoader()

                        if (response1.body() != null) {

                            Toast.makeText(this@SplashScreen, response1.body()!!.message.toString(), Toast.LENGTH_LONG).show()


                        }
                    }
                }

                override fun onFailure(call: Call<ServicecallResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if (t is NoConnectivityException) {

                        Toast.makeText(this@SplashScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@SplashScreen, "" + t.message, Toast.LENGTH_SHORT ).show()
                    }
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



    fun getDriverDetails(sharedPreferences: SharedPreferences, serviceId: String /*, service_type: String*/) {


        try {

            val apiservice1: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            req["service_id"] = serviceId

            if (sharedPreferences.getString("idSharedPrefAfterLog", "").equals("")) {

                req["driver_id"] = sharedPreferences.getString("idSharedPref", "")!!
                response4 = apiservice1.doingservicecall_detailsinformation(
                    sharedPreferences.getString(
                        "AuthSharedPref",
                        ""
                    ), req
                )

            } else {

                req["driver_id"] = sharedPreferences.getString("idSharedPrefAfterLog", "")!!
                response4 = apiservice1.doingservicecall_detailsinformation(
                    sharedPreferences.getString(
                        "Authorization",
                        ""
                    ), req
                )
            }

            response4!!.enqueue(object : Callback<ServiceCallStatuswiseetailsResponse?> {
                override fun onResponse(
                    call: Call<ServiceCallStatuswiseetailsResponse?>,
                    response: Response<ServiceCallStatuswiseetailsResponse?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            //  isAccepted = true

                            if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "Accepted",
                                    ignoreCase = true
                                )
                            ) {

                                val myIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                                myIntent.putExtra(
                                    "status",
                                    response.body()!!.detail!!.status.toString()
                                )
                                myIntent.putExtra(
                                    "customerid",
                                    response.body()!!.detail!!.customerId.toString()
                                )
                                myIntent.putExtra(
                                    "driverid",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "vehicleType",
                                    response.body()!!.detail!!.vehicleType.toString()
                                )
                                myIntent.putExtra(
                                    "pick_lat",
                                    response.body()!!.detail!!.pickLat.toString()
                                )
                                myIntent.putExtra(
                                    "pick_long",
                                    response.body()!!.detail!!.pickLong.toString()
                                )
                                myIntent.putExtra(
                                    "driver_id",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id.toString()
                                )
                                myIntent.putExtra(
                                    "pickPlace",
                                    response.body()!!.detail!!.pickPlace.toString()
                                )
                                myIntent.putExtra(
                                    "drop_lat",
                                    response.body()!!.detail!!.dropLat.toString()
                                )
                                myIntent.putExtra(
                                    "drop_long",
                                    response.body()!!.detail!!.dropLong.toString()
                                )
                                myIntent.putExtra(
                                    "dropPlace",
                                    response.body()!!.detail!!.dropPlace.toString()
                                )
                                myIntent.putExtra(
                                    "service_id",
                                    response.body()!!.detail!!.serviceId.toString()
                                )
                                myIntent.putExtra(
                                    "arrived_time",
                                    response.body()!!.detail!!.arrivedTime
                                )
                                myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id
                                )
                                myIntent.putExtra(
                                    "loadUnload",
                                    response.body()!!.detail!!.loadUnload
                                )
                                myIntent.putExtra(
                                    "unattendDrop",
                                    response.body()!!.detail!!.unattendDrop
                                )
                                myIntent.putExtra(
                                    "vehicleImage",
                                    response.body()!!.detail!!.uploadPic.toString()
                                )
                                myIntent.putExtra(
                                    "customer_name",
                                    response.body()!!.detail!!.customerName.toString()
                                )
                                myIntent.putExtra(
                                    "customer_image",
                                    response.body()!!.detail!!.customer_image.toString()
                                )

                                startActivity(myIntent)

                            }
                            else if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "ArrivedAtPickUp",
                                    ignoreCase = true
                                )
                            ) {


                                val myIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                                myIntent.putExtra(
                                    "driver_id",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id.toString()
                                )
                                myIntent.putExtra(
                                    "status",
                                    response.body()!!.detail!!.status.toString()
                                )
                                myIntent.putExtra(
                                    "customerid",
                                    response.body()!!.detail!!.customerId.toString()
                                )
                                myIntent.putExtra(
                                    "driverid",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "vehicleType",
                                    response.body()!!.detail!!.vehicleType.toString()
                                )
                                myIntent.putExtra(
                                    "pick_lat",
                                    response.body()!!.detail!!.pickLat.toString()
                                )
                                myIntent.putExtra(
                                    "pick_long",
                                    response.body()!!.detail!!.pickLong.toString()
                                )
                                myIntent.putExtra(
                                    "pickPlace",
                                    response.body()!!.detail!!.pickPlace.toString()
                                )
                                myIntent.putExtra(
                                    "drop_lat",
                                    response.body()!!.detail!!.dropLat.toString()
                                )
                                myIntent.putExtra(
                                    "drop_long",
                                    response.body()!!.detail!!.dropLong.toString()
                                )
                                myIntent.putExtra(
                                    "dropPlace",
                                    response.body()!!.detail!!.dropPlace.toString()
                                )
                                myIntent.putExtra(
                                    "service_id",
                                    response.body()!!.detail!!.serviceId.toString()
                                )
                                myIntent.putExtra(
                                    "arrived_time",
                                    response.body()!!.detail!!.arrivedTime
                                )
                                myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id
                                )
                                myIntent.putExtra(
                                    "loadUnload",
                                    response.body()!!.detail!!.loadUnload
                                )
                                myIntent.putExtra(
                                    "unattendDrop",
                                    response.body()!!.detail!!.unattendDrop
                                )
                                myIntent.putExtra(
                                    "vehicleImage",
                                    response.body()!!.detail!!.uploadPic.toString()
                                )
                                myIntent.putExtra(
                                    "customer_name",
                                    response.body()!!.detail!!.customerName.toString()
                                )
                                myIntent.putExtra(
                                    "customer_image",
                                    response.body()!!.detail!!.customer_image.toString()
                                )

                                startActivity(myIntent)

                            }
                            else if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "CustomerCodeEntered",
                                    ignoreCase = true
                                )
                            ) {


                                val myIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                                myIntent.putExtra(
                                    "driver_id",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id.toString()
                                )
                                myIntent.putExtra(
                                    "status",
                                    response.body()!!.detail!!.status.toString()
                                )
                                myIntent.putExtra(
                                    "customerid",
                                    response.body()!!.detail!!.customerId.toString()
                                )
                                myIntent.putExtra(
                                    "driverid",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "vehicleType",
                                    response.body()!!.detail!!.vehicleType.toString()
                                )
                                myIntent.putExtra(
                                    "pick_lat",
                                    response.body()!!.detail!!.pickLat.toString()
                                )
                                myIntent.putExtra(
                                    "pick_long",
                                    response.body()!!.detail!!.pickLong.toString()
                                )
                                myIntent.putExtra(
                                    "pickPlace",
                                    response.body()!!.detail!!.pickPlace.toString()
                                )
                                myIntent.putExtra(
                                    "drop_lat",
                                    response.body()!!.detail!!.dropLat.toString()
                                )
                                myIntent.putExtra(
                                    "drop_long",
                                    response.body()!!.detail!!.dropLong.toString()
                                )
                                myIntent.putExtra(
                                    "dropPlace",
                                    response.body()!!.detail!!.dropPlace.toString()
                                )
                                myIntent.putExtra(
                                    "service_id",
                                    response.body()!!.detail!!.serviceId.toString()
                                )
                                myIntent.putExtra(
                                    "arrived_time",
                                    response.body()!!.detail!!.arrivedTime
                                )
                                myIntent.putExtra(
                                    "verifyCodeTime",
                                    response.body()!!.detail!!.verifyCodeTime
                                )
                                myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id
                                )
                                myIntent.putExtra(
                                    "loadUnload",
                                    response.body()!!.detail!!.loadUnload
                                )
                                myIntent.putExtra(
                                    "unattendDrop",
                                    response.body()!!.detail!!.unattendDrop
                                )
                                myIntent.putExtra(
                                    "vehicleImage",
                                    response.body()!!.detail!!.uploadPic.toString()
                                )
                                myIntent.putExtra(
                                    "customer_name",
                                    response.body()!!.detail!!.customerName.toString()
                                )
                                myIntent.putExtra(
                                    "customer_image",
                                    response.body()!!.detail!!.customer_image.toString()
                                )

                                startActivity(myIntent)

                            }
                            else if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "DrivingToDestination",
                                    ignoreCase = true
                                )
                            ) {

                                val myIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                                myIntent.putExtra(
                                    "driver_id",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id.toString()
                                )
                                myIntent.putExtra(
                                    "status",
                                    response.body()!!.detail!!.status.toString()
                                )
                                myIntent.putExtra(
                                    "customerid",
                                    response.body()!!.detail!!.customerId.toString()
                                )
                                myIntent.putExtra(
                                    "driverid",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "vehicleType",
                                    response.body()!!.detail!!.vehicleType.toString()
                                )
                                myIntent.putExtra(
                                    "pick_lat",
                                    response.body()!!.detail!!.pickLat.toString()
                                )
                                myIntent.putExtra(
                                    "pick_long",
                                    response.body()!!.detail!!.pickLong.toString()
                                )
                                myIntent.putExtra(
                                    "pickPlace",
                                    response.body()!!.detail!!.pickPlace.toString()
                                )
                                myIntent.putExtra(
                                    "drop_lat",
                                    response.body()!!.detail!!.dropLat.toString()
                                )
                                myIntent.putExtra(
                                    "drop_long",
                                    response.body()!!.detail!!.dropLong.toString()
                                )
                                myIntent.putExtra(
                                    "dropPlace",
                                    response.body()!!.detail!!.dropPlace.toString()
                                )
                                myIntent.putExtra(
                                    "service_id",
                                    response.body()!!.detail!!.serviceId.toString()
                                )
                                myIntent.putExtra(
                                    "arrived_time",
                                    response.body()!!.detail!!.arrivedTime
                                )
                                myIntent.putExtra(
                                    "verifyCodeTime",
                                    response.body()!!.detail!!.verifyCodeTime
                                )
                                myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id
                                )
                                myIntent.putExtra(
                                    "loadUnload",
                                    response.body()!!.detail!!.loadUnload
                                )
                                myIntent.putExtra(
                                    "unattendDrop",
                                    response.body()!!.detail!!.unattendDrop
                                )
                                myIntent.putExtra(
                                    "vehicleImage",
                                    response.body()!!.detail!!.uploadPic.toString()
                                )
                                myIntent.putExtra(
                                    "customer_name",
                                    response.body()!!.detail!!.customerName.toString()
                                )
                                myIntent.putExtra(
                                    "customer_image",
                                    response.body()!!.detail!!.customer_image.toString()
                                )

                                startActivity(myIntent)

                            }
                            else if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "ArrivedAtDestination",
                                    ignoreCase = true
                                )
                            ) {

                                val myIntent = Intent(this@SplashScreen, HomeActivity::class.java)
                                myIntent.putExtra(
                                    "status",
                                    response.body()!!.detail!!.status.toString()
                                )
                                myIntent.putExtra(
                                    "customerid",
                                    response.body()!!.detail!!.customerId.toString()
                                )
                                myIntent.putExtra(
                                    "driverid",
                                    response.body()!!.detail!!.driverId.toString()
                                )
                                myIntent.putExtra(
                                    "vehicleType",
                                    response.body()!!.detail!!.vehicleType.toString()
                                )
                                myIntent.putExtra(
                                    "pick_lat",
                                    response.body()!!.detail!!.pickLat.toString()
                                )
                                myIntent.putExtra(
                                    "pick_long",
                                    response.body()!!.detail!!.pickLong.toString()
                                )
                                myIntent.putExtra(
                                    "pickPlace",
                                    response.body()!!.detail!!.pickPlace.toString()
                                )
                                myIntent.putExtra(
                                    "drop_lat",
                                    response.body()!!.detail!!.dropLat.toString()
                                )
                                myIntent.putExtra(
                                    "drop_long",
                                    response.body()!!.detail!!.dropLong.toString()
                                )
                                myIntent.putExtra(
                                    "dropPlace",
                                    response.body()!!.detail!!.dropPlace.toString()
                                )
                                myIntent.putExtra(
                                    "service_id",
                                    response.body()!!.detail!!.serviceId.toString()
                                )
                                myIntent.putExtra(
                                    "arrived_time",
                                    response.body()!!.detail!!.arrivedTime
                                )
                                myIntent.putExtra(
                                    "arrivedDestinationTime",
                                    response.body()!!.detail!!.arrivedDestinationTime
                                )
                                myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                myIntent.putExtra(
                                    "second_driver_id",
                                    response.body()!!.detail!!.second_driver_id
                                )
                                myIntent.putExtra(
                                    "loadUnload",
                                    response.body()!!.detail!!.loadUnload
                                )
                                myIntent.putExtra(
                                    "unattendDrop",
                                    response.body()!!.detail!!.unattendDrop
                                )
                                myIntent.putExtra(
                                    "vehicleImage",
                                    response.body()!!.detail!!.uploadPic.toString()
                                )
                                myIntent.putExtra(
                                    "customer_name",
                                    response.body()!!.detail!!.customerName.toString()
                                )
                                myIntent.putExtra(
                                    "customer_image",
                                    response.body()!!.detail!!.customer_image.toString()
                                )

                                startActivity(myIntent)

                            }
                            else if (response.body()!!.detail!!.serviceStatus!!.equals(
                                    "Paused",
                                    ignoreCase = true
                                )
                            ) {

                                try {

                                    AppProgressBar.openLoader(
                                        this@SplashScreen,
                                        getResources().getString(R.string.pleasewait)
                                    )

                                    val apiservice: ApiInterface =
                                        ApiClient.getClient(this@SplashScreen)
                                            .create(ApiInterface::class.java)
                                    val req = HashMap<String, Any>()
                                    if (sharedPreferences.getString("idSharedPrefAfterLog", "")
                                            .equals("")
                                    ) {
                                        req["service_id"] = response.body()!!.detail!!.serviceId!!
                                        req["driver_id"] =
                                            sharedPreferences.getString("idSharedPref", "")!!

                                        responseServiceCallListDetail =
                                            apiservice.doingservicecall_detailsinformation(
                                                sharedPreferences.getString("AuthSharedPref", ""),
                                                req
                                            )

                                    } else {
                                        req["service_id"] = response.body()!!.detail!!.serviceId!!
                                        req["driver_id"] = sharedPreferences.getString(
                                            "idSharedPrefAfterLog",
                                            ""
                                        )!!

                                        responseServiceCallListDetail =
                                            apiservice.doingservicecall_detailsinformation(
                                                sharedPreferences.getString("Authorization", ""),
                                                req
                                            )


                                    }
                                    responseServiceCallListDetail!!.enqueue(object :
                                        Callback<ServiceCallStatuswiseetailsResponse?> {
                                        override fun onResponse(
                                            call: Call<ServiceCallStatuswiseetailsResponse?>,
                                            response: Response<ServiceCallStatuswiseetailsResponse?>
                                        ) {
                                            if (response.isSuccessful()) {
                                                AppProgressBar.closeLoader()

                                                if (response.body()!!.status == true) {

                                                    val myIntent = Intent(this@SplashScreen, HomeActivity::class.java
                                                    )

                                                    myIntent.putExtra(
                                                        "driver_id",
                                                        response.body()!!.detail!!.driverId.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "second_driver_id",
                                                        response.body()!!.detail!!.second_driver_id.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "status",
                                                        response.body()!!.detail!!.status
                                                    )
                                                    myIntent.putExtra(
                                                        "customerid",
                                                        response.body()!!.detail!!.customerId.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "driverid",
                                                        response.body()!!.detail!!.driverId.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "vehicleType",
                                                        response.body()!!.detail!!.vehicleType.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "pick_lat",
                                                        response.body()!!.detail!!.pickLat.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "pick_long",
                                                        response.body()!!.detail!!.pickLong.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "pickPlace",
                                                        response.body()!!.detail!!.pickPlace.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "drop_lat",
                                                        response.body()!!.detail!!.dropLat.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "drop_long",
                                                        response.body()!!.detail!!.dropLong.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "dropPlace",
                                                        response.body()!!.detail!!.dropPlace.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "service_id",
                                                        response.body()!!.detail!!.serviceId.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "arrived_time",
                                                        response.body()!!.detail!!.arrivedTime
                                                    )
                                                    myIntent.putExtra(
                                                        "pause_time",
                                                        response.body()!!.detail!!.pauseTime
                                                    )
                                                    myIntent.putExtra("service_type", response.body()!!.detail!!.service_type.toString())
                                                    myIntent.putExtra(
                                                        "second_driver_id",
                                                        response.body()!!.detail!!.second_driver_id
                                                    )
                                                    myIntent.putExtra(
                                                        "loadUnload",
                                                        response.body()!!.detail!!.loadUnload
                                                    )
                                                    myIntent.putExtra(
                                                        "unattendDrop",
                                                        response.body()!!.detail!!.unattendDrop
                                                    )
                                                    myIntent.putExtra(
                                                        "vehicleImage",
                                                        response.body()!!.detail!!.uploadPic.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "customer_name",
                                                        response.body()!!.detail!!.customerName.toString()
                                                    )
                                                    myIntent.putExtra(
                                                        "customer_image",
                                                        response.body()!!.detail!!.customer_image.toString()
                                                    )

                                                    startActivity(myIntent)

                                                } else {

                                                    AppProgressBar.closeLoader()

                                                    if (response.body() != null) {

                                                        if (response.body()!!.message.equals(
                                                                resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                                                ignoreCase = true
                                                            )
                                                        ) {

                                                            val myIntent =
                                                                Intent(
                                                                    this@SplashScreen,
                                                                    WelcomeActivity::class.java
                                                                )
                                                            startActivity(myIntent)

                                                            finish()

                                                        } else {


                                                            Toast.makeText(
                                                                this@SplashScreen,
                                                                response.body()!!.message.toString(),
                                                                Toast.LENGTH_LONG
                                                            ).show()


                                                        }

                                                    }
                                                }


                                            } else {
                                                try {

                                                    AppProgressBar.closeLoader();
                                                    if (response.body() != null) {

                                                        //  val jObjError = JSONObject(response.errorBody()!!.string())
                                                        Toast.makeText(
                                                            this@SplashScreen,
                                                            response.body()!!.message.toString(),
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(
                                                        this@SplashScreen,
                                                        e.message,
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
                                                }

                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ServiceCallStatuswiseetailsResponse?>,
                                            t: Throwable
                                        ) {
                                            AppProgressBar.closeLoader()
                                            if (t is NoConnectivityException) {
                                                Toast.makeText(
                                                    this@SplashScreen,
                                                    "" + t.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {

                                                Toast.makeText(
                                                    this@SplashScreen,
                                                    "" + t.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    })
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                            else {

                            }


                        } else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                Toast.makeText(
                                    this@SplashScreen,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()


                            }
                        }


                    } else {

                        AppProgressBar.closeLoader()

                        if (response.body() != null) {

                            Toast.makeText(
                                this@SplashScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }

                override fun onFailure(
                    call: Call<ServiceCallStatuswiseetailsResponse?>,
                    t: Throwable
                ) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Toast.makeText(
                            this@SplashScreen,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Toast.makeText(
                            this@SplashScreen,
                            "" + t.message,
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
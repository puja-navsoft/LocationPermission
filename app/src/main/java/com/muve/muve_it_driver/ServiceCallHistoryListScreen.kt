package com.muve.muve_it_driver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.servicecalllisting.DetailItem
import com.muve.muve_it_driver.model.servicecalllisting.ServiceCallListingResponse
import com.muve.muve_it_driver.model.servicecallstatuswisedetails.ServiceCallStatuswiseetailsResponse
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.serviceCallDetailsScreen.ServiceDetailsScreen
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerServiceCallList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class ServiceCallHistoryListScreen : AppCompatActivity(),
    RecyclerViewItemClickListenerServiceCallList {

    var servicelist: ArrayList<DetailItem?>? = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var sharedPreferences: SharedPreferences? = null
    var back: ImageView? = null
    var onoffcall: Boolean? = false
    var rcv_trip_recycler: RecyclerView? = null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var service_type: String = ""
    var responseServiceCallList: Call<ServiceCallListingResponse>? = null
    var responseServiceCallListDetail: Call<ServiceCallStatuswiseetailsResponse>? = null
    lateinit var serviceCallAllListAdapter: ServiceCallAllListAdapter
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_call_history_list_screen)


        back = findViewById<ImageView>(R.id.img_back_arrow)
        rcv_trip_recycler = findViewById<RecyclerView>(R.id.rcv_trip_recycler)
        sharedPreferenceManager = SharedPreferenceManager(this)

        // rcv_trip_recycler = RecyclerView(this)
        // layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        // rcv_trip_recycler!!.layoutManager = layoutManager


        try {

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            token = sharedPreferences!!.getString("Authorization", "").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref", "").toString()

        } catch (e: java.lang.Exception) {

            e.printStackTrace()
        }

        setupRecyleview()
        getServiceCallList()

        back!!.setOnClickListener {

            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)
            // finish()
        }
    }


    private fun getServiceCallList() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            if (defaultIdLog.equals("")) {
                req["driver_id"] = defaultIdReg
                responseServiceCallList = apiservice.doingservicecall_history_list(tokenReg, req)

            } else {
                req["driver_id"] = defaultIdLog
                responseServiceCallList = apiservice.doingservicecall_history_list(token, req)


            }
            responseServiceCallList!!.enqueue(object : Callback<ServiceCallListingResponse?> {
                override fun onResponse(
                    call: Call<ServiceCallListingResponse?>,
                    response: Response<ServiceCallListingResponse?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {


                            servicelist = response.body()!!.detail!!
                            rcv_trip_recycler = RecyclerView(this@ServiceCallHistoryListScreen!!)


                            if (servicelist!!.size > 0 && servicelist != null) {

                                serviceCallAllListAdapter.setItems(servicelist)
                                serviceCallAllListAdapter.notifyDataSetChanged()

                            } else {

                                Toast.makeText(
                                    this@ServiceCallHistoryListScreen,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                            Toast.makeText(
                                this@ServiceCallHistoryListScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

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
                                            this@ServiceCallHistoryListScreen,

                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                } else {


                                    Toast.makeText(
                                        this@ServiceCallHistoryListScreen,
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
                                    this@ServiceCallHistoryListScreen,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        } catch (e: Exception) {

                            Toast.makeText(
                                this@ServiceCallHistoryListScreen,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<ServiceCallListingResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Toast.makeText(
                            this@ServiceCallHistoryListScreen,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Toast.makeText(
                            this@ServiceCallHistoryListScreen,
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

    fun setupRecyleview() {

        serviceCallAllListAdapter = ServiceCallAllListAdapter(this, this)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_trip_recycler!!.layoutManager = layoutManager
        rcv_trip_recycler!!.adapter = serviceCallAllListAdapter

    }


    override fun onResume() {
        super.onResume()
    }

    override fun onItemClick(position: Int, countryList: ArrayList<DetailItem>) {

        try {

            if (countryList.get(position).loadUnload == false) {

                service_type = ""
            } else {

                service_type = "secondary"

            }


            if (countryList.get(position).status.equals("Completed") || countryList.get(position).status.equals(
                    "Cancelled"
                ) || countryList.get(position).status.equals("Incompleted")
            ) {


                val myIntent = Intent(this, ServiceDetailsScreen::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )

                startActivity(myIntent)
                // finish()
            } else if (countryList.get(position).status.equals("Accepted", ignoreCase = true)) {


                val myIntent = Intent(this, HomeActivity::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("customerid", countryList.get(position).customerId.toString())
                myIntent.putExtra("driverid", countryList.get(position).driverId.toString())
                myIntent.putExtra("vehicleType", countryList.get(position).vehicleType.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("pickPlace", countryList.get(position).pickPlace.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("dropPlace", countryList.get(position).dropPlace.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("arrived_time", countryList.get(position).arrivedTime)
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )

                startActivity(myIntent)

            } else if (countryList.get(position).status.equals(
                    "ArrivedAtPickUp",
                    ignoreCase = true
                )
            ) {


                val myIntent = Intent(this, HomeActivity::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("customerid", countryList.get(position).customerId.toString())
                myIntent.putExtra("driverid", countryList.get(position).driverId.toString())
                myIntent.putExtra("vehicleType", countryList.get(position).vehicleType.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("pickPlace", countryList.get(position).pickPlace.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("dropPlace", countryList.get(position).dropPlace.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("arrived_time", countryList.get(position).arrivedTime)
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )


                startActivity(myIntent)

            } else if (countryList.get(position).status.equals(
                    "CustomerCodeEntered",
                    ignoreCase = true
                )
            ) {


                val myIntent = Intent(this, HomeActivity::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("customerid", countryList.get(position).customerId.toString())
                myIntent.putExtra("driverid", countryList.get(position).driverId.toString())
                myIntent.putExtra("vehicleType", countryList.get(position).vehicleType.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("pickPlace", countryList.get(position).pickPlace.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("dropPlace", countryList.get(position).dropPlace.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("arrived_time", countryList.get(position).arrivedTime)
                myIntent.putExtra("verifyCodeTime", countryList.get(position).verifyCodeTime)
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )


                startActivity(myIntent)

            } else if (countryList.get(position).status.equals(
                    "DrivingToDestination",
                    ignoreCase = true
                )
            ) {

                val myIntent = Intent(this, HomeActivity::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("customerid", countryList.get(position).customerId.toString())
                myIntent.putExtra("driverid", countryList.get(position).driverId.toString())
                myIntent.putExtra("vehicleType", countryList.get(position).vehicleType.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("pickPlace", countryList.get(position).pickPlace.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("dropPlace", countryList.get(position).dropPlace.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("arrived_time", countryList.get(position).arrivedTime)
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )


                startActivity(myIntent)

            } else if (countryList.get(position).status.equals(
                    "ArrivedAtDestination",
                    ignoreCase = true
                )
            ) {

                val myIntent = Intent(this, HomeActivity::class.java)
                myIntent.putExtra("status", countryList.get(position).status.toString())
                myIntent.putExtra("customerid", countryList.get(position).customerId.toString())
                myIntent.putExtra("driverid", countryList.get(position).driverId.toString())
                myIntent.putExtra("vehicleType", countryList.get(position).vehicleType.toString())
                myIntent.putExtra("pick_lat", countryList.get(position).pickLat.toString())
                myIntent.putExtra("pick_long", countryList.get(position).pickLong.toString())
                myIntent.putExtra("pickPlace", countryList.get(position).pickPlace.toString())
                myIntent.putExtra("drop_lat", countryList.get(position).dropLat.toString())
                myIntent.putExtra("drop_long", countryList.get(position).dropLong.toString())
                myIntent.putExtra("dropPlace", countryList.get(position).dropPlace.toString())
                myIntent.putExtra("service_id", countryList.get(position).serviceId.toString())
                myIntent.putExtra("arrived_time", countryList.get(position).arrivedTime)
                myIntent.putExtra(
                    "arrivedDestinationTime",
                    countryList.get(position).arrivedDestinationTime
                )
                myIntent.putExtra("service_type", service_type)
                myIntent.putExtra("second_driver_id", countryList.get(position).second_driver_id)
                myIntent.putExtra("loadUnload", countryList.get(position).loadUnload)
                myIntent.putExtra("unattendDrop", countryList.get(position).unattendDrop)
                myIntent.putExtra("vehicleImage", countryList.get(position).uploadPic.toString())
                myIntent.putExtra("customer_name", countryList.get(position).fullname.toString())
                myIntent.putExtra(
                    "customer_images",
                    countryList.get(position).customer_images.toString()
                )

                startActivity(myIntent)

            } else if (countryList.get(position).status.equals("Paused", ignoreCase = true)) {

                try {

                    AppProgressBar.openLoader(
                        this,
                        this.getResources().getString(R.string.pleasewait)
                    )

                    val apiservice: ApiInterface =
                        ApiClient.getClient(this).create(ApiInterface::class.java)
                    val req = HashMap<String, Any>()
                    if (defaultIdLog.equals("")) {
                        req["service_id"] = countryList.get(position).serviceId!!
                        req["driver_id"] = defaultIdReg!!

                        responseServiceCallListDetail =
                            apiservice.doingservicecall_detailsinformation(tokenReg, req)

                    } else {
                        req["service_id"] = countryList.get(position).serviceId!!
                        req["driver_id"] = defaultIdLog!!

                        responseServiceCallListDetail =
                            apiservice.doingservicecall_detailsinformation(token, req)


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

                                    val myIntent = Intent(
                                        this@ServiceCallHistoryListScreen,
                                        HomeActivity::class.java
                                    )
                                    myIntent.putExtra("status", response.body()!!.detail!!.status)
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
                                    myIntent.putExtra("service_type", service_type)
                                    myIntent.putExtra(
                                        "second_driver_id",
                                        countryList.get(position).second_driver_id
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
                                        "customer_images",
                                        countryList.get(position).customer_images.toString()
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
                                                    this@ServiceCallHistoryListScreen,
                                                    WelcomeActivity::class.java
                                                )
                                            startActivity(myIntent)

                                            finish()

                                        } else {


                                            Toast.makeText(
                                                this@ServiceCallHistoryListScreen,
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
                                            this@ServiceCallHistoryListScreen,
                                            response.body()!!.message.toString(),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@ServiceCallHistoryListScreen,
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
                                    this@ServiceCallHistoryListScreen,
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                Toast.makeText(
                                    this@ServiceCallHistoryListScreen,
                                    "" + t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
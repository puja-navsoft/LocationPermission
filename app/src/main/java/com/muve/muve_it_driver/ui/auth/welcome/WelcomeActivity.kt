package com.muve.muve_it_driver.ui.auth.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.ActivityDashboardScreenModifyBinding
import com.muve.muve_it_driver.model.aboutusmodel.AboutUsPojo
import com.muve.muve_it_driver.model.aboutusmodel.DataItemAbout
import com.muve.muve_it_driver.model.aboutusmodel.cityDataItemAbout
import com.muve.muve_it_driver.model.countrymodel.CountryPojo
import com.muve.muve_it_driver.model.countrymodel.DataItem
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.util.AppProgressBar
import com.google.gson.Gson
import com.muve.muve_it_driver.util.NetworkUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WelcomeActivity : AppCompatActivity() {

    var sharedPreferenceManager: SharedPreferenceManager? = null
    var arr_countrylist: ArrayList<DataItem> = ArrayList<DataItem>()
    var arr_aboutuslist: ArrayList<DataItemAbout> = ArrayList<DataItemAbout>()
    var arr_aboutuslistcity: ArrayList<cityDataItemAbout> = ArrayList<cityDataItemAbout>()
    var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val wBinding : ActivityDashboardScreenModifyBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_screen_modify)
        val viewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)
        wBinding.welcome = viewModel
        sharedPreferenceManager = SharedPreferenceManager(this)

        if (NetworkUtility.isNetworkAvailable(this)) {
            getCountryList()
        }
        else{
            Toast.makeText(
                this,
                getString(R.string.msg_no_internet),
                Toast.LENGTH_LONG
            ).show()
        }

        if (NetworkUtility.isNetworkAvailable(this)) {
            getAboutUsList()
        }else{
            Toast.makeText(
                this,
                getString(R.string.msg_no_internet),
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun getAboutUsList() {

        try {
            // AppProgressBar.openLoader(activity, resources.getString(R.string.pleasewait))
            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val response1: Call<AboutUsPojo> = apiservice.getfirst_service_call()
            response1.enqueue(object : Callback<AboutUsPojo> {
                override fun onResponse(
                    call: Call<AboutUsPojo>,
                    response: Response<AboutUsPojo>
                ) {
                    AppProgressBar.closeLoader()
                    if (response.isSuccessful) {

                        //  Toast.makeText(requireContext(), ""+response.body()!!.status, Toast.LENGTH_LONG).show()

                      //  AppProgressBar.closeLoader();
                        if (response.body()!!.status == true) {

                            arr_aboutuslist.clear()
                            arr_aboutuslistcity.clear()
                            response.body()?.let {
                                sharedPreferenceManager!!.saveAboutList(it.data)
                                sharedPreferenceManager!!.saveCityList(it.city)


                                arr_aboutuslist.addAll(it.data)
                                arr_aboutuslistcity.addAll(it.city)

                            }


                            saveSupportAboutUs(arr_aboutuslist)
                        } else {


                          //  AppProgressBar.closeLoader()


                            if (response.body()!!.details.equals(
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
                                        this@WelcomeActivity,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()

                            } else {


                                Toast.makeText(
                                    this@WelcomeActivity,
                                    response.body()!!.details.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }


                        }
                    }
                    else{

                     //   AppProgressBar.closeLoader()


                    }
                }

                override fun onFailure(call: Call<AboutUsPojo>, t: Throwable) {
                   // AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@WelcomeActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@WelcomeActivity,
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

    private fun getCountryList() {

        try {
            AppProgressBar.openLoader(this, resources.getString(R.string.pleasewait))
            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val response1: Call<CountryPojo> = apiservice.getCountryList()
            response1.enqueue(object : Callback<CountryPojo> {
                override fun onResponse(
                    call: Call<CountryPojo>,
                    response: Response<CountryPojo>
                ) {
                    AppProgressBar.closeLoader()
                    if (response.isSuccessful) {

                        //  Toast.makeText(requireContext(), ""+response.body()!!.status, Toast.LENGTH_LONG).show()



                        arr_countrylist.clear()
                        response.body()?.let {
                            sharedPreferenceManager!!.saveCountryList(it.data)
                            arr_countrylist.addAll(it.data) }


                        saveSupportCountry(arr_countrylist)


                    }
                    else{
                        AppProgressBar.closeLoader();

                        Toast.makeText(this@WelcomeActivity, "Check your network!"+response.body()!!.status, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CountryPojo>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@WelcomeActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@WelcomeActivity,
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

    fun saveSupportCountry(dataItem: ArrayList<DataItem>)
    {
        val prefs = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(dataItem)
        editor.remove("COUNTRYLIST").commit()
        editor.putString("COUNTRYLIST", json)
        editor.commit()

    }

    fun saveSupportAboutUs(dataItem1: ArrayList<DataItemAbout>)
    {
        val prefs = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(dataItem1)
        editor.remove("ABOUTLIST").commit()
        editor.putString("ABOUTLIST", json)
        editor.commit()

    }


    override fun onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)

    }
}

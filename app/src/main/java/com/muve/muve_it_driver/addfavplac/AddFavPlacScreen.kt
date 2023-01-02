package com.muve.muve_it_driver.addfavplac

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.application.Muve
import com.muve.muve_it_driver.favouritelocation.FavouriteLocationScreen
import com.muve.muve_it_driver.model.addaddress.AddaddressResponse
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class AddFavPlacScreen : AppCompatActivity() {

    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
    var defaultIdLog: String?=""
    var iv_back: ImageView?=null
    var tv_home: TextView?=null
    var tv_work: TextView?=null
    var et_lablname: EditText?=null
    var et_address: EditText?=null
    var tv_custom: TextView?=null
    var tv_saveaddress: TextView?=null
    var cL_main: ConstraintLayout?=null
    var response1: Call<AddaddressResponse>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_fav_plac_screen)

        iv_back=findViewById(R.id.iv_back)
        tv_home=findViewById(R.id.tv_home)
        tv_work=findViewById(R.id.tv_work)
        et_lablname=findViewById(R.id.et_lablname)
        tv_custom=findViewById(R.id.tv_custom)
        tv_saveaddress=findViewById(R.id.tv_saveaddress)
        et_address=findViewById(R.id.et_address)
        cL_main=findViewById(R.id.cL_main)
        sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")


        sharedPreferenceManager = SharedPreferenceManager(this)


        iv_back!!.setOnClickListener {

            finish()

        }

        cL_main!!.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)

        }

        tv_home!!.setOnClickListener {

            et_lablname!!.setText("Home")
            et_lablname!!.isCursorVisible=false
            et_lablname!!.isEnabled=false
            et_lablname!!.isClickable=false
            et_lablname!!.setTextColor(resources.getColor(R.color.black))
            et_address!!.setTextColor(resources.getColor(R.color.black))

            tv_home!!.background=resources.getDrawable(R.drawable.round_whitewithboarderorngfill)
            tv_work!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)
            tv_custom!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)

            tv_custom!!.setTextColor(resources.getColor(R.color.black))
            tv_home!!.setTextColor(resources.getColor(R.color.colorWhite))
            tv_work!!.setTextColor(resources.getColor(R.color.black))
        }

        tv_work!!.setOnClickListener {

            et_lablname!!.setText("Work")
            et_lablname!!.isCursorVisible=false
            et_lablname!!.isEnabled=false
            et_lablname!!.isClickable=false
            et_lablname!!.setTextColor(resources.getColor(R.color.black))
            et_address!!.setTextColor(resources.getColor(R.color.black))

            tv_home!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)
            tv_work!!.background=resources.getDrawable(R.drawable.round_whitewithboarderorngfill)
            tv_custom!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)
            tv_custom!!.setTextColor(resources.getColor(R.color.black))
            tv_home!!.setTextColor(resources.getColor(R.color.black))
            tv_work!!.setTextColor(resources.getColor(R.color.white))
        }

        tv_custom!!.setOnClickListener {

            et_address!!.setTextColor(resources.getColor(R.color.black))

            et_lablname!!.isCursorVisible=true
            et_lablname!!.isEnabled=true
            et_lablname!!.isClickable=true
            et_lablname!!.setTextColor(resources.getColor(R.color.black))
            tv_home!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)
            tv_work!!.background=resources.getDrawable(R.drawable.round_whitewithboardergray)
            tv_custom!!.background=resources.getDrawable(R.drawable.round_whitewithboarderorngfill)
            tv_custom!!.setTextColor(resources.getColor(R.color.white))
            tv_home!!.setTextColor(resources.getColor(R.color.black))
            tv_work!!.setTextColor(resources.getColor(R.color.black))
            et_lablname!!.setText("")
            et_lablname!!.setHint("Enter Label Name")
            et_address!!.setHint("Your address")
        }


        et_address!!.setOnClickListener {

            et_address!!.setTextColor(resources.getColor(R.color.black))

        }




        tv_saveaddress!!.setOnClickListener {

            if (et_lablname!!.text.toString().equals("") || et_address!!.text.toString().equals("")){

                Toast.makeText(this, "Enter your Lable and address", Toast.LENGTH_LONG).show()
            }else{

                addaddressToFav()
            }

        }
    }

    private fun addaddressToFav() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["id"] = defaultIdLog!!
            req["name"] = et_lablname!!.text.toString()
            req["address"] = et_address!!.text.toString()
            req["latitude"] =  "43.56"
            req["longitude"] =  "20.24"

            response1 = apiservice.doingaddaddress( req)
            response1!!.enqueue(object : Callback<AddaddressResponse?> {
                override fun onResponse(
                    call: Call<AddaddressResponse?>,
                    response: Response<AddaddressResponse?>
                ) {

                     AppProgressBar.closeLoader();
                    if (response.isSuccessful()) {

                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            et_lablname!!.setHint("Enter Label Name")
                            et_address!!.setHint("Your address")

                            et_lablname!!.setText("")
                            et_address!!.setText("")


                            Toast.makeText(
                                this@AddFavPlacScreen,
                                response.body()!!.detail,
                                Toast.LENGTH_LONG
                            ).show()

                            val myIntent = Intent(this@AddFavPlacScreen, FavouriteLocationScreen::class.java)
                            //  myIntent.putExtra("locationscreen","Camera")
                            startActivity(myIntent)
                            finish()

                            }

                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.detail.equals(
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
                                            this@AddFavPlacScreen,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                }
                                else {


                                    Toast.makeText(
                                        this@AddFavPlacScreen,
                                        response.body()!!.detail.toString(),
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
                                    this@AddFavPlacScreen,
                                    response.body()!!.detail,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(Muve.getInstance(), e.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<AddaddressResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@AddFavPlacScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@AddFavPlacScreen,
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
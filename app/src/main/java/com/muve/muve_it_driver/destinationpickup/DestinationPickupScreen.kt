package com.muve.muve_it_driver.destinationpickup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.FavPlaceAdapterAnotherforDestination
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.application.Muve
import com.muve.muve_it_driver.favouritelocation.FavouriteLocationScreen
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class DestinationPickupScreen : AppCompatActivity(), RecyclerViewItemClickListenerFavPlace {


    var rcyclFvrtPlac: RecyclerView?=null
    var iv_back: ImageView?=null
    var tv_favlist: TextView?=null
    var et_currentloc: EditText?=null
    var et_destLoc: EditText?=null
    var defaultIdLog: String =""
    var city: String =""
    var token: String =""
    var response1: Call<UserDetailPojo>? = null
    var favoritePlaces: List<FavouritePlacesItem?>? = null
    lateinit var favPlaceAdapter: FavPlaceAdapterAnotherforDestination
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var lat = 0.0
    var longitude = 0.0
    var storelat = 0.0
    var storelongitude = 0.0
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_pickup_screen)

        rcyclFvrtPlac = findViewById<RecyclerView>(R.id.rcyclFvrtPlac)
        iv_back = findViewById<ImageView>(R.id.iv_back)
        tv_favlist = findViewById<TextView>(R.id.tv_favlist)
        et_currentloc = findViewById<EditText>(R.id.et_currentloc)
        et_destLoc = findViewById<EditText>(R.id.et_destLoc)
        et_destLoc!!.isCursorVisible=true

        sharedPreferenceManager = SharedPreferenceManager(this)

        val sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
        lat = sharedPreferences!!.getString("idSharedPrefAfterLoglat","")!!.toDouble()
        longitude = sharedPreferences!!.getString("fnameSharedPrefAfterLoglongitude","")!!.toDouble()
        city = sharedPreferences!!.getString("lnameSharedPrefAfterLogcity","")!!
        token = sharedPreferences!!.getString("Authorization","")!!

       // AppUtilities.hideSoftKeyboard(this)

        if (intent.getStringExtra("Destinationaddress")!=null && !intent.getStringExtra("Destinationaddress").equals("")){
            storelat = intent.getStringExtra("Destinationlat")!!.toDouble()
            storelongitude = intent.getStringExtra("Destinationlong")!!.toDouble()
            et_destLoc!!.setText(intent.getStringExtra("Destinationaddress"))
        }else{


        }

      //  et_currentloc!!.isClickable=false

        et_currentloc!!.setOnClickListener {

            et_currentloc!!.isCursorVisible=true
            et_currentloc!!.isEnabled=true
            et_currentloc!!.isClickable=true
            et_currentloc!!.isClickable=true

        }


        et_destLoc!!.setOnClickListener {


        }

        iv_back!!.setOnClickListener {

            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)

            finish()

        }
       /* setupRecyleview()
        fetchFevPlac()*/

        tv_favlist!!.setOnClickListener {

            val myIntent = Intent(this, FavouriteLocationScreen::class.java)
            //  myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)

        }

        try {
            if (city!=null && !city.equals("") ){

                et_currentloc!!.setText(city)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun setupRecyleview() {

        favPlaceAdapter = FavPlaceAdapterAnotherforDestination(this@DestinationPickupScreen, this@DestinationPickupScreen)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyclFvrtPlac!!.layoutManager = layoutManager
        rcyclFvrtPlac!!.adapter = favPlaceAdapter
    }

    private fun fetchFevPlac() {

        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["driver_id"] = defaultIdLog
          //  req["Authorization"] = token


            response1 =
                apiservice.getuserdetails(token, req /*binding.edtPassword.text.toString(),binding.txtCountryCode.text.toString(),binding.edtPhoneNumber.text.toString(),"normal"*/)
            // Call<RejectorderModel> response1 = apiservice.rejectitemorder(kitchecn_order_id,item_id,reason_id_);
            response1!!.enqueue(object : Callback<UserDetailPojo?>/*,
                RecyclerViewItemClickListenerFavPlace*/
            /*RecyclerViewItemClickListenerFavPlace*/ {
                override fun onResponse(
                    call: Call<UserDetailPojo?>,
                    response: Response<UserDetailPojo?>
                ) {

                    // AppProgressBar.closeLoader();
                    if (response.isSuccessful()) {

                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            favoritePlaces = response.body()!!.favouritePlaces
                            rcyclFvrtPlac = RecyclerView(this@DestinationPickupScreen)

                            if (favoritePlaces!!.size > 0  && favoritePlaces!=null) {

                                favPlaceAdapter.setItems(favoritePlaces)
                                favPlaceAdapter.notifyDataSetChanged()

                            }


                        } else {
                            AppProgressBar.closeLoader();

                            if (response.body()!!.message.equals(resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true)){

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(this@DestinationPickupScreen, WelcomeActivity::class.java)
                                startActivity(myIntent)

                                finish()

                            }

                            else{


                                Toast.makeText(
                                    this@DestinationPickupScreen,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                        }
                    } else {
                        try {
                            AppProgressBar.closeLoader()
                            Toast.makeText(
                                this@DestinationPickupScreen,
                                /*response.body()!!.detail*/ ""+response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(Muve.getInstance(), e.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<UserDetailPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DestinationPickupScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DestinationPickupScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemClick(position: Int, countryList: List<FavouritePlacesItem>) {

    }

    override fun onResume() {
        super.onResume()

        setupRecyleview()
        if (NetworkUtility.isNetworkAvailable(this)) {
            fetchFevPlac()
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
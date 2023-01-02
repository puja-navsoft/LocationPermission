package com.muve.muve_it_driver.favouritelocation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.FavPlaceAdapterAnother
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.UserDetailPojo.FavouritePlacesItem
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.addfavplac.AddFavPlacScreen
import com.muve.muve_it_driver.application.Muve
import com.muve.muve_it_driver.destinationpickup.DestinationPickupScreen
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerFavPlace
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FavouriteLocationScreen : AppCompatActivity(), RecyclerViewItemClickListenerFavPlace {

    var favListLoc: RecyclerView?=null
    var iv_back: ImageView?=null
    var iv_flotingIcon: ImageView?=null
    var defaultIdLog: String =""
    var token: String =""
    var response1: Call<UserDetailPojo>? = null
    var favoritePlaces: MutableList<FavouritePlacesItem>?= null
    lateinit var favPlaceAdapter: FavPlaceAdapterAnother
    private lateinit var layoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_location_screen)


        val sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
        token = sharedPreferences!!.getString("Authorization","").toString()

        favListLoc = findViewById<RecyclerView>(R.id.favListLoc)
        iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_flotingIcon = findViewById<ImageView>(R.id.iv_flotingIcon)

        if (NetworkUtility.isNetworkAvailable(this)) {
            fetchFevPlac()
        }else{

        }
        setupRecyleview()

       /* setupRecyleview()
        fetchFevPlac()

        iv_back!!.setOnClickListener {

            finish()

        }
*/
        iv_flotingIcon!!.setOnClickListener {

            val myIntent = Intent(this, AddFavPlacScreen::class.java)
            startActivity(myIntent)

        }
    }

    private fun setupRecyleview() {

        favPlaceAdapter = FavPlaceAdapterAnother(this, this )

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        favListLoc!!.layoutManager = layoutManager
        favListLoc!!.adapter = favPlaceAdapter
    }

    public fun fetchFevPlac() {

        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["id"] = defaultIdLog
          //  req["Authorization"] = token


            response1 =
                apiservice.getuserdetails(token,req /*binding.edtPassword.text.toString(),binding.txtCountryCode.text.toString(),binding.edtPhoneNumber.text.toString(),"normal"*/)
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
                            favoritePlaces?.clear()
                            favoritePlaces = response.body()!!.favouritePlaces
                            favListLoc = RecyclerView(this@FavouriteLocationScreen)


                            if (favoritePlaces!!.size > 0  && favoritePlaces!=null) {

                                favPlaceAdapter.setItems(favoritePlaces)
                                favPlaceAdapter.notifyDataSetChanged()

                            }

                        } else {
                            Log.v("error...","eror")
                            AppProgressBar.closeLoader()

                            //   val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                this@FavouriteLocationScreen,
                                "No address found",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        try {

                            Log.v("error","eror")
                            AppProgressBar.closeLoader()

                            // val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                this@FavouriteLocationScreen,
                                /*response.body()!!.detail*/ "No address found",
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
                            this@FavouriteLocationScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@FavouriteLocationScreen,
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

        Log.v("lat",""+countryList.get(position).latitude)
        Log.v("long",""+countryList.get(position).longitude)
        Log.v("address",""+countryList.get(position).address)


        val myIntent = Intent(this, DestinationPickupScreen::class.java)
        myIntent.putExtra("Destinationlat",countryList.get(position).latitude)
        myIntent.putExtra("Destinationlong",countryList.get(position).longitude)
        myIntent.putExtra("Destinationaddress",
            Character.toUpperCase(countryList.get(position).address!!.get(0))
                .toString() + countryList.get(position).address!!.substring(1))
        startActivity(myIntent)

    }

    override fun onResume() {
        super.onResume()




        favPlaceAdapter.notifyDataSetChanged()

        iv_back!!.setOnClickListener {

            finish()

        }


    }


}
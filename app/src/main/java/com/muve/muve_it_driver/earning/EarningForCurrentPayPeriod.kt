package com.muve.muve_it_driver.earning

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.EarningAdapter
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.model.earninglist.Detail
import com.muve.muve_it_driver.model.earninglist.EarningListResponse
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.util.AppProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class EarningForCurrentPayPeriod : AppCompatActivity() {

    var iv_back: ImageView? = null
    var earningperday: RecyclerView? = null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var tv_earningToday: TextView ? = null
    var tv_servicecall: TextView ? = null
    var sharedPreferences: SharedPreferences? = null
    var earningListAdapter: EarningAdapter? = null
    var cardListArr: List<Detail>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earning_for_current_pay_period)
        earningperday = findViewById<RecyclerView>(R.id.earningperday)
        iv_back = findViewById<ImageView>(R.id.iv_back)
        tv_earningToday = findViewById<TextView>(R.id.tv_earningToday)
        tv_servicecall = findViewById<TextView>(R.id.tv_servicecall)


        iv_back!!.setOnClickListener {

           finish()

        }

        try {
            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()

        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }


        getEarningList()

        earningListAdapter = EarningAdapter(this)


    }
    fun setupRecyleview()

    {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        earningperday!!.setLayoutManager(layoutManager)
        earningperday!!.setAdapter(earningListAdapter)


    }

    private fun getEarningList() {

        var response1: Call<EarningListResponse>? = null

        try {
            AppProgressBar.openLoader(this, resources.getString(R.string.pleasewait))
            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            token = sharedPreferences!!.getString("Authorization", "")!!
            tokenReg = sharedPreferences!!.getString("AuthSharedPref", "")!!
            sharedPreferences!!.getString("stripe_customer_id", "")

            val logReq = HashMap<String, Any>()

            if (defaultIdLog.equals("")) {

                logReq["driver_id"] = defaultIdReg!!

                response1 = ApiClient.getClient(this).create(ApiInterface::class.java)
                    .getearning(tokenReg, logReq)
            } else {

                logReq["driver_id"] = defaultIdLog!!

                response1 = ApiClient.getClient(this).create(ApiInterface::class.java)
                    .getearning(token, logReq)
            }
            response1!!.enqueue(object : Callback<EarningListResponse?> {
                override fun onResponse(
                    call: Call<EarningListResponse?>,
                    response: Response<EarningListResponse?>
                ) {

                    setupRecyleview()

                    if (response.body() != null && response.body()!!.status == true) {
                        AppProgressBar.closeLoader()
                        //Toast.makeText(this@EarningForCurrentPayPeriod, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        if (response.body()!!.detail != null) {

                           /* Toast.makeText(
                                this@EarningForCurrentPayPeriod,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()*/

                            if (response.body()!!.todayEarning!=null){
                                tv_earningToday!!.setText(response.body()!!.todayEarning.toString())
                            }

                            if (response.body()!!.todayServicecall!=null) {

                                tv_servicecall!!.setText(response.body()!!.todayServicecall.toString())

                            }

                            cardListArr = response.body()!!.detail

                            if (cardListArr != null) {
                                earningperday!!.setVisibility(View.VISIBLE)

                                earningListAdapter?.setItems(cardListArr!!)

                            } else {
                                earningperday!!.setVisibility(View.GONE)
                            }
                        }


                    } else {
                        if (response.body() != null) {
                            AppProgressBar.closeLoader()
                            Log.v("tmsg",""+response.body()!!.message)

                            Toast.makeText(
                                this@EarningForCurrentPayPeriod,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }

                override fun onFailure(call: Call<EarningListResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Log.v("tmsg",""+t.message)
                        // show No Connectivity message to user or do whatever you want.
                        Toast.makeText(this@EarningForCurrentPayPeriod, t.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Log.v("tmsg",""+t.message)

                        Toast.makeText(this@EarningForCurrentPayPeriod, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}
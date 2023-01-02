package com.muve.muve_it_driver

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.model.notificationlist.NotificationListResponse
import com.muve.muve_it_driver.model.notificationlist.Result
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.util.AppProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    val countryList: ArrayList<String> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var sharedPreferences: SharedPreferences? = null
    var back: ImageView? = null
    var rcv_notification_recycler: RecyclerView? = null
    var notificationListAdapter: NotificationListAdapter? = null
    var cardListArr: List<Result>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        back = findViewById<ImageView>(R.id.img_back_arrow)
        rcv_notification_recycler = findViewById<RecyclerView>(R.id.rcv_notification_recycler)


        try {
            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()

        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }

        getNotificationList()


        back!!.setOnClickListener {

            finish()
        }

        notificationListAdapter = NotificationListAdapter(this)

    }

    fun setupRecyleview()

    {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcv_notification_recycler!!.setLayoutManager(layoutManager)
        rcv_notification_recycler!!.setAdapter(notificationListAdapter)


    }


    private fun getNotificationList() {

        var response1: Call<NotificationListResponse>? = null

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
                    .getnotification(tokenReg, logReq)
            } else {

                logReq["driver_id"] = defaultIdLog!!

                response1 = ApiClient.getClient(this).create(ApiInterface::class.java)
                    .getnotification(token, logReq)
            }
            response1!!.enqueue(object : Callback<NotificationListResponse?> {
                override fun onResponse(
                    call: Call<NotificationListResponse?>,
                    response: Response<NotificationListResponse?>
                ) {

                    setupRecyleview()

                    if (response.body() != null && response.body()!!.status == true) {
                        AppProgressBar.closeLoader()
                        Toast.makeText(this@NotificationActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        if (response.body()!!.result != null) {

                            Toast.makeText(
                                this@NotificationActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()


                            cardListArr = response.body()!!.result

                            if (cardListArr != null) {
                                rcv_notification_recycler!!.setVisibility(View.VISIBLE)

                                notificationListAdapter?.setItems(cardListArr!!)

                            } else {
                                rcv_notification_recycler!!.setVisibility(View.GONE)
                            }
                        }


                    } else {
                        if (response.body() != null) {
                            AppProgressBar.closeLoader()

                            Toast.makeText(
                                this@NotificationActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }

                override fun onFailure(call: Call<NotificationListResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        // show No Connectivity message to user or do whatever you want.
                        Toast.makeText(this@NotificationActivity, t.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@NotificationActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }



}

package com.muve.muve_it_driver.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.muve.muve_it_driver.Driverstatusstoreresponse
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.NetworkUtility.Companion.HAULERS_URL
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class SettingsActivity : AppCompatActivity() {
    var iv_back: ImageView?=null
    var tv_morefav: TextView?=null
    var tv_logout: TextView?=null
    var tv_deleteaccnt: TextView?=null
    lateinit var channel: PrivateChannel
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var defaultIdLog: String =""
    var defaultIdReg: String =""
    var tokenReg: String =""
    var token: String =""
    var sharedPreferences: SharedPreferences?=null
    var responseDeleteAccount: Call<VerifyOtpPojo>? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null
    var deleteview: ConstraintLayout?=null
    var tv_yes: TextView?=null
    var tv_no: TextView?=null
    var tv_about: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferenceManager = SharedPreferenceManager(this)
        iv_back = findViewById<ImageView>(R.id.iv_back)
        tv_morefav = findViewById<TextView>(R.id.tv_morefav)
        tv_logout = findViewById<TextView>(R.id.tv_logout)
        tv_deleteaccnt = findViewById<TextView>(R.id.tv_deleteaccnt)
        deleteview = findViewById<ConstraintLayout>(R.id.deleteview)
        tv_yes = findViewById<TextView>(R.id.tv_yes)
        tv_no = findViewById<TextView>(R.id.tv_no)
        tv_about = findViewById<TextView>(R.id.tv_about)

        sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)


        try {

            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            // token = sharedPreferences!!.getString("Authorization","").toString()
           // availability = sharedPreferences!!.getString("availability","").toString()
            // tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()


        }

        catch (e:Exception){
            e.printStackTrace()
        }


        if (defaultIdLog.equals("")){

            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()

        }
        else{

            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!

            token = sharedPreferences!!.getString("Authorization","").toString()

        }



       // val authorizer = HttpAuthorizer("http://3.234.0.204:8555/pusher/auth")
        val authorizer = HttpAuthorizer(NetworkUtility.authUrl)
        val options = PusherOptions().setAuthorizer(authorizer)
        //options.setCluster("ap2")
        options.setCluster(NetworkUtility.cluster)

        val sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

       // val pusher = Pusher("d5041f2ec10120e451ce", options)
        val pusher = Pusher(NetworkUtility.key, options)

        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i("Pusher", "State changed from ${change.previousState} to ${change.currentState}")

                if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                    val socketId = pusher.connection.socketId
                    val channelname = "private-my-channel"
                    Log.v("socketId","socketId"+socketId)
                    val parameters = HashMap<String, String>()
                    parameters["socket_id"] = socketId
                    parameters["channel_name"] = "private-my-channel"
                    authorizer.setHeaders(parameters)
                    pusher!!.unsubscribe(channelname)

                    channel = pusher.subscribePrivate("private-my-channel")

                }


            }

            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                Log.i("Pusher", "There was a problem connecting! code ($code), message ($message), exception($e)")
            }

        }, ConnectionState.ALL)


        iv_back!!.setOnClickListener {
            finish()
        }

        deleteview!!.setOnClickListener {

            deleteview!!.visibility = View.GONE

        }

        tv_deleteaccnt!!.setOnClickListener {


            deleteview!!.visibility = View.VISIBLE

        }


        tv_about!!.setOnClickListener {

            val uri: Uri = Uri.parse(HAULERS_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)


        }


        tv_yes!!.setOnClickListener {

            deleteAccountAPICall()

        }

        tv_no!!.setOnClickListener {

            deleteview!!.visibility = View.GONE

        }


/*
        tv_deleteaccnt!!.setOnClickListener {


            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(applicationContext.getString(R.string.delete))
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(
                "OK"
            ) { dialog, id ->

                deleteAccountAPICall()



            }

            alertDialog.setNegativeButton(
                "CANCEL"
            ) { dialog, id ->

                dialog.dismiss()

            }

            alertDialog.show()
        }
*/


        tv_logout!!.setOnClickListener {

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(applicationContext.getString(R.string.logout))

            //alertDialog.setIcon(R.drawable.icon);
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(
                "OK"
            ) { dialog, id ->

                //10jan
            //    sharedPreferenceManager!!.logoutUser()


                getLoggedOut()


            }

            alertDialog.setNegativeButton(
                "CANCEL"
            ) { dialog, id ->

               dialog.dismiss()

            }

            alertDialog.show()

        }

    }

    private fun getLoggedOut() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")) {

                req["id"] = defaultIdReg
                req["driver_status"] = "Logged out"

                responseDriverStatusSend = apiservice.doingdriver_status_send(tokenReg, req)
            }
            else {

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

                        if (response1.body()?.status == true) {
                            AppProgressBar.closeLoader()

                            val sharedPreferences =
                                getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            sharedPreferenceManager!!.logoutUser()
                            editor.clear()
                            editor.commit()


                            val myIntent =
                                Intent(this@SettingsActivity, WelcomeActivity::class.java)
                            startActivity(myIntent)

                            finish()

                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response1.body() != null) {

                                if (response1.body()!!.message.equals(
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
                                            this@SettingsActivity,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                } else {


                                    Toast.makeText(
                                        this@SettingsActivity,
                                        response1.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }
                    } else {

                        AppProgressBar.closeLoader()

                    }
                }

                override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@SettingsActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@SettingsActivity,
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

    private fun deleteAccountAPICall() {
        try {

             AppProgressBar.openLoader(
                 this,
                 this.getResources().getString(R.string.pleasewait)
             )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                responseDeleteAccount = apiservice.doingdeleteaccount(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                responseDeleteAccount = apiservice.doingdeleteaccount(token,req)

            }

            responseDeleteAccount!!.enqueue(object : Callback<VerifyOtpPojo?> {
                override fun onResponse(
                    call: Call<VerifyOtpPojo?>,
                    response: Response<VerifyOtpPojo?>
                ) {
                    if (response.isSuccessful()) {

                        AppProgressBar.closeLoader()

                        if (response.body()!=null && response.body()!!.status==true) {

                            AppProgressBar.closeLoader()

                            Toast.makeText(
                                this@SettingsActivity,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            val sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            sharedPreferenceManager!!.logoutUser()
                            editor.clear()
                            editor.commit()


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
                                            this@SettingsActivity,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                } else {


                                    Toast.makeText(
                                        this@SettingsActivity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }


                    } else {
                        if (response.body()!=null) {
                            AppProgressBar.closeLoader()
                            Toast.makeText(
                                this@SettingsActivity,
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
                            this@SettingsActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@SettingsActivity,
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
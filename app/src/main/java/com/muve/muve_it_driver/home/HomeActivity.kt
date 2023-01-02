package com.muve.muve_it_driver.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muve.muve_it_driver.*
import com.muve.muve_it_driver.EditProfile.EditProfileActivity
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.earning.EarningForCurrentPayPeriod
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.settings.SettingsActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.pusher.client.channel.PrivateChannel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : AppCompatActivity() {

///
    lateinit var channel: PrivateChannel
    var storeuserimage: String?=""
    var storeuserfirstname: String?=""
    var storeuserlastname: String?=""
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var storeuserphone: String?=""
    var storeuserphonecode: String?=""
    var storeuseremail: String?=""
    var storepin: String?=""
    var fnameSharedPrefAfterLog: String = ""
    var fnameSharedPrefAfterReg: String = ""
    var lnameSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var tokenReg: String = ""
    var status_codeFromPush: Int = 0
    var myEdit: SharedPreferences.Editor?=null
    var token: String = ""
    var ryt_home: RelativeLayout? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var response1: Call<UserDetailPojo>? = null
     var img_profileD: ImageView? = null
     var ryt_heading1: RelativeLayout? = null
     var ryt_help: RelativeLayout? = null
     var ryt_notification: RelativeLayout? = null
     var tv_version: TextView? = null
     var txt_name1: TextView? = null
     var txt_code: TextView? = null
     var ryt_invite_friends: RelativeLayout? = null
     var ryt_my_trip: RelativeLayout? = null
    var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_home)
        sharedPreferenceManager = SharedPreferenceManager(this)
        img_profileD = findViewById<CircleImageView>(R.id.img_profileD)
         ryt_heading1 = findViewById<RelativeLayout>(R.id.ryt_heading1)
         txt_name1 = findViewById<TextView>(R.id.txt_name1)
         ryt_invite_friends = findViewById<RelativeLayout>(R.id.ryt_invite_friends)
        ryt_my_trip = findViewById<RelativeLayout>(R.id.ryt_my_trip)
        ryt_home = findViewById<RelativeLayout>(R.id.ryt_home)
        txt_code = findViewById<TextView>(R.id.txt_code)
        ryt_help = findViewById<RelativeLayout>(R.id.ryt_help)
        ryt_notification = findViewById<RelativeLayout>(R.id.ryt_notification)
        tv_version = findViewById<TextView>(R.id.tv_version)

        val sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)

        try {

            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()



        }

        catch (e:Exception){
            e.printStackTrace()
        }

        if (defaultIdLog.equals("")){

            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            fnameSharedPrefAfterReg = sharedPreferences!!.getString("fnameSharedPref","").toString()
            lnameSharedPrefAfterReg = sharedPreferences!!.getString("lnameSharedPref","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()
            txt_name1!!.setText(fnameSharedPrefAfterReg +" "+ lnameSharedPrefAfterReg )

        }
        else{

            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            fnameSharedPrefAfterLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","").toString()
            lnameSharedPrefAfterLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","").toString()
            token = sharedPreferences!!.getString("Authorization","").toString()
            txt_name1!!.setText(fnameSharedPrefAfterLog +" "+ lnameSharedPrefAfterLog )

        }

        val pInfo: PackageInfo =
            getPackageManager().getPackageInfo(getPackageName(), 0)
        val versionName: String = pInfo.versionName

        tv_version!!.setText("Version : "+versionName)


        ryt_invite_friends!!.setOnClickListener {

            val myIntent = Intent(this, SettingsActivity::class.java)
            startActivity(myIntent)

        }

        // Notification

        ryt_my_trip!!.setOnClickListener {

            setSupportActionBar(toolbar)
            val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)

            navDrawer.closeDrawer(
                GravityCompat.START
            )

            val myIntent = Intent(this, NotificationActivity::class.java)
            startActivity(myIntent)

        }

        // Earnings
        ryt_notification!!.setOnClickListener {

            setSupportActionBar(toolbar)
            val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)

            navDrawer.closeDrawer(
                GravityCompat.START
            )

            val myIntent = Intent(this, EarningForCurrentPayPeriod::class.java)
            startActivity(myIntent)

        }


        ryt_heading1!!.setOnClickListener {


                setSupportActionBar(toolbar)
                val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)

                navDrawer.closeDrawer(
                    GravityCompat.START
                )

                val myIntent = Intent(this, EditProfileActivity::class.java)
                myIntent.putExtra("storeuserimage", storeuserimage)
                myIntent.putExtra("storeuserfirstname", storeuserfirstname)
                myIntent.putExtra("storeuserlastname", storeuserlastname)
                myIntent.putExtra("storeuserphone", storeuserphone)
                myIntent.putExtra("storeuserphonecode", storeuserphonecode)
                myIntent.putExtra("storeuseremail", storeuseremail)
                myIntent.putExtra("storepin", storepin)
                startActivity(myIntent)
            finish()

        }

        ryt_home!!.setOnClickListener {


            setSupportActionBar(toolbar)
            val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)

            navDrawer.closeDrawer(
                GravityCompat.START
            )

            val myIntent = Intent(this, ServiceCallHistoryListScreen::class.java)
            startActivity(myIntent)
           // finish()

        }


        ryt_help!!.setOnClickListener {


            setSupportActionBar(toolbar)
            val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)

            navDrawer.closeDrawer(
                GravityCompat.START
            )
            val myIntent = Intent(this, HelpScreen::class.java)
            startActivity(myIntent)
          //  finish()

        }




    }



    fun openCloseDrawer() {

         setSupportActionBar(toolbar)
         val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)
         if (!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START) else navDrawer.closeDrawer(
             GravityCompat.END
         )

    }





    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),
            drawer_layout
        )
    }


    private fun getUserDetails() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            if (defaultIdLog.equals("")){

                req["driver_id"] = defaultIdReg
                response1 = apiservice.getuserdetails(tokenReg,req )

            }

            else {
                req["driver_id"] = defaultIdLog
                response1 = apiservice.getuserdetails(token,req )

            }
            response1!!.enqueue(object : Callback<UserDetailPojo?>/*,
                RecyclerViewItemClickListenerFavPlace*/
            /*RecyclerViewItemClickListenerFavPlace*/ {
                override fun onResponse(
                    call: Call<UserDetailPojo?>,
                    response: Response<UserDetailPojo?>
                ) {
                    if (response.body()==null){
                        AppProgressBar.closeLoader()
                        return
                    }else if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            storeuserimage = response.body()!!.detail!!.driver_image
                            storeuserfirstname = response.body()!!.detail!!.firstname
                            storeuserlastname = response.body()!!.detail!!.lastname
                            storeuserphone = response.body()!!.detail!!.phoneNumber
                            storeuserphonecode = response.body()!!.detail!!.countryCode
                            storeuseremail = response.body()!!.detail!!.email
                            storepin = response.body()!!.detail!!.fixedPin

                            try {
                                if (!this@HomeActivity.isFinishing()) {

                                    Glide.with(this@HomeActivity)
                                        .load(NetworkUtility.imgbaseUrl + response.body()!!.detail!!.driver_image)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true).into(img_profileD!!)
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                            }

                            txt_name1!!.setText(response.body()!!.detail!!.firstname+" "+response.body()!!.detail!!.lastname)
                            txt_code!!.setText(storepin)
                            txt_ratings!!.setText(response.body()!!.detail!!.rating)

                        }

                        else {

                            AppProgressBar.closeLoader()

                            if (response.body()!!.message.equals(resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true)){

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(this@HomeActivity, WelcomeActivity::class.java)
                                startActivity(myIntent)

                                finish()

                            }

                            else{
                                AppProgressBar.closeLoader()


                                Toast.makeText(
                                    this@HomeActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }


                        }
                    } else {
                        AppProgressBar.closeLoader()

                    }
                }

                override fun onFailure(call: Call<UserDetailPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@HomeActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@HomeActivity,
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

    override fun onResume() {
        super.onResume()
        if (NetworkUtility.isNetworkAvailable(this)) {
            getUserDetails()
        }else{

            Toast.makeText(
                this,
                getString(R.string.msg_no_internet),
                Toast.LENGTH_LONG
            ).show()
        }
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
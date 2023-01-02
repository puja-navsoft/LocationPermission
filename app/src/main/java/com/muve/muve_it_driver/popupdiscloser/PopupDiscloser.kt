package com.muve.muve_it_driver.popupdiscloser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.ui.auth.permissionscreen.PermissionScreen

class PopupDiscloser : AppCompatActivity() {

    var tv_deny: TextView? =null
    var tv_accept: TextView? = null
    var isshowpopup: Boolean ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_popup_discloser)

        tv_deny = findViewById<TextView>(R.id.tv_deny)
        tv_accept = findViewById<EditText>(R.id.tv_accept)


        val sharedPreferences = getSharedPreferences("MySharedPref1", AppCompatActivity.MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        isshowpopup = sharedPreferences!!.getBoolean("isshowpopup",false)!!

        tv_accept!!.setOnClickListener {


            isshowpopup = true

            myEdit.putBoolean("isshowpopup",isshowpopup!!)
            myEdit.apply()

            val isshowpopup1 =  sharedPreferences.getBoolean("isshowpopup",false)
            Log.v("isshowpopup1","isshowpopup1"+isshowpopup1)

            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Location")

            if (intent.hasExtra("is_driverinformation_status") && intent.hasExtra("is_driver_vehicle_information_status")){
                myIntent.putExtra("is_driverinformation_status", intent.getBooleanExtra("is_driverinformation_status",false))
                myIntent.putExtra("is_driver_vehicle_information_status", intent.getBooleanExtra("is_driver_vehicle_information_status",false))
            }

            startActivity(myIntent)
            finish()



        }

        tv_deny!!.setOnClickListener {

            finishAffinity()
           // System.exit(0)

            isshowpopup = false

            myEdit.putBoolean("isshowpopup",isshowpopup!!)
            myEdit.apply()

        }
    }
}
package com.muve.muve_it_driver.drivercapability

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity

class DriverCapability : AppCompatActivity() {

    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_capability)

        var nextBt = findViewById<Button>(R.id.next)
        var checkedTextView = findViewById<CheckBox>(R.id.checkedTextView)
        var iv_back = findViewById<ImageView>(R.id.iv_back)

        iv_back.setOnClickListener {
            finish()
        }

        checkedTextView!!.setOnClickListener {

            if(checkedTextView!!.isChecked){
                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))

                nextBt.isEnabled=true
            }else{
                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))

                nextBt.isEnabled=false

            }
        }

        nextBt.setOnClickListener {

            sharedPreferences = getSharedPreferences("MySharedPref1", 0)
            myEdit = sharedPreferences!!.edit()

            myEdit!!.putBoolean("isDriverCapabilityVal", true)
            myEdit!!.commit()
            // myEdit!!.apply()

            // AppData.isDriverCapabilityVal = true

            val myIntent = Intent(this, WelcomeActivity::class.java)

            startActivity(myIntent)
            finish()
        }
    }
}
package com.muve.muve_it_driver.verifycustomerpickupcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.muve.muve_it_driver.R

class VerifyCustomerPickupCode : AppCompatActivity() {
    var iv_back: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_customer_pickup_screen)

        iv_back = findViewById<ImageView>(R.id.iv_back)

        iv_back!!.setOnClickListener {

            finish()
        }
    }
}
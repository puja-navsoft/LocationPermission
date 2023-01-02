package com.muve.muve_it_driver.cancelservicetoreturnhome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.home.HomeActivity
import java.text.DecimalFormat

class AfterCancelServiceScreen : AppCompatActivity() {

    var tv_returnToHome:Button? = null
    var tv_servicenumber:TextView? = null
    var tv_price:TextView? = null
    var imageView44:ImageView? = null
   // var tv_earnedmoney:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_cancel_service_screen)

        tv_returnToHome = findViewById<Button>(R.id.tv_returnToHome)
        tv_servicenumber = findViewById<TextView>(R.id.tv_servicenumber)
        tv_price = findViewById<TextView>(R.id.tv_price)
        imageView44 = findViewById<ImageView>(R.id.imageView44)
       // tv_earnedmoney =findViewById<TextView>(R.id.tv_earnedmoney)


        Log.v("service_idAfter" ,"service_id"+intent.getStringExtra("service_id")!!)
        Log.v("cancelchargesAfter" ,"cancelcharges"+intent.getDoubleExtra("cancelcharges",0.0))

        if ( intent.hasExtra("service_id") &&  intent.hasExtra("cancelcharges")) {

            if (intent.getDoubleExtra("cancelcharges",0.0)!!
                    .equals("0.0") || intent.getDoubleExtra("cancelcharges",0.0)!!.equals("0")
            ) {

                tv_servicenumber!!.setText("Service request #KIT- " + intent.getStringExtra("service_id") + " hasbeen cancelled.")
                tv_price!!.visibility = View.GONE
                imageView44!!.visibility = View.GONE

            } else {

                tv_servicenumber!!.setText(
                    "Service request #KIT- " + intent.getStringExtra("service_id") + " has been cancelled. You have been charged: " + DecimalFormat(
                        "$0.00"
                    ).format(
                        (intent.getDoubleExtra(
                            "cancelcharges",
                            0.0
                        ))
                    ) + " ( inclusive of all taxes )"
                )
                tv_price!!.setText(
                    DecimalFormat("$0.00").format(
                        (intent.getDoubleExtra(
                            "cancelcharges",
                            0.0
                        ))
                    )
                )
                tv_price!!.visibility = View.VISIBLE
                imageView44!!.visibility = View.VISIBLE
                // tv_earnedmoney!!.setText("cancelled: You have earned: "+DecimalFormat("$0.00").format((intent.getDoubleExtra("cancelcharges",0.0)))+"(inclusive of all taxes)")


            }
        }

        tv_returnToHome!!.setOnClickListener {

            val myIntent = Intent(this , HomeActivity::class.java)
            startActivity(myIntent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val myIntent = Intent(this , HomeActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}
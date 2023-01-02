package com.muve.muve_it_driver.completeservicecallsecondary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.home.HomeActivity
import java.text.DecimalFormat

class AfterCompleteSecondaryServiceScreen : AppCompatActivity() {

    var tv_returnToHome:Button? = null
    var tv_servicenumber:TextView? = null
    var tv_price:TextView? = null
    var tv_earnedmoney:TextView? = null
    var imageView44:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_service_call_screen)

        tv_returnToHome = findViewById<Button>(R.id.tv_returnToHome)
        tv_servicenumber = findViewById<TextView>(R.id.tv_servicenumber)
        tv_price = findViewById<TextView>(R.id.tv_price)
        imageView44 = findViewById<ImageView>(R.id.imageView44)
      //  tv_earnedmoney =findViewById<TextView>(R.id.tv_earnedmoney)

        if ( intent.hasExtra("service_id") ) {

            if (intent.getDoubleExtra("price"/*"cancelcharges"*/,0.0)!!
                    .equals(0.0) || intent.getDoubleExtra(/*"cancelcharges"*/"price",0.0)!!.equals(0)
            ) {

                tv_servicenumber!!.setText("Service request #KIT- "+intent.getStringExtra("service_id")+" hasbeen cancelled.")

                tv_price!!.visibility = View.GONE
                imageView44!!.visibility = View.GONE
            }

            else{

                tv_servicenumber!!.setText(
                    "Service request #KIT- " + intent.getStringExtra("service_id") + "Completed and Closed. You have been charged: " + DecimalFormat(
                        "$0.00"
                    ).format((intent.getDoubleExtra("price", 0.0))) + " ( inclusive of all taxes )"
                )
                tv_price!!.setText(
                    DecimalFormat("$0.00").format(
                        (intent.getDoubleExtra(
                            "price",
                            0.0
                        ))
                    )
                )

                tv_price!!.visibility = View.VISIBLE
                imageView44!!.visibility = View.VISIBLE

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
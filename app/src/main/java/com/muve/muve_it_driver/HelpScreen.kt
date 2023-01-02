package com.muve.muve_it_driver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.muve.muve_it_driver.contactus.ContactusScreen
import com.muve.muve_it_driver.featurerequest.ApplicationFeatureRequestScreen
import com.muve.muve_it_driver.reportproblem.ReportProblemScreen
import com.muve.muve_it_driver.serviceimprovementscreen.ServiceImprovementScreen
import com.muve.muve_it_driver.util.NetworkUtility.Companion.HAULERS_URL
import com.muve.muve_it_driver.util.NetworkUtility.Companion.TERMS_CONDITIONS_HAULERS_URL


class HelpScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_screen)

        val iv_back = findViewById<ImageView>(R.id.iv_back)
        val v_service = findViewById<View>(R.id.v_service)
        val v_feature = findViewById<View>(R.id.v_feature)
        val v_contact = findViewById<View>(R.id.v_contact)
        val textv_report = findViewById<View>(R.id.v_faq)
        val v_report = findViewById<View>(R.id.v_report)
        val tv_termcondition = findViewById<View>(R.id.v_tremcondition)

        textv_report.setOnClickListener {

            val uri: Uri = Uri.parse(HAULERS_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

          /*  val intent: Intent = Intent(
                this,
                WebViewScreen::class.java
            )
            intent.putExtra("driverhelp","driverhelp")

            startActivity(intent)*/
        }


        tv_termcondition.setOnClickListener {


            val uri: Uri = Uri.parse(TERMS_CONDITIONS_HAULERS_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)

          /*  val intent: Intent = Intent(
                this,
                WebViewScreenTermsCondition::class.java
            )
            startActivity(intent)*/
        }


        iv_back.setOnClickListener {

            /* val myIntent = Intent(this, HomeActivity::class.java)
             //  myIntent.putExtra("locationscreen","Camera")
             startActivity(myIntent)*/

            finish()
        }


        v_service.setOnClickListener {

          val myIntent = Intent(this, ServiceImprovementScreen::class.java)
             //  myIntent.putExtra("locationscreen","Camera")
             startActivity(myIntent)

           // finish()
        }


        v_report.setOnClickListener {

            val myIntent = Intent(this, ReportProblemScreen::class.java)
            //  myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)

           // finish()
        }


        v_feature.setOnClickListener {

            val myIntent = Intent(this, ApplicationFeatureRequestScreen::class.java)
            //  myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)

           // finish()
        }

        v_contact.setOnClickListener {

            val myIntent = Intent(this, ContactusScreen::class.java)
            //  myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)

          //  finish()
        }

    }
}
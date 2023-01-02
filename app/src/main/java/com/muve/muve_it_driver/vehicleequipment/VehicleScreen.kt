package com.muve.muve_it_driver.vehicleequipment

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.minimumreq.MinimumReqScreen

class VehicleScreen : AppCompatActivity() {
    var firstrow: Boolean = false
    var secondrow:Boolean = false
    var thirdrow:Boolean = false
    var fourthrow:Boolean = false
    var select1:Boolean = false



    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_screen)

        var nextBt = findViewById<Button>(R.id.next)
        var iv_yesYear = findViewById<ImageView>(R.id.iv_yesYear)
        var iv_noYear = findViewById<ImageView>(R.id.iv_noYear)
        var iv_yesDamage = findViewById<ImageView>(R.id.iv_yesDamage)
        var iv_noDamage = findViewById<ImageView>(R.id.iv_noDamage)
        var iv_yesCriminal = findViewById<ImageView>(R.id.iv_yesCriminal)
        var iv_noCriminal = findViewById<ImageView>(R.id.iv_noCriminal)
        var imageView = findViewById<ImageView>(R.id.imageView)
        var iv_noProtect = findViewById<ImageView>(R.id.iv_noProtect)
        var iv_info = findViewById<ImageView>(R.id.iv_info)
        var maintransView = findViewById<View>(R.id.maintransView)
        var tooltipView = findViewById<View>(R.id.tooltipView)
        var tooltipImg = findViewById<ImageView>(R.id.tooltipImg)
        var tooltipTxt = findViewById<TextView>(R.id.tooltipTxt)
        var iv_back = findViewById<ImageView>(R.id.iv_back)

        sharedPreferences = getSharedPreferences("MySharedPref1", 0)
        myEdit = sharedPreferences!!.edit()


        iv_back.setOnClickListener {
            finish()
        }

        maintransView.visibility=View.GONE
        tooltipView.visibility=View.GONE
        tooltipImg.visibility=View.GONE
        tooltipTxt.visibility=View.GONE

        maintransView.setOnClickListener {

            maintransView.visibility=View.GONE
            tooltipView.visibility=View.GONE
            tooltipImg.visibility=View.GONE
            tooltipTxt.visibility=View.GONE
        }

        iv_info.setOnClickListener {

            maintransView.visibility=View.VISIBLE
            tooltipView.visibility=View.VISIBLE
            tooltipImg.visibility=View.VISIBLE
            tooltipTxt.visibility=View.VISIBLE
        }


        iv_yesYear.setOnClickListener {

            firstrow=true

            iv_yesYear!!.setImageResource(R.drawable.yesclickorng)
            iv_noYear!!.setImageResource(R.drawable.nowhite)
           /* nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
            nextBt.isEnabled=true*/


            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }
        }

        iv_noYear.setOnClickListener {

            firstrow=false
            iv_yesYear!!.setImageResource(R.drawable.yeswhite)
            iv_noYear!!.setImageResource(R.drawable.noblack)
          /*  nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
            nextBt.isEnabled=false*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }
        }

        iv_yesDamage.setOnClickListener {

            secondrow=true
            iv_yesDamage!!.setImageResource(R.drawable.yesclickorng)
            iv_noDamage!!.setImageResource(R.drawable.nowhite)
         /*   nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
            nextBt.isEnabled=true*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }
        }

        iv_noDamage.setOnClickListener {

            secondrow=false
            iv_yesDamage!!.setImageResource(R.drawable.yeswhite)
            iv_noDamage!!.setImageResource(R.drawable.noblack)
          /*  nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
            nextBt.isEnabled=false*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }
        }

        iv_yesCriminal.setOnClickListener {

            thirdrow=true

            iv_yesCriminal!!.setImageResource(R.drawable.yesclickorng)
            iv_noCriminal!!.setImageResource(R.drawable.nowhite)
          /*  nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
            nextBt.isEnabled=true*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }

        }

        iv_noCriminal.setOnClickListener {

            thirdrow=false

            iv_yesCriminal!!.setImageResource(R.drawable.yeswhite)
            iv_noCriminal!!.setImageResource(R.drawable.noblack)
          /*  nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
            nextBt.isEnabled=false*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }
        }

        imageView.setOnClickListener {

            fourthrow=true
            imageView!!.setImageResource(R.drawable.yesclickorng)
            iv_noProtect!!.setImageResource(R.drawable.nowhite)
          /*  nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
            nextBt.isEnabled=true*/

            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }

        }

        iv_noProtect.setOnClickListener {

            fourthrow=false

            imageView!!.setImageResource(R.drawable.yeswhite)
            iv_noProtect!!.setImageResource(R.drawable.noblack)
           /* nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
            nextBt.isEnabled=false*/
            if (firstrow==false || secondrow == false || thirdrow == false || fourthrow == false){

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
                nextBt.isEnabled=false

            }
            else{

                nextBt!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
                nextBt.isEnabled=true
            }

        }




        nextBt.setOnClickListener {

            maintransView.visibility=View.GONE
            tooltipView.visibility=View.GONE
            tooltipImg.visibility=View.GONE
            tooltipTxt.visibility=View.GONE



            myEdit!!.putBoolean("isVehicleScreenVal", true)
            myEdit!!.commit()
           // myEdit!!.apply()


            val myIntent = Intent(this, MinimumReqScreen::class.java)

            startActivity(myIntent)
            finish()
        }

    }
}
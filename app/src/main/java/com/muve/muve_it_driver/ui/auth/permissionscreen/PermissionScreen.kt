package com.muve.muve_it_driver.ui.auth.permissionscreen

import android.Manifest
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.muve.muve_it_driver.Driverstatusstoreresponse
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.Util
import com.muve.muve_it_driver.bankdetailsscreen.BankDetailsScreen
import com.muve.muve_it_driver.driverevaluation.DriverEvalution
import com.muve.muve_it_driver.driverinformation.DriverInformationScreen
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.service.LocationService
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility.Companion.PRIVACY_URL
import com.muve.muve_it_driver.vehicleinformation.VehicleinfoScreen
import kotlinx.android.synthetic.main.activity_permission_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class PermissionScreen : AppCompatActivity() {

    var screening: String = "Location"
    var locationallow: Boolean? = false
    var cameraallow: Boolean? = false
    var storageallow: Boolean? = false
    var phoneallow: Boolean? = false

    var sharedPreferences: SharedPreferences? = null
    var sharedPreferences1: SharedPreferences? = null
    var myEdit: SharedPreferences.Editor? = null
    var mLocationService: LocationService = LocationService()
    lateinit var mServiceIntent: Intent
    var receiver: BroadcastReceiver? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var fnameSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterLog: String = ""
    var emailSharedPrefAfterLog: String = ""
    var emailSharedPrefAfterReg: String = ""
    var phoneSharedPrefAfterLog: String = ""
    var phoneSharedPrefAfterReg: String = ""
    var codeSharedPrefAfterReg: String = ""
    var codeSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var fnameSharedPrefAfterReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var username: String = ""
    var sharedPreferenceManager: SharedPreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_screen)


        val intent = intent

        sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences1 = getSharedPreferences("MySharedPref1", AppCompatActivity.MODE_PRIVATE)
        myEdit = sharedPreferences1!!.edit()

        if (intent.getStringExtra("locationscreen") != null)
            screening = intent.getStringExtra("locationscreen").toString()

        val tv_heading_txt: TextView = findViewById(R.id.tv_heading_txt)
        val tv_heading: TextView = findViewById(R.id.tv_heading)
        val btn_permission: Button = findViewById(R.id.btn_permission)
        val iv_img: ImageView = findViewById(R.id.iv_img)
        val iv_img_back: ImageView = findViewById(R.id.iv_img_back)
        val privacy_privacy: TextView = findViewById(R.id.privacy_privacy)

        try {
            sharedPreferenceManager = SharedPreferenceManager(this)

            sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            defaultIdReg =  sharedPreferences!!.getString("idSharedPref","")!!
            fnameSharedPrefAfterLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","").toString()
            lnameSharedPrefAfterLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","").toString()
            fnameSharedPrefAfterReg = sharedPreferences!!.getString("fnameSharedPref","").toString()
            lnameSharedPrefAfterReg = sharedPreferences!!.getString("lnameSharedPref","").toString()
            token = sharedPreferences!!.getString("Authorization","").toString()
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()
            emailSharedPrefAfterLog = sharedPreferences!!.getString("emailSharedPrefAfterLog","").toString()
            emailSharedPrefAfterReg = sharedPreferences!!.getString("emailSharedPref","").toString()
            phoneSharedPrefAfterLog = sharedPreferences!!.getString("phoneSharedPrefAfterLog","").toString()
            codeSharedPrefAfterLog = sharedPreferences!!.getString("codeSharedPrefAfterLog","").toString()
            phoneSharedPrefAfterReg = sharedPreferences!!.getString("phoneSharedPref","").toString()
            codeSharedPrefAfterReg = sharedPreferences!!.getString("codeSharedPref","").toString()


        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }

        iv_img_back.setOnClickListener {

            finish()
        }

        if (screening.equals("Location")) {

            tv_heading_txt.setText(getString(R.string.allow_location_txt))
            tv_heading.setText(getString(R.string.location_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.home))
            screening = "Location"


        } else if (screening.equals("Camera")) {

            tv_heading_txt.setText(getString(R.string.allow_camera_txt))
            tv_heading.setText(getString(R.string.camera_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.camera))
            screening = "Camera"

        } else if (screening.equals("Storage")) {

            tv_heading_txt.setText(getString(R.string.allow_storage_txt))
            tv_heading.setText(getString(R.string.storage_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.storage))
            screening = "Storage"
        } else if (screening.equals("Phone")) {

            tv_heading_txt.setText(getString(R.string.allow_phone_txt))
            tv_heading.setText(getString(R.string.phone_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.phone))
            screening = "Phone"

        } else {


        }

        privacy_privacy.setOnClickListener {

            val uri: Uri = Uri.parse(PRIVACY_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        btn_permission.setOnClickListener {

            if (screening.equals("Location")) {
                fetchLocation();
            } else if (screening.equals("Camera")) {

                fetchLocationForCamera();
            } else if (screening.equals("Storage")) {

                fetchLocationForStorage();
            } else if (screening.equals("Phone")) {


                fetchLocationForPhone();
            } else {


            }

        }

    }

    private fun fetchLocationForPhone() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CALL_PHONE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_ACCESS_PHONE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_ACCESS_PHONE
                )
            }
        } else {


           /* try {


                if (intent.hasExtra("is_driverinformation_status") && intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) == false
                ) {

                    val myIntent =
                        Intent(this, *//*HomeActivity*//* DriverInformationScreen::class.java)
                    startActivity(myIntent)
                    finish()
                } else if (intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) == false
                ) {
                    val myIntent = Intent(this, *//*HomeActivity*//* VehicleinfoScreen::class.java)
                    startActivity(myIntent)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == false
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view =
                        layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                    }
                    builder.show()

                } else if (sharedPreferences!!.getBoolean("document_verify_status", false) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals("No")
                    && sharedPreferences!!.getBoolean("attempt_status", false) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == true && sharedPreferences!!.getBoolean("attempt_status", false) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()
                } else if (sharedPreferences!!.getString("is_training", "")!!
                        .equals("No") && sharedPreferences!!.getBoolean(
                        "attempt_status",
                        false
                    ) == true
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view = layoutInflater.inflate(
                        R.layout.custom_layout_for_pending,
                        null
                    )
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt =
                        view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg =
                        view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                    }
                    builder.show()

                } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Pending",
                        ignoreCase = true
                    ) && sharedPreferences!!.getBoolean(
                        "is_driverinformation_status",
                        false
                    ) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals(
                        "Yes"
                    ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                    && sharedPreferences!!.getBoolean(
                        "is_driver_vehicle_information_status",
                        false
                    ) == true
                    && sharedPreferences!!.getBoolean(
                        "is_bank_status",
                        false
                    ) == false
                ) {

                   *//* val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.custom_layout_for_pending)
                    val btn_close = dialog.findViewById(R.id.btn_close) as Button

                    btn_close.setOnClickListener {
                        dialog.dismiss()
                        finishAffinity()
                        //   System.exit(0)
                    }

                    dialog.show()*//*

                    val myIntent_ =
                        Intent(this, BankDetailsScreen::class.java)
                    startActivity(myIntent_)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "is_bank_status",
                        false
                    ) == false && sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Active",
                        ignoreCase = true
                    )
                ) {

                    val myIntent_ =
                        Intent(this, BankDetailsScreen::class.java)
                    startActivity(myIntent_)
                    finish()
                } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Active",
                        ignoreCase = true
                    )
                ) {

                    val myIntent_ =
                        Intent(this, HomeActivity::class.java)
                    myIntent_.putExtra(
                        "availability",
                        sharedPreferences!!.getString("availability", "")
                    )
                    startActivity(myIntent_)
                    finish()

                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
*/


            try {


                if (intent.hasExtra("is_driverinformation_status") && intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) == false
                ) {

                    val myIntent =
                        Intent(this, /*HomeActivity*/ DriverInformationScreen::class.java)
                    startActivity(myIntent)
                    finish()
                } else if (intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) == false
                ) {
                    val myIntent = Intent(this, /*HomeActivity*/ VehicleinfoScreen::class.java)
                    startActivity(myIntent)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == false
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view =
                        layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                    }
                    builder.show()

                } else if (sharedPreferences!!.getBoolean("document_verify_status", false) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals("No")
                    && sharedPreferences!!.getBoolean("attempt_status", false) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == true && sharedPreferences!!.getBoolean("attempt_status", false) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()
                } else if (sharedPreferences!!.getString("is_training", "")!!
                        .equals("No") && sharedPreferences!!.getBoolean(
                        "attempt_status",
                        false
                    ) == true
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view = layoutInflater.inflate(
                        R.layout.custom_layout_for_pending,
                        null
                    )
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt =
                        view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg =
                        view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                    }
                    builder.show()

                } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Pending",
                        ignoreCase = true
                    ) && sharedPreferences!!.getBoolean(
                        "is_driverinformation_status",
                        false
                    ) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals(
                        "Yes"
                    ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                    && sharedPreferences!!.getBoolean(
                        "is_driver_vehicle_information_status",
                        false
                    ) == true
                    && sharedPreferences!!.getBoolean(
                        "is_bank_status",
                        false
                    ) == false
                ) {

                    /* val dialog = Dialog(this)
                     dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                     dialog.setCancelable(false)
                     dialog.setContentView(R.layout.custom_layout_for_pending)
                     val btn_close = dialog.findViewById(R.id.btn_close) as Button
                     btn_close.setOnClickListener {
                         dialog.dismiss()
                         finishAffinity()
                         //   System.exit(0)
                     }
                     dialog.show()*/

                    val myIntent_ =
                        Intent(this, BankDetailsScreen::class.java)
                    startActivity(myIntent_)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "is_bank_status",
                        false
                    ) == false && sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Active",
                        ignoreCase = true
                    )
                ) {

                    val myIntent_ =
                        Intent(this, BankDetailsScreen::class.java)
                    startActivity(myIntent_)
                    finish()
                } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Active",
                        ignoreCase = true
                    )
                ) {

                    val myIntent_ =
                        Intent(this, HomeActivity::class.java)
                    myIntent_.putExtra(
                        "availability",
                        sharedPreferences!!.getString("availability", "")
                    )
                    startActivity(myIntent_)
                    finish()

                }

            } catch (e: Exception) {

                e.printStackTrace()
            }

        }

    }

    private fun fetchLocationForStorage() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_ACCESS_STORAGE
                )


            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_ACCESS_STORAGE
                )

            }
        } else {

            fetchLocationForPhone()

        }


    }

    private fun fetchLocationForCamera() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {

                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA
                )

            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA
                )

            }
        } else {

            fetchLocationForStorage()

        }


    }

    private fun fetchLocation() {


        /*  if (ContextCompat.checkSelfPermission(
                  this,
                  Manifest.permission.ACCESS_COARSE_LOCATION
              )
              != PackageManager.PERMISSION_GRANTED
          ) {

              if (ActivityCompat.shouldShowRequestPermissionRationale(
                      this,
                      Manifest.permission.ACCESS_COARSE_LOCATION
                  )
              ) {

                  ActivityCompat.requestPermissions(
                      this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                      MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                  )

              }

              else {
                  // No explanation needed; request the permission
                  ActivityCompat.requestPermissions(
                      this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                      MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION
                  )

              }
          }

          else {

              fetchLocationwithoutClick()



          }
  */
        // *****----------- background service location permission  ------------

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {

                    AlertDialog.Builder(this).apply {
                        setTitle("Background permission")
                        setMessage(R.string.background_location_permission_message)
                        /*  setPositiveButton("Start service anyway",
                              DialogInterface.OnClickListener { dialog, id ->

                                  fetchLocationwithoutClick()
                              })*/
                        setNegativeButton("Grant background Permission",
                            DialogInterface.OnClickListener { dialog, id ->
                                requestBackgroundLocationPermission()
                            })
                    }.create().show()

                } else if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    fetchLocationwithoutClick()

                }
            } else {

                fetchLocationwithoutClick()
            }

        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                requestFineLocationPermission()
                /*AlertDialog.Builder(this)
                    .setTitle("ACCESS_FINE_LOCATION")
                    .setMessage("Location permission required")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestFineLocationPermission()
                    }
                    .create()
                    .show()*/
            } else {
                requestFineLocationPermission()
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            /*MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION*/ MY_FINE_LOCATION_REQUEST -> {
                if (/*grantResults.size > 0 &&*/ grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationallow = false

                    myEdit!!.putBoolean("locationallow", locationallow!!)
                    myEdit!!.apply()

                  //  fetchLocationwithoutClick()
                    Log.v("locationallow", "accept")

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {

                        AlertDialog.Builder(this).apply {
                            setTitle("Background permission")
                            setMessage(R.string.background_location_permission_message)
                            /*  setPositiveButton("Start service anyway",
                                  DialogInterface.OnClickListener { dialog, id ->

                                      fetchLocationwithoutClick()
                                  })*/
                            setNegativeButton("Grant background Permission",
                                DialogInterface.OnClickListener { dialog, id ->
                                    requestBackgroundLocationPermission()
                                })
                        }.create().show()

                    }

                } else {

                    locationallow = false

                    myEdit!!.putBoolean("locationallow", locationallow!!)
                    myEdit!!.apply()

                    Log.v("locationpermission", "deny")

                    Toast.makeText(
                        this,
                        "ACCESS_FINE_LOCATION permission denied",
                        Toast.LENGTH_LONG
                    ).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }

                }
                return
            }
            MY_BACKGROUND_LOCATION_REQUEST -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        locationallow = true

                        myEdit!!.putBoolean("locationallow", locationallow!!)
                        myEdit!!.apply()

                        Toast.makeText(
                            this,
                            "Background location Permission Granted",
                            Toast.LENGTH_LONG
                        ).show()
                        fetchLocationwithoutClick()
                    }
                } else {
                    Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG)
                        .show()
                    locationallow = false

                    myEdit!!.putBoolean("locationallow", locationallow!!)
                    myEdit!!.apply()
                }
                return
            }
            MY_PERMISSIONS_REQUEST_ACCESS_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraallow = true
                    myEdit!!.putBoolean("cameraallow", cameraallow!!)
                    myEdit!!.apply()

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        fetchLocationForStoragewithoutClick()
                    }

                } else {

                    cameraallow = false
                    myEdit!!.putBoolean("cameraallow", cameraallow!!)
                    myEdit!!.apply()

                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return

            }
            MY_PERMISSIONS_REQUEST_ACCESS_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storageallow = true
                    myEdit!!.putBoolean("storageallow", storageallow!!)
                    myEdit!!.apply()
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        fetchLocationForPhonewithoutClick()
                    }
                } else {
                    storageallow = false
                    myEdit!!.putBoolean("storageallow", storageallow!!)
                    myEdit!!.apply()

                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
           /* MY_PERMISSIONS_REQUEST_ACCESS_PHONE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //after allow
                        phoneallow = true
                        myEdit!!.putBoolean("phoneallow", phoneallow!!)
                        myEdit!!.apply()

                        try {
                            if (intent.getBooleanExtra(
                                    "is_driverinformation_status",
                                    false
                                ) != null && intent.getBooleanExtra(
                                    "is_driverinformation_status",
                                    false
                                ) == false
                            ) {

                                val myIntent =
                                    Intent(
                                        this, *//*HomeActivity*//*
                                        DriverInformationScreen::class.java
                                    )
                                startActivity(myIntent)
                                finish()
                            } else if (intent.getBooleanExtra(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) != null && intent.getBooleanExtra(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) == false
                            ) {
                                val myIntent =
                                    Intent(this, *//*HomeActivity*//* VehicleinfoScreen::class.java)
                                startActivity(myIntent)
                                finish()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == false
                            ) {

                                val builder =
                                    AlertDialog.Builder(this)
                                        .create()
                                val view =
                                    layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                                // val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
                                val btn_close = view.findViewById(R.id.btn_close) as Button
                                val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                                val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                                builder.setView(view)

                                builder.setCanceledOnTouchOutside(false)
                                tv_headettxt.setText(getString(R.string.documents_verification))
                                tv_entiremsg.setText(getString(R.string.complete_document_txt))

                                btn_close.setOnClickListener {
                                    builder.dismiss()
                                    finishAffinity()
                                    // System.exit(0)
                                }
                                builder.show()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == true
                                && sharedPreferences!!.getString("is_training", "")!!.equals("No")
                                && sharedPreferences!!.getBoolean("attempt_status", false) == false
                            ) {

                                val myIntent_ =
                                    Intent(this, DriverEvalution::class.java)
                                startActivity(myIntent_)
                                finish()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == true && sharedPreferences!!.getBoolean(
                                    "attempt_status",
                                    false
                                ) == false
                            ) {

                                val myIntent_ =
                                    Intent(this, DriverEvalution::class.java)
                                startActivity(myIntent_)
                                finish()
                            } else if (sharedPreferences!!.getString("is_training", "")!!
                                    .equals("No") && sharedPreferences!!.getBoolean(
                                    "attempt_status",
                                    false
                                ) == true
                            ) {

                                val builder =
                                    AlertDialog.Builder(this)
                                        .create()
                                val view = layoutInflater.inflate(
                                    R.layout.custom_layout_for_pending,
                                    null
                                )
                                val btn_close = view.findViewById(R.id.btn_close) as Button
                                val tv_headettxt =
                                    view.findViewById(R.id.tv_headettxt) as TextView
                                val tv_entiremsg =
                                    view.findViewById(R.id.tv_entiremsg) as TextView

                                builder.setView(view)

                                builder.setCanceledOnTouchOutside(false)
                                tv_headettxt.setText(getString(R.string.documents_verification))
                                tv_entiremsg.setText(getString(R.string.complete_document_txt))

                                btn_close.setOnClickListener {
                                    builder.dismiss()
                                    finishAffinity()
                                    //   System.exit(0)
                                }
                                builder.show()

                            } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                                    "Pending",
                                    ignoreCase = true
                                ) && sharedPreferences!!.getBoolean(
                                    "is_driverinformation_status",
                                    false
                                ) == true
                                && sharedPreferences!!.getString("is_training", "")!!.equals(
                                    "Yes"
                                ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                                && sharedPreferences!!.getBoolean(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) == true
                            ) {

                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.custom_layout_for_pending)
                                val btn_close = dialog.findViewById(R.id.btn_close) as Button

                                btn_close.setOnClickListener {
                                    dialog.dismiss()
                                    finishAffinity()
                                    //    System.exit(0)
                                }

                                dialog.show()

                            } else if (sharedPreferences!!.getBoolean(
                                    "is_bank_status",
                                    false
                                ) == false && sharedPreferences!!.getString("account_Status", "")!!
                                    .equals(
                                        "Active",
                                        ignoreCase = true
                                    )
                            ) {

                                val myIntent_ =
                                    Intent(this, BankDetailsScreen::class.java)
                                startActivity(myIntent_)
                                finish()
                            } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                                    "Active",
                                    ignoreCase = true
                                )
                            ) {

                                val myIntent_ =
                                    Intent(this, HomeActivity::class.java)
                                myIntent_.putExtra(
                                    "availability",
                                    sharedPreferences!!.getString("availability", "")
                                )
                                startActivity(myIntent_)
                                finish()

                            } else {
                                val myIntent =
                                    Intent(
                                        this,
                                        HomeActivity *//*DriverInformationScreen*//*::class.java
                                    )
                                startActivity(myIntent)
                                finish()
                            }
                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }

                }
                else {
                    phoneallow = false
                    myEdit!!.putBoolean("phoneallow", phoneallow!!)
                    myEdit!!.apply()

                    Toast.makeText(this, "Phone permission denied", Toast.LENGTH_LONG).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null),),)
                    }
                }
                return
            }*/


            MY_PERMISSIONS_REQUEST_ACCESS_PHONE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //after allow
                        phoneallow = true
                        myEdit!!.putBoolean("phoneallow", phoneallow!!)
                        myEdit!!.apply()

                        try {
                            if (intent.getBooleanExtra(
                                    "is_driverinformation_status",
                                    false
                                ) != null && intent.getBooleanExtra(
                                    "is_driverinformation_status",
                                    false
                                ) == false
                            ) {

                                val myIntent =
                                    Intent(
                                        this, /*HomeActivity*/
                                        DriverInformationScreen::class.java
                                    )
                                startActivity(myIntent)
                                finish()
                            } else if (intent.getBooleanExtra(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) != null && intent.getBooleanExtra(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) == false
                            ) {
                                val myIntent =
                                    Intent(this, /*HomeActivity*/ VehicleinfoScreen::class.java)
                                startActivity(myIntent)
                                finish()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == false
                            ) {

                                val builder =
                                    AlertDialog.Builder(this)
                                        .create()
                                val view =
                                    layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                                // val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
                                val btn_close = view.findViewById(R.id.btn_close) as Button
                                val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                                val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                                builder.setView(view)

                                builder.setCanceledOnTouchOutside(false)
                                tv_headettxt.setText(getString(R.string.documents_verification))
                                tv_entiremsg.setText(getString(R.string.complete_document_txt))

                                btn_close.setOnClickListener {
                                    builder.dismiss()
                                    finishAffinity()
                                    // System.exit(0)
                                }
                                builder.show()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == true
                                && sharedPreferences!!.getString("is_training", "")!!.equals("No")
                                && sharedPreferences!!.getBoolean("attempt_status", false) == false
                            ) {

                                val myIntent_ =
                                    Intent(this, DriverEvalution::class.java)
                                startActivity(myIntent_)
                                finish()

                            } else if (sharedPreferences!!.getBoolean(
                                    "document_verify_status",
                                    false
                                ) == true && sharedPreferences!!.getBoolean(
                                    "attempt_status",
                                    false
                                ) == false
                            ) {

                                val myIntent_ =
                                    Intent(this, DriverEvalution::class.java)
                                startActivity(myIntent_)
                                finish()
                            } else if (sharedPreferences!!.getString("is_training", "")!!
                                    .equals("No") && sharedPreferences!!.getBoolean(
                                    "attempt_status",
                                    false
                                ) == true
                            ) {

                                val builder =
                                    AlertDialog.Builder(this)
                                        .create()
                                val view = layoutInflater.inflate(
                                    R.layout.custom_layout_for_pending,
                                    null
                                )
                                val btn_close = view.findViewById(R.id.btn_close) as Button
                                val tv_headettxt =
                                    view.findViewById(R.id.tv_headettxt) as TextView
                                val tv_entiremsg =
                                    view.findViewById(R.id.tv_entiremsg) as TextView

                                builder.setView(view)

                                builder.setCanceledOnTouchOutside(false)
                                tv_headettxt.setText(getString(R.string.documents_verification))
                                tv_entiremsg.setText(getString(R.string.complete_document_txt))

                                btn_close.setOnClickListener {
                                    builder.dismiss()
                                    finishAffinity()
                                    //   System.exit(0)
                                }
                                builder.show()

                            } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                                    "Pending",
                                    ignoreCase = true
                                ) && sharedPreferences!!.getBoolean(
                                    "is_driverinformation_status",
                                    false
                                ) == true
                                && sharedPreferences!!.getString("is_training", "")!!.equals(
                                    "Yes"
                                ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                                && sharedPreferences!!.getBoolean(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) == true
                            ) {

                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.custom_layout_for_pending)
                                val btn_close = dialog.findViewById(R.id.btn_close) as Button

                                btn_close.setOnClickListener {
                                    dialog.dismiss()
                                    finishAffinity()
                                    //    System.exit(0)
                                }

                                dialog.show()

                            } else if (sharedPreferences!!.getBoolean(
                                    "is_bank_status",
                                    false
                                ) == false && sharedPreferences!!.getString("account_Status", "")!!
                                    .equals(
                                        "Pending",
                                        ignoreCase = true
                                    )
                            ) {

                                val myIntent_ =
                                    Intent(this, BankDetailsScreen::class.java)
                                startActivity(myIntent_)
                                finish()
                            }
                            else if (sharedPreferences!!.getString("account_Status", "")!!.equals("Pending", ignoreCase = true) && sharedPreferences!!.getBoolean(
                                    "is_driverinformation_status",
                                    false
                                ) == true
                                && sharedPreferences!!.getString("is_training", "")!!.equals(
                                    "Yes"
                                ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                                && sharedPreferences!!.getBoolean(
                                    "is_driver_vehicle_information_status",
                                    false
                                ) == true && sharedPreferences!!.getBoolean(
                                    "is_bank_status", false) == true
                            )
                            {
                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.custom_layout_for_pending)
                                val btn_close = dialog.findViewById(R.id.btn_close) as Button

                                btn_close.setOnClickListener {
                                    dialog.dismiss()
                                    finishAffinity()
                                    //    System.exit(0)
                                }

                                dialog.show()
                            }
                            else if (sharedPreferences!!.getString("account_Status", "")!!.equals("Active", ignoreCase = true))
                            {

                                val myIntent_ =
                                    Intent(this, HomeActivity::class.java)
                                myIntent_.putExtra(
                                    "availability",
                                    sharedPreferences!!.getString("availability", "")
                                )
                                startActivity(myIntent_)
                                finish()

                            }

                        }
                        catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }

                }
                else {
                    phoneallow = false
                    myEdit!!.putBoolean("phoneallow", phoneallow!!)
                    myEdit!!.apply()

                    Toast.makeText(this, "Phone permission denied", Toast.LENGTH_LONG).show()
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null),),)
                    }
                }
                return
            }
        }
    }



    private fun fetchLocationForPhonewithoutClick() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            tv_heading_txt.setText(getString(R.string.allow_phone_txt))
            tv_heading.setText(getString(R.string.phone_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.phone))
            screening = "Phone"

        } else {

            try {
                if (intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driverinformation_status",
                        false
                    ) == false
                ) {

                    val myIntent =
                        Intent(
                            this, /*HomeActivity*/
                            DriverInformationScreen::class.java
                        )
                    startActivity(myIntent)
                    finish()
                } else if (intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) != null && intent.getBooleanExtra(
                        "is_driver_vehicle_information_status",
                        false
                    ) == false
                ) {
                    val myIntent =
                        Intent(this, /*HomeActivity*/ VehicleinfoScreen::class.java)
                    startActivity(myIntent)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == false
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view =
                        layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                    // val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                        // System.exit(0)
                    }
                    builder.show()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals("No")
                    && sharedPreferences!!.getBoolean("attempt_status", false) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()

                } else if (sharedPreferences!!.getBoolean(
                        "document_verify_status",
                        false
                    ) == true && sharedPreferences!!.getBoolean(
                        "attempt_status",
                        false
                    ) == false
                ) {

                    val myIntent_ =
                        Intent(this, DriverEvalution::class.java)
                    startActivity(myIntent_)
                    finish()
                } else if (sharedPreferences!!.getString("is_training", "")!!
                        .equals("No") && sharedPreferences!!.getBoolean(
                        "attempt_status",
                        false
                    ) == true
                ) {

                    val builder =
                        AlertDialog.Builder(this)
                            .create()
                    val view = layoutInflater.inflate(
                        R.layout.custom_layout_for_pending,
                        null
                    )
                    val btn_close = view.findViewById(R.id.btn_close) as Button
                    val tv_headettxt =
                        view.findViewById(R.id.tv_headettxt) as TextView
                    val tv_entiremsg =
                        view.findViewById(R.id.tv_entiremsg) as TextView

                    builder.setView(view)

                    builder.setCanceledOnTouchOutside(false)
                    tv_headettxt.setText(getString(R.string.documents_verification))
                    tv_entiremsg.setText(getString(R.string.complete_document_txt))

                    btn_close.setOnClickListener {
                        builder.dismiss()
                        finishAffinity()
                        //   System.exit(0)
                    }
                    builder.show()

                } else if (sharedPreferences!!.getString("account_Status", "")!!.equals(
                        "Pending",
                        ignoreCase = true
                    ) && sharedPreferences!!.getBoolean(
                        "is_driverinformation_status",
                        false
                    ) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals(
                        "Yes"
                    ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                    && sharedPreferences!!.getBoolean(
                        "is_driver_vehicle_information_status",
                        false
                    ) == true
                ) {

                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.custom_layout_for_pending)
                    val btn_close = dialog.findViewById(R.id.btn_close) as Button

                    btn_close.setOnClickListener {
                        dialog.dismiss()
                        finishAffinity()
                        //    System.exit(0)
                    }

                    dialog.show()

                } else if (sharedPreferences!!.getBoolean(
                        "is_bank_status",
                        false
                    ) == false && sharedPreferences!!.getString("account_Status", "")!!
                        .equals(
                            "Pending",
                            ignoreCase = true
                        )
                ) {

                    val myIntent_ =
                        Intent(this, BankDetailsScreen::class.java)
                    startActivity(myIntent_)
                    finish()
                }
                else if (sharedPreferences!!.getString("account_Status", "")!!.equals("Pending", ignoreCase = true) && sharedPreferences!!.getBoolean(
                        "is_driverinformation_status",
                        false
                    ) == true
                    && sharedPreferences!!.getString("is_training", "")!!.equals(
                        "Yes"
                    ) && sharedPreferences!!.getBoolean("attempt_status", false) == true
                    && sharedPreferences!!.getBoolean(
                        "is_driver_vehicle_information_status",
                        false
                    ) == true && sharedPreferences!!.getBoolean(
                        "is_bank_status", false) == true
                )
                {
                    val dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.custom_layout_for_pending)
                    val btn_close = dialog.findViewById(R.id.btn_close) as Button

                    btn_close.setOnClickListener {
                        dialog.dismiss()
                        finishAffinity()
                        //    System.exit(0)
                    }

                    dialog.show()
                }
                else if (sharedPreferences!!.getString("account_Status", "")!!.equals("Active", ignoreCase = true))
                {

                    val myIntent_ =
                        Intent(this, HomeActivity::class.java)
                    myIntent_.putExtra(
                        "availability",
                        sharedPreferences!!.getString("availability", "")
                    )
                    startActivity(myIntent_)
                    finish()

                }

            }
            catch (e: Exception) {

                e.printStackTrace()
            }

          /*  val myIntent = Intent(this, DriverInformationScreen::class.java)
            startActivity(myIntent)
            finish()*/

        }

    }

    private fun fetchLocationForStoragewithoutClick() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            tv_heading_txt.setText(getString(R.string.allow_storage_txt))
            tv_heading.setText(getString(R.string.storage_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.storage))
            screening = "Storage"

        } else {

            fetchLocationForPhonewithoutClick()

        }


    }

    private fun fetchLocationwithoutClick() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            tv_heading_txt.setText(getString(R.string.allow_camera_txt))
            tv_heading.setText(getString(R.string.camera_txt))
            iv_img.setImageDrawable(getDrawable(R.drawable.camera))
            screening = "Camera"

        } else {

            fetchLocationForStoragewithoutClick()

        }

    }

    private fun requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), MY_BACKGROUND_LOCATION_REQUEST
        )
    }

    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_FINE_LOCATION_REQUEST
        )
    }
    private fun starServiceFunc() {
        mLocationService = LocationService()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (!Util.isMyServiceRunning(mLocationService.javaClass, this)) {
            startService(mServiceIntent)

            Toast.makeText(this, getString(R.string.service_start_successfully), Toast.LENGTH_SHORT)
                .show()

            fetchLocationwithoutClick()

            // fetchLocationwithoutClick()
        } else {
            Toast.makeText(this, getString(R.string.service_already_running), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun stopServiceFunc() {
        mLocationService = LocationService()
        mServiceIntent = Intent(this, mLocationService.javaClass)
        if (Util.isMyServiceRunning(mLocationService.javaClass, this)) {
            stopService(mServiceIntent)
            Toast.makeText(this, "Service stopped!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Service is already stopped!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLoggedOut() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = java.util.HashMap<String, Any>()


            if (defaultIdLog.equals("")) {

                req["id"] = defaultIdReg
                req["driver_status"] = "Logged out"

                responseDriverStatusSend = apiservice.doingdriver_status_send(tokenReg, req)
            } else {

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

                        AppProgressBar.closeLoader()

                        val sharedPreferences = getSharedPreferences(
                            "MySharedPref",
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences.edit()
                        sharedPreferenceManager!!.logoutUser()
                        editor.clear()
                        editor.commit()


                        val myIntent = Intent(this@PermissionScreen, WelcomeActivity::class.java)
                        startActivity(myIntent)
                        finish()


                    } else {

                        AppProgressBar.closeLoader()

                        if (response1.body() != null) {

                            if (response1.body()!!.message.equals(
                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    ignoreCase = true
                                )
                            ) {
                                val sharedPreferences =
                                    getSharedPreferences(
                                        "MySharedPref",
                                        Context.MODE_PRIVATE
                                    )
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(
                                        this@PermissionScreen,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()


                            } else {

                                Toast.makeText(this@PermissionScreen, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()



                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if (t is NoConnectivityException) {
                        Toast.makeText(this@PermissionScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@PermissionScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    companion object {
       // private const val MY_FINE_LOCATION_REQUEST = 99
        private const val MY_BACKGROUND_LOCATION_REQUEST = 100

      //  private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: Int = 1
        private val MY_FINE_LOCATION_REQUEST: Int = 1
        private val MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: Int = 2
        private val MY_PERMISSIONS_REQUEST_ACCESS_STORAGE: Int = 3
        private val MY_PERMISSIONS_REQUEST_ACCESS_PHONE: Int = 4

    }

    override fun onResume() {
        super.onResume()


        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                try {

                    if (intent.getIntExtra(
                            "status_codeFromPushMultipleDeviceLogin",
                            0
                        ) == 204
                    ) {

                        Toast.makeText(this@PermissionScreen, "Your Account is Logged In from another Device", Toast.LENGTH_SHORT).show()

                        getLoggedOut()


                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver!!),
            IntentFilter("com.muve.muve_it_driver")
        )
    }
}
package com.muve.muve_it_driver.cancelservicecallscreen

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.muve.muve_it.CancelResponse
import com.muve.muve_it_driver.ImagePickerActivity
import com.muve.muve_it_driver.ImageUploadResponse
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.cancelservicetoreturnhome.AfterCancelServiceScreen
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muve.muve_it_driver.util.NetworkUtility
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kotlinx.android.synthetic.main.activity_cancel_service_call_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.HashMap

class CancelServiceCallScreen : AppCompatActivity() {
     var iv_back : ImageView ? = null
     var iv_infomore : ImageView ? = null
     var tooltipView : View ? = null
     var tv_cancellCall : TextView ? = null
     var tooltipTxt : TextView ? = null
     var maintransViewCancelServiceCall : View? = null
     var tooltipImg : ImageView? = null
     var vehicle1 : ImageView? = null
     var vehicle2 : ImageView? = null
     var restricted1 : ImageView? = null
     var restricted2 : ImageView? = null
    var defaultIdReg: String = ""
    var defaultIdLog: String = ""
    var fnameSharedPrefAfterLog: String = ""
    var fnameSharedPrefAfterReg: String = ""
    var lnameSharedPrefAfterLog: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var check_loadCargo: CheckBox ? =null
    var check_needservice: CheckBox ? =null
    var checkother: CheckBox ? =null
    var sharedPreferences: SharedPreferences?=null
    var check_inappropriatebehave: CheckBox?=null
    var checkBox: CheckBox?=null
    var checkBox2: CheckBox?=null
    var et_other: EditText?=null
    var check_loadCargoVal: Boolean=false
    var vehicleImg1: Boolean=false
    var vehicleImg2: Boolean=false
    var restricImg1: Boolean=false
    var restricImg2: Boolean=false
    var vehicle_part: Boolean=false
    var restricted_part: Boolean=false
    var check_inappropriatebehaveVal: Boolean=false
    var check_needserviceVal: Boolean=false
    var check_checkotherVal: Boolean=false
    var responseServicecallcancel: Call<CancelResponse>? = null
    var REQUEST_IMAGE = 100
    var encodedImage: String=""
    var encodedImage1: String=""
    var encodedImage2: String=""
    var encodedImage3: String=""
    var storeImage: String=""
    var storeImage1: String=""
    var storeImage2: String=""
    var storeImage3: String=""
    var ImageUrl: Uri? = null
    var responseImageUpload: Call<ImageUploadResponse>? = null
    var pd_loadervehicle1: ProgressBar? = null
    var pd_loadervehicle2: ProgressBar? = null
    var pd_loaderRestricted1: ProgressBar? = null
    var pd_loaderRestricted2: ProgressBar? = null
    var channel: PrivateChannel ? = null
    var driver_updatestatus: String? = "Cancelled"
    var pusher :Pusher ? =null
    var authorizer = HttpAuthorizer(NetworkUtility.authUrl)
    var options = PusherOptions().setAuthorizer(authorizer)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_service_call_screen)

        iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_infomore = findViewById<ImageView>(R.id.iv_infomore)
        tv_cancellCall = findViewById<TextView>(R.id.tv_cancellCall)
        maintransViewCancelServiceCall = findViewById<View>(R.id.maintransViewCancelServiceCall)
        tooltipImg = findViewById<ImageView>(R.id.tooltipImg)
        vehicle1 = findViewById<ImageView>(R.id.vehicle1)
        tooltipView = findViewById<View>(R.id.tooltipView)
        vehicle2 = findViewById<ImageView>(R.id.vehicle2)
        restricted1 = findViewById<ImageView>(R.id.restricted1)
        restricted2 = findViewById<ImageView>(R.id.restricted2)
        tooltipTxt = findViewById<TextView>(R.id.tooltipTxt)
        checkBox = findViewById<CheckBox>(R.id.checkBox)
        check_loadCargo = findViewById<CheckBox>(R.id.check_loadCargo)
        check_needservice = findViewById<CheckBox>(R.id.check_needservice)
        checkBox2 = findViewById<CheckBox>(R.id.checkBox2)
        check_inappropriatebehave = findViewById<CheckBox>(R.id.check_inappropriatebehave)
        pd_loadervehicle1 =findViewById<ProgressBar>(R.id.pd_loadervehicle1)
        pd_loadervehicle2 =findViewById<ProgressBar>(R.id.pd_loadervehicle2)
        pd_loaderRestricted2 =findViewById<ProgressBar>(R.id.pd_loaderRestricted2)
        pd_loaderRestricted1 =findViewById<ProgressBar>(R.id.pd_loaderRestricted1)
        pd_loadervehicle1 =findViewById<ProgressBar>(R.id.pd_loadervehicle1)
        checkother =findViewById<CheckBox>(R.id.checkother)
        et_other =findViewById<EditText>(R.id.et_other)

        sharedPreferences = this.getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)


        options.setCluster(NetworkUtility.cluster)

         authorizer = HttpAuthorizer(NetworkUtility.authUrl)
         options = PusherOptions().setAuthorizer(authorizer)
        options.setCluster(NetworkUtility.cluster)

        pusher = Pusher(NetworkUtility.key, options)

        val authorizer = HttpAuthorizer(NetworkUtility.authUrl)

        val options = PusherOptions().setAuthorizer(authorizer)

        options.setCluster(NetworkUtility.cluster)

        pusher = Pusher(NetworkUtility.key, options)


        pusher!!.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i(
                    "Pusher",
                    "State changed from ${change.previousState} to ${change.currentState}"
                )

                if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                    val socketId = pusher!!.connection.socketId
                     val channelname = "private-my-channel"
                    Log.v("socketId", "socketId" + socketId)
                    val parameters = HashMap<String, String>()
                    parameters["socket_id"] = socketId
                    parameters["channel_name"] = "private-my-channel"
                    authorizer.setHeaders(parameters)
                    pusher!!.unsubscribe(channelname)




                    try {

                        if (channel == null) {

                            channel =
                                pusher!!.subscribePrivate("private-my-channel")
                        }else {

                          //  Toast.makeText(this@CancelServiceCallScreen, "Please wait Pusher channel is creating", Toast.LENGTH_LONG).show()
                            channel = pusher!!.subscribePrivate("private-my-channel")
                        }


                    } catch (e: Exception) {

                        e.printStackTrace()
                    }

                    if (channel!=null) {

                        if (channel!!.isSubscribed) {

                        } else {
                          //  channel = pusher!!.subscribePrivate("private-my-channel")
                        }
                    }
                }


            }

            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                Log.i(
                    "Pusher",
                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                )
            }

        }, ConnectionState.ALL)



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
            tokenReg = sharedPreferences!!.getString("AuthSharedPref","").toString()

            //  txt_name1!!.setText(fnameSharedPrefAfterReg +" "+ lnameSharedPrefAfterReg )

        }
        else{

            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            fnameSharedPrefAfterLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","").toString()
            lnameSharedPrefAfterLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","").toString()
            token = sharedPreferences!!.getString("Authorization","").toString()

            //  txt_name1!!.setText(fnameSharedPrefAfterLog +" "+ lnameSharedPrefAfterLog )

        }

        vehicle1!!.setOnClickListener {

            vehicleImg1 = true
            vehicleImg2=false
            restricImg1=false
            restricImg2=false

            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()

        }

        vehicle2!!.setOnClickListener {

            vehicleImg1 = false
            vehicleImg2=true
            restricImg1=false
            restricImg2=false

            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()

        }

        restricted1!!.setOnClickListener {

            vehicleImg1 = false
            vehicleImg2=false
            restricImg1=true
            restricImg2=false

            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()

        }

        restricted2!!.setOnClickListener {

            vehicleImg1 = false
            vehicleImg2=false
            restricImg1=false
            restricImg2=true

            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()

        }





        iv_back!!.setOnClickListener {

            finish()
        }

        iv_infomore!!.setOnClickListener {

            maintransViewCancelServiceCall!!.visibility = View.VISIBLE
            tooltipView!!.visibility = View.VISIBLE
            tooltipImg!!.visibility = View.VISIBLE
            tooltipTxt!!.visibility = View.VISIBLE

        }

        maintransViewCancelServiceCall!!.setOnClickListener {

            maintransViewCancelServiceCall!!.visibility = View.GONE
            tooltipView!!.visibility = View.GONE
            tooltipImg!!.visibility = View.GONE
            tooltipTxt!!.visibility = View.GONE
        }




        checkBox!!.setOnClickListener {
            if(checkBox!!.isChecked) {


                vehicle_part = true
            }

            else{
                vehicle_part = false

                storeImage=""
                storeImage1=""
            }

        }

        checkBox2!!.setOnClickListener {

            if(checkBox2!!.isChecked) {


                restricted_part = true
            }

            else{
                restricted_part = false
                storeImage2=""
                storeImage3=""
            }

        }

        check_loadCargo!!.setOnClickListener {

            if(check_loadCargo!!.isChecked){

                check_loadCargoVal = true

            }else{

                check_loadCargoVal = false

            }

        }

        check_inappropriatebehave!!.setOnClickListener {
            if(check_inappropriatebehave!!.isChecked){

                check_inappropriatebehaveVal = true

            }else{

                check_inappropriatebehaveVal = false

            }

        }

        check_needservice!!.setOnClickListener {
            if(check_needservice!!.isChecked){

                check_needserviceVal = true

            }else{

                check_needserviceVal = false

            }

        }
        checkother!!.setOnClickListener {
            if(checkother!!.isChecked){

                check_checkotherVal = true

            }else{

                check_checkotherVal = false
                et_other!!.setText("")
            }

        }

        tv_cancellCall!!.setOnClickListener (object :View.OnClickListener{
            override fun onClick(p0: View?) {


                if (vehicle_part == false && restricted_part == false  && check_loadCargoVal == false && check_needserviceVal== false && check_inappropriatebehaveVal == false && check_checkotherVal == false){

                    Toast.makeText(this@CancelServiceCallScreen, "Please check at least one reason", Toast.LENGTH_LONG).show()


                }
                else if (vehicle_part == true && storeImage.equals("") && storeImage1.equals("") ){

                   // if (storeImage.equals("") && storeImage1.equals("")){

                        Toast.makeText(this@CancelServiceCallScreen, "Please upload at least one picture of the excessive cargo", Toast.LENGTH_LONG).show()

                  //  }

                }
              /*  else if (vehicle_part == false){
                    storeImage=""
                    storeImage1=""

                }*/
                else if (restricted_part == true && storeImage2.equals("") && storeImage3.equals("")){

                  //  if (storeImage2.equals("") && storeImage3.equals("")){
                        Toast.makeText(this@CancelServiceCallScreen, "Please upload at least one picture of the restricted cargo", Toast.LENGTH_LONG).show()

                 //   }

                }
               /* else if (restricted_part == false){
                    storeImage2=""
                    storeImage3=""

                }*/


                else if (vehicle_part == true ||  restricted_part ==true || check_loadCargoVal ==true || check_needserviceVal == true || check_inappropriatebehaveVal == true || check_checkotherVal == true){

                    // cancelServiceCallAPI()


                    try {

                        AppProgressBar.openLoader(
                            this@CancelServiceCallScreen,
                            this@CancelServiceCallScreen.resources.getString(R.string.pleasewait)
                        )

                        val apiservice: ApiInterface = ApiClient.getClient(this@CancelServiceCallScreen).create(ApiInterface::class.java)
                        val req = HashMap<String, Any>()


                        if (defaultIdLog.equals("")) {

                            //  req["driver_id"] = defaultIdReg
                            req["service_id"] = intent.getStringExtra("service_id")!!
                            req["driver_id"] = defaultIdReg!!
                            req["type"] = "reason"
                            req["vehicle_image1"] = storeImage
                            req["vehicle_image2"] = storeImage1
                            req["restricted_image1"] = storeImage2
                            req["restricted_image2"] = storeImage3
                            req["no_help_toload"] = check_loadCargoVal
                            req["customer_no_longer"] = check_needserviceVal
                            req["customer_behaviour"] = check_inappropriatebehaveVal
                            req["other"] = et_other!!.text.toString()

                            responseServicecallcancel = apiservice.doingservicecallcancelled(tokenReg, req)

                        }

                        else{

                            req["service_id"] = intent.getStringExtra("service_id")!!
                            req["driver_id"] = defaultIdLog!!
                            req["type"] = "reason"
                            req["vehicle_image1"] = storeImage
                            req["vehicle_image2"] = storeImage1
                            req["restricted_image1"] = storeImage2
                            req["restricted_image2"] = storeImage3
                            req["no_help_toload"] = check_loadCargoVal
                            req["customer_no_longer"] = check_needserviceVal
                            req["customer_behaviour"] = check_inappropriatebehaveVal
                            req["other"] = et_other!!.text.toString()


                            responseServicecallcancel = apiservice.doingservicecallcancelled(token, req)

                        }

                        responseServicecallcancel!!.enqueue(object : Callback<CancelResponse?> {
                            override fun onResponse(
                                call: Call<CancelResponse?>,
                                response1: Response<CancelResponse?>
                            ) {
                                if (response1.isSuccessful()) {

                                    AppProgressBar.closeLoader()

                                    driver_updatestatus = "Cancelled"

                                    val req = HashMap<String, Any>()
                                    val gson = Gson()
                                    req["id"] = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
                                    req["driver_long"] = intent.getDoubleExtra("driver_long",0.0)!!
                                    req["driver_lat"] = intent.getDoubleExtra("driver_lat",0.0)!!
                                    req["status"] =driver_updatestatus!!

                                    val final = gson.toJson(req)

                                    try {

                                        if (channel == null) {

                                            channel =
                                                pusher!!.subscribePrivate("private-my-channel")
                                        }else {

                                           // Toast.makeText(this@CancelServiceCallScreen, "Please wait Pusher channel is creating", Toast.LENGTH_LONG).show()
                                            channel = pusher!!.subscribePrivate("private-my-channel")
                                        }


                                    } catch (e: Exception) {

                                        e.printStackTrace()
                                    }
                                    if (channel!=null) {

                                        channel!!.trigger("client-order-progress-status", final)
                                    }
                                    Log.v("clientorderprogrestatus" ,"clientorderprogressstatus"+final)
                                    Log.v("service_id" ,"service_id"+intent.getStringExtra("service_id")!!)
                                    Log.v("cancelcharges" ,"cancelcharges"+response1.body()!!.detail!!.cancelCharges!!.toDouble())

                                    val myIntent = Intent(this@CancelServiceCallScreen , AfterCancelServiceScreen::class.java)
                                    myIntent.putExtra("service_id", intent.getStringExtra("service_id")!!)
                                    myIntent.putExtra("cancelcharges", response1.body()!!.detail!!.cancelCharges!!.toDouble())
                                    startActivity(myIntent)
                                    // finish()



                                } else {

                                    AppProgressBar.closeLoader()


                                    Toast.makeText(
                                        this@CancelServiceCallScreen,
                                        response1.body()!!.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()


                                }
                            }

                            override fun onFailure(call: Call<CancelResponse?>, t: Throwable) {
                                AppProgressBar.closeLoader()
                                if(t is NoConnectivityException) {
                                    Toast.makeText(
                                        this@CancelServiceCallScreen,
                                        ""+t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{

                                    Toast.makeText(
                                        this@CancelServiceCallScreen,
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

        })

    }


    private fun onPayInDocUpload() {

        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showImagePickerOptions() {


        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }

            override fun onDocumentSelected() {
                // launchDocumentIntent()
            }
        })
    }

    private fun launchCameraIntent() {

        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(
            intent, REQUEST_IMAGE
        )
    }

    private fun launchGalleryIntent() {

        val intent = Intent(this, ImagePickerActivity::class.java)

        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2

        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(
            intent,
            REQUEST_IMAGE
        )

    }

    private fun showSettingsDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(
            getString(R.string.go_to_settings)
        ) { dialog, which ->
            dialog.cancel()
            this.openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    ImageUrl = uri

                    if (vehicleImg1 == true) {

                        encodedImage = ""


                        val imageStream: InputStream? =
                            this.contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage = encodeImage(selectedImage)!!

                        Log.v("image64", "" + encodedImage)

                        loadProfile(uri.toString())

                    }
                    else if (vehicleImg2 == true) {

                        encodedImage1 = ""

                        val imageStream: InputStream? =
                            this.contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage1 = encodeImage(selectedImage)!!

                        Log.v("image64", "" + encodedImage1)

                        loadProfile1(uri.toString())

                    }

                    else if (restricImg1 == true) {

                        encodedImage2 = ""


                        val imageStream: InputStream? =
                            this.contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage2 = encodeImage(selectedImage)!!

                        Log.v("image64", "" + encodedImage2)

                        loadProfile2(uri.toString())

                    }

                    else if (restricImg2 == true) {

                        encodedImage3 = ""


                        val imageStream: InputStream? =
                            this.contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage3 = encodeImage(selectedImage)!!

                        Log.v("image64", "" + encodedImage3)

                        loadProfile3(uri.toString())

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun loadProfile(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )

        pd_loadervehicle1!!.isVisible=true
        pd_loadervehicle2!!.isVisible=false
        pd_loaderRestricted1!!.isVisible=false
        pd_loaderRestricted2!!.isVisible=false
        imageuploadAPICall1(url)

    }

    fun loadProfile1(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervehicle1!!.isVisible=false
        pd_loadervehicle2!!.isVisible=true

        pd_loaderRestricted1!!.isVisible=false
        pd_loaderRestricted2!!.isVisible=false

        imageuploadAPICall2(url)

    }

    fun loadProfile2(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervehicle1!!.isVisible=false
        pd_loadervehicle2!!.isVisible=false

        pd_loaderRestricted1!!.isVisible=true
        pd_loaderRestricted2!!.isVisible=false

        imageuploadAPICall3(url)

    }


    fun loadProfile3(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervehicle1!!.isVisible=false
        pd_loadervehicle2!!.isVisible=false

        pd_loaderRestricted1!!.isVisible=false
        pd_loaderRestricted2!!.isVisible=true

        imageuploadAPICall4(url)

    }



    private fun imageuploadAPICall1(url: String) {

        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage
                responseImageUpload = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage
                responseImageUpload = apiservice.doingImageUpload(token,req)

            }

            responseImageUpload!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {


                        pd_loadervehicle1!!.isVisible=false


                        if (response.body()!!.status==true) {

                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            pd_loadervehicle1!!.isVisible=false

                            Glide.with(this@CancelServiceCallScreen).load(url)
                                .into(vehicle1!!)

                            storeImage = response.body()!!.detail!!.images.toString()

                        }else{
                            pd_loadervehicle1!!.isVisible=false
                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loadervehicle1!!.isVisible=false
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loadervehicle1!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CancelServiceCallScreen,
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

    private fun imageuploadAPICall2(url: String) {

        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage1
                responseImageUpload = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage1
                responseImageUpload = apiservice.doingImageUpload(token,req)

            }

            responseImageUpload!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {


                        pd_loadervehicle2!!.isVisible=false


                        if (response.body()!!.status==true) {

                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            pd_loadervehicle2!!.isVisible=false

                            Glide.with(this@CancelServiceCallScreen).load(url)
                                .into(vehicle2!!)

                            storeImage1 = response.body()!!.detail!!.images.toString()

                        }else{
                            pd_loadervehicle2!!.isVisible=false
                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loadervehicle2!!.isVisible=false
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loadervehicle2!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CancelServiceCallScreen,
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

    private fun imageuploadAPICall3(url: String) {

        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage2
                responseImageUpload = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage2
                responseImageUpload = apiservice.doingImageUpload(token,req)

            }

            responseImageUpload!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {


                        pd_loaderRestricted1!!.isVisible=false


                        if (response.body()!!.status==true) {

                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            pd_loaderRestricted1!!.isVisible=false

                            Glide.with(this@CancelServiceCallScreen).load(url)
                                .into(restricted1!!)

                            storeImage2 = response.body()!!.detail!!.images.toString()

                        }else{
                            pd_loaderRestricted1!!.isVisible=false
                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loaderRestricted1!!.isVisible=false
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loaderRestricted1!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CancelServiceCallScreen,
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

    private fun imageuploadAPICall4(url: String) {

        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage3
                responseImageUpload = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage3
                responseImageUpload = apiservice.doingImageUpload(token,req)

            }

            responseImageUpload!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {


                        pd_loaderRestricted2!!.isVisible=false


                        if (response.body()!!.status==true) {

                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            pd_loaderRestricted2!!.isVisible=false

                            Glide.with(this@CancelServiceCallScreen).load(url)
                                .into(restricted2!!)

                            storeImage3 = response.body()!!.detail!!.images.toString()

                        }else{
                            pd_loaderRestricted2!!.isVisible=false
                            Toast.makeText(
                                this@CancelServiceCallScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loaderRestricted2!!.isVisible=false
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loaderRestricted2!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CancelServiceCallScreen,
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

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun cancelServiceCallAPI() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")) {

                //  req["driver_id"] = defaultIdReg
                req["service_id"] = intent.getStringExtra("service_id")!!
                req["driver_id"] = defaultIdReg!!
                req["type"] = "reason"
                req["vehicle_image1"] = storeImage
                req["vehicle_image2"] = storeImage1
                req["restricted_image1"] = storeImage2
                req["restricted_image2"] = storeImage3
                req["no_help_toload"] = check_loadCargoVal
                req["customer_no_longer"] = check_needserviceVal
                req["customer_behaviour"] = check_inappropriatebehaveVal
                req["other"] = et_other!!.text.toString()

                responseServicecallcancel = apiservice.doingservicecallcancelled(tokenReg, req)

            }

            else{

                req["service_id"] = intent.getStringExtra("service_id")!!
                req["driver_id"] = defaultIdLog!!
                req["type"] = "reason"
                req["vehicle_image1"] = storeImage
                req["vehicle_image2"] = storeImage1
                req["restricted_image1"] = storeImage2
                req["restricted_image2"] = storeImage3
                req["no_help_toload"] = check_loadCargoVal
                req["customer_no_longer"] = check_needserviceVal
                req["customer_behaviour"] = check_inappropriatebehaveVal
                req["other"] = et_other!!.text.toString()


                responseServicecallcancel = apiservice.doingservicecallcancelled(token, req)

            }

            responseServicecallcancel!!.enqueue(object : Callback<CancelResponse?> {
                override fun onResponse(
                    call: Call<CancelResponse?>,
                    response1: Response<CancelResponse?>
                ) {
                    if (response1.isSuccessful()) {

                        AppProgressBar.closeLoader()


                        val authorizer = HttpAuthorizer(NetworkUtility.authUrl)
                        //  val authorizer = HttpAuthorizer("http://192.168.3.60:8555/pusher/auth")
                        val options = PusherOptions().setAuthorizer(authorizer)
                        //options.setCluster("ap2")
                        options.setCluster(NetworkUtility.cluster)

                        //pusher = Pusher("d5041f2ec10120e451ce", options)
                        pusher = Pusher(NetworkUtility.key, options)


                        pusher!!.connect(object : ConnectionEventListener {
                            override fun onConnectionStateChange(change: ConnectionStateChange) {
                                Log.i(
                                    "Pusher",
                                    "State changed from ${change.previousState} to ${change.currentState}"
                                )

                                if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                                    val socketId = pusher!!.connection.socketId
                                    val channelname = "private-my-channel"
                                    Log.v("socketId", "socketId" + socketId)
                                    val parameters = HashMap<String, String>()
                                    parameters["socket_id"] = socketId
                                    parameters["channel_name"] = "private-my-channel"
                                    authorizer.setHeaders(parameters)
                                    pusher!!.unsubscribe(channelname)

                                    try {

                                        if (channel == null) {

                                            channel =
                                                pusher!!.subscribePrivate("private-my-channel")
                                        }



                                    } catch (e: Exception) {

                                        e.printStackTrace()
                                    }



                                }


                            }

                            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                                Log.i(
                                    "Pusher",
                                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                                )
                            }

                        }, ConnectionState.ALL)


                        driver_updatestatus = "Cancelled"

                        val req = HashMap<String, Any>()
                        val gson = Gson()
                        req["id"] = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
                        req["driver_long"] = intent.getDoubleExtra("driver_long",0.0)!!
                        req["driver_lat"] = intent.getDoubleExtra("driver_lat",0.0)!!
                        req["status"] =driver_updatestatus!!

                        val final = gson.toJson(req)

                        try {

                            if (channel == null) {

                                channel =
                                    pusher!!.subscribePrivate("private-my-channel")
                            }else {

                                channel!!.trigger("client-order-progress-status", final)
                            }


                        } catch (e: Exception) {

                            e.printStackTrace()
                        }

                        Log.v("clientorderprogrestatus" ,"clientorderprogressstatus"+final)

                        val myIntent = Intent(this@CancelServiceCallScreen , AfterCancelServiceScreen::class.java)

                        myIntent.putExtra("service_id",  intent.getStringExtra("service_id")!!)
                        myIntent.putExtra("cancelcharges", response1.body()!!.detail!!.cancelCharges!!.toDouble())
                        startActivity(myIntent)

                       // finish()



                    } else {

                        AppProgressBar.closeLoader()


                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            response1.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<CancelResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@CancelServiceCallScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@CancelServiceCallScreen,
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
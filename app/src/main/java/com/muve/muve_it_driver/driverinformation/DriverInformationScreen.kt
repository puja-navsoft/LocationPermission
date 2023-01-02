package com.muve.muve_it_driver.driverinformation

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.muve.muve_it_driver.*
import com.muve.muve_it_driver.model.driverinformation.DriverInfoPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.isEditUserDone
import com.muve.muve_it_driver.vehicleinformation.VehicleinfoScreen
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DriverInformationScreen : AppCompatActivity() {

    val dateFormatter: DateFormat ? =null
    var year: Int ? =null
    var month: Int ? =null
    var day: Int ? =null
    var ev_address: EditText? = null
    var ev_city: TextView? = null
    var ev_province: TextView? = null
    var ev_postalcode: TextView? = null
    var editLicense: TextView? = null
    var editDate: EditText? = null
    var iv_front: ImageView? = null
    var iv_back_: ImageView? = null
    var iv_cal: ImageView? = null
    var pd_loader: ProgressBar? = null
    var pd_loaderFront: ProgressBar? = null
    var pd_loaderBack: ProgressBar? = null
    var iv_editPrflPic: ImageView? = null
    var iv_back: ImageView? = null
    var iv_user: CircleImageView? = null
    var btn_nxt_click: Button? = null
    var calendar: Calendar? = null
    var sharedPreferences: SharedPreferences?=null
    private var part1: String=""
    private var part2: String=""
    var encodedImage: String=""
    var encodedImage1: String=""
    var storeImageFront: String=""
    var storeImageBack: String=""
    var storeImage: String=""
    var encodedImage2: String=""
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
   // var REQUEST_IMAGE = 100
    var REQUEST_IMAGE = 0
    var REQUEST_DOCUMENT = 101
    var proofAttachmentType: String? = null
    var ImageUrl: Uri? = null
    var ImageUrlPayOut: Uri? = null
    var iv_img: TextView? = null
    var response1: Call<DriverInfoPojo>? = null
    var response2: Call<ImageUploadResponse>? = null
    var dateSelected = Calendar.getInstance()
    var datePickerDialog: DatePickerDialog? = null
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"
    private val BUFFER_SIZE = 1024 * 2
    val myCalendar = Calendar.getInstance()
    var userImage : Boolean = false
    var userImage1 : Boolean = false
    var frontImage : Boolean = false
    var frontImage1 : Boolean = false
    var backImage : Boolean = false
    var backImage1 : Boolean = false
    var userProfileImage: String = ""
    var userFrontImage: String = ""
    var userBackImage: String = ""
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var tooltipView: ConstraintLayout? = null
    var maintransView: View? = null
    var imageView34: ImageView? = null
    var iv_info: ImageView? = null
    var receiver: BroadcastReceiver? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_information_screen)



        ev_address = findViewById<EditText>(R.id.ev_address)
        ev_city = findViewById<EditText>(R.id.ev_city)
        ev_province = findViewById<EditText>(R.id.ev_province)
        ev_postalcode = findViewById<EditText>(R.id.ev_postalcode)
        editLicense = findViewById<EditText>(R.id.editLicense)
        editDate = findViewById<EditText>(R.id.editDate)
        iv_front = findViewById<ImageView>(R.id.iv_front)
        iv_back_ = findViewById<ImageView>(R.id.iv_back_)
        iv_editPrflPic = findViewById<ImageView>(R.id.iv_editPrflPic)
        iv_user = findViewById<CircleImageView>(R.id.iv_user)
       // iv_back = findViewById<ImageView>(R.id.iv_back)
        btn_nxt_click = findViewById<Button>(R.id.btn_nxt_click)
        val parent = findViewById<ConstraintLayout>(R.id.parent)
        iv_cal = findViewById<ImageView>(R.id.iv_cal)
        pd_loader = findViewById<ProgressBar>(R.id.pd_loader)
        pd_loaderFront = findViewById<ProgressBar>(R.id.pd_loaderFront)
        pd_loaderBack = findViewById<ProgressBar>(R.id.pd_loaderBack)
        tooltipView = findViewById<ConstraintLayout>(R.id.tooltipView)
        maintransView = findViewById<View>(R.id.maintransView)
        imageView34 = findViewById<ImageView>(R.id.imageView34)
        iv_info = findViewById<ImageView>(R.id.iv_info)


        calendar = Calendar.getInstance()
        year = calendar!!.get(Calendar.YEAR)
        month = calendar!!.get(Calendar.MONTH)
        day = calendar!!.get(Calendar.DAY_OF_MONTH)


        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }

        parent.setOnClickListener {


            // Toast.makeText(this, "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()

            AppUtilities.hideSoftKeyboard(this)

        }

      /*  iv_back!!.setOnClickListener {


            finish()
        }*/


        iv_info!!.setOnClickListener {

            maintransView!!.visibility = View.VISIBLE
            tooltipView!!.visibility = View.VISIBLE
            imageView34!!.visibility = View.VISIBLE

        }

        maintransView!!.setOnClickListener {

            maintransView!!.visibility = View.GONE
            tooltipView!!.visibility = View.GONE
            imageView34!!.visibility = View.GONE
        }


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


        iv_editPrflPic!!.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)
            userImage = true
            frontImage=false
            backImage=false
            onPayInDocUpload()
        }

        iv_front!!.setOnClickListener {


            userImage=false
            backImage=false
            frontImage = true

            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()
        }

        iv_back_!!.setOnClickListener {

            backImage=true
            userImage = false
            frontImage=false
            AppUtilities.hideSoftKeyboard(this)

            onPayInDocUpload()
        }



        editDate!!.setOnClickListener {


           /* DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).datePicker.minDate = System.currentTimeMillis() - 1000
*/

            val datePickerDialog = DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = myCalendar!!.timeInMillis - 1000
            datePickerDialog.show()

        }


        btn_nxt_click!!.setOnClickListener {

           /* if (ev_address!!.text.toString().trim().equals("") || ev_city!!.text.toString().trim().equals("") || ev_province!!.text.toString().trim().equals("") || ev_postalcode!!.text.toString().trim().equals("") ||

                editLicense!!.text.toString().trim().equals("") || editDate!!.text.toString().trim().equals("")|| userImage1==false || frontImage1 == false || backImage1 == false
            ){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()

            }
            else*/

            if (storeImage.equals("")) {

                Toast.makeText(this, "Please upload driver photo", Toast.LENGTH_LONG).show()
            }

            else if (ev_address!!.text.toString().trim().equals("")) {

              //  Toast.makeText(this, "Please fill address fields", Toast.LENGTH_LONG).show()
                ev_address!!.setError("Please fill diver address fields")
                ev_address!!.requestFocus()
            }
            else if (ev_city!!.text.toString().trim().equals("")) {

               // Toast.makeText(this, "Please fill city fields", Toast.LENGTH_LONG).show()
                ev_province!!.setError("Please fill driver city")
                ev_province!!.requestFocus()
            }
            else if (ev_province!!.text.toString().trim().equals("")) {

               // Toast.makeText(this, "Please fill province fields", Toast.LENGTH_LONG).show()
                ev_province!!.setError("Please fill province fields")
                ev_province!!.requestFocus()
            }
            else if (ev_postalcode!!.text.toString().trim().equals("")) {

              //  Toast.makeText(this, "Please fill postal code fields", Toast.LENGTH_LONG).show()
                ev_postalcode!!.setError("Please fill postal code fields")
                ev_postalcode!!.requestFocus()
            }
            else if (storeImageFront.equals("")) {

                Toast.makeText(this, "Please upload front image of driver licence", Toast.LENGTH_LONG).show()
            }
            else if (storeImageBack.equals("")) {

                Toast.makeText(this, "Please upload back image of driver licence", Toast.LENGTH_LONG).show()
            }
            else if (editLicense!!.text.toString().trim().equals("")) {

                //Toast.makeText(this, "Please fill license number fields", Toast.LENGTH_LONG).show()
                editLicense!!.setError("Please fill license number")
                editLicense!!.requestFocus()

            }
            else if (editDate!!.text.toString().trim().equals("")) {

                Toast.makeText(this, "Please fill expiry date", Toast.LENGTH_LONG).show()

              //  editDate!!.setError("Please fill expiry date")
                editDate!!.requestFocus()
            }
            else{

                editProfile()
            }
        }

    }

    private fun updateLabel() {

        //val myFormat = "dd/MM/yy"
        val myFormat = "MM/dd/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        editDate!!.setText(dateFormat.format(myCalendar.time))
        editDate!!.setTextColor(resources.getColor(R.color.black))
    }

    private fun editProfile() {

        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["address"] =  ev_address!!.text.toString().trim()
                req["city"] =ev_city!!.text.toString().trim()
                req["province"] = ev_province!!.text.toString().trim()
                req["postal_code"] =ev_postalcode!!.text.toString().trim()
                req["licence_number"] =editLicense!!.text.toString().trim()
                req["date_of_expiry"] =editDate!!.text.toString().trim()
                req["driver_image"] = storeImage
                req["driver_licence_front"] = storeImageFront
                req["driver_licence_back"] = storeImageBack
                response1 = apiservice.doingdriverinformation(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["address"] =   ev_address!!.text.toString().trim()
                req["city"] = ev_city!!.text.toString().trim()
                req["province"] =ev_province!!.text.toString().trim()
                req["postal_code"] =ev_postalcode!!.text.toString().trim()
                req["licence_number"] =editLicense!!.text.toString().trim()
                req["date_of_expiry"] =editDate!!.text.toString().trim()
                req["driver_image"] = storeImage
                req["driver_licence_front"] = storeImageFront
                req["driver_licence_back"] = storeImageBack
                response1 = apiservice.doingdriverinformation(token,req)

            }

            response1!!.enqueue(object : Callback<DriverInfoPojo?> {
                override fun onResponse(
                    call: Call<DriverInfoPojo?>,
                    response: Response<DriverInfoPojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status==true) {

                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            val myIntent = Intent(this@DriverInformationScreen,
                                VehicleinfoScreen::class.java
                            )
                            startActivity(myIntent)
                            finish()


                        }

                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.detail!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    )
                                ) {

                                    Toast.makeText(
                                        this@DriverInformationScreen,
                                        response.body()!!.detail.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    getLoggedOut()

                                }
                                else {


                                    Toast.makeText(
                                        this@DriverInformationScreen,
                                        response.body()!!.detail.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }


                    } else {

                        AppProgressBar.closeLoader()
                        if (response.body() != null) {


                            Toast.makeText(
                                this@DriverInformationScreen,
                                "Error",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
                }

                override fun onFailure(call: Call<DriverInfoPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DriverInformationScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DriverInformationScreen,
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

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_proof_attachment_camera)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val tvTakePicture = dialog.findViewById<TextView>(R.id.tvTakePicture)
        // TextView tvChooseGallery = dialog.findViewById(R.id.tvChooseGallery);
        //  TextView tvChooseDocument = dialog.findViewById(R.id.tvChooseDocument);
        // TextView tvChooseGallery = dialog.findViewById(R.id.tvChooseGallery);
        //  TextView tvChooseDocument = dialog.findViewById(R.id.tvChooseDocument);
        val IvDialogClose = dialog.findViewById<ImageView>(R.id.IvDialogClose)

        IvDialogClose.setOnClickListener { dialog.dismiss() }

        tvTakePicture.setOnClickListener {

            ImagePicker.with(this)
                .cropSquare() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
                .start(0)

            // listener.onTakeCameraSelected()
            dialog.dismiss()
        }

/*
        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }

            override fun onDocumentSelected() {
                launchDocumentIntent()
            }
        })
*/
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

    private fun launchDocumentIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_DOCUMENT_PICKER_OPTION,
            ImagePickerActivity.REQUEST_DOCUMENT
        )

        startActivityForResult(
            intent,
            REQUEST_DOCUMENT
        )

    }
    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
               // val uri = data!!.getParcelableExtra<Uri>("path")
                val uri = data?.data
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    ImageUrl = uri
                    Log.v("ImageUrl","ImageUrl"+ImageUrl)
                    // loading profile image from local cache
                    /* if (com.rest.rms.activities.DrawerManagementActivity.proofAttachmentType == "payIn") {
                         com.rest.rms.activities.DrawerManagementActivity.ImageUrl = uri
                     } else {
                         com.rest.rms.activities.DrawerManagementActivity.ImageUrlPayOut = uri
                     }*/

                    if (userImage == true){

                        encodedImage=""


                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage= encodeImage(selectedImage)!!
                        System.out.println(encodedImage)

                        loadProfile(uri!!)


                    }else if (frontImage == true){
                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage1=""
                        encodedImage1= encodeImage(selectedImage)!!

                        loadProfileFront(uri.toString())


                    }else if ( backImage == true){

                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage2=""
                        encodedImage2= encodeImage(selectedImage)!!

                        loadProfileBack(uri.toString())

                    }

                 /*   val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    encodedImage= encodeImage(selectedImage)!!
*/
                    Log.v("image64",""+encodedImage)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

     fun loadProfileBack(url: String) {

        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        backImage1 = true

         pd_loader!!.isVisible=false
         pd_loaderFront!!.isVisible=false
         pd_loaderBack!!.isVisible=true
         imageuploadAPICallBack(url)



    }

    private fun imageuploadAPICallBack(url: String) {

        try {

            /* AppProgressBar.openLoader(
                 this,
                 this.getResources().getString(R.string.pleasewait)
             )*/

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage2
                response2 = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage2
                response2 = apiservice.doingImageUpload(token,req)

            }

            response2!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {
                        // AppProgressBar.closeLoader()
                        pd_loaderBack!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pd_loaderBack!!.isVisible=false
                            Glide.with(this@DriverInformationScreen).load(url)
                                .into(iv_back_!!)

                            storeImageBack = response.body()!!.detail!!.images.toString()

                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{
                            pd_loaderBack!!.isVisible=false
                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loaderBack!!.isVisible=false
                        Toast.makeText(
                            this@DriverInformationScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loaderBack!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DriverInformationScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DriverInformationScreen,
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

    fun loadProfileFront(url: String) {

        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        frontImage1 = true
        pd_loader!!.isVisible=false
        pd_loaderBack!!.isVisible=false
        pd_loaderFront!!.isVisible=true

         imageuploadAPICallFront(url)

    }

     fun imageuploadAPICallFront(url: String) {

        try {

            /* AppProgressBar.openLoader(
                 this,
                 this.getResources().getString(R.string.pleasewait)
             )*/

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage1
                response2 = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage1
                response2 = apiservice.doingImageUpload(token,req)

            }

            response2!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {
                        // AppProgressBar.closeLoader()
                        pd_loaderFront!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pd_loaderFront!!.isVisible=false

                            Glide.with(this@DriverInformationScreen).load(url)
                                .into(iv_front!!)

                           storeImageFront =  response.body()!!.detail!!.images.toString()

                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{
                            pd_loaderFront!!.isVisible=false

                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loaderFront!!.isVisible=false

                        Toast.makeText(
                            this@DriverInformationScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loaderFront!!.isVisible=false

                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DriverInformationScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DriverInformationScreen,
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

    fun loadProfile(url: Uri) {

        Log.d("ImagePath", "Image cache path: $url")

        pd_loaderBack!!.isVisible=false
        pd_loaderFront!!.isVisible=false

        pd_loader!!.isVisible=true

         imageuploadAPICall(url)

        userImage1 = true

        isEditUserDone =true

      /*  iv_img!!.alpha=1f
        iv_img!!.isEnabled=true*/


    }

     fun imageuploadAPICall(url: Uri) {

        try {

         /*   AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )*/

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage
                response2 = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage
                response2 = apiservice.doingImageUpload(token,req)

            }

            response2!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful()) {
                       // AppProgressBar.closeLoader()
                        pd_loader!!.isVisible=false

                        if (response.body()!!.status==true) {

                            pd_loader!!.isVisible=false

                            storeImage= response.body()!!.detail!!.images.toString()

                            Glide.with(this@DriverInformationScreen).load(url)
                                .into(iv_user!!)


                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{

                            pd_loader!!.isVisible=false

                            Toast.makeText(
                                this@DriverInformationScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                      //  AppProgressBar.closeLoader()
                        pd_loader!!.isVisible=false


                        Toast.makeText(
                            this@DriverInformationScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                  //  AppProgressBar.closeLoader()
                    pd_loader!!.isVisible=false

                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@DriverInformationScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@DriverInformationScreen,
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

    override fun onBackPressed() {

        //   super.onBackPressed();
        return
        //super.onBackPressed()
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


                        val myIntent = Intent(this@DriverInformationScreen, WelcomeActivity::class.java)
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
                                        this@DriverInformationScreen,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()


                            } else {

                                Toast.makeText(this@DriverInformationScreen, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()



                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if (t is NoConnectivityException) {
                        Toast.makeText(this@DriverInformationScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@DriverInformationScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

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

                        Toast.makeText(this@DriverInformationScreen, "Your Account is Logged In from another Device", Toast.LENGTH_SHORT).show()

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
package com.muve.muve_it_driver.vehicleinformation

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
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
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.driverinformation.DriverInfoPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.permissionscreen.PermissionScreen
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.isEditUserDone
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muve.muve_it_driver.*
import com.muve.muve_it_driver.popupdiscloser.PopupDiscloser
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


class VehicleinfoScreen : AppCompatActivity() {

    var radioButton: RadioButton? = null
    val dateFormatter: DateFormat? =null
    var year: Int ? =null
    var month: Int ? =null
    var day: Int ? =null
    var editDate: EditText? = null
    var iv_cal: ImageView? = null
    var imageView34: ImageView? = null
    var pd_cargo: ProgressBar? = null
    var pd_loadervan: ProgressBar? = null
    var pd_owner: ProgressBar? = null
    var pd_loaderinsurencephoto: ProgressBar? = null
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
    var storeImageVehicleInsurence: String=""
    var storeImageVehicleOwner: String=""
    var storeImageVehicleSecond: String=""
    var storeImageVehicle: String=""
    var encodedImage2: String=""
    var encodedImage3: String=""
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
  //  var REQUEST_IMAGE = 100
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
  //  var userImage : Boolean = false
    var vehicleImg1 : Boolean = false
    var vehicleImg2 : Boolean = false
    var ownership : Boolean = false
    var insurenceImg : Boolean = false

    var vehicleImg1_ : Boolean = false
    var vehicleImg2_ : Boolean = false
    var ownership_ : Boolean = false
    var insurenceImg_ : Boolean = false
  //  var userImage1 : Boolean = false
   // var frontImage : Boolean = false
  //  var frontImage1 : Boolean = false
  //  var backImage : Boolean = false
  //  var backImage1 : Boolean = false
    var userProfileImage: String = ""
    var userFrontImage: String = ""
    var userBackImage: String = ""
    var selectval: String = ""
    var radioGrp: RadioGroup? = null
    var radioGrpSelect: Boolean? = false
    var rd_pickup: RadioButton? = null
    var rd_dropoff: RadioButton? = null
    var radioSelectedButton: RadioButton? = null
    var iv_cargo: ImageView? = null
    var iv_infoPhoto: ImageView? = null
    var iv_van: ImageView? = null
    var iv_owner: ImageView? = null
    var licencplt: EditText? = null
    var yearselect: EditText? = null
    var makemodel: EditText? = null
    var color: EditText? = null
    var iv_insurencephoto: ImageView? = null
    var selectedId: Int? = 0
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var tooltipView: ConstraintLayout? = null
    var maintransView: View? = null
    var receiver: BroadcastReceiver? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicleinfo_screen)

        radioGrp = findViewById<RadioGroup>(R.id.radioGrp)
        rd_pickup = findViewById<RadioButton>(R.id.rd_pickup)
        rd_dropoff = findViewById<RadioButton>(R.id.rd_dropoff)
        iv_cargo = findViewById<ImageView>(R.id.iv_cargo)
        iv_infoPhoto = findViewById<ImageView>(R.id.iv_infoPhoto)
        iv_van = findViewById<ImageView>(R.id.iv_van)
        iv_owner = findViewById<ImageView>(R.id.iv_owner)
        licencplt = findViewById<EditText>(R.id.licencplt)
        yearselect = findViewById<EditText>(R.id.year)
        makemodel = findViewById<EditText>(R.id.makemodel)
        color = findViewById<EditText>(R.id.color)
        iv_insurencephoto = findViewById<ImageView>(R.id.iv_insurencephoto)
        editDate = findViewById<EditText>(R.id.editDate)
      //  iv_back = findViewById<ImageView>(R.id.iv_back)
        btn_nxt_click = findViewById<Button>(R.id.btn_nxt_click)
        val parent = findViewById<ConstraintLayout>(R.id.parent)
        iv_cal = findViewById<ImageView>(R.id.iv_cal)
        imageView34 = findViewById<ImageView>(R.id.imageView34)
        pd_cargo = findViewById<ProgressBar>(R.id.pd_cargo)
        pd_loadervan = findViewById<ProgressBar>(R.id.pd_loadervan)
        pd_owner = findViewById<ProgressBar>(R.id.pd_owner)
        pd_loaderinsurencephoto = findViewById<ProgressBar>(R.id.pd_loaderinsurencephoto)
        tooltipView = findViewById<ConstraintLayout>(R.id.tooltipView)
        maintransView = findViewById<View>(R.id.maintransView)
        sharedPreferenceManager = SharedPreferenceManager(this)


        try {

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

        calendar = Calendar.getInstance()
        year = calendar!!.get(Calendar.YEAR)
        month = calendar!!.get(Calendar.MONTH)
        day = calendar!!.get(Calendar.DAY_OF_MONTH)

        radioGrp!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

          /*  radioGrpSelect =true
           // checkRd = checkedId
            radioButton = findViewById<View>(checkedId) as RadioButton
            Toast.makeText(baseContext, radioButton!!.getText(), Toast.LENGTH_SHORT).show()
            selectval = radioButton!!.getText().toString()*/

            selectedId = radioGrp!!.getCheckedRadioButtonId()
          //  selectval = findViewById(selectedId) as RadioButton
            // find the radiobutton by returned id
            // find the radiobutton by returned id
            radioButton = findViewById<View>(selectedId!!) as RadioButton
            Toast.makeText(
                applicationContext,
                radioButton!!.getText().toString() + " is selected",
                Toast.LENGTH_SHORT
            ).show()
        }
        )

        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }

        parent.setOnClickListener {


            // Toast.makeText(this, "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()

            AppUtilities.hideSoftKeyboard(this)

        }

     /*   iv_back!!.setOnClickListener {


            finish()
        }
*/
        iv_infoPhoto!!.setOnClickListener {

            maintransView!!.visibility = View.VISIBLE
            tooltipView!!.visibility = View.VISIBLE
            imageView34!!.visibility = View.VISIBLE

        }

        maintransView!!.setOnClickListener {

            maintransView!!.visibility = View.GONE
            tooltipView!!.visibility = View.GONE
            imageView34!!.visibility = View.GONE
        }

        iv_cargo!!.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = true
            vehicleImg2=false
            ownership=false
            insurenceImg=false
            onPayInDocUpload()

        }

        iv_van!!.setOnClickListener {
            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = false
            vehicleImg2=true
            ownership=false
            insurenceImg=false
            onPayInDocUpload()

        }

        iv_owner!!.setOnClickListener {
            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = false
            vehicleImg2=false
            ownership=true
            insurenceImg=false
            onPayInDocUpload()

        }

        iv_insurencephoto!!.setOnClickListener {
            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = false
            vehicleImg2=false
            ownership=false
            insurenceImg=true
            onPayInDocUpload()

        }

        editDate!!.setOnClickListener {

           /* DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()*/

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

           /* if (radioGrp!! .getCheckedRadioButtonId() == -1 || licencplt!!.text.toString().trim().equals("") || yearselect!!.text.toString().trim().equals("") || makemodel!!.text.toString().trim().equals("") ||

                color!!.text.toString().trim().equals("") || editDate!!.text.toString().trim().equals("")|| vehicleImg1_==false || vehicleImg2_ == false || ownership_ == false || insurenceImg_ == false
            ){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()

            }*/

            if (radioGrp!! .getCheckedRadioButtonId() == -1){

                 Toast.makeText(this, "Please select your vehicle type", Toast.LENGTH_LONG).show()

            }

            else if (storeImageVehicle.equals("")) {

                Toast.makeText(this, "Please add vehicle photo", Toast.LENGTH_LONG).show()
            }
            else if (storeImageVehicleSecond.equals("")) {

                Toast.makeText(this, "Please add vehicle photo", Toast.LENGTH_LONG).show()
            }

            else if (storeImageVehicleOwner.equals("")) {

                Toast.makeText(this, "Please add picture of ownership", Toast.LENGTH_LONG).show()
            }

            else if (licencplt!!.text.toString().trim().equals("")) {

                licencplt!!.setError("Please fill License plate number")
                licencplt!!.requestFocus()
            }
            else if (yearselect!!.text.toString().trim().equals("")) {

             //   Toast.makeText(this, "Please select year", Toast.LENGTH_LONG).show()
                yearselect!!.setError("Please add year")
                yearselect!!.requestFocus()
            }
            else if (makemodel!!.text.toString().trim().equals("")) {

                makemodel!!.setError("Please fill model")
                makemodel!!.requestFocus()
               // Toast.makeText(this, "Please province model", Toast.LENGTH_LONG).show()
            }
            else if (color!!.text.toString().trim().equals("")) {

                color!!.setError("Please add vehicle color")
                color!!.requestFocus()
               // Toast.makeText(this, "Please provide color", Toast.LENGTH_LONG).show()
            }
            else if (storeImageVehicleInsurence.equals("")) {

                Toast.makeText(this, "Please add vehicle insurence photo", Toast.LENGTH_LONG).show()
            }

            else if (editDate!!.text.toString().trim().equals("")) {

                editDate!!.setError("Please add insurance expiry date")
                editDate!!.requestFocus()
                //Toast.makeText(this, "Please fill expiry date", Toast.LENGTH_LONG).show()
            }
            else{

                editProfile()
            }
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
              //  val uri = data!!.getParcelableExtra<Uri>("path")

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

                    if (vehicleImg1 == true){

                        encodedImage=""


                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage= encodeImage(selectedImage)!!
                        System.out.println(encodedImage)

                        loadVehicleImg(uri.toString())


                    }else if (vehicleImg2 == true){
                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage1=""
                        encodedImage1= encodeImage(selectedImage)!!

                        loadVehicleImgSecond(uri.toString())


                    }else if ( ownership == true){

                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage2=""
                        encodedImage2= encodeImage(selectedImage)!!

                        loadOwnershipImg(uri.toString())

                    }
                    else if ( insurenceImg == true){

                        val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        encodedImage3=""
                        encodedImage3= encodeImage(selectedImage)!!

                        loadInsurenceImg(uri.toString())

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

    private fun loadInsurenceImg(url: String) {

        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervan!!.isVisible=false
        pd_owner!!.isVisible=false
        pd_loaderinsurencephoto!!.isVisible=true
        pd_cargo!!.isVisible=false

        imageuploadAPICallForInsurence(url)

        insurenceImg_ = true

        isEditUserDone =true


    }

    private fun loadOwnershipImg(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervan!!.isVisible=false
        pd_owner!!.isVisible=true
        pd_loaderinsurencephoto!!.isVisible=false
        pd_cargo!!.isVisible=false

        imageuploadAPICallForOwner(url)

        ownership_ = true

        isEditUserDone =true

        /*  iv_img!!.alpha=1f
          iv_img!!.isEnabled=true*/


    }

    fun loadVehicleImg(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        pd_loadervan!!.isVisible=false
        pd_owner!!.isVisible=false
        pd_loaderinsurencephoto!!.isVisible=false
        pd_cargo!!.isVisible=true

        imageuploadAPICallForVehicle(url)

        vehicleImg1_ = true

        isEditUserDone =true

        /*  iv_img!!.alpha=1f
          iv_img!!.isEnabled=true*/


    }

    fun loadVehicleImgSecond(url: String) {

        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        vehicleImg2_ = true
        pd_owner!!.isVisible=false
        pd_loaderinsurencephoto!!.isVisible=false
        pd_cargo!!.isVisible=false
        pd_loadervan!!.isVisible=true

        imageuploadAPICallForVehicleSecond(url)


    }

    private fun imageuploadAPICallForInsurence(url: String) {

        try {

            /* AppProgressBar.openLoader(
                 this,
                 this.getResources().getString(R.string.pleasewait)
             )*/

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage3
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
                        pd_loaderinsurencephoto!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pd_loaderinsurencephoto!!.isVisible=false
                            Glide.with(this@VehicleinfoScreen).load(url)
                                .into(iv_insurencephoto!!)

                            storeImageVehicleInsurence = response.body()!!.detail!!.images.toString()

                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{
                            pd_loaderinsurencephoto!!.isVisible=false
                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loaderinsurencephoto!!.isVisible=false
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loaderinsurencephoto!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@VehicleinfoScreen,
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


    fun imageuploadAPICallForOwner(url: String) {

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
                        pd_owner!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pd_owner!!.isVisible=false
                            Glide.with(this@VehicleinfoScreen).load(url)
                                .into(iv_owner!!)

                            storeImageVehicleOwner = response.body()!!.detail!!.images.toString()

                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{
                            pd_owner!!.isVisible=false
                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_owner!!.isVisible=false
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_owner!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@VehicleinfoScreen,
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


    public fun imageuploadAPICallForVehicleSecond(url: String) {

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
                        pd_loadervan!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pd_loadervan!!.isVisible=false

                            Glide.with(this@VehicleinfoScreen).load(url)
                                .into(iv_van!!)

                            storeImageVehicleSecond =  response.body()!!.detail!!.images.toString()

                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{
                            pd_loadervan!!.isVisible=false

                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()

                        pd_loadervan!!.isVisible=false

                        Toast.makeText(
                            this@VehicleinfoScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_loadervan!!.isVisible=false

                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@VehicleinfoScreen,
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


    public fun imageuploadAPICallForVehicle(url: String) {

        try {

            /* AppProgressBar.openLoader(
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
                        pd_cargo!!.isVisible=false

                        if (response.body()!!.status==true) {

                            pd_cargo!!.isVisible=false

                            storeImageVehicle= response.body()!!.detail!!.images.toString()

                            Glide.with(this@VehicleinfoScreen).load(url)
                                .into(iv_cargo!!)


                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }else{

                            pd_cargo!!.isVisible=false

                            Toast.makeText(
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        //  AppProgressBar.closeLoader()
                        pd_cargo!!.isVisible=false


                        Toast.makeText(
                            this@VehicleinfoScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    //  AppProgressBar.closeLoader()
                    pd_cargo!!.isVisible=false

                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@VehicleinfoScreen,
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


     fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

     fun updateLabel() {

      //  val myFormat = "dd/MM/yy"
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
                req["vehicle_type"] = radioButton!!.getText().toString()
                req["licence_plate"] =licencplt!!.text.toString().trim()
                req["select_year"] = yearselect!!.text.toString().trim()
                req["model"] =makemodel!!.text.toString().trim()
                req["color"] =color!!.text.toString().trim()
                req["date_of_insurance_expiry"] =editDate!!.text.toString().trim()
                req["ownership_photo"] = storeImageVehicleOwner
                req["vehicle_insurance_photo"] = storeImageVehicleInsurence
                req["image1"] = storeImageVehicle
                req["image2"] = storeImageVehicleSecond
                response1 = apiservice.doingdriver_vehicle_information(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["vehicle_type"] = radioButton!!.getText().toString()
                req["licence_plate"] =licencplt!!.text.toString().trim()
                req["select_year"] = yearselect!!.text.toString().trim()
                req["model"] =makemodel!!.text.toString().trim()
                req["color"] =color!!.text.toString().trim()
                req["date_of_insurance_expiry"] =editDate!!.text.toString().trim()
                req["ownership_photo"] = storeImageVehicleOwner
                req["vehicle_insurance_photo"] = storeImageVehicleInsurence
                req["image1"] = storeImageVehicle
                req["image2"] = storeImageVehicleSecond
                response1 = apiservice.doingdriver_vehicle_information(token,req)


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
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                           /* val myIntent = Intent(this@vehicleinfoScreen,
                                DriverEvalution::class.java
                            )
                            startActivity(myIntent)
                            finish()*/


                            val dialog = Dialog(this@VehicleinfoScreen)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.custom_layout_for_pending)
                            val btn_close = dialog.findViewById(R.id.btn_close) as Button
                            val tv_headettxt = dialog.findViewById(R.id.tv_headettxt) as TextView
                            val tv_entiremsg = dialog.findViewById(R.id.tv_entiremsg) as TextView

                            tv_headettxt.text = getString(R.string.documents_verification)
                            tv_entiremsg.text = getString(R.string.complete_document_txt)

                            btn_close.setOnClickListener {
                                dialog.dismiss()
                                finishAffinity()
                              //  System.exit(0)
                            }
                            dialog.show()



                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.detail!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    )
                                ) {

                                    getLoggedOut()

                                }
                                else {


                                    Toast.makeText(
                                        this@VehicleinfoScreen,
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
                                this@VehicleinfoScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()


                        }
                    }
                }

                override fun onFailure(call: Call<DriverInfoPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@VehicleinfoScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@VehicleinfoScreen,
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

    private fun fetchLocation() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(this, PopupDiscloser::class.java)
            startActivity(myIntent)

           /* val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Location")
            startActivity(myIntent)*/

        }
        else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        )  {
            // Permission has already been granted

            // Toast.makeText(this, "Location permission Grant..", Toast.LENGTH_LONG).show()

            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)


        }

        else  if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Storage")
            startActivity(myIntent)

        }

        else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Phone")
            startActivity(myIntent)

        }
        else{

            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)
        }

    }


    override fun onBackPressed() {

        //   super.onBackPressed();
        return
        //super.onBackPressed()
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

                        Toast.makeText(this@VehicleinfoScreen, "There is currently another device logged into your account.", Toast.LENGTH_SHORT).show()

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


                        val myIntent = Intent(this@VehicleinfoScreen, WelcomeActivity::class.java)
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
                                        this@VehicleinfoScreen,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()


                            } else {

                                Toast.makeText(this@VehicleinfoScreen, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()



                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if (t is NoConnectivityException) {
                        Toast.makeText(this@VehicleinfoScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(this@VehicleinfoScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                    }

                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
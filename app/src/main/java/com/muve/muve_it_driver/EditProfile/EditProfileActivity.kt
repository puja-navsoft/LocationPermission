package com.muve.muve_it_driver.EditProfile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
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
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muve.muve_it_driver.EditProfileUpdate
import com.muve.muve_it_driver.ImagePickerActivity
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.editprofile.EditProfilePojo
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.create_password.CreatePasswordActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.*
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
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


class EditProfileActivity : AppCompatActivity() {

    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
    private var part1: String=""
    private var part2: String=""
    var encodedImage: String=""
    var totalname: String=""
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

    var token: String = ""
    var username: String = ""
    var firstName: String = ""
    var driver_image_status: Boolean = false
    var firstname_status: Boolean = false
    var lastname_status: Boolean = false
    var phonenumber_status: Boolean = false
    var countrycode_status: Boolean = false
    var email_status: Boolean = false
    var lastName: String = ""
  //  var REQUEST_IMAGE = 100
    var REQUEST_IMAGE = 0
    var REQUEST_DOCUMENT = 101
    var proofAttachmentType: String? = null
    var iv_user: CircleImageView? = null
    var iv_editPrflPic: ImageView? = null
    var tv_rating: TextView? = null
    var iv_nameEdit: ImageView? = null
    var ImageUrl: Uri? = null
    var ImageUrlPayOut: Uri? = null
    var iv_img: TextView? = null
    var tv_fullName: TextView? = null
    //  var tv_lastName: TextView? = null
    // var lastname: TextView? = null
    var tv_phone: TextView? = null
    var textView9: TextView? = null
    var tv_acceptanceRateVal: TextView? = null
    var tv_cancelRateVal: TextView? = null
    var tv_drivingTimeVal: TextView? = null
    var version_name: TextView? = null
    var tv_email: TextView? = null
    var response1: Call<EditProfilePojo>? = null
    var response2: Call<VerifyOtpPojo>? = null
    var responseProfile: Call<UserDetailPojo>? = null
    var storeuserimage: String?=""
    var storeuserfirstname: String?=""
    var storeuserlastname: String?=""
    var storeuserphone: String?=""
    var storeuserphonecode: String?=""
    var storeuseremail: String?=""
    var storepin: String?=""
    var fnameSharedPrefAfterReg: String = ""
    var lnameSharedPrefAfterReg: String = ""
    var tokenReg: String = ""

    var sharedPreferenceManager: SharedPreferenceManager? = null
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"
    private val BUFFER_SIZE = 1024 * 2

    var cameraCurrentImageTakenFile: File? = null
    var cameraCurrentImageTakenFileUri: Uri? = null
    val FILE_REQUEST_CODE_BY_CAMERA = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile2)


        tv_fullName = findViewById<TextView>(R.id.tv_fullName)
        //   tv_lastName = findViewById<TextView>(R.id.tv_lastName)
        // lastname = findViewById<TextView>(R.id.lastname)
        tv_phone = findViewById<TextView>(R.id.tv_phone)
        tv_email = findViewById<TextView>(R.id.tv_email)
        val tv_changePassword = findViewById<TextView>(R.id.tv_changePassword)
        val tv_changeLoginSocial = findViewById<TextView>(R.id.tv_changeLoginSocial)
        val iv_back = findViewById<ImageView>(R.id.iv_back)
        iv_nameEdit = findViewById<ImageView>(R.id.iv_nameEdit)
        val parent = findViewById<RelativeLayout>(R.id.parent)
        iv_editPrflPic = findViewById<ImageView>(R.id.iv_editPrflPic)
        tv_rating = findViewById<TextView>(R.id.tv_rating)
        iv_user = findViewById<CircleImageView>(R.id.iv_user)
        iv_img = findViewById<TextView>(R.id.iv_img)
        textView9 = findViewById<TextView>(R.id.textView9)
        tv_acceptanceRateVal = findViewById<TextView>(R.id.tv_acceptanceRateVal)
        tv_cancelRateVal = findViewById<TextView>(R.id.tv_cancelRateVal)
        tv_drivingTimeVal = findViewById<TextView>(R.id.tv_drivingTimeVal)
        version_name = findViewById<TextView>(R.id.version_name)

        //tv_firstName!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        sharedPreferenceManager = SharedPreferenceManager(this)


       // dataPopulate()

        iv_back.setOnClickListener {

            val intent = Intent(this , HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        parent.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)

        }


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


        }catch (e:java.lang.Exception){

            e.printStackTrace()
        }

        emailSharedPrefAfterLog = sharedPreferences!!.getString("emailSharedPrefAfterLog","").toString()
        emailSharedPrefAfterReg = sharedPreferences!!.getString("emailSharedPref","").toString()

        //31st jna

        phoneSharedPrefAfterLog = sharedPreferences!!.getString("phoneSharedPrefAfterLog","").toString()
        codeSharedPrefAfterLog = sharedPreferences!!.getString("codeSharedPrefAfterLog","").toString()
        phoneSharedPrefAfterReg = sharedPreferences!!.getString("phoneSharedPref","").toString()
        codeSharedPrefAfterReg = sharedPreferences!!.getString("codeSharedPref","").toString()



        tv_changePassword.setOnClickListener {

            doingForgetPasswordPhoneForOTP()


        }

        iv_editPrflPic!!.setOnClickListener {

            //  Toast.makeText(this, "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()

            AppUtilities.hideSoftKeyboard(this)

/*
            AlertDialogHelper.showCameraGalleryDialog(this, object : onItemClickReturnInt {
                override fun onItemClick(integerValue: Int) {
                    val multiplePermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (integerValue == 0) {
                        PermissionHelperClass.checkMultiplePermissionsForActivity(this@EditProfileActivity, true, multiplePermissions, getString(R.string.confirm), getString(R.string.pleaseGiveBothCameraAndStoragePermissions), FILE_REQUEST_CODE_BY_CAMERA,
                            object : MultiplePermissionHelperInterface {
                                override fun multiplePermissionGivenStatus(permissionGivenStatus: Boolean?) {
                                    if (!permissionGivenStatus!!) {
                                      //  showToast(this@EditProfileActivity, )
                                        Toast.makeText(this@EditProfileActivity, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
                                    } else {
                                        uploadFileByCamera()
                                    }
                                }
                            })
                    }

                 else if (integerValue == 1) {
                        PermissionHelperClass.checkMultiplePermissionsForActivity(this@EditProfileActivity, true, multiplePermissions, getString(R.string.confirm), getString(R.string.pleaseGiveBothCameraAndStoragePermissions), FILE_REQUEST_CODE
                        ) { permissionGivenStatus ->
                            if (!permissionGivenStatus!!) {
                                ShowToast.showShortToast(this@EditProfileActivity, getString(R.string.permission_denied))
                            } else {
                              //  uploadFile()

                            }
                        }
                    }
                }
            })
*/


             onPayInDocUpload()
        }

        Log.v("storeuserfirstname","storeuserfirstname"+intent.getStringExtra("storeuserfirstname")!!.toString() )


        // isEditUserDone=false

        iv_nameEdit!!.setOnClickListener {

            val myIntent = Intent(this, EditProfileUpdate::class.java)
            myIntent.putExtra("driver_image",storeuserimage)
            startActivity(myIntent)
            // finish()
        }

      //  fetchFevPlac()

    }

     fun uploadFileByCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraCurrentImageTakenFile = CameraHelper.saveAFileInExternalStorageAndGetTheFile(this, ".jpeg")
        cameraCurrentImageTakenFileUri = CameraHelper.getTheUriOfTheFileToSendInTheCameraIntent(this, cameraCurrentImageTakenFile)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraCurrentImageTakenFileUri)
        startActivityForResult(takePictureIntent, FILE_REQUEST_CODE_BY_CAMERA)
        Log.v("cmraCrentImgTknFile", cameraCurrentImageTakenFile.toString())



    }

/*
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == FILE_REQUEST_CODE_BY_CAMERA) {
            PermissionHelperClass.onRequestPermissionResultForActivity(this, true, getString(R.string.please_give_camera_permission),
                getString(R.string.please_give_permission_from_app_settings), permissions, grantResults, object : PermissionHelperInterface {

                    override fun singlePermissionGivenStatus(permissionGivenStatus: Boolean) {
                        if (permissionGivenStatus) {
                            uploadFileByCamera()
                        }
                    }
                })
        } */
/*else if (requestCode == FILE_REQUEST_CODE) {
            PermissionHelperClass.onRequestPermissionResultForActivity(this, true, getString(R.string.please_give_camera_permission),
                getString(R.string.please_give_permission_from_app_settings), permissions, grantResults) { permissionGivenStatus ->
                if (permissionGivenStatus) {
                    uploadFile()
                }
            }
        }*//*

    }
*/





    private fun doingForgetPasswordPhoneForOTP() {

        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req ["phone"] = intent.getStringExtra("storeuserphone")!!
            req["country_code"] = intent.getStringExtra("storeuserphonecode")!!


            response2 = apiservice.doingforgot_password_send_otp(req)
            // Call<RejectorderModel> response1 = apiservice.rejectitemorder(kitchecn_order_id,item_id,reason_id_);
            response2!!.enqueue(object : Callback<VerifyOtpPojo?> {
                override fun onResponse(
                    call: Call<VerifyOtpPojo?>,
                    response: Response<VerifyOtpPojo?>
                ) {

                    // AppProgressBar.closeLoader();
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if ( response.body() != null && response.body()!!.status == true) {


                            Toast.makeText(
                                this@EditProfileActivity,
                                "OTP sent successfully",
                                Toast.LENGTH_LONG
                            ).show()


                            val myIntent = Intent(this@EditProfileActivity, CreatePasswordActivity::class.java)

                            myIntent.putExtra("changepassword",true)
                            startActivity(myIntent)


                            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            myEdit = sharedPreferences!!.edit()

                            sharedPreferences!!.getString("phonenumberSharedPref","")!!
                            sharedPreferences!!.getString("phonecodeSharedPref","")!!

                            myEdit!!.clear().apply()

                            myEdit!!.putString("phonenumberSharedPref",intent.getStringExtra("storeuserphone")!!)
                            myEdit!!.putString("phonecodeSharedPref", intent.getStringExtra("storeuserphonecode")!!)

                            myEdit!!.apply()

                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.message!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true
                                    )
                                ) {

                                    val sharedPreferences =
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            this@EditProfileActivity,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                }
                                else {


                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }
                    } else {
                        // try {

                        if (response.body() != null) {
                            AppProgressBar.closeLoader();
                            Toast.makeText(
                                this@EditProfileActivity,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@EditProfileActivity,
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

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
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
                    // loading profile image from local cache
                    /* if (com.rest.rms.activities.DrawerManagementActivity.proofAttachmentType == "payIn") {
                         com.rest.rms.activities.DrawerManagementActivity.ImageUrl = uri
                     } else {
                         com.rest.rms.activities.DrawerManagementActivity.ImageUrlPayOut = uri
                     }*/
                    loadProfile(uri.toString())

                    val imageStream: InputStream? = contentResolver.openInputStream(ImageUrl!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    encodedImage= encodeImage(selectedImage)!!

                    Log.v("image64",""+encodedImage)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    public fun loadProfile(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )
        isEditUserDone=true

        Glide.with(this).load(url).into(iv_user!!)

        iv_img!!.isEnabled=true
        //  iv_img!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
        iv_img!!.alpha=1f



    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }


    private fun editProfile(part1:String , part2:String) {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["id"] = defaultIdReg
                req["firstname"] =  /*part1*/ firstName
                req["lastname"] =/*part2*/ lastName
                req["phone_number"] = storeuserphone!!
                req["country_code"] = storeuserphonecode!!
                req["email"] = storeuseremail!!

                if (isEditUserDone==true) {
                    driver_image_status=true
                    req["driver_image"] = "data:image/png;base64," + encodedImage
                    req["driver_image_status"] = driver_image_status
                }else{
                    driver_image_status=false
                    req["driver_image"] =""
                    req["driver_image_status"] =driver_image_status
                }
                req["firstname_status"] =firstname_status
                req["lastname_status"] =lastname_status
                req["phone_number_status"] =phonenumber_status
                req["country_code_status"] =countrycode_status
                req["email_status"] =email_status

                response1 = apiservice.doingEditProfile(tokenReg,req)

            }else{
                req["id"] = defaultIdLog
                req["firstname"] =  /*part1*/ firstName
                req["lastname"] =/*part2*/ lastName
                req["phone_number"] = storeuserphone!!
                req["country_code"] = storeuserphonecode!!
                req["email"] = storeuseremail!!

                if (isEditUserDone==true) {
                    driver_image_status=true
                    req["driver_image"] = "data:image/png;base64," + encodedImage
                    req["driver_image_status"] = driver_image_status
                }else{
                    driver_image_status=false
                    req["driver_image"] =""
                    req["driver_image_status"] = driver_image_status
                }

                req["firstname_status"] =firstname_status
                req["lastname_status"] =lastname_status
                req["phone_number_status"] =phonenumber_status
                req["country_code_status"] =countrycode_status
                req["email_status"] =email_status

                // req["customer_image"] = "data:image/png;base64,"+encodedImage
                response1 = apiservice.doingEditProfile(token,req)

            }

            response1!!.enqueue(object : Callback<EditProfilePojo?> {
                override fun onResponse(
                    call: Call<EditProfilePojo?>,
                    response: Response<EditProfilePojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if ( response.body() != null && response.body()!!.status==true) {


                            isEditUserDone=false
                            iv_img!!.isEnabled=false
                            iv_img!!.isClickable=false

                            Toast.makeText(
                                this@EditProfileActivity,
                                response.body()!!.detail.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            Glide.with(this@EditProfileActivity).load(NetworkUtility.imgbaseUrl+response.body()!!.customerImage!!.driver_image!!.trim())/*.placeholder(R.drawable.defaultperson)*/.error(R.drawable.defaultperson).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(iv_user!!)

                            tv_fullName!!.setText(firstName +" "+lastName/*response.body()!!.customerImage!!.firstname +" "+response.body()!!.customerImage!!.lastname*/)
                            iv_img!!.alpha=0.5f
                            iv_img!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))


                            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            myEdit = sharedPreferences!!.edit()

                            myEdit!!.putString("encodeimageurl", encodedImage)
                            myEdit!!.apply()
                            myEdit!!.commit()


                        }else {

                            if (response.body() != null) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    response.body()!!.detail.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }


                    }
                    else {

                        AppProgressBar.closeLoader()

                        if (response.body() != null) {

                            if (response.body()!!.detail.equals(
                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    ignoreCase = true
                                )
                            ) {

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(
                                        this@EditProfileActivity,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()

                            } else {


                                Toast.makeText(
                                    this@EditProfileActivity,
                                    response.body()!!.detail.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                        }
                    }
                }

                override fun onFailure(call: Call<EditProfilePojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@EditProfileActivity,
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


    private fun fetchFevPlac() {


        try {

            AppProgressBar.openLoader(
                this,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            if (defaultIdLog.equals("")){

                req["driver_id"] = defaultIdReg
                responseProfile = apiservice.getuserdetails(tokenReg,req )

            }

            else {
                req["driver_id"] = defaultIdLog
                responseProfile = apiservice.getuserdetails(token,req )

            }
            responseProfile!!.enqueue(object : Callback<UserDetailPojo?>/*,
                RecyclerViewItemClickListenerFavPlace*/
            /*RecyclerViewItemClickListenerFavPlace*/ {
                override fun onResponse(
                    call: Call<UserDetailPojo?>,
                    response: Response<UserDetailPojo?>
                ) {
                    if (response.body()==null){
                        AppProgressBar.closeLoader()
                        return
                    }

                    else if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status == true) {

                            storeuserimage = response.body()!!.detail!!.driver_image
                            firstName = response.body()!!.detail!!.firstname!!
                            lastName = response.body()!!.detail!!.lastname!!
                            storeuserphone = response.body()!!.detail!!.phoneNumber
                            storeuserphonecode = response.body()!!.detail!!.countryCode
                            storeuseremail = response.body()!!.detail!!.email
                            storepin = response.body()!!.detail!!.fixedPin

                            Glide.with(this@EditProfileActivity).load(NetworkUtility.imgbaseUrl+response.body()!!.detail!!.driver_image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true) .into(iv_user!!)

                            tv_fullName!!.setText(response.body()!!.detail!!.firstname+" "+response.body()!!.detail!!.lastname)
                            tv_phone!!.setText("Phone No : " + response.body()!!.detail!!.countryCode +" "+ response.body()!!.detail!!.phoneNumber)
                            tv_email!!.setText("Email Id : " + response.body()!!.detail!!.email)
                            tv_rating!!.setText(response.body()!!.detail!!.rating)

                        }

                        else {

                            AppProgressBar.closeLoader()

                            if (response.body()!!.message.equals(resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true)){

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(this@EditProfileActivity, WelcomeActivity::class.java)
                                startActivity(myIntent)

                                finish()

                            }

                            else{
                                AppProgressBar.closeLoader()


                                Toast.makeText(
                                    this@EditProfileActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }


                        }
                    } else {
                        AppProgressBar.closeLoader()

                    }
                }

                override fun onFailure(call: Call<UserDetailPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@EditProfileActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()


        if (encodedImage.equals("")) {

            if (NetworkUtility.isNetworkAvailable(this)) {
                fetchFevPlac()
            }
            else{
                Toast.makeText(
                    this,
                    getString(R.string.msg_no_internet),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        iv_img!!.setOnClickListener {

            if (tv_fullName!!.text.toString().contains(" ")) {
               /* val parts: List<String> = tv_fullName!!.text.toString().split(" ")
                val firstName =parts[0]firstName
                val latsName = parts[1]lastName*/
                editProfile(firstName , lastName)
                driver_image_status = true
            }else{
              //  editProfile(tv_fullName!!.text.toString() , "")
                editProfile(firstName , lastName)
                driver_image_status = true
            }

        }

        if (isEditUserDone==true) {

            iv_img!!.setBackground(resources.getDrawable(R.drawable.round_background_2))
            iv_img!!.isEnabled=true
            // iv_img!!.isClickable=true


        }
        else{

            iv_img!!.setBackground(resources.getDrawable(R.drawable.round_background_2_grey))
            iv_img!!.isEnabled=false
        }


    }

}
package com.muve.muve_it_driver.bankdetailsscreen

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.format.Formatter
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muve.muve_it_driver.*
import com.muve.muve_it_driver.model.criminalrecord.CriminalRecordResponse
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.NetworkUtility.Companion.ONLINE_POLICE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import kotlin.collections.HashMap


class BankDetailsScreen : AppCompatActivity() {

    var fileval: File ? = null
    var file: File? = null
    var docName: String ? = null
    var et_accountHolderName :EditText ?= null
    var editAccountNumber :EditText ?= null
    var etTransitNumber :EditText ?= null
    var editTransit :EditText ?= null
    var editInstitution :EditText ?= null
    var editBranch :EditText ?= null
    var btn_cntnue :Button ?= null
    var btn_close :Button ?= null
    var transparentView :View ?= null
    var iv_criminalrecord :ImageView ?= null
    var tv_filenm :TextView ?= null
    var clickcheckcriminalrecord :TextView ?= null
    var vehicleImg1 : Boolean = false
    var includebottomsheetdocverification :RelativeLayout ?= null
    var defaultIdLog: String = ""
    var defaultIdReg: String = ""
    var token: String = ""
    var tokenReg: String = ""
    var encodeFileToBase64Binary: String = ""
    var sharedPreferences: SharedPreferences? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    // var REQUEST_IMAGE = 100
    var REQUEST_IMAGE = 0
    // var REQUEST_DOCUMENT = 101
    var REQUEST_DOCUMENT = 2
    var ImageUrl: Uri? = null
    var encodedImage: String? = null
    var docFile: Uri? = null
    var responsePdf: Call<ImageUploadResponse>? = null
    var iv_criminalrecordcam: ImageView? = null
    var iv_criminalrecorddummy: ImageView? = null
    var storepdf: String ? = null
    var pdloader: ProgressBar? = null
    var receiver: BroadcastReceiver? = null
    var responseDriverStatusSend: Call<Driverstatusstoreresponse>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var IPaddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details_screen)

        et_accountHolderName = findViewById<EditText>(R.id.et_accountHolderName)
        iv_criminalrecordcam = findViewById<ImageView>(R.id.iv_criminalrecordcam)
        iv_criminalrecorddummy = findViewById<ImageView>(R.id.iv_criminalrecorddummy)
        editAccountNumber = findViewById<EditText>(R.id.editAccountNumber)
        // editTransit = findViewById<EditText>(R.id.editTransit)
        editInstitution = findViewById<EditText>(R.id.editInstitution)
        editBranch = findViewById<EditText>(R.id.editBranch)
        btn_cntnue = findViewById<Button>(R.id.btn_cntnue)
        btn_close = findViewById<Button>(R.id.btn_close)
        transparentView = findViewById<View>(R.id.transparentView)
        iv_criminalrecord = findViewById<ImageView>(R.id.iv_criminalrecord)
        tv_filenm = findViewById<TextView>(R.id.tv_filenm)
        pdloader = findViewById<ProgressBar>(R.id.pdloader)
        etTransitNumber = findViewById<EditText>(R.id.etTransitNumber)
        clickcheckcriminalrecord = findViewById<TextView>(R.id.clickcheckcriminalrecord)
        includebottomsheetdocverification = findViewById<RelativeLayout>(R.id.includebottomsheetdocverification)
        bottomSheetBehavior = BottomSheetBehavior.from<RelativeLayout>(includebottomsheetdocverification!!)

        sharedPreferenceManager = SharedPreferenceManager(this)



        try {

            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            token = sharedPreferences!!.getString("Authorization", "")!!
            tokenReg = sharedPreferences!!.getString("AuthSharedPref", "")!!
            sharedPreferences!!.getString("stripe_customer_id", "")

        }catch (e:Exception){
            e.printStackTrace()
        }

        clickcheckcriminalrecord!!.setOnClickListener {

            val uri: Uri = Uri.parse(ONLINE_POLICE_URL)

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)


        }



        btn_cntnue!!.setOnClickListener {


            // Toast.makeText(this@BankDetailsScreen, "submit", Toast.LENGTH_LONG).show()

            if (et_accountHolderName!!.text.toString().trim().equals("")){

                et_accountHolderName!!.setError("Please give holder name")
                et_accountHolderName!!.requestFocus()

            }else if (editAccountNumber!!.text.toString().trim().equals("")){

                editAccountNumber!!.setError("Please give account number")
                editAccountNumber!!.requestFocus()
            }
            else if (editInstitution!!.text.toString().trim().equals("")){

                editInstitution!!.setError("Please give transit number")
                editInstitution!!.requestFocus()
            }
            else if (etTransitNumber!!.text.toString().trim().equals("")){

                etTransitNumber!!.setError("Please give institution number")
                etTransitNumber!!.requestFocus()
            }


            else if (/*encodeFileToBase64Binary.equals("") &&*/ storepdf.equals("") || storepdf.equals(null)){

                Toast.makeText(this@BankDetailsScreen, "submit your criminal record document", Toast.LENGTH_LONG).show()
            }
            else {

                getbankdetails()

            }

        }


        iv_criminalrecord!!.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = true
            onPayInDocUpload()

        }


        iv_criminalrecorddummy!!.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)
            vehicleImg1 = true
            onPayInDocUpload()

        }

        btn_close!!.setOnClickListener {

            finishAffinity()
           // System.exit(0)

        }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                try {

                     if (intent.getIntExtra(
                            "status_codeFromPushMultipleDeviceLogin",
                            0
                        ) == 204
                    ) {

                            Toast.makeText(this@BankDetailsScreen, "There is currently another device logged into your account.", Toast.LENGTH_SHORT).show()

                            getLoggedOut()


                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }


    }


    fun networdDetect() {

        var WIFI = false

        var MOBILE = false

        val CM = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        val networkInfo = CM!!.allNetworkInfo

        for (netInfo in networkInfo) {
            if (netInfo.typeName.equals("WIFI", ignoreCase = true)) if (netInfo.isConnected) WIFI =
                true
            if (netInfo.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (netInfo.isConnected) MOBILE = true
        }

        if (WIFI == true) {
            IPaddress = GetDeviceipWiFiData()

        }

        if (MOBILE == true) {
            IPaddress = GetDeviceipMobileData()

        }


    }

    fun GetDeviceipWiFiData(): String? {

        val wm =getSystemService(WIFI_SERVICE) as WifiManager?
        return Formatter.formatIpAddress(wm!!.connectionInfo.ipAddress);

    }

    fun GetDeviceipMobileData(): String? {

        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return null
    }


    private fun onPayInDocUpload() {

        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showPdfPickerOptions()
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


    fun showPdfPickerOptions() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_proof_attachment_pdf)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val tvTakePicture = dialog.findViewById<TextView>(R.id.tvTakePicture)
        val tvChooseGallery = dialog.findViewById<TextView>(R.id.tvChooseGallery)
        val tvChooseDocument = dialog.findViewById<TextView>(R.id.tvChooseDocument)
        val IvDialogClose = dialog.findViewById<ImageView>(R.id.IvDialogClose)



        IvDialogClose.setOnClickListener { dialog.dismiss() }

        tvTakePicture.setOnClickListener {

            try {
                ImagePicker.with(this)
                    .cropSquare() //Crop image(Optional), Check Customization for more option
                    .compress(1024) //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(
                        1080,
                        1080
                    ) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(0)
            }catch (e:Exception){
                e.printStackTrace()
            }


            // listener.onTakeCameraSelected()
            dialog.dismiss()
        }

        /* tvChooseGallery.setOnClickListener {
        listener.onChooseGallerySelected()
        dialog.dismiss()
        }*/


        tvChooseDocument.setOnClickListener {

            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val chooseFile = Intent()
                            chooseFile.type = "application/pdf"
                            chooseFile.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(
                                Intent.createChooser(chooseFile, "Select Pdf"),
                                ImagePickerActivity.REQUEST_DOCUMENT
                            )
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()


            // listener.onDocumentSelected()
            dialog.dismiss()
        }

        /* ImagePickerActivity.showPdfPickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
        override fun onTakeCameraSelected() {
        launchCameraIntent()
        }

        override fun onChooseGallerySelected() {
        launchGalleryIntent()
        }

        override fun onDocumentSelected() {
        launchDocumentIntent()
        }
        })*/
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
        intent.putExtra(ImagePickerActivity.INTENT_DOCUMENT_PICKER_OPTION, ImagePickerActivity.REQUEST_DOCUMENT)

        startActivityForResult(intent, REQUEST_DOCUMENT)

    }
    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // val uri = data!!.getParcelableExtra<Uri>("path")

                val uri = data!!.data

                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    ImageUrl = uri

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
        else if (requestCode == REQUEST_DOCUMENT) {
            if (resultCode == RESULT_OK) {
                // val uri = data!!.getParcelableExtra<Uri>("path")

                val uri = data!!.data

                Log.e("URI_SCHEME", " >>> " + uri!!.scheme)

                if (checkDocSize(uri)) {
                    docFile = uri
                    setDocName()

                    Log.d("", "URI = $uri")

                    Log.d("", "Chosen path ="+contentResolver.openInputStream(uri))
                    // Log.d("", "Chosen path ="+uri.toFile())
                    val f: File = getFile(applicationContext, uri)!!
                    Log.d("", "Chosen pathf ="+f)


                    encodeFileToBase64Binary = convertToBase64(f)
                    // val selectedFilePath: String = FilePath.getPath(this, uri)!!
                    // val file = File(selectedFilePath)

                    Log.v("encodeFileBase64Binary",""+encodeFileToBase64Binary)

                    loadpdfImgSecond()


                } else {
                    Toast.makeText(
                        this,
                        "File error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Log.e("DOCUMENT URL", " >>> $uri")
            }
        }
    }

    fun loadProfile(url: String) {
        Log.d(
            "ImagePath",
            "Image cache path: $url"
        )

        pdloader!!.isVisible=true
        imageuploadAPICall(url)

    }

    fun imageuploadAPICall(url: String) {


        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = java.util.HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:image/png;base64,"+encodedImage
                responsePdf = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:image/png;base64,"+encodedImage
                responsePdf = apiservice.doingImageUpload(token,req)

            }

            responsePdf!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful() && response.body()!=null) {
                        // AppProgressBar.closeLoader()
                        pdloader!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pdloader!!.isVisible=false
                            iv_criminalrecorddummy!!.visibility = View.GONE
                            // tv_filenm!!.setText(docName +".pdf")

                            storepdf = response.body()!!.detail!!.images.toString()

                            Glide.with(this@BankDetailsScreen).load(url)
                                .into(iv_criminalrecord!!)

                            tv_filenm!!.visibility = View.INVISIBLE

                        }else{
                            pdloader!!.isVisible=false
                            Toast.makeText(
                                this@BankDetailsScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        pdloader!!.isVisible=false
                        Toast.makeText(
                            this@BankDetailsScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    // AppProgressBar.closeLoader()
                    pdloader!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@BankDetailsScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@BankDetailsScreen,
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


    fun loadpdfImgSecond() {

        try {

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = java.util.HashMap<String, Any>()


            if (defaultIdLog.equals("")){
                req["driver_id"] = defaultIdReg
                req["image"] = "data:application/pdf;base64,"+encodeFileToBase64Binary
                responsePdf = apiservice.doingImageUpload(tokenReg,req)

            }else{
                req["driver_id"] = defaultIdLog
                req["image"] = "data:application/pdf;base64,"+encodeFileToBase64Binary
                responsePdf = apiservice.doingImageUpload(token,req)

            }

            responsePdf!!.enqueue(object : Callback<ImageUploadResponse?> {
                override fun onResponse(
                    call: Call<ImageUploadResponse?>,
                    response: Response<ImageUploadResponse?>
                ) {
                    if (response.isSuccessful() && response.body()!=null) {
                        // AppProgressBar.closeLoader()
                        pdloader!!.isVisible=false
                        if (response.body()!!.status==true) {
                            pdloader!!.isVisible=false
                            tv_filenm!!.visibility = View.VISIBLE

                            tv_filenm!!.setText(docName +".pdf")

                            storepdf = response.body()!!.detail!!.images.toString()

                        }else{
                            pdloader!!.isVisible=false

                            Toast.makeText(
                                this@BankDetailsScreen,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    } else {

                        pdloader!!.isVisible=false
                        Toast.makeText(
                            this@BankDetailsScreen,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse?>, t: Throwable) {
                    // AppProgressBar.closeLoader()
                    pdloader!!.isVisible=false
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@BankDetailsScreen,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@BankDetailsScreen,
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

    private fun checkDocSize(uri: Uri): Boolean {
        file = File(uri.path)
        val file_size: Int = java.lang.String.valueOf(file!!.length() / (1024 * 1024)).toInt()
        Log.e("FILE SIZE", " >>> $file_size")
        return file_size <= 2
    }

    private fun setDocName() {

        docName = getFileName(docFile!!)!!

        iv_criminalrecorddummy!!.visibility = View.VISIBLE
        iv_criminalrecordcam!!.visibility = View.GONE
        Log.e("docName", " >>> $docName")

    }

    fun getFileName(uri: Uri): String? {

        fileval = File(uri.path)
        return fileval!!.absolutePath
        Log.v("absolutePath",""+fileval!!.absolutePath)
    }

    @Throws(IOException::class)
    fun getFile(context: Context, uri: Uri): File? {
        val destinationFilename = File(
            context.getFilesDir().getPath() + File.separatorChar.toString() + queryName(
                context,
                uri
            )
        )
        try {
            context.getContentResolver().openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(
                        ins,
                        destinationFilename
                    )
                }
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
        return destinationFilename
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: Exception) {
            Log.e("Save File", ex.message!!)
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String {
        val returnCursor: Cursor =
            context.getContentResolver().query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    fun convertToBase64(attachment: File): String {
        return Base64.encodeToString(attachment.readBytes(), Base64.NO_WRAP)
    }



    private fun getbankdetails() {

        var response1: Call<CriminalRecordResponse>? = null

        try {

            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog", "")!!
            defaultIdReg = sharedPreferences!!.getString("idSharedPref", "")!!
            token = sharedPreferences!!.getString("Authorization", "")!!
            tokenReg = sharedPreferences!!.getString("AuthSharedPref", "")!!
            sharedPreferences!!.getString("stripe_customer_id", "")
            AppProgressBar.openLoader(this, resources.getString(R.string.pleasewait))

            val logReq = HashMap<String, Any>()

            if (defaultIdLog.equals("")) {

                logReq["driver_id"] = defaultIdReg!!
                logReq["account_holder_name"] = et_accountHolderName!!.text.toString().trim()!!
                logReq["institution_number"] = editInstitution!!.text.toString().trim()!!+"-"+etTransitNumber!!.text.toString().trim() /*"110000000"*/
                logReq["account_number"] = editAccountNumber!!.text.toString().trim()!! /*"000123456789"*/
                logReq["criminal_record_photo"] = storepdf !!
                logReq["stripe_ip"] = IPaddress!!

                response1 = ApiClient.getClient(this).create(ApiInterface::class.java)
                    .getadd_bank_account_details(tokenReg, logReq)
            } else {

                logReq["driver_id"] = defaultIdLog!!
                logReq["account_holder_name"] = et_accountHolderName!!.text.toString().trim()!!
                logReq["institution_number"] = editInstitution!!.text.toString().trim()!!+"-"+etTransitNumber!!.text.toString().trim() /*"110000000"*/
                logReq["account_number"] = editAccountNumber!!.text.toString().trim()!! /*"000123456789"*/
                logReq["criminal_record_photo"] = storepdf !!
                logReq["stripe_ip"] = IPaddress!!

                response1 = ApiClient.getClient(this).create(ApiInterface::class.java)
                    .getadd_bank_account_details(token, logReq)
            }

            response1!!.enqueue(object : Callback<CriminalRecordResponse?> {
                override fun onResponse(
                    call: Call<CriminalRecordResponse?>,
                    response: Response<CriminalRecordResponse?>
                ) {

                    if (response.body() != null && response.body()!!.status == true) {
                        AppProgressBar.closeLoader()
                        Toast.makeText(this@BankDetailsScreen, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        if (response.body()!!.detail != null) {

                            Toast.makeText(
                                this@BankDetailsScreen,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()


                            btn_cntnue!!.visibility = View.GONE
                            transparentView!!.visibility = View.VISIBLE

                            bottomSheetBehavior!!.peekHeight = resources.getDimension(R.dimen._180sdp).toInt()
                            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                            bottomSheetBehavior.isDraggable = false
                        }

                    }
                    else {

                        if (response.body() != null) {
                            AppProgressBar.closeLoader()

                            Toast.makeText(
                                this@BankDetailsScreen,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()

                            getLoggedOut()

                        }
                    }
                }

                override fun onFailure(call: Call<CriminalRecordResponse?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        // show No Connectivity message to user or do whatever you want.
                        Toast.makeText(this@BankDetailsScreen, t.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@BankDetailsScreen, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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


                                val myIntent = Intent(this@BankDetailsScreen, WelcomeActivity::class.java)
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
                                                this@BankDetailsScreen,
                                                WelcomeActivity::class.java
                                            )
                                        startActivity(myIntent)

                                        finish()


                                } else {

                                        Toast.makeText(this@BankDetailsScreen, response1.body()!!.message.toString(), Toast.LENGTH_SHORT).show()



                                }

                            }
                        }
                    }

                    override fun onFailure(call: Call<Driverstatusstoreresponse?>, t: Throwable) {
                        AppProgressBar.closeLoader()

                            if (t is NoConnectivityException) {
                                Toast.makeText(this@BankDetailsScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(this@BankDetailsScreen, "" + t.message, Toast.LENGTH_SHORT).show()
                            }

                    }
                })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    override fun onBackPressed() {

        // super.onBackPressed();
        return
        //super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()

        networdDetect()

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver!!),
            IntentFilter("com.muve.muve_it_driver")
        )
    }
}
package com.muve.muve_it_driver.ui.auth.login

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.bankdetailsscreen.BankDetailsScreen
import com.muve.muve_it_driver.databinding.ActivityLogInBinding
import com.muve.muve_it_driver.driverevaluation.DriverEvalution
import com.muve.muve_it_driver.driverinformation.DriverInformationScreen
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.UserResponse
import com.muve.muve_it_driver.model.loginmodel.LoginPojo
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.popupdiscloser.PopupDiscloser
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment
import com.muve.muve_it_driver.ui.auth.create_password.CreatePasswordActivity
import com.muve.muve_it_driver.ui.auth.permissionscreen.PermissionScreen
import com.muve.muve_it_driver.ui.auth.verifycode.VerifyCodeFragment
import com.muve.muve_it_driver.ui.auth.verifycodeemail.VerifyEmailFragment
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.muve.muve_it_driver.util.isLogInButtonClicked
import com.muve.muve_it_driver.vehicleinformation.VehicleinfoScreen
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.txt_country_code
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogInActivity : AppCompatActivity(),OSSubscriptionObserver,
    AuthListener {

    var isPasswordHide: Boolean = true
    var defaultId: String? = ""
    var storeFirstname: String? = ""
    var storeLastname: String? = ""
    var storeEmail: String? = ""
    var storePhone: String? = ""
    var storeCode: String? = ""
    var device_id: String? = ""
    var token: String? = ""
    var UUID: String? = ""
    var model: String? = ""
    var bottomSheetFragment: SelectCountryCodeFragment? = null
    var verifyCodeBottomsheet: VerifyCodeFragment? = null
    var verifyCodeBottomsheet1: VerifyEmailFragment? = null
    val locationPermission = ACCESS_FINE_LOCATION
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    lateinit var binding: ActivityLogInBinding
    var response1: Call<LoginPojo>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    private val MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: Int = 2
    private val MY_PERMISSIONS_REQUEST_ACCESS_STORAGE: Int = 3
    private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: Int = 1
    private val MY_PERMISSIONS_REQUEST_ACCESS_PHONE: Int = 4
    var response2: Call<VerifyOtpPojo>? = null
    var sharedPreferences: SharedPreferences? = null
    var myEdit: SharedPreferences.Editor? = null
    var myEdit1: SharedPreferences.Editor? = null
    var locationallow: Boolean? = false
    var versionName :String?=""

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.login = viewModel
        viewModel.myAuthListener = this
        sharedPreferenceManager = SharedPreferenceManager(this)
        bottomSheetFragment = SelectCountryCodeFragment()
        verifyCodeBottomsheet = VerifyCodeFragment()
        verifyCodeBottomsheet1 = VerifyEmailFragment()
        binding.checkBox.isChecked = true
        binding.edtPhoneNumber.requestFocus()
        binding.edtPhoneNumber.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        // getFCMToken()
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val pInfo: PackageInfo =
            getPackageManager().getPackageInfo(getPackageName(), 0)
        versionName  = pInfo.versionName

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId("47dc713b-e510-4d58-b2a3-cdc626332793")
        OneSignal.addSubscriptionObserver(this)

        OneSignal.disablePush(false)

        val device = OneSignal.getDeviceState()
        val subscribed: Boolean = device!!.isSubscribed()
        Log.i("Debugsubscribed", "onOSSubscriptionChangedsubscribed: " +subscribed)

        // OneSignal.promptForPushNotifications(false)
        OneSignal.addSubscriptionObserver {

            if (!it.from.isSubscribed &&
                it!!.to.isSubscribed) {
                /* AlertDialog.Builder(this)
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show()*/
                // get player ID
                it.getTo().getUserId()
            }

            Log.i("Debugstate", "onOSSubscriptionChanged: " + it)

            token = it.getTo().getUserId()
        }

        token =OneSignal.getDeviceState()!!.getUserId();


        /* FirebaseMessaging.getInstance().token
             .addOnCompleteListener { task ->
                 if (!task.isSuccessful) {
                     //   Log.d("isSuccessful","=="+token)
                     // token=""
                     return@addOnCompleteListener
                 }
                 // Get new Instance ID token
                 token = task.result!!
                 Log.d("token", "==" + token)
             }*/



        device_id = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        )


        getYourDeviceName()

        if (intent.getStringExtra("fromsplash").equals("fromsplash")) {

            doingLogin(false, token!!)

        }


        //10jan
/*
if(sharedPreferenceManager!!.loginUserData !=null){
  if(sharedPreferenceManager!!.loginUserData.isRemember == true){
      binding.edtPhoneNumber.setText(sharedPreferenceManager!!.loginUserData.mobileNumber.toString())
      binding.edtPassword.setText(sharedPreferenceManager!!.loginUserData.password.toString())
      binding.txtCountryCode.setText(sharedPreferenceManager!!.loginUserData.cCode.toString())
      binding.checkBox.isChecked=true
  }
}else{
    binding.edtPhoneNumber.setText("")
    binding.edtPassword.setText("")
    binding.txtCountryCode.setText("+1")
    binding.checkBox.isChecked=false
}*/

        /* if(sharedPreferenceManager!!.loginUserData !=null){
             if(sharedPreferenceManager!!.loginUserData.isRemember == true){
                 binding.edtPhoneNumber.setText(sharedPreferenceManager!!.loginUserData.mobileNumber.toString())
                 binding.edtPassword.setText(sharedPreferenceManager!!.loginUserData.password.toString())
                 binding.txtCountryCode.setText(sharedPreferenceManager!!.loginUserData.cCode.toString())
                 binding.checkBox.isChecked=true
             }
         }else{
             binding.edtPhoneNumber.setText("")
             binding.edtPassword.setText("")
             binding.txtCountryCode.setText("+1")
           //  binding.txtCountryCode.setText("+1")
             binding.checkBox.isChecked=false
         }
 */



        binding.edtPhoneNumber.setOnClickListener {

            binding.edtPhoneNumber.setCursorVisible(true)

        }

        binding.parent.setOnClickListener {

            AppUtilities.hideSoftKeyboard(this)

        }

        binding.edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                Log.v("charSequence", "charSequence" + charSequence)

                if (charSequence.length == 0) {

                    binding.linLay.isVisible = true
                }

                if (charSequence.matches(Regex("[0-9]+"))) {

                    Log.v("charfound", "charSequence" + charSequence)
                    binding.linLay.isVisible = true

                    if (charSequence.length == 0) {

                        binding.linLay.isVisible = true
                    }
                } else {

                    binding.linLay.isVisible = false

                    if (charSequence.length == 0) {

                        binding.linLay.isVisible = true
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })


        binding.txtForgotPassword.setOnClickListener {

            if (binding.edtPhoneNumber.text.toString().equals("")) {

                Toast.makeText(this, "Please enter phone number/email address", Toast.LENGTH_LONG)
                    .show()
                binding.edtPhoneNumber.requestFocus()

            } else {

                doingForgetPasswordPhoneForOTP()
            }


            /* val myIntent = Intent(this, ForgetPasswordFirstStep::class.java)
             startActivity(myIntent)*/


        }


        if (intent.hasExtra("updateprofile")) {

            if (intent.getBooleanExtra("updateprofile", true)) {

                isLogInButtonClicked = true

                if (token.equals("")) {

                  /*  FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                //   Log.d("isSuccessful","=="+token)
                                //token=""
                                return@addOnCompleteListener
                            }
                            // Get new Instance ID token
                            token = task.result!!
                            Log.d("token1", "==" + token)

                            doingLogin(true, token!!)
                        }*/

                    UUID = OneSignal.getDeviceState()!!.userId
                    token = UUID
                    doingLogin(true, token!!)

                } else {

                    doingLogin(true, token!!)
                }


            } else {


            }

        }


    }


    private fun getYourDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        model = Build.MODEL
        return if (model!!.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            capitalize(model!!)
        } else {
            capitalize(manufacturer).toString() + " " + model
        }
    }

    private fun capitalize(s: String): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }


    override fun showHidePassword() {

        if (isPasswordHide) {

            isPasswordHide = false
            edt_password?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmpassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.edtPassword.setSelection(binding.edtPassword.text.length)
            tv_showclick_login.text = getString(R.string.tv_hide_password)
            //  tv_showclickConfirmPas.text = getString(R.string.tv_hide_password)

        } else {

            isPasswordHide = true
            edt_password?.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmpassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.edtPassword.setSelection(binding.edtPassword.text.length)

            tv_showclick_login.text = getString(R.string.tv_show_password)
            //   tv_showclickConfirmPas.text = getString(R.string.tv_show_password)


        }
    }


    override fun onStarting() {

    }

    override fun onSuccess() {


        if (binding.edtPhoneNumber.text.toString().equals("")) {

            Toast.makeText(this, "Enter Phone number to continue", Toast.LENGTH_LONG).show()
            binding.edtPhoneNumber.requestFocus()

        } else if (binding.edtPassword.text.toString().equals("")) {

            Toast.makeText(this, "Enter your password to continue", Toast.LENGTH_LONG).show()
            binding.edtPassword.requestFocus()

        } else {

            //10jan

            /* if(binding.checkBox.isChecked){
                 val userdata = UserResponse()
                 userdata.isRemember=true
                 userdata.password=binding.edtPassword.text.toString()
                 userdata.mobileNumber=binding.edtPhoneNumber.text.toString()
                 userdata.cCode=binding.txtCountryCode.text.toString()
                 sharedPreferenceManager?.saveLoginUserData(userdata)
             }
             else{
                 val userdata = UserResponse()
                 userdata.isRemember=false
                 userdata.password=binding.edtPassword.text.toString()
                 userdata.mobileNumber=binding.edtPhoneNumber.text.toString()
                 userdata.cCode=binding.txtCountryCode.text.toString()
                 sharedPreferenceManager?.saveLoginUserData(userdata)
             }
 */



        if (token!=null) {
            doingLogin(false, token!!)
        }
            else{

            OneSignal.addSubscriptionObserver {

                if (!it.from.isSubscribed &&
                    it!!.to.isSubscribed) {
                    /* AlertDialog.Builder(this)
                        .setMessage("You've successfully subscribed to push notifications!")
                        .show()*/
                    // get player ID
                    it.getTo().getUserId()
                }

                Log.i("Debugstate", "onOSSubscriptionChanged: " + it)

                token = it.getTo().getUserId()
            }

        }

            /* val myIntent = Intent(this, HomeActivity::class.java)
             startActivity(myIntent)*/

        }

    }


    private fun doingLogin(fromeditupdateprofile: Boolean = false, token: String) {


        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            if (fromeditupdateprofile) {
                if (!sharedPreferences!!.getString("storeforprofileupdateemail", "")?.trim()!!.isNullOrEmpty() && intent.getBooleanExtra("updateprofile",false))
                {

                    req["phone_number"] =
                        sharedPreferences!!.getString("storeforprofileupdateemail", "")!!
                    req["country_code"] =
                        sharedPreferences!!.getString("storeforprofileupdatephone_numbercode", "")!!
                    req["password"] =
                        sharedPreferences!!.getString("storeforprofileupdatepassword", "")!!

                } else {
                    req["phone_number"] =
                        sharedPreferences!!.getString("storeforprofileupdatephone_number", "")!!
                    req["country_code"] =
                        sharedPreferences!!.getString("storeforprofileupdatephone_numbercode", "")!!
                    req["password"] =
                        sharedPreferences!!.getString("storeforprofileupdatepassword", "")!!
                }

            } else if (intent.getStringExtra("fromsplash").equals("fromsplash")) {

                req["phone_number"] =
                    sharedPreferenceManager!!.loginUserData.mobileNumber.toString()
                req["password"] = sharedPreferenceManager!!.loginUserData.password.toString()
                req["country_code"] = sharedPreferenceManager!!.loginUserData.cCode.toString()

            } else {
                req["phone_number"] =
                    binding.edtPhoneNumber.text.toString().replace("\"", "").trim()
                req["password"] = binding.edtPassword.text.toString().replace("\"", "").trim()
                req["country_code"] = binding.txtCountryCode.text.toString().trim()
            }

            req["type"] = "normal"
            req["device_model"] = model!!
            req["device_fcm_token"] = token!!
            req["device_id"] = device_id!!
            req["app_version"] = versionName!!

            response1 =
                apiservice.doingLogin(req /*binding.edtPassword.text.toString(),binding.txtCountryCode.text.toString(),binding.edtPhoneNumber.text.toString(),"normal"*/)
            // Call<RejectorderModel> response1 = apiservice.rejectitemorder(kitchecn_order_id,item_id,reason_id_);
            response1!!.enqueue(object : Callback<LoginPojo?> {
                override fun onResponse(
                    call: Call<LoginPojo?>,
                    response: Response<LoginPojo?>
                ) {

                    // AppProgressBar.closeLoader();

                    // if (response.isSuccessful()) {
                    if (response.body() == null) {

                        AppProgressBar.closeLoader()

                        return
                    }

                    if (response.body()!!.status == true) {

                        AppProgressBar.closeLoader()

                        Toast.makeText(this@LogInActivity, response.body()!!.message!!.toString(), Toast.LENGTH_SHORT).show()


                        if (binding.checkBox.isChecked) {
                            val userdata = UserResponse()
                            userdata.isRemember = true
                            userdata.password = binding.edtPassword.text.toString()
                            userdata.mobileNumber = binding.edtPhoneNumber.text.toString()
                            userdata.cCode = binding.txtCountryCode.text.toString()
                            sharedPreferenceManager?.saveLoginUserData(userdata)

                        } else {
                            val userdata = UserResponse()
                            userdata.isRemember = false
                            userdata.password = binding.edtPassword.text.toString()
                            userdata.mobileNumber = binding.edtPhoneNumber.text.toString()
                            userdata.cCode = binding.txtCountryCode.text.toString()
                            sharedPreferenceManager?.saveLoginUserData(userdata)
                        }


                        //10th Jan

                        /*  sharedPreferenceManager?.storeUserProfileDetail(response.body()!!.details!!.id.toString(),response.body()!!.details!!.firstname!!,response.body()!!.details!!.lastname!!,response.body()!!.details!!.email!! ,response.body()!!.details!!.phone!!,response.body()!!.details!!.countryCode!!,response.body()!!.token!! )
                          binding.edtPhoneNumber.setText("")
                          binding.edtPassword.setText("")
                          binding.txtCountryCode.setText("+1")
*/

                        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                        val sharedPreferences1 = getSharedPreferences("MySharedPref1", MODE_PRIVATE)
                        sharedPreferences1!!.getBoolean("locationallow", false)
                        val myEdit = sharedPreferences.edit()
                        val myEdit1 = sharedPreferences1.edit()

                        /* val sharedPreferences_rem = getSharedPreferences("MySharedPrefRem", MODE_PRIVATE)
                         val myEdit_rem = sharedPreferences_rem.edit()
*/
                        defaultId = sharedPreferences!!.getString("idSharedPref", "")
                        //  myEdit!!.clear().apply()


                        myEdit.putString(
                            "storeforprofileupdatephone_number",
                            response.body()!!.detail!!.phoneNumber!!
                        )
                        myEdit.putString(
                            "storeforprofileupdatephone_numbercode",
                            response.body()!!.detail!!.countryCode!!
                        )
                        if (binding.edtPassword.text.isNotEmpty())
                            myEdit.putString(
                                "storeforprofileupdatepassword",
                                binding.edtPassword.text.toString()/*.replace("\"", "")*/.trim()
                            )
                        myEdit.putString(
                            "storeforprofileupdateemail",
                            response.body()!!.detail!!.email!!
                        )

                        println(sharedPreferences.getString("storeforprofileupdatepassword", ""))


                        myEdit.putString(
                            "idSharedPrefAfterLog",
                            response.body()!!.detail!!.driverId!!
                        )
                        myEdit.putString(
                            "fnameSharedPrefAfterLog",
                            response.body()!!.detail!!.firstname!!
                        )
                        myEdit.putString(
                            "lnameSharedPrefAfterLog",
                            response.body()!!.detail!!.lastname!!
                        )
                        myEdit.putString(
                            "emailSharedPrefAfterLog",
                            response.body()!!.detail!!.email!!
                        )
                        myEdit.putString(
                            "phoneSharedPrefAfterLog",
                            response.body()!!.detail!!.phoneNumber!!
                        )
                        myEdit.putString(
                            "codeSharedPrefAfterLog",
                            response.body()!!.detail!!.countryCode!!
                        )
                        myEdit.putString("Authorization", response.body()!!.token!!)
                        //   myEdit.putString("AuthSharedPref", response.body()!!.token!!)
                        myEdit.putString("availability", response.body()!!.detail!!.availability!!)
                        myEdit.putBoolean(
                            "mobileVerifyStatus",
                            response.body()!!.detail!!.mobileVerifyStatus!!
                        )
                        myEdit.putBoolean(
                            "emailVerifyStatus",
                            response.body()!!.detail!!.emailVerifyStatus!!
                        )
                        myEdit.putString(
                            "account_Status",
                            response.body()!!.detail!!.account_status!!
                        )
                        myEdit.putBoolean(
                            "is_driver_vehicle_information_status",
                            response.body()!!.detail!!.is_driver_vehicle_information_status!!
                        )
                        myEdit.putBoolean(
                            "is_driverinformation_status",
                            response.body()!!.detail!!.is_driverinformation_status!!
                        )
                        myEdit.putString("vehicle_type", response.body()!!.detail!!.vehicle_type!!)
                        myEdit.putBoolean(
                            "is_bank_status",
                            response.body()!!.detail!!.is_bank_status!!
                        )
                        myEdit.putBoolean(
                            "isEmailVerify",
                            response.body()!!.detail!!.emailVerifyStatus!!
                        )
                        myEdit.putString("is_training", response.body()!!.detail!!.is_training!!)
                        myEdit.putBoolean(
                            "attempt_status",
                            response.body()!!.detail!!.attempt_status!!
                        )
                        myEdit.putBoolean(
                            "document_verify_status",
                            response.body()!!.detail!!.document_verify_status!!
                        )

                        /* myEdit!!.putBoolean("isVehicleScreenVal", true)
                         myEdit!!.putBoolean("isMinimumReqVal", true)
                         myEdit!!.putBoolean("isDriverCapabilityVal", true)*/
                        myEdit.apply()

                        if (fromeditupdateprofile == true) {
                            myEdit.putBoolean("fromeditupdateprofile", fromeditupdateprofile)
                        } else {
                            myEdit.putBoolean("fromeditupdateprofile", false)

                        }


/*                        if (ContextCompat.checkSelfPermission(
                                this@LogInActivity,
                                ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Fine Location permission is granted
                            // Check if current android version >= 11, if >= 11 check for Background Location permission
                            //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                // Background Location Permission is granted so do your work here
                            } else {
                                // Ask for Background Location Permission
                                askPermissionForBackgroundUsage()
                            }
                            //  }
                        } else {
                            // Fine Location Permission is not granted so ask for permission
                            askForLocationPermission()
                        }*/


                        if (response.body()!!.detail!!.mobileVerifyStatus == true
                            && response.body()!!.detail!!.emailVerifyStatus == true
                            && response.body()!!.detail!!.is_driverinformation_status!! == true
                            && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true
                            && response.body()!!.detail!!.is_bank_status == true
                            && response.body()!!.detail!!.attempt_status == true
                            && sharedPreferences1!!.getBoolean("isshowpopup", false) == true
                            && sharedPreferences1!!.getBoolean("locationallow", false) == true
                            && sharedPreferences1!!.getBoolean("cameraallow", false) == true
                            && sharedPreferences1!!.getBoolean("storageallow", false) == true
                            && sharedPreferences1!!.getBoolean("phoneallow", false) == true
                            && response.body()!!.detail!!.account_status.equals(
                                "Active",
                                ignoreCase = true

                        )
                        )

                        {
                            val myIntent_ =
                                Intent(this@LogInActivity, HomeActivity::class.java)
                            startActivity(myIntent_)
                            finish()

                        }

                        /*  {
                            if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true) {
                                    val myIntent =
                                        Intent(this@LogInActivity, PermissionScreen::class.java)
                                    myIntent.putExtra("locationscreen", "Location")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)
                                    finish()
                                }
                                else {
                                    val myIntent = Intent(this@LogInActivity, PopupDiscloser::class.java)
                                    myIntent.putExtra("locationscreen", "Location")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)
                                    finish()
                                }
                            }
                            else if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.CAMERA
                                )
                                != PackageManager.PERMISSION_GRANTED
                            )
                            {
                                val myIntent =
                                    Intent(this@LogInActivity, PermissionScreen::class.java)
                                myIntent.putExtra("locationscreen", "Camera")
                                myIntent.putExtra(
                                    "is_driverinformation_status",
                                    response.body()!!.detail!!.is_driverinformation_status
                                )
                                myIntent.putExtra(
                                    "is_driver_vehicle_information_status",
                                    response.body()!!.detail!!.is_driver_vehicle_information_status
                                )
                                startActivity(myIntent)
                            }
                            else if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                                != PackageManager.PERMISSION_GRANTED
                            )
                            {
                                val myIntent =
                                    Intent(this@LogInActivity, PermissionScreen::class.java)
                                myIntent.putExtra("locationscreen", "Storage")
                                myIntent.putExtra(
                                    "is_driverinformation_status",
                                    response.body()!!.detail!!.is_driverinformation_status
                                )
                                myIntent.putExtra(
                                    "is_driver_vehicle_information_status",
                                    response.body()!!.detail!!.is_driver_vehicle_information_status
                                )
                                startActivity(myIntent)
                            }
                            else if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.CALL_PHONE
                                )
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                val myIntent =
                                    Intent(this@LogInActivity, PermissionScreen::class.java)
                                myIntent.putExtra("locationscreen", "Phone")
                                myIntent.putExtra(
                                    "is_driverinformation_status",
                                    response.body()!!.detail!!.is_driverinformation_status
                                )
                                myIntent.putExtra(
                                    "is_driver_vehicle_information_status",
                                    response.body()!!.detail!!.is_driver_vehicle_information_status
                                )
                                startActivity(myIntent)
                                finish()
                            }
                            else
                            {
                                if (response.body()!!.detail!!.is_driverinformation_status!! == false) {
                                    val myIntent_ = Intent(
                                        this@LogInActivity,
                                        DriverInformationScreen::class.java
                                    )
                                    startActivity(myIntent_)
                                    finish()
                                }
                                else if (response.body()!!.detail!!.is_driver_vehicle_information_status!! == false) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, VehicleinfoScreen::class.java)
                                    startActivity(myIntent_)
                                    finish()
                                } else if (response.body()!!.detail!!.document_verify_status!! == false) {
                                    val builder =
                                        AlertDialog.Builder(this@LogInActivity)
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
                                }
                                else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.is_training!!.equals(
                                        "No"
                                    ) && response.body()!!.detail!!.attempt_status!! == false
                                ) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, DriverEvalution::class.java)
                                    startActivity(myIntent_)
                                    finish()
                                }
                                else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.attempt_status!! == false) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, DriverEvalution::class.java)
                                    startActivity(myIntent_)
                                    finish()
                                } else if (response.body()!!.detail!!.is_training!!.equals("No") && response.body()!!.detail!!.attempt_status!! == true) {
                                    val builder =
                                        AlertDialog.Builder(this@LogInActivity)
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
                                }
                                else if (response.body()!!.detail!!.account_status.equals(
                                        "Pending",
                                        ignoreCase = true
                                    ) && response.body()!!.detail!!.is_driverinformation_status!! == true && response.body()!!.detail!!.is_training!!.equals(
                                        "Yes"
                                    ) && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true
                                ) {
                                    val dialog = Dialog(this@LogInActivity)
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                    dialog.setCancelable(false)
                                    dialog.setContentView(R.layout.custom_layout_for_pending)
                                    val btn_close = dialog.findViewById(R.id.btn_close) as Button
                                    btn_close.setOnClickListener {
                                        dialog.dismiss()
                                        finishAffinity()
                                    }
                                    dialog.show()
                                }
                                else if (response.body()!!.detail!!.is_bank_status!! == false && response.body()!!.detail!!.account_status.equals(
                                        "Active",
                                        ignoreCase = true
                                    )
                                ) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, HomeActivity::class.java)
                                    startActivity(myIntent_)
                                    finish()
                                }
                                else if (response.body()!!.detail!!.is_bank_status!! == false && !response.body()!!.detail!!.account_status.equals(
                                        "Active",
                                        ignoreCase = true
                                    )
                                ) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, BankDetailsScreen::class.java)
                                    startActivity(myIntent_)
                                    finish()
                                }
                                else if (response.body()!!.detail!!.account_status.equals(
                                        "Active",
                                        ignoreCase = true
                                    )
                                ) {
                                    val myIntent_ =
                                        Intent(this@LogInActivity, HomeActivity::class.java)
                                    myIntent_.putExtra(
                                        "availability",
                                        response.body()!!.detail!!.availability
                                    )
                                    startActivity(myIntent_)
                                    finish()
                                }
                            }
                        }*/


                        //outer else if conditions
                        // case: 1


                        else if (response.body()!!.detail!!.mobileVerifyStatus == false) {


                            showVerifyBottomSheetdialog(
                                response.body()!!.detail!!.driverId!!,
                                "afterlogin",
                                "phone",
                                response.body()!!.detail!!.phoneNumber!!,
                                response.body()!!.detail!!.countryCode!!,
                                response.body()!!.detail!!.email!!,
                                fromeditupdateprofile!!
                            )

                        }
                        else if (response.body()!!.detail!!.emailVerifyStatus == false) {

                            verifyCodeBottomsheet1 = VerifyEmailFragment()
                            val bundle = Bundle()
                            bundle.putString("id", response.body()!!.detail!!.driverId!!)
                            bundle.putString("phone", response.body()!!.detail!!.phoneNumber!!)
                            bundle.putString("codeverify", "email")
                            bundle.putBoolean("fromeditupdateprofile", fromeditupdateprofile!!)
                            bundle.putBoolean("updateprofile", intent.getBooleanExtra("updateprofile",false)!!)

                            bundle.putString("withoutChangeCountrycodeEditPhone", "login")
                            bundle.putString(
                                "country_code",
                                response.body()!!.detail!!.countryCode!!
                            )
                            bundle.putString("email", response.body()!!.detail!!.email!!)
                            bundle.putString(
                                "afterloginphoneverifyneedemail",
                                "afterloginphoneverifyneedemail"
                            )
                            verifyCodeBottomsheet1!!.setArguments(bundle)
                            verifyCodeBottomsheet1!!.isCancelable = false
                            verifyCodeBottomsheet1!!.show(
                                supportFragmentManager,
                                verifyCodeBottomsheet1!!.tag
                            )


                        }

/*                        else if (sharedPreferences!!.getBoolean("isshowpopup", false) == true && sharedPreferences!!.getBoolean("locationallow", false) == false) {

                            val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                            myIntent.putExtra("locationscreen", "Location")
                            myIntent.putExtra(
                                "is_driverinformation_status",
                                response.body()!!.detail!!.is_driverinformation_status
                            )
                            myIntent.putExtra(
                                "is_driver_vehicle_information_status",
                                response.body()!!.detail!!.is_driver_vehicle_information_status
                            )
                            startActivity(myIntent)
                            finish()

                        }*/
                        //case:2
                       /* else if (sharedPreferences!!.getBoolean("isshowpopup", false) == false) {

                            val myIntent = Intent(this@LogInActivity, DummyPopupDiscloser::class.java)
                            myIntent.putExtra("locationscreen", "Location")
                            myIntent.putExtra(
                                "is_driverinformation_status",
                                response.body()!!.detail!!.is_driverinformation_status
                            )
                            myIntent.putExtra(
                                "is_driver_vehicle_information_status",
                                response.body()!!.detail!!.is_driver_vehicle_information_status
                            )
                            startActivity(myIntent)
                            finish()

                        }*/
                        //case: 3
                        else if (sharedPreferences1!!.getBoolean("locationallow", false) == false) {

                            if (ContextCompat.checkSelfPermission(this@LogInActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                            {

                                if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true) {

                                    val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                    myIntent.putExtra("locationscreen", "Location")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)
                                    finish()


                                } else {

                                    val myIntent = Intent(this@LogInActivity, PopupDiscloser::class.java)
                                    myIntent.putExtra("locationscreen", "Location")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)
                                    finish()

                                }
                            }
                            else{

                                if (ContextCompat.checkSelfPermission(this@LogInActivity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                {
                                    if (ContextCompat.checkSelfPermission(this@LogInActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                    {
                                       // sharedPreferences1!!.getBoolean("locationallow", false)
                                        locationallow = true

                                        myEdit1!!.putBoolean("locationallow", locationallow!!)
                                        myEdit1!!.apply()


                                        // Toast.makeText(this@LogInActivity, "accept location with background", Toast.LENGTH_LONG).show()
                                        if (ContextCompat.checkSelfPermission(this@LogInActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                                        { // camera permission not granted then go to permissionscreen
                                            val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                            myIntent.putExtra("locationscreen", "Camera")
                                            myIntent.putExtra(
                                                "is_driverinformation_status",
                                                response.body()!!.detail!!.is_driverinformation_status
                                            )
                                            myIntent.putExtra(
                                                "is_driver_vehicle_information_status",
                                                response.body()!!.detail!!.is_driver_vehicle_information_status
                                            )
                                            startActivity(myIntent)


                                        }else{

                                            if (ContextCompat.checkSelfPermission(this@LogInActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

                                            { // if external storage permission not given ,goto permissionscreen

                                                val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                                myIntent.putExtra("locationscreen", "Storage")
                                                myIntent.putExtra(
                                                    "is_driverinformation_status",
                                                    response.body()!!.detail!!.is_driverinformation_status
                                                )
                                                myIntent.putExtra(
                                                    "is_driver_vehicle_information_status",
                                                    response.body()!!.detail!!.is_driver_vehicle_information_status
                                                )
                                                startActivity(myIntent)

                                            }

                                            else{

                                                if (ContextCompat.checkSelfPermission(this@LogInActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)

                                                { // if call phone permission not given , goto permission screen
                                                    val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                                    myIntent.putExtra("locationscreen", "Phone")
                                                    myIntent.putExtra(
                                                        "is_driverinformation_status",
                                                        response.body()!!.detail!!.is_driverinformation_status
                                                    )
                                                    myIntent.putExtra(
                                                        "is_driver_vehicle_information_status",
                                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                                    )
                                                    startActivity(myIntent)
                                                    finish()

                                                }
                                                else{

                                                  //  Toast.makeText(this@LogInActivity, "accept all", Toast.LENGTH_LONG).show()
                                                    val myIntent_ = Intent(this@LogInActivity, HomeActivity::class.java)
                                                    startActivity(myIntent_)
                                                    finish()

                                                }
                                            }

                                        }


                                    }

                                    //backgroundlocation needed programaticallty if there is no background location for particular device

                                    else{
                                        locationallow = false

                                        myEdit1!!.putBoolean("locationallow", locationallow!!)
                                        myEdit1!!.apply()

                                        val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                        myIntent.putExtra("locationscreen", "Location")
                                        myIntent.putExtra(
                                            "is_driverinformation_status",
                                            response.body()!!.detail!!.is_driverinformation_status
                                        )
                                        myIntent.putExtra(
                                            "is_driver_vehicle_information_status",
                                            response.body()!!.detail!!.is_driver_vehicle_information_status
                                        )
                                        startActivity(myIntent)
                                        finish()

                                    }
                                }
                                else{

                                    val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                    myIntent.putExtra("locationscreen", "Location")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)
                                    finish()


                                }
                            }

                        }
                        //case: 4
                        // when location permission is true.
                        else if (sharedPreferences1!!.getBoolean("locationallow", false) == true) {

                            //check camera permission
                            if (ContextCompat.checkSelfPermission(
                                    this@LogInActivity,
                                    Manifest.permission.CAMERA
                                )
                                != PackageManager.PERMISSION_GRANTED
                            ) { // camera permission not granted then go to permissionscreen
                                val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                myIntent.putExtra("locationscreen", "Camera")
                                myIntent.putExtra(
                                    "is_driverinformation_status",
                                    response.body()!!.detail!!.is_driverinformation_status
                                )
                                myIntent.putExtra(
                                    "is_driver_vehicle_information_status",
                                    response.body()!!.detail!!.is_driver_vehicle_information_status
                                )
                                startActivity(myIntent)


                            } else { //if camera permission granted then check storage permission.

                                if (ContextCompat.checkSelfPermission(
                                        this@LogInActivity,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                    )
                                    != PackageManager.PERMISSION_GRANTED
                                ) { // if external storage permission not given ,goto permissionscreen

                                    val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                    myIntent.putExtra("locationscreen", "Storage")
                                    myIntent.putExtra(
                                        "is_driverinformation_status",
                                        response.body()!!.detail!!.is_driverinformation_status
                                    )
                                    myIntent.putExtra(
                                        "is_driver_vehicle_information_status",
                                        response.body()!!.detail!!.is_driver_vehicle_information_status
                                    )
                                    startActivity(myIntent)

                                } else {

                                    //condition check for phone
                                    if (ContextCompat.checkSelfPermission(
                                            this@LogInActivity,
                                            Manifest.permission.CALL_PHONE
                                        )
                                        != PackageManager.PERMISSION_GRANTED
                                    ) { // if call phone permission not given , goto permission screen
                                        val myIntent = Intent(this@LogInActivity, PermissionScreen::class.java)
                                        myIntent.putExtra("locationscreen", "Phone")
                                        myIntent.putExtra(
                                            "is_driverinformation_status",
                                            response.body()!!.detail!!.is_driverinformation_status
                                        )
                                        myIntent.putExtra(
                                            "is_driver_vehicle_information_status",
                                            response.body()!!.detail!!.is_driver_vehicle_information_status
                                        )
                                        startActivity(myIntent)
                                        finish()

                                    } else {
                                        // if location, camera, storage, phone permissions are all given then
                                         if (response.body()!!.detail!!.mobileVerifyStatus == false) {


                                            showVerifyBottomSheetdialog(
                                                response.body()!!.detail!!.driverId!!,
                                                "afterlogin",
                                                "phone",
                                                response.body()!!.detail!!.phoneNumber!!,
                                                response.body()!!.detail!!.countryCode!!,
                                                response.body()!!.detail!!.email!!,
                                                fromeditupdateprofile!!
                                            )

                                        }
                                         else if (response.body()!!.detail!!.emailVerifyStatus == false) {

                                            verifyCodeBottomsheet1 = VerifyEmailFragment()
                                            val bundle = Bundle()
                                            bundle.putString("id", response.body()!!.detail!!.driverId!!)
                                            bundle.putString("phone", response.body()!!.detail!!.phoneNumber!!)
                                            bundle.putString("codeverify", "email")
                                            bundle.putBoolean("fromeditupdateprofile", fromeditupdateprofile!!)

                                            bundle.putString("withoutChangeCountrycodeEditPhone", "login")
                                            bundle.putString(
                                                "country_code",
                                                response.body()!!.detail!!.countryCode!!
                                            )
                                            bundle.putString("email", response.body()!!.detail!!.email!!)
                                            bundle.putString(
                                                "afterloginphoneverifyneedemail",
                                                "afterloginphoneverifyneedemail"
                                            )
                                            verifyCodeBottomsheet1!!.setArguments(bundle)
                                            verifyCodeBottomsheet1!!.isCancelable = false
                                            verifyCodeBottomsheet1!!.show(
                                                supportFragmentManager,
                                                verifyCodeBottomsheet1!!.tag
                                            )


                                        }
                                         else if (response.body()!!.detail!!.is_driverinformation_status!! == false) {

                                            val myIntent_ =
                                                Intent(this@LogInActivity, DriverInformationScreen::class.java)
                                            startActivity(myIntent_)
                                            finish()
                                        }
                                         else if (response.body()!!.detail!!.is_driver_vehicle_information_status!! == false) {

                                            val myIntent_ =
                                                Intent(this@LogInActivity, VehicleinfoScreen::class.java)
                                            startActivity(myIntent_)
                                            finish()
                                        }
                                         else if (response.body()!!.detail!!.is_driverinformation_status!! == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true && response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.is_training.equals(
                                                "No"
                                            ) && response.body()!!.detail!!.attempt_status == false
                                        ) {

                                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                                            startActivity(myIntent_)
                                            finish()
                                        }
                                        else if (response.body()!!.detail!!.document_verify_status!! == false) {

                                            val builder =
                                                AlertDialog.Builder(this@LogInActivity/*,R.style.CustomAlertDialog*/)
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
                                              //  System.exit(0)
                                            }
                                            builder.show()

                                        }
                                        else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.is_training!!.equals(
                                                "No"
                                            ) && response.body()!!.detail!!.attempt_status!! == false
                                        ) {

                                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                                            startActivity(myIntent_)
                                            finish()

                                        }
                                         else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.attempt_status!! == false) {

                                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                                            startActivity(myIntent_)
                                            finish()
                                        } else if (response.body()!!.detail!!.is_training.equals("No") && response.body()!!.detail!!.attempt_status == true) {
                                            val builder =
                                                AlertDialog.Builder(this@LogInActivity/*,R.style.CustomAlertDialog*/)
                                                    .create()
                                            val view =
                                                layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                                            // val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
                                            val btn_close = view.findViewById(R.id.btn_close) as Button
                                            val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                                            val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                                            builder.setView(view)

                                            builder.setCanceledOnTouchOutside(false)
                                            tv_headettxt.setText(getString(R.string.assessment_notification))
                                            tv_entiremsg.setText(getString(R.string.complete_assessment_notification))

                                            btn_close.setOnClickListener {
                                                builder.dismiss()
                                                finishAffinity()
                                               // System.exit(0)
                                            }
                                            builder.show()


                                        }
                                         else if (response.body()!!.detail!!.is_training.equals("Yes") && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true &&
                                             response.body()!!.detail!!.is_driverinformation_status == true && response.body()!!.detail!!.is_bank_status == false && response.body()!!.detail!!.account_status.equals(
                                                 "Pending",
                                                 ignoreCase = true
                                             )
                                         ) {

                                             val myIntent_ =
                                                 Intent(this@LogInActivity, BankDetailsScreen::class.java)
                                             startActivity(myIntent_)
                                             finish()
                                         }
                                         else if (response.body()!!.detail!!.is_training.equals("Yes") && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true &&
                                             response.body()!!.detail!!.is_driverinformation_status == true && response.body()!!.detail!!.is_bank_status == false && response.body()!!.detail!!.account_status.equals(
                                                 "Active",
                                                 ignoreCase = true
                                             )
                                         ) {

                                             val myIntent_ = Intent(this@LogInActivity, HomeActivity::class.java)
                                             startActivity(myIntent_)
                                             finish()
                                         }

                                         else if (response.body()!!.detail!!.account_status.equals(
                                                 "Pending",
                                                 ignoreCase = true
                                             ) && response.body()!!.detail!!.is_driverinformation_status!! == true && response.body()!!.detail!!.is_training.equals(
                                                 "Yes"
                                             ) && response.body()!!.detail!!.attempt_status == true &&  response.body()!!.detail!!.is_driver_vehicle_information_status!! == true && response.body()!!.detail!!.is_bank_status == true
                                         ) {

                                             //  showpopup();
                                             val dialog = Dialog(this@LogInActivity)
                                             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                             dialog.setCancelable(false)
                                             dialog.setContentView(R.layout.custom_layout_for_pending)
                                             val btn_close = dialog.findViewById(R.id.btn_close) as Button
                                             //  val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
                                             // val noBtn = dialog.findViewById(R.id.noBtn) as TextView
                                             btn_close.setOnClickListener {
                                                 dialog.dismiss()
                                                 finishAffinity()
                                                 //  System.exit(0)
                                             }
                                             dialog.show()

                                         }

                                         else if (response.body()!!.detail!!.account_status.equals("Active", ignoreCase = true)){
                                             val myIntent_ =
                                                 Intent(
                                                     this@LogInActivity,
                                                     HomeActivity::class.java
                                                 )
                                             startActivity(myIntent_)
                                             finish()

                                         }


                                    }

                                }

                            }

                        }

                        //17.08.22 commented this code.
                        else if (response.body()!!.detail!!.mobileVerifyStatus == false) {


                            showVerifyBottomSheetdialog(
                                response.body()!!.detail!!.driverId!!,
                                "afterlogin",
                                "phone",
                                response.body()!!.detail!!.phoneNumber!!,
                                response.body()!!.detail!!.countryCode!!,
                                response.body()!!.detail!!.email!!,
                                fromeditupdateprofile!!
                            )

                        } else if (response.body()!!.detail!!.emailVerifyStatus == false) {

                            verifyCodeBottomsheet1 = VerifyEmailFragment()
                            val bundle = Bundle()
                            bundle.putString("id", response.body()!!.detail!!.driverId!!)
                            bundle.putString("phone", response.body()!!.detail!!.phoneNumber!!)
                            bundle.putString("codeverify", "email")
                            bundle.putBoolean("fromeditupdateprofile", fromeditupdateprofile!!)

                            bundle.putString("withoutChangeCountrycodeEditPhone", "login")
                            bundle.putString(
                                "country_code",
                                response.body()!!.detail!!.countryCode!!
                            )
                            bundle.putString("email", response.body()!!.detail!!.email!!)
                            bundle.putString(
                                "afterloginphoneverifyneedemail",
                                "afterloginphoneverifyneedemail"
                            )
                            verifyCodeBottomsheet1!!.setArguments(bundle)
                            verifyCodeBottomsheet1!!.isCancelable = false
                            verifyCodeBottomsheet1!!.show(
                                supportFragmentManager,
                                verifyCodeBottomsheet1!!.tag
                            )


                        } else if (response.body()!!.detail!!.is_driverinformation_status!! == false) {

                            val myIntent_ =
                                Intent(this@LogInActivity, DriverInformationScreen::class.java)
                            startActivity(myIntent_)
                            finish()
                        } else if (response.body()!!.detail!!.is_driver_vehicle_information_status!! == false) {

                            val myIntent_ =
                                Intent(this@LogInActivity, VehicleinfoScreen::class.java)
                            startActivity(myIntent_)
                            finish()
                        } else if (response.body()!!.detail!!.is_driverinformation_status!! == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true && response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.is_training.equals(
                                "No"
                            ) && response.body()!!.detail!!.attempt_status == false
                        ) {

                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                            startActivity(myIntent_)
                            finish()
                        }
                        else if (response.body()!!.detail!!.document_verify_status!! == false) {

                            val builder =
                                AlertDialog.Builder(this@LogInActivity)
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
                               // System.exit(0)
                            }
                            builder.show()

                        }
                        else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.is_training!!.equals(
                                "No"
                            ) && response.body()!!.detail!!.attempt_status!! == false
                        ) {

                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                            startActivity(myIntent_)
                            finish()

                        } else if (response.body()!!.detail!!.document_verify_status!! == true && response.body()!!.detail!!.attempt_status!! == false) {

                            val myIntent_ = Intent(this@LogInActivity, DriverEvalution::class.java)
                            startActivity(myIntent_)
                            finish()
                        } else if (response.body()!!.detail!!.is_training.equals("No") && response.body()!!.detail!!.attempt_status == true) {

                            val builder =
                                AlertDialog.Builder(this@LogInActivity).create()
                            val view =
                                layoutInflater.inflate(R.layout.custom_layout_for_pending, null)
                            val btn_close = view.findViewById(R.id.btn_close) as Button
                            val tv_headettxt = view.findViewById(R.id.tv_headettxt) as TextView
                            val tv_entiremsg = view.findViewById(R.id.tv_entiremsg) as TextView

                            builder.setView(view)

                            builder.setCanceledOnTouchOutside(false)
                            tv_headettxt.setText(getString(R.string.assessment_notification))
                            tv_entiremsg.setText(getString(R.string.complete_assessment_notification))

                            btn_close.setOnClickListener {
                                builder.dismiss()
                                finishAffinity()
                               // System.exit(0)
                            }
                            builder.show()


                        } else if (response.body()!!.detail!!.is_training.equals("Yes") && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true &&
                            response.body()!!.detail!!.is_bank_status == false && response.body()!!.detail!!.account_status.equals(
                                "Active",
                                ignoreCase = true
                            )
                        ) {

                            val myIntent_ = Intent(this@LogInActivity, HomeActivity::class.java)
                            startActivity(myIntent_)
                            finish()
                        } else if (response.body()!!.detail!!.is_training.equals("Yes") && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true &&
                            response.body()!!.detail!!.is_bank_status == false && !response.body()!!.detail!!.account_status.equals(
                                "Active",
                                ignoreCase = true
                            )
                        ) {

                            val myIntent_ =
                                Intent(this@LogInActivity, BankDetailsScreen::class.java)
                            startActivity(myIntent_)
                            finish()
                        } else if (response.body()!!.detail!!.account_status.equals(
                                "Pending",
                                ignoreCase = true
                            ) && response.body()!!.detail!!.is_driverinformation_status!! == true && response.body()!!.detail!!.is_training.equals(
                                "Yes"
                            ) && response.body()!!.detail!!.attempt_status == true && response.body()!!.detail!!.is_driver_vehicle_information_status!! == true && response.body()!!.detail!!.is_bank_status == true
                        ) {

                            val dialog = Dialog(this@LogInActivity)
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialog.setCancelable(false)
                            dialog.setContentView(R.layout.custom_layout_for_pending)
                            val btn_close = dialog.findViewById(R.id.btn_close) as Button
                            btn_close.setOnClickListener {
                                dialog.dismiss()
                                finishAffinity()
                             //   System.exit(0)
                            }
                            dialog.show()

                        }


                    }

                    else {

                        AppProgressBar.closeLoader()

                        if (response.body()!!.message!! != null) {

                            if (response.body()!!.message!!.equals(
                                    resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    ignoreCase = true)
                            ) {

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(
                                        this@LogInActivity,
                                        WelcomeActivity::class.java
                                    )
                                startActivity(myIntent)

                                finish()

                            } else {

                                //  runOnUiThread {

                                Toast.makeText(
                                    this@LogInActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            //   }


                        }
                    }

                }

                override fun onFailure(call: Call<LoginPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Toast.makeText(
                            this@LogInActivity,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Toast.makeText(
                            this@LogInActivity,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun checkPermission1() {


    }

    fun showpopup() {


    }

    private fun doingForgetPasswordPhoneForOTP() {

        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface =
                ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["phone"] = binding.edtPhoneNumber.text.toString()
            req["country_code"] = binding.txtCountryCode.text.toString().replace("\"", "")


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

                        if (response.body()!!.status == true) {


                            Toast.makeText(
                                this@LogInActivity,
                                "OTP sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()


                            val myIntent =
                                Intent(this@LogInActivity, CreatePasswordActivity::class.java)
                            startActivity(myIntent)


                            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            myEdit = sharedPreferences!!.edit()

                            sharedPreferences!!.getString("phonenumberSharedPref", "")!!
                            sharedPreferences!!.getString("phonecodeSharedPref", "")!!

                           // myEdit!!.clear().apply()

                            myEdit!!.putString(
                                "phonenumberSharedPref",
                                binding.edtPhoneNumber.text.toString()
                            )
                            myEdit!!.putString(
                                "phonecodeSharedPref",
                                binding.txtCountryCode.text.toString()
                            )

                            myEdit!!.apply()

                        } else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.message!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true)
                                ) {

                                    val sharedPreferences =
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            this@LogInActivity,
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    finish()

                                } else {


                                    Toast.makeText(
                                        this@LogInActivity,
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }

                    } else {
                        // try {
                        AppProgressBar.closeLoader()

                        if (response.body() != null) {

                            Toast.makeText(
                                this@LogInActivity,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if (t is NoConnectivityException) {
                        Toast.makeText(
                            this@LogInActivity,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        Toast.makeText(
                            this@LogInActivity,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


  /*  private fun fetchLocation() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Location")
            startActivity(myIntent)

        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Camera")
            startActivity(myIntent)


        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Storage")
            startActivity(myIntent)

        } else if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val myIntent = Intent(this, PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Phone")
            startActivity(myIntent)

        } else {

            val myIntent =
                Intent(this@LogInActivity,DriverInformationScreen::class.java)
            startActivity(myIntent)
            finish()
        }

    }
*/

    override fun onFailure(message: String) {

    }

    override fun onBackButtonPressed() {
        finish()
    }

    override fun onCountryOpen() {

        showBottomSheetDialogFragment()


    }

    private fun showBottomSheetDialogFragment() {


        val bundle = Bundle()
        bundle.putString("test", "Login")
        bottomSheetFragment!!.arguments = bundle

        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)

        //   bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)

        /*  verifyCodeBottomsheet = VerifyCodeFragment()
          val bundle = Bundle()
          bundle.putInt("id", response.body()!!.details!!.id!!)
          bundle.putString("afterlogin", "afterlogin")
          bundle.putString("phone", response.body()!!.details!!.phone!!)
          bundle.putString(
                  "country_code",
                  response.body()!!.details!!.countryCode!!
          )
          bundle.putString("email", response.body()!!.details!!.email!!)
          bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)
  */
    }

    public fun showVerifyBottomSheetdialog(
        custRegId: String,
        afterlogin: String,
        phone: String,
        phonenumber: String,
        country_code: String,
        email: String,
        fromeditupdateprofile: Boolean
    ) {

        verifyCodeBottomsheet = VerifyCodeFragment()
        val bundle = Bundle()
        bundle.putString("id", custRegId)
        bundle.putString("afterlogin", afterlogin)
        bundle.putString("codeverify", phone)
        bundle.putString("phone", phonenumber)
        bundle.putString(
            "country_code",
            country_code
        )
        bundle.putString("email", email)
        bundle.putBoolean("fromeditupdateprofile", fromeditupdateprofile)

        verifyCodeBottomsheet!!.setArguments(bundle)
        verifyCodeBottomsheet!!.isCancelable = false
        verifyCodeBottomsheet!!.show(
            supportFragmentManager,
            verifyCodeBottomsheet!!.tag
        )

    }


    fun cancelCountryPopup(countryCode: Int) {

        txt_country_code.text = "+" + countryCode.toString()
        bottomSheetFragment?.dismiss()
    }


    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun cancelVerifyCodePopUp() {

        verifyCodeBottomsheet!!.dismiss()

        // closing input keyboard

        //  hideKeyboard()

    }

    override fun onResume() {
        super.onResume()


        /*  val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
          if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                  LocationManager.NETWORK_PROVIDER
              )
          ) {
              fetchLocation()

          }else{

              startActivity(
                  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(
                      Intent.FLAG_ACTIVITY_NEW_TASK
                  )
              )

          }*/


    }

    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {

        if (!stateChanges!!.from.isSubscribed &&
            stateChanges!!.to.isSubscribed) {
            /* AlertDialog.Builder(this)
                .setMessage("You've successfully subscribed to push notifications!")
                .show()*/
            // get player ID
            stateChanges.getTo().getUserId()
        }

        Log.i("Debug", "onOSSubscriptionChanged: " + stateChanges.getTo().getUserId())

        token = stateChanges.getTo().getUserId()
    }


}

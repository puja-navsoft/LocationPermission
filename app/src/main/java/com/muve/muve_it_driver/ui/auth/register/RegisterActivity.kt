package com.muve.muve_it_driver.ui.auth.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.ActivityRegisterBinding
import com.muve.muve_it_driver.model.registrationmodel.RegistrationPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.about_us.SelectAboutUsFragment
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment
import com.muve.muve_it_driver.ui.auth.edit_number.EditNumberFragment
import com.muve.muve_it_driver.ui.auth.prefered_city.SelectPreferedCityFragment
import com.muve.muve_it_driver.ui.auth.verifycode.VerifyCodeFragment
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.AppUtilities
import com.google.firebase.messaging.FirebaseMessaging
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.NetworkUtility.Companion.PRIVACY_URL
import com.muve.muve_it_driver.util.NetworkUtility.Companion.TERMS_CONDITIONS_HAULERS_URL
import com.muve.muve_it_driver.util.NetworkUtility.Companion.TERMS_CONDITIONS_URL
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterActivity : AppCompatActivity(), RegistrationListener , OSSubscriptionObserver {

    var clickPreferedCity: Boolean? =null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
    private var defaultId: String?=""
    var storeFirstname :String?=""
    var storeLastname :String?=""
    var storeEmail :String?=""
    var storeEmailUpdate :String?=""
    var storePhone :String?=""
    var iphoneUpdateSharedPref :String?=""
    var iphoneCodeUpdateSharedPref :String?=""
    var storeCode :String?=""
    var storeToken :String?=""
    var storePhoneUpdate :String?=""
    var storePhoneCodeUpdate :String?=""
    var device_id :String?=""
    var token :String?=""
    var UUID :String?=""
    var model :String?=""
    var versionName :String?=""
    var bottomSheetFragment: SelectCountryCodeFragment? = null
    var bottomSheetFragmentAboutUs: SelectAboutUsFragment? = null
    var bottomSheetFragmentPreferedCity: SelectPreferedCityFragment? = null
    var isPasswordHide : Boolean = true
    var custRegId : String? = ""
    var verifyCodeBottomsheet: VerifyCodeFragment? = null
    var editNumberFragment: EditNumberFragment? = null
    private lateinit var binding: ActivityRegisterBinding
    private var response1: Call<RegistrationPojo>? = null
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    companion object{
        var updatephonenumber :String?=""
        var updatephonecode :String?=""
        var updatepassword :String?=""
        var updatedemail :String?=""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        val viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding.register = viewModel
        viewModel.regListener = this

        device_id = Settings.Secure.getString(getContentResolver(),
            Settings.Secure.ANDROID_ID)

      //  getFCMToken()

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
        // UUID = OneSignal.getDeviceState()!!.userId
       // token = UUID
        Log.v("oneSignalUserID","oneSignalUserID"+UUID)
        Log.v("oneSignalUserID","oneSignalUserID"+token)
        token =OneSignal.getDeviceState()!!.getUserId();


        getYourDeviceName()

        sharedPreferenceManager = SharedPreferenceManager(this)
        bottomSheetFragment = SelectCountryCodeFragment()
        bottomSheetFragmentAboutUs = SelectAboutUsFragment()
        bottomSheetFragmentPreferedCity = SelectPreferedCityFragment()
        verifyCodeBottomsheet = VerifyCodeFragment()
        editNumberFragment = EditNumberFragment()

        binding.parent.setOnClickListener {


            // Toast.makeText(this, "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()

            AppUtilities.hideSoftKeyboard(this)

        }

        binding.termCondition.setOnClickListener {

            val uri: Uri = Uri.parse(TERMS_CONDITIONS_HAULERS_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        binding.tvPrivacy.setOnClickListener {

            val uri: Uri = Uri.parse(PRIVACY_URL) // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


    }


    override fun onStarting() {
    }


    override fun onSuccess() {

        // showVerifyBottomSheetdialogTesting()
        var em= Patterns.EMAIL_ADDRESS.matcher(binding.edtBirthday.text.toString()).matches()

         if (binding.edtFirstName.text.toString().trim().equals("")){

            // Toast.makeText(this, "Please give your first name", Toast.LENGTH_LONG).show()
            binding.edtFirstName.setError("Please give your first name")
            binding.edtFirstName.requestFocus()


        }

        else if (binding.edtFirstName.text.toString().get(0).toString().contains(" ")){

            // Toast.makeText(this, "Remove space from first name", Toast.LENGTH_LONG).show()
            binding.edtFirstName.setError("Remove first space from your first name")

            binding.edtFirstName.requestFocus()

        }
        else if (binding.edtFirstName.text.toString().trim().length>20){

            Toast.makeText(this, "First name will be 20 character only", Toast.LENGTH_LONG).show()
            binding.edtFirstName.requestFocus()

        }
        else if (binding.edtLastName.text.toString().trim().equals("")){

            // Toast.makeText(this, "Please give your last name", Toast.LENGTH_LONG).show()
            binding.edtLastName.setError("Please give your last name")
            binding.edtLastName.requestFocus()
        }

        else if (binding.edtLastName.text.toString().get(0).toString().contains(" ")){

            // Toast.makeText(this, "Remove space from first name", Toast.LENGTH_LONG).show()
            binding.edtLastName.setError("Remove first space from your last name")

            binding.edtLastName.requestFocus()

        }

        else if (binding.edtLastName.text.toString().trim().length>20){

            Toast.makeText(this, "First name will be 20 character only", Toast.LENGTH_LONG).show()
            binding.edtLastName.requestFocus()
        }
        else if (binding.edtPhoneNumber.text.toString().trim().equals("")){

            // Toast.makeText(this, "Please give phone number", Toast.LENGTH_LONG).show()
            binding.edtPhoneNumber.setError("Please give phone number")
            binding.edtPhoneNumber.requestFocus()
        }
        else if ( binding.edtPhoneNumber.text.toString().trim().length <9){

            // Toast.makeText(this, "Phone number should be minimum 10 digit", Toast.LENGTH_LONG).show()
            binding.edtPhoneNumber.setError("Phone number should be minimum 9 digit")
            binding.edtPhoneNumber.requestFocus()
        }

        else if ( binding.edtPhoneNumber.text.toString().trim().length >15){

            // Toast.makeText(this, "Phone number should be maximum 16 digit", Toast.LENGTH_LONG).show()
            binding.edtPhoneNumber.setError("Phone number should be maximum 15 digit")
            binding.edtPhoneNumber.requestFocus()

        }
        else if (binding.edtBirthday.text.toString().trim().equals("") ) {

            // Toast.makeText(this, "Enter Email address !", Toast.LENGTH_SHORT).show()
            binding.edtBirthday.setError("Enter Email address !")
            binding.edtBirthday.requestFocus()
        }

        else if (binding.edtBirthday.text.toString().contains(" ") ) {

            // Toast.makeText(this, "Enter Email address !", Toast.LENGTH_SHORT).show()
            binding.edtBirthday.setError("Remove space from Email address !")
            binding.edtBirthday.requestFocus()
        }
        else if (binding.edtBirthday.text.toString().trim().isEmpty() || (Patterns.EMAIL_ADDRESS.matcher(binding.edtBirthday.text.toString().trim()).matches()==false)) {

            // Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            binding.edtBirthday.setError("Enter valid Email address !")
            binding.edtBirthday.requestFocus()
        }

         else if (binding.edtPrfrdcity.text.toString().trim().isEmpty()) {

             // Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
             binding.edtPrfrdcity.setError("Enter your prefered city !")
             binding.edtPrfrdcity.requestFocus()
         }

        else if (binding.password.text.toString().trim()!!.equals("")){

            // Toast.makeText(this, "Please insert password", Toast.LENGTH_LONG).show()
            binding.password.setError("Please insert password!")
            binding.password.requestFocus()

        }

         else if (!binding.password.text.toString().trim().matches(Regex("^(?=.*[A-Za-z!\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~])(?=.*\\d)[A-Za-z\\d!\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]{6,20}$"))){

            // Toast.makeText(this, "Password should contain digit and charecter both", Toast.LENGTH_LONG).show()
            binding.password.setError("Password should be 6-20 characters long and must have one letter and one digit, no space are allowed.")
            binding.password.requestFocus()
        }

        else if (binding.confirmpassword.text.toString().trim()!!.equals("")){

            // Toast.makeText(this, "Please insert confirm password", Toast.LENGTH_LONG).show()
            binding.confirmpassword.setError("Please insert confirm password")
            binding.confirmpassword.requestFocus()

        }

        else if ( binding.confirmpassword.text.toString().contains(" ")){

            // Toast.makeText(this, "Please remove space from your confirm password", Toast.LENGTH_LONG).show()
            binding.confirmpassword.setError("Please remove space from your confirm password")
            binding.confirmpassword.requestFocus()
        }

        else if (binding.password.text.toString().trim() != binding.confirmpassword.text.toString().trim()){

            // Toast.makeText(this, "Password and confirm password doesn't match", Toast.LENGTH_LONG).show()

            binding.confirmpassword.setError("Password and confirm password doesn't match")
            binding.confirmpassword.requestFocus()
        }

        else if (binding.checkBox.isChecked==false){

            Toast.makeText(this, "You have to accept terms and condition policy", Toast.LENGTH_LONG).show()

        }


        else{


             if (NetworkUtility.isNetworkAvailable(this)) {

                 val pInfo: PackageInfo =
                     getPackageManager().getPackageInfo(getPackageName(), 0)
                  versionName  = pInfo.versionName

                 doRegistration()
             }
             else{
                 Toast.makeText(
                     this,
                     getString(R.string.msg_no_internet),
                     Toast.LENGTH_LONG
                 ).show()
             }


        }


        // showVerifyBottomSheetdialog(161)

    }

    private fun doRegistration() {

        try {

            AppProgressBar.openLoader(
                this as Activity?,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["firstname"] = binding.edtFirstName.text.toString().trim()
            req["lastname"] = binding.edtLastName.text.toString().trim()
            req["phone_number"] = binding.edtPhoneNumber.text.toString().replace("\"", "").trim()
            req["country_code"] = binding.txtCountryCode.text.toString().trim()
            req["email"] = binding.edtBirthday.text.toString().trim()
            req["password"] = binding.password.text.toString().trim()
            req["hear_about_Us"] = binding.edtAboutus.text.toString().trim()
            req["preferred_city"] = binding.edtPrfrdcity.text.toString().trim()

            req["device_model"] =  model!!
            req["device_fcm_token"] =  token!!
            req["device_id"] =  device_id!!
            req["app_version"] =  versionName!!

          //  req["alternate_city"] = binding.edtAlternatecity.text.toString().trim()

            response1 = apiservice.doingRegistration(req)
            // Call<RejectorderModel> response1 = apiservice.rejectitemorder(kitchecn_order_id,item_id,reason_id_);
            response1!!.enqueue(object : Callback<RegistrationPojo?> {
                override fun onResponse(
                    call: Call<RegistrationPojo?>,
                    response: Response<RegistrationPojo?>
                ) {

                    // AppProgressBar.closeLoader();
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader();

                        if (response.body()!!.status==true) {


                            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                            myEdit = sharedPreferences!!.edit()


                            defaultId = sharedPreferences!!.getString("idSharedPref","")!!
                            storeFirstname = sharedPreferences!!.getString("fnameSharedPref","")!!
                            storeLastname = sharedPreferences!!.getString("lnameSharedPref","")!!
                            storeEmail = sharedPreferences!!.getString("emailSharedPref","")!!
                            storePhone = sharedPreferences!!.getString("phoneSharedPref","")!!
                            storeCode = sharedPreferences!!.getString("codeSharedPref","")!!
                            storeToken = sharedPreferences!!.getString("AuthSharedPref","")!!
                            iphoneUpdateSharedPref = sharedPreferences!!.getString("iphoneUpdateSharedPref","")!!
                            iphoneCodeUpdateSharedPref = sharedPreferences!!.getString("iphoneCodeUpdateSharedPref","")!!

                            myEdit!!.clear().apply()


                            myEdit!!.putString("idSharedPref", response.body()!!.driverDetails!!.driverId!!)
                            myEdit!!.putString("fnameSharedPref", response.body()!!.driverDetails!!.firstname)
                            myEdit!!.putString("lnameSharedPref", response.body()!!.driverDetails!!.lastname)
                            myEdit!!.putString("emailSharedPref", response.body()!!.driverDetails!!.email)
                            myEdit!!.putString("phoneSharedPref", response.body()!!.driverDetails!!.phoneNumber!!)
                            myEdit!!.putString("codeSharedPref", response.body()!!.driverDetails!!.countryCode!!)
                            myEdit!!.putString("AuthSharedPref", response.body()!!.token)

                            //30.08.22
                           /* myEdit!!.putBoolean(
                                "is_driver_vehicle_information_status",
                                response.body()!!.driverDetails!!.is_driver_vehicle_information_status!!
                            )
                            myEdit!!.putBoolean(
                                "is_driverinformation_status",
                                response.body()!!.driverDetails!!.is_driverinformation_status!!
                            )*/

                           /* myEdit!!.putBoolean("isVehicleScreenVal", true)
                            myEdit!!.putBoolean("isMinimumReqVal", true)
                            myEdit!!.putBoolean("isDriverCapabilityVal", true)*/
                            myEdit!!.apply()


                            AppProgressBar.closeLoader()

                            Toast.makeText(
                                this@RegisterActivity,
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            custRegId= response.body()!!.driverDetails!!.driverId!!

                            updatephonecode =binding.txtCountryCode.text.toString().trim()
                            updatephonenumber =binding.edtPhoneNumber.text.toString().trim()
                            updatepassword =binding.password.text.toString().trim()
                            updatedemail =binding.edtBirthday.text.toString().trim()


                            showVerifyBottomSheetdialog(custRegId!!)

                            binding.edtFirstName.setText("")
                            binding.edtLastName.setText("")
                            binding.txtCountryCode.setText("+1")
                            binding.edtBirthday.setText("")
                            binding.password.setText("")
                            binding.confirmpassword.setText("")
                            binding.edtAboutus.setText("")
                            binding.checkBox.isChecked=false

                        }

                        else{

                            AppProgressBar.closeLoader()


                            if (response.body()!!.message.equals(resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true)){

                                val sharedPreferences =
                                    getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                sharedPreferenceManager!!.logoutUser()
                                editor.clear()
                                editor.commit()


                                val myIntent =
                                    Intent(this@RegisterActivity, WelcomeActivity::class.java)
                                startActivity(myIntent)

                                finish()

                            }

                            else{


                                Toast.makeText(
                                    this@RegisterActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()


                            }

                            if (response.body()!=null){

                                Toast.makeText(
                                    this@RegisterActivity,
                                    response.body()!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        }

                    }

                    else {

                        AppProgressBar.closeLoader();

                        Toast.makeText(
                            this@RegisterActivity,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

                override fun onFailure(call: Call<RegistrationPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()

                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            this@RegisterActivity,
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            this@RegisterActivity,
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

    override fun onFailure(message: String) {
    }

    override fun onBackButtonPressed() {
        finish()
    }

    override fun onCountryOpen() {

        showBottomSheetDialogFragment()
    }

    override fun clickAboutUsButton() {

        showBottomSheetDialogFragmentForAboutUs()
    }

    override fun clickPreferedCityButton() {

        showBottomSheetDialogFragmentForclickPreferedCityButton()
    }

    override fun clickAlternateCityButton() {

      //  showBottomSheetDialogFragmentForclickAlternateCityButton()
    }

    override fun showPassword() {

        if (isPasswordHide) {

            isPasswordHide = false
            password?.transformationMethod = HideReturnsTransformationMethod.getInstance()

            tv_showclick.text = getString(R.string.tv_hide_password)

        } else {

            isPasswordHide = true
            password?.transformationMethod = PasswordTransformationMethod.getInstance()

            tv_showclick.text = getString(R.string.tv_show_password)


        }
    }

    override fun showPasswordConfirm() {

        if (isPasswordHide) {

            isPasswordHide = false
            // password?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmpassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()

            // tv_showclick.text = getString(R.string.tv_hide_password)
            tv_showclickConfirmPas.text = getString(R.string.tv_hide_password)

        } else {

            isPasswordHide = true
            // password?.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmpassword?.transformationMethod = PasswordTransformationMethod.getInstance()

            // tv_showclick.text = getString(R.string.tv_show_password)
            tv_showclickConfirmPas.text = getString(R.string.tv_show_password)


        }
    }


    private fun showBottomSheetDialogFragment() {


        val bundle = Bundle()
        bundle.putString("test", "Register")
        bottomSheetFragment!!.arguments = bundle
        bottomSheetFragment!!.isCancelable=false
        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)
    }

    private fun showBottomSheetDialogFragmentForAboutUs() {
        bottomSheetFragmentAboutUs!!.isCancelable=false
        bottomSheetFragmentAboutUs?.show(supportFragmentManager, bottomSheetFragmentAboutUs!!.tag)
    }

    private fun showBottomSheetDialogFragmentForclickPreferedCityButton() {

        clickPreferedCity=true
        bottomSheetFragmentPreferedCity!!.isCancelable=false
        bottomSheetFragmentPreferedCity?.show(supportFragmentManager, bottomSheetFragmentPreferedCity!!.tag)
    }

    private fun showBottomSheetDialogFragmentForclickAlternateCityButton() {

        clickPreferedCity=false
        bottomSheetFragmentPreferedCity!!.isCancelable=false
        bottomSheetFragmentPreferedCity?.show(supportFragmentManager, bottomSheetFragmentPreferedCity!!.tag)
    }

    public fun showVerifyBottomSheetdialog(custRegId : String) {

        verifyCodeBottomsheet = VerifyCodeFragment()
        val bundle = Bundle()
        bundle.putString("codeverify", "phone")
        bundle.putString("mobile", binding.edtPhoneNumber.text.toString() )
        bundle.putString("mobilecode", binding.txtCountryCode.text.toString() )
        bundle.putString("email", binding.edtBirthday.text.toString() )
        bundle.putString("custRegId",custRegId)

        binding.edtPhoneNumber.setText("")
        verifyCodeBottomsheet!!.setArguments(bundle)
        verifyCodeBottomsheet!!.isCancelable = false
        verifyCodeBottomsheet!!.show(supportFragmentManager,verifyCodeBottomsheet!!.tag)

    }

/*
 public fun showVerifyBottomSheetdialogTesting() {

 verifyCodeBottomsheet = VerifyCodeFragment()
 val bundle = Bundle()
 bundle.putString("codeverify", "phone")
 bundle.putString("mobile", binding.edtPhoneNumber.text.toString() )
 bundle.putString("mobilecode", binding.txtCountryCode.text.toString() )
 bundle.putString("email", binding.edtBirthday.text.toString() )
 bundle.putInt("custRegId",custRegId)

 binding.edtPhoneNumber.setText("")
 verifyCodeBottomsheet!!.setArguments(bundle)
 verifyCodeBottomsheet!!.isCancelable = false
 verifyCodeBottomsheet!!.show(supportFragmentManager,verifyCodeBottomsheet!!.tag)

 }
*/

    fun cancelCountryPopup(countryCode: Int) {

        txt_country_code.text = "+"+countryCode.toString()
        bottomSheetFragment?.dismiss()
    }

    fun cancelAboutusPopup(aboutUs: String) {

        edt_aboutus.text = aboutUs.toString()
        bottomSheetFragmentAboutUs?.dismiss()
    }

    fun cancelPreferedCityPopup(aboutUs: String) {

        if( clickPreferedCity==true) {
            edt_prfrdcity.text = aboutUs.toString()

        }else{
            edt_alternatecity.text = aboutUs.toString()

        }
        bottomSheetFragmentPreferedCity?.dismiss()
    }

    fun cancelVerifyCodePopUp() {

        verifyCodeBottomsheet?.dismiss()
    }

    fun cancelEditCodePopUp() {

        editNumberFragment?.dismiss()
    }

/*
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    //   Log.d("isSuccessful","=="+token)
                    token=""
                    return@addOnCompleteListener
                }
                // Get new Instance ID token
                token = task.result!!
                Log.d("token","=="+token)
            }

    }
*/

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
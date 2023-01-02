package com.muve.muve_it_driver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.muve.muve_it_driver.UserDetailPojo.UserDetailPojo
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.editprofile.EditProfilePojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.isEditUserDone
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.HashMap

class EditProfileUpdate : AppCompatActivity() {

    var sharedPreferences: SharedPreferences?=null
    var myEdit: SharedPreferences.Editor?=null
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
    var firstname_status: Boolean = false
    var driverimage_status: Boolean = false
    var lastname_status: Boolean = false
    var phone_number_status: Boolean = false
    var country_code_status: Boolean = false
    var email_status: Boolean = false
    var isEmailphonebothVerify: Boolean = false
    var token: String = ""
    var tokenReg: String = ""
    var txt_country_code: TextView? = null
    var et_fname: EditText? = null
    var et_lname: EditText? = null
    var et_email: EditText? = null
    var edt_phone_number: EditText? = null
    var bt_update: Button? = null
    var iv_back: ImageView? = null
    var onCountryListOpen: LinearLayout? = null
    var response1: Call<EditProfilePojo>? = null
    var bottomSheetFragment: SelectCountryCodeFragment? = null
    var encodedImage: String=""
    var responseProfile: Call<UserDetailPojo>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var storeuserimage: String?=""
    var firstName: String?=""
    var lastName: String?=""
    var storeuserphone: String?=""
    var storeuserphonecode: String?=""
    var storeuseremail: String?=""
    var storepin: String?=""
    var encodeimageurl: String?=""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_update)

        et_fname = findViewById<EditText>(R.id.et_fname)
        et_lname = findViewById<EditText>(R.id.et_lname)
        et_email = findViewById<EditText>(R.id.et_email)
        edt_phone_number = findViewById<EditText>(R.id.edt_phone_number)
        txt_country_code = findViewById<TextView>(R.id.txt_country_code)
        iv_back = findViewById<ImageView>(R.id.iv_back)
        bt_update = findViewById<Button>(R.id.bt_update)
        onCountryListOpen = findViewById<LinearLayout>(R.id.onCountryListOpen)

        et_fname!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        et_lname!!.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)

        sharedPreferences = getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        sharedPreferenceManager = SharedPreferenceManager(this)

        try {

            bottomSheetFragment = SelectCountryCodeFragment()

            println(sharedPreferences!!.getString("storeforprofileupdatepassword",""))

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

            if (defaultIdLog.equals("")){

                et_fname!!.setText(fnameSharedPrefAfterReg)
                et_lname!!.setText(lnameSharedPrefAfterReg)
                edt_phone_number!!.setText(phoneSharedPrefAfterReg)
                txt_country_code!!.setText(codeSharedPrefAfterReg)
                et_email!!.setText(emailSharedPrefAfterReg)

            }
            else{

                et_fname!!.setText(fnameSharedPrefAfterLog)
                et_lname!!.setText(lnameSharedPrefAfterLog)
                edt_phone_number!!.setText(phoneSharedPrefAfterLog)
                txt_country_code!!.setText(codeSharedPrefAfterLog)
                et_email!!.setText(emailSharedPrefAfterLog)

            }

            onCountryListOpen!!.setOnClickListener {

                showBottomSheetDialogFragment()

            }


            et_fname!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    Log.v("charSequence","charSequence"+charSequence)


                    if (defaultIdLog.equals("")){

                        if (fnameSharedPrefAfterReg.equals(charSequence.toString())){

                            firstname_status =false
                        }else{
                            firstname_status =true
                        }
                    }else {


                        if (fnameSharedPrefAfterLog.equals(charSequence.toString())) {

                            firstname_status = false
                        } else {
                            firstname_status = true
                        }
                    }

                }

                override fun afterTextChanged(editable: Editable) {

                }
            })

            et_lname!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    Log.v("charSequence","charSequence"+charSequence)

                    if (defaultIdLog.equals("")){

                        if (lnameSharedPrefAfterReg.equals(charSequence.toString())){

                            lastname_status =false
                        }else{
                            lastname_status =true
                        }
                    }else {


                        if (lnameSharedPrefAfterLog.equals(charSequence.toString())) {

                            lastname_status = false
                        } else {
                            lastname_status = true
                        }
                    }

                }

                override fun afterTextChanged(editable: Editable) {

                }
            })

            edt_phone_number!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    Log.v("charSequence","charSequence"+charSequence)


                    if (defaultIdLog.equals("")){

                        if (phoneSharedPrefAfterReg.equals(charSequence.toString())){

                            phone_number_status =false
                        }else{
                            phone_number_status =true
                        }
                    }else {


                        if (phoneSharedPrefAfterLog.equals(charSequence.toString())) {

                            phone_number_status = false
                        } else {
                            phone_number_status = true
                        }
                    }

                }

                override fun afterTextChanged(editable: Editable) {

                }
            })



            et_email!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    Log.v("charSequence","charSequence"+charSequence)


                    if (defaultIdLog.equals("")){

                        if (emailSharedPrefAfterReg.equals(charSequence.toString())){

                            email_status =false
                        }else{
                            email_status =true
                        }
                    }else {


                        if (emailSharedPrefAfterLog.equals(charSequence.toString())) {

                            email_status = false
                        } else {
                            email_status = true
                        }
                    }

                }

                override fun afterTextChanged(editable: Editable) {

                }
            })


            bt_update!!.setOnClickListener {

                try {

                    AppProgressBar.openLoader(
                        this,
                        this.getResources().getString(R.string.pleasewait)
                    )

                    val apiservice: ApiInterface = ApiClient.getClient(this).create(ApiInterface::class.java)
                    val req = HashMap<String, Any>()


                    if (defaultIdLog.equals("")){
                        req["id"] = defaultIdReg
                        req["firstname"] = et_fname!!.text.toString().trim()
                        req["lastname"] = et_lname!!.text.toString().trim()
                        req["phone_number"] = edt_phone_number!!.text.toString().trim()
                        req["country_code"] = txt_country_code!!.text.toString().trim()
                        req["email"] = et_email!!.text.toString().trim()
                        req["driver_image"] = "data:image/png;base64," + encodeimageurl
                        req["driver_image_status"] = driverimage_status

                        req["firstname_status"] =firstname_status
                        req["lastname_status"] =lastname_status
                        req["phone_number_status"] =phone_number_status

                        if (txt_country_code!!.text.toString().equals(codeSharedPrefAfterReg)) {
                            country_code_status =false
                            req["country_code_status"] = country_code_status
                        }else{
                            country_code_status =true
                            req["country_code_status"] = country_code_status
                        }
                        req["email_status"] =email_status

                        response1 = apiservice.doingEditProfile(tokenReg,req)

                    }else{
                        req["id"] = defaultIdLog
                        req["firstname"] = et_fname!!.text.toString().trim()
                        req["lastname"] = et_lname!!.text.toString().trim()
                        req["phone_number"] = edt_phone_number!!.text.toString().trim()
                        req["country_code"] = txt_country_code!!.text.toString().trim()
                        req["email"] = et_email!!.text.toString().trim()
                        req["driver_image"] = "data:image/png;base64," + encodeimageurl
                        req["driver_image_status"] = driverimage_status

                        req["firstname_status"] =firstname_status
                        req["lastname_status"] =lastname_status
                        req["phone_number_status"] =phone_number_status

                        if (txt_country_code!!.text.toString().equals(codeSharedPrefAfterLog)) {
                            country_code_status =false
                            req["country_code_status"] = country_code_status
                        }else{
                            country_code_status =true
                            req["country_code_status"] = country_code_status
                        }
                        req["email_status"] =email_status

                        response1 = apiservice.doingEditProfile(token,req)

                    }

                    response1!!.enqueue(object : Callback<EditProfilePojo?> {
                        override fun onResponse(
                            call: Call<EditProfilePojo?>,
                            response: Response<EditProfilePojo?>
                        ) {
                            if (response.isSuccessful()) {
                                AppProgressBar.closeLoader()

                                if ( response.body() == null ){
                                    AppProgressBar.closeLoader()
                                    return
                                }

                                else if ( response.body() != null && response.body()!!.status==true) {

                                    val storepassword = sharedPreferences!!.getString("storeforprofileupdatepassword","")
                                    println(sharedPreferences!!.getString("storeforprofileupdatepassword",""))
                                    myEdit = sharedPreferences!!.edit()
                                    myEdit!!.putString("idSharedPrefAfterLog", response.body()!!.customerImage!!.driver_id!!)
                                    myEdit!!.putString("fnameSharedPrefAfterLog", response.body()!!.customerImage!!.firstname!!)
                                    myEdit!!.putString("lnameSharedPrefAfterLog", response.body()!!.customerImage!!.lastname!!)
                                    myEdit!!.putString("emailSharedPrefAfterLog", response.body()!!.customerImage!!.email!!)
                                    myEdit!!.putString("storeforprofileupdatephone_number", response.body()!!.customerImage!!.phone_number!!)
                                    myEdit!!.putString("storeforprofileupdatephone_numbercode", response.body()!!.customerImage!!.country_code!!)
                                    myEdit!!.putString("storeforprofileupdateemail", response.body()!!.customerImage!!.email!!)
                                    myEdit!!.putString("phoneSharedPrefAfterLog", response.body()!!.customerImage!!.phone_number!!)
                                    myEdit!!.putString("codeSharedPrefAfterLog", response.body()!!.customerImage!!.country_code!!)
                                    myEdit!!.putBoolean("isMobileVerify", response.body()!!.customerImage!!.mobile_verify_status!!)
                                    myEdit!!.putBoolean("isEmailVerify", response.body()!!.customerImage!!.email_verify_status!!)

                                    if (response.body()!!.customerImage!!.mobile_verify_status == true && response.body()!!.customerImage!!.email_verify_status == true){

                                        myEdit!!.putBoolean("isEmailphonebothVerify", isEmailphonebothVerify)

                                    }else{

                                        myEdit!!.putBoolean("isEmailphonebothVerify", isEmailphonebothVerify)

                                    }

                                    myEdit!!.putString("storeforprofileupdatepassword", storepassword)
                                    // myEdit.putString("Authorization", response.body()!!.token!!)
                                   // myEdit.putString("fixed_pin", response.body()!!.details!!.fixed_pin!!)
                                    myEdit!!.putString("device_fcm_token", response.body()!!.customerImage!!.device_fcm_token!!)
                                   // myEdit.putString("stripe_customer_id", response.body()!!.details!!.stripe_customer_id!!)

                                    myEdit!!.apply()


                                    if (response.body()!!.customerImage!!.mobile_verify_status == true && response.body()!!.customerImage!!.email_verify_status == true){

                                        Toast.makeText(
                                            this@EditProfileUpdate,
                                            response.body()!!.detail.toString(),
                                            Toast.LENGTH_LONG
                                        ).show()



                                        val myIntent = Intent(this@EditProfileUpdate, HomeActivity::class.java)
                                        // myIntent.putExtra("locationscreen",editoff)
                                        startActivity(myIntent)
                                        finish()


                                    }
                                    else if (response.body()!!.customerImage!!.mobile_verify_status == false || response.body()!!.customerImage!!.email_verify_status == false){

                                      //  if (response.body()!!.customerImage!!.mobile_verify_status == true && response.body()!!.customerImage!!.email_verify_status == true) {

                                            val myIntent = Intent(this@EditProfileUpdate, LogInActivity::class.java)

                                            myIntent.putExtra("updateprofile",true)

                                            myIntent.putExtra("isEmailphonebothVerify", isEmailphonebothVerify)

                                            myEdit = sharedPreferences!!.edit()
                                            Log.v("isEmailphonebothVerify",""+isEmailphonebothVerify)
                                            myEdit!!.putBoolean("isEmailphonebothVerify", isEmailphonebothVerify)
                                            myEdit!!.apply()
                                            myEdit!!.commit()

                                            startActivity(myIntent)
                                            finish()
                                     //   }
                                       /* else{
                                            isEmailphonebothVerify = false

                                            val myIntent = Intent(this@EditProfileUpdate, LogInActivity::class.java)
                                            myIntent.putExtra("updateprofile",true)
                                            myIntent.putExtra("isEmailphonebothVerify", isEmailphonebothVerify)

                                            myEdit = sharedPreferences!!.edit()
                                            Log.v("isEmailphonebothVerify",""+isEmailphonebothVerify)
                                            myEdit!!.putBoolean("isEmailphonebothVerify", isEmailphonebothVerify)
                                            myEdit!!.apply()

                                            startActivity(myIntent)
                                            finish()
                                        }*/
                                       /* myEdit = sharedPreferences!!.edit()
                                        Log.v("isEmailphonebothVerify",""+isEmailphonebothVerify)
                                        myEdit!!.putBoolean("isEmailphonebothVerify", isEmailphonebothVerify)
                                        myEdit!!.apply()

                                        startActivity(myIntent)
                                        finish()*/

                                    }


                                    else{


                                    }


                                }else {

                                    if (response.body() != null) {
                                        Toast.makeText(
                                            this@EditProfileUpdate,
                                            response.body()!!.detail.toString(),
                                            Toast.LENGTH_LONG
                                        ).show()

                                    }
                                }


                            } else {

                                AppProgressBar.closeLoader()

                                if (response.body() != null) {
                                    Toast.makeText(
                                        this@EditProfileUpdate,
                                        response.body()!!.detail.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                            }
                        }

                        override fun onFailure(call: Call<EditProfilePojo?>, t: Throwable) {
                            AppProgressBar.closeLoader()
                            if (t is IOException) {
                                if (t is NoConnectivityException) {
                                    Toast.makeText(
                                        this@EditProfileUpdate,
                                        "" + t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {

                                    if (t.message!!.contains("Failed to connect")) {

                                        Toast.makeText(
                                            this@EditProfileUpdate,
                                            "No Internet Connection Available",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {

                                        Toast.makeText(
                                            this@EditProfileUpdate,
                                            "" + t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            iv_back!!.setOnClickListener {

                finish()
            }

        }
        catch (e:Exception){

            e.printStackTrace()
        }
    }

    fun showBottomSheetDialogFragment() {

        bottomSheetFragment!!.isCancelable=false
        val bundle = Bundle()
        bundle.putString("test", "EditProfile")
        bottomSheetFragment!!.arguments = bundle
        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment!!.tag)

    }

    fun cancelCountryPopup(countryCode: Int) {

        txt_country_code!!.text = "+"+countryCode.toString()
        bottomSheetFragment?.dismiss()
    }

    override fun onResume() {

        super.onResume()

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences!!.edit()


        encodeimageurl = sharedPreferences!!.getString("encodeimageurl","")!!

    }

}
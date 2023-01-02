package com.muve.muve_it_driver.ui.auth.verifycodeemail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.FragmentVerifyEmailBinding
import com.muve.muve_it_driver.driverinformation.DriverInformationScreen
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.popupdiscloser.PopupDiscloser
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.editemail.EditEmailFragment
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.permissionscreen.PermissionScreen
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.NetworkUtility
import com.muve.muve_it_driver.util.clickOnPinView
import com.muve.muve_it_driver.util.isLogInButtonClicked
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class VerifyEmailFragment : BottomSheetDialogFragment() {


    var sharedPreferences: SharedPreferences? = null
    var sharedPreferences1: SharedPreferences? = null
    var responseResend: Call<ResendOtpPojo>? = null
    var visible_resentBtn : Boolean= false

    var defaultId: String?=""
    var defaultIdLog: String?=""
    var storeFirstname :String?=""
    var storeFirstnameLog :String?=""
    var storeLastnameLog :String?=""
    var storeLastname :String?=""
    var storeEmail :String?=""
    var storeEmailLog :String?=""
    var storePhoneLog :String?=""
    var emailUpdateSharedPref :String?=""
    var fromeditupdateprofile :Boolean?=false
    var updateprofile :Boolean?=false
    var storePhone :String?=""
    var iphoneUpdateSharedPref :String?=""
    var iphoneCodeUpdateSharedPref :String?=""
    var storeCode :String?=""
    var storeCodeLog :String?=""
    var response1: Call<VerifyOtpPojo>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null

    private lateinit var viewModel: VerifyEmailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vBinding: FragmentVerifyEmailBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_verify_email, container, false)
        viewModel = ViewModelProviders.of(this).get(VerifyEmailViewModel::class.java)
        vBinding.verifyemailcode = viewModel
        vBinding.lifecycleOwner = this


        try {

            sharedPreferenceManager = SharedPreferenceManager(requireActivity())

            sharedPreferences = requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            sharedPreferences1 = requireContext().getSharedPreferences("MySharedPref1", AppCompatActivity.MODE_PRIVATE)


            defaultId = sharedPreferences!!.getString("idSharedPref","")!!
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            storeFirstname = sharedPreferences!!.getString("fnameSharedPref","")!!
            storeFirstnameLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","")!!
            storeLastname = sharedPreferences!!.getString("lnameSharedPref","")!!
            storeLastnameLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","")!!
            storeEmail = sharedPreferences!!.getString("emailSharedPref","")!!
            storeEmailLog = sharedPreferences!!.getString("emailSharedPrefAfterLog","")!!
            storePhone = sharedPreferences!!.getString("phoneSharedPref","")!!
            storePhoneLog = sharedPreferences!!.getString("phoneSharedPrefAfterLog","")!!
            storeCode = sharedPreferences!!.getString("codeSharedPref","")!!
            storeCodeLog = sharedPreferences!!.getString("codeSharedPrefAfterLog","")!!
            iphoneUpdateSharedPref = sharedPreferences!!.getString("iphoneUpdateSharedPref","")!!
            iphoneCodeUpdateSharedPref = sharedPreferences!!.getString("iphoneCodeUpdateSharedPref","")!!
            emailUpdateSharedPref =sharedPreferences!!.getString("emailUpdateSharedPref","")!!
            updateprofile =requireArguments().getBoolean("updateprofile")


            fromeditupdateprofile =sharedPreferences!!.getBoolean("fromeditupdateprofile",false)!!

            try {
                if (requireArguments()!!.getBoolean("fromeditupdateprofile") != null) {

                    fromeditupdateprofile =
                        requireArguments()!!.getBoolean("fromeditupdateprofile", false)
                }


            }catch (e:Exception){

                e.printStackTrace()
            }


        }

        catch ( e : Exception){
            e.printStackTrace()
        }

        vBinding.btnContinueForEmail.setOnClickListener {


            if (vBinding.edtPassword1.text.toString().equals("") && vBinding.edtPassword2.text.toString().equals("") && vBinding.edtPassword3.text.toString().equals(
                    ""
                ) && vBinding.edtPassword4.text.toString().equals("")
            ) {

                Toast.makeText(requireContext(), "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()
            }

            else {

                if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                    verifyOTPForEmail(vBinding)
                }
                else{
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.msg_no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        vBinding.btnContinueOfSuccess.setOnClickListener {

            dismiss()


            fetchLocation()

        }

        startCounterEmail(vBinding)



        vBinding.txtResendTxt.setOnClickListener {

            vBinding.btnContinueForEmail.isEnabled=true
            vBinding.btnContinueForEmail.isClickable=true
            vBinding.btnContinueForEmail!!.setBackgroundResource(/*getDrawable(*/R.drawable.round_background_2)


            reSendAPI(vBinding)
            startCounterEmail(vBinding)


        }

        try {

            sharedPreferences = requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            val myEdit = sharedPreferences!!.edit()

            if (!sharedPreferences!!.getString("emailUpdateSharedPref","")!!.equals("")) {

                emailUpdateSharedPref = sharedPreferences!!.getString("emailUpdateSharedPref", "")!!

                vBinding.txtSubTonumber.setText(emailUpdateSharedPref)

                //  iphoneUpdateSharedPref =""
                //  iphoneCodeUpdateSharedPref=""
                myEdit.remove(iphoneUpdateSharedPref)
                myEdit.remove(iphoneCodeUpdateSharedPref)
                myEdit.commit()

            }else{

            }





            if (arguments?.getString("afterloginphoneverifyneedemail").equals("afterloginphoneverifyneedemail")){

                if (!sharedPreferences!!.getString("emailSharedPrefAfterLog","")!!.equals("")){
                    vBinding.txtSubTonumber.setText(sharedPreferences!!.getString("emailSharedPrefAfterLog",""))
                }
            }
            else{

                if (storeEmailLog.equals("") || storeEmailLog==null) {
                    vBinding.txtSubTonumber.setText(storeEmail)
                }
                else {

                    vBinding.txtSubTonumber.setText(storeEmailLog)
                }



            }

            if (emailUpdateSharedPref!= null && !emailUpdateSharedPref.equals("")){

                vBinding.txtSubTonumber.setText(emailUpdateSharedPref)
            }
            else if (storeEmailLog.equals("") || storeEmailLog==null){
                vBinding.txtSubTonumber.setText(storeEmail)
            }else{
                vBinding.txtSubTonumber.setText(storeEmailLog)
            }

        }

        catch ( e : Exception){
            e.printStackTrace()
        }

        vBinding.imgClose.setOnClickListener {

            dismiss()

            if  (isLogInButtonClicked) {

                Intent(requireActivity(), LogInActivity::class.java).also {

                    requireActivity().startActivity(it)
                }
            }

            else {
                Intent(requireActivity(), RegisterActivity::class.java).also {

                    requireActivity().startActivity(it)
                }

            }



        }


        vBinding.imgEdit.setOnClickListener(View.OnClickListener {


            // dismiss()

            val bottomSheetFragment = EditEmailFragment()
            val bundle = Bundle()
            bundle.putString("emailshow",vBinding.txtSubTonumber.text.toString())
            bundle.putString("emailshow",vBinding.txtSubTonumber.text.toString())
            bottomSheetFragment.setArguments(bundle)
            bottomSheetFragment.isCancelable=false

            bottomSheetFragment.show(requireFragmentManager(),bottomSheetFragment!!.tag)

        })


        clickOnPinView(vBinding)

        return vBinding.root
    }

    private fun reSendAPI(vBinding: FragmentVerifyEmailBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["phone_email"] = vBinding.txtSubTonumber.text.toString()
            req["type"] = "email"

            responseResend = apiservice.doingResendAPIOTP(req)
            responseResend!!.enqueue(object : Callback<ResendOtpPojo?> {
                override fun onResponse(
                    call: Call<ResendOtpPojo?>,
                    response: Response<ResendOtpPojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status==true) {

                            AppProgressBar.closeLoader();

                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.detail!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                    )
                                ) {

                                    val sharedPreferences =requireActivity().
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(
                                            requireActivity(),
                                            WelcomeActivity::class.java
                                        )
                                    startActivity(myIntent)

                                    requireActivity().finish()

                                }
                                else {


                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.detail.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }


                    } else {

                        AppProgressBar.closeLoader();
                        if (response.body() != null) {

                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<ResendOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            requireActivity(),
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            requireActivity(),
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


        if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true && sharedPreferences1!!.getBoolean("locationallow", false) == true
            && sharedPreferences1!!.getBoolean("cameraallow", false) == true && sharedPreferences1!!.getBoolean("storageallow", false) == true
            && sharedPreferences1!!.getBoolean("phoneallow", false) == true && sharedPreferences!!.getBoolean("is_driverinformation_status", false) == false) {

            val myIntent_ =
                Intent(requireActivity(), DriverInformationScreen::class.java)
            requireActivity().startActivity(myIntent_)
            requireActivity().finish()
        }

       /* if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true && sharedPreferences1!!.getBoolean("locationallow", false) == true
            && sharedPreferences1!!.getBoolean("cameraallow", false) == true && sharedPreferences1!!.getBoolean("storageallow", false) == true
            && sharedPreferences1!!.getBoolean("phoneallow", false) == true && sharedPreferences!!.getBoolean("is_driverinformation_status", false) == true && sharedPreferences!!.getBoolean("is_driver_vehicle_information_status", false) == false) {

            val myIntent_ =
                Intent(requireActivity(), VehicleinfoScreen::class.java)
            requireActivity().startActivity(myIntent_)
            requireActivity().finish()
        }*/

        else if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true && sharedPreferences1!!.getBoolean("locationallow", false) == false) {

            val myIntent =
                Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen", "Location")
            myIntent.putExtra(
                "is_driverinformation_status",
                sharedPreferences!!.getBoolean("is_driverinformation_status", false)
            )
            myIntent.putExtra(
                "is_driver_vehicle_information_status",
                sharedPreferences!!.getBoolean("is_driver_vehicle_information_status", false)
            )
            requireActivity().startActivity(myIntent)
            requireActivity().finish()

        }else if (sharedPreferences1!!.getBoolean("isshowpopup", false) == false){

            val myIntent =
                Intent(requireActivity(), PopupDiscloser::class.java)
            myIntent.putExtra("locationscreen", "Location")
            myIntent.putExtra(
                "is_driverinformation_status",
                sharedPreferences!!.getBoolean("is_driverinformation_status", false)
            )
            myIntent.putExtra(
                "is_driver_vehicle_information_status",
                sharedPreferences!!.getBoolean("is_driver_vehicle_information_status", false)
            )
            requireActivity().startActivity(myIntent)
            requireActivity().finish()

        }


        else if (sharedPreferences1!!.getBoolean("locationallow", false) == false) {

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                if (sharedPreferences1!!.getBoolean("isshowpopup", false) == true) {

                    val myIntent =
                        Intent(requireActivity(), PermissionScreen::class.java)
                    myIntent.putExtra("locationscreen", "Location")
                    myIntent.putExtra(
                        "is_driverinformation_status",
                        sharedPreferences!!.getBoolean("is_driverinformation_status", false)
                    )
                    myIntent.putExtra(
                        "is_driver_vehicle_information_status",
                        sharedPreferences!!.getBoolean("is_driver_vehicle_information_status", false)
                    )
                    requireActivity().startActivity(myIntent)
                    requireActivity().finish()


                }
                else {

                    val myIntent =
                        Intent(requireActivity(), PopupDiscloser::class.java)
                    myIntent.putExtra("locationscreen", "Location")
                    myIntent.putExtra(
                        "is_driverinformation_status",
                        sharedPreferences!!.getBoolean("is_driverinformation_status", false)
                    )
                    myIntent.putExtra(
                        "is_driver_vehicle_information_status",
                        sharedPreferences!!.getBoolean("is_driver_vehicle_information_status", false)
                    )
                    requireActivity().startActivity(myIntent)
                    requireActivity().finish()

                }
            }

        }
        else{

            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
            requireActivity().startActivity(myIntent)
            requireActivity().finish()

            //add logout for ios issue

           // getLoggedOut()

        }

        //17.08.22 Morning

          /*  if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(requireActivity(), DummyPopupDiscloser::class.java)
            requireActivity().startActivity(myIntent)

           *//* val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Location")
            startActivity(myIntent)*//*

        }
        else if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        )  {

            val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Camera")
            startActivity(myIntent)


        }

        else  if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Storage")
            startActivity(myIntent)

        }

        else if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Phone")
            startActivity(myIntent)

        }
        else if (sharedPreferences!!.getBoolean("is_driverinformation_status",false)==false){

            val myIntent = Intent(requireActivity(), DriverInformationScreen::class.java)
            startActivity(myIntent)
            requireActivity().finish()
        }else{
            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(myIntent)
            requireActivity().finish()

        }
*/
    }

    fun getLoggedOut() {



    }

    private fun verifyOTPForEmail(vBinding: FragmentVerifyEmailBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["phone_email"] = vBinding.txtSubTonumber.text.toString()
            req["type"] = "email"
            req["otp"] =  vBinding.edtPassword1.text.toString()+vBinding.edtPassword2.text.toString()+vBinding.edtPassword3.text.toString()+vBinding.edtPassword4.text.toString()

            response1 = apiservice.doingVerifyOTP(req)
            response1!!.enqueue(object : Callback<VerifyOtpPojo?> {
                override fun onResponse(
                    call: Call<VerifyOtpPojo?>,
                    response: Response<VerifyOtpPojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status==true) {
                            AppProgressBar.closeLoader();

                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            if (fromeditupdateprofile == true) {

                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(myIntent)
                                requireActivity().finish()


                            }
                           else if (updateprofile == true) {

                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(myIntent)
                                requireActivity().finish()


                            }

                            else if (sharedPreferences!!.getBoolean("isEmailphonebothVerify",false) == true) {

                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(myIntent)
                                requireActivity().finish()


                            }



                            else {


                                vBinding.mainRl.isVisible = false
                                vBinding.txtVerifyCode.isVisible = false
                                vBinding.successPopup.isVisible = true

                               // vBinding.imgCloseSuccess.isVisible = true
                                vBinding.imgCheck.isVisible = true
                                vBinding.txtOfsuccess.isVisible = true
                                vBinding.txtOfsuccessHeader.isVisible = true
                                vBinding.btnContinueOfSuccess.isVisible = true
                                vBinding.successPopup.background =
                                    requireContext().getDrawable(R.drawable.rounded_corner_4)

                            }
                        }


                            else{

                                AppProgressBar.closeLoader()

                                vBinding.edtPassword1.text.clear()
                                vBinding.edtPassword2.text.clear()
                                vBinding.edtPassword3.text.clear()
                                vBinding.edtPassword4.text.clear()


                                if (response.body()!!.message.equals(resources.getString(R.string.youhavebeenloggedinfromanotherdevice),ignoreCase = true)){

                                    val sharedPreferences =requireActivity().
                                        getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    sharedPreferenceManager!!.logoutUser()
                                    editor.clear()
                                    editor.commit()


                                    val myIntent =
                                        Intent(requireActivity(), WelcomeActivity::class.java)
                                    startActivity(myIntent)

                                    requireActivity().finish()

                                }

                                else{


                                    Toast.makeText(
                                        requireActivity(),
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                        }


                    } else {


                        AppProgressBar.closeLoader()

                        Toast.makeText(
                            requireActivity(),
                            response.body()!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }

                override fun onFailure(call: Call<VerifyOtpPojo?>, t: Throwable) {
                    AppProgressBar.closeLoader()
                    if(t is NoConnectivityException) {
                        Toast.makeText(
                            requireActivity(),
                            ""+t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{

                        Toast.makeText(
                            requireActivity(),
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

    private fun startCounterEmail(vBinding: FragmentVerifyEmailBinding) {

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                visible_resentBtn = false

                val text = "${String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            ))} "
                // vBinding.txtResendTxt.setText((millisUntilFinished / 1000).toString())
                vBinding.txtResendTxt.setText(text)
                //  tv_btnText.setText("Continue")
            }

            override fun onFinish() {
                // vBinding.txtResend.setVisibility(View.VISIBLE)
                // vBinding.txtResend.setVisibility(View.VISIBLE)
                vBinding.txtResendTxt.setVisibility(View.VISIBLE)
                visible_resentBtn = true
                vBinding.txtResendTxt.setText("Resend")
                vBinding.btnContinueForEmail.isEnabled=false
                vBinding.btnContinueForEmail.isClickable=false
                vBinding.btnContinueForEmail!!.setBackgroundResource(/*getDrawable(*/R.drawable.round_background_2_grey)


            }
        }.start()


    }



}
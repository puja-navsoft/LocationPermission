package com.muve.muve_it_driver.ui.auth.verifycode

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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.application.Muve
import com.muve.muve_it_driver.databinding.VerifyCodeFragmentBinding
import com.muve.muve_it_driver.home.HomeActivity
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.model.verifyotp.VerifyOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.verifycodeemail.VerifyEmailFragment
import com.muve.muve_it_driver.ui.auth.edit_number.EditNumberFragment
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.permissionscreen.PermissionScreen
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.muve.muve_it_driver.util.clickOnPinView
import com.muve.muve_it_driver.util.isLogInButtonClicked
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muve.muve_it_driver.popupdiscloser.PopupDiscloser
import com.muve.muve_it_driver.util.NetworkUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit


class VerifyCodeFragment : BottomSheetDialogFragment()/*, verifyListener*/ {


    var sharedPreferences: SharedPreferences? = null
    var sharedPreferences1: SharedPreferences? = null
    var myEdit: SharedPreferences.Editor?=null
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
    var storePhone :String?=""
    var iphoneUpdateSharedPref :String?=""
    var iphoneCodeUpdateSharedPref :String?=""
    var storeCode :String?=""
    var storeCodeLog :String?=""

    var bottomSheetFragmentEditNumber: EditNumberFragment? = null
    var verifyEmailBottomsheet: VerifyEmailFragment? = null
    var phoneverification=true
    private lateinit var viewModel: VerifyCodeViewModel
    //  var verifyCodeGamilBottomsheet: VerifyCodeGamilFragment? = null
    private val MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: Int =2
    private val MY_PERMISSIONS_REQUEST_ACCESS_STORAGE: Int =3
    private val MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: Int =1
    private val MY_PERMISSIONS_REQUEST_ACCESS_PHONE: Int =4
    var phonecode : kotlin.String ?="phonecode"
    var codeverify : kotlin.String =""
    var afterloginphone : kotlin.String =""
    var mobile : kotlin.String =""
    var email : kotlin.String =""
    var afterEditEmail : kotlin.String =""
    var mobilecode : kotlin.String =""
    var afterlogincountry_code : kotlin.String =""
    var AfterSelectCodeEmail : kotlin.String =""
    var afterloginemail : kotlin.String =""
    var afterloginphoneverifyneed : kotlin.String =""
    var afterloginphoneverifyneedemail : kotlin.String =""
    var withoutChangeCountrycodeEditPhone : kotlin.String =""
    var editemail : kotlin.String =""
    var custRegId : String =""
    var afterloginid : String =""
    var savecustRegId : kotlin.Int =0
    var afterEditEmailsavecustRegId : kotlin.Int =0
    var AfterSelectCodecustRegId : kotlin.Int =0
    var visible_resentBtn : Boolean= false
    var editemailchange : Boolean= false
    var editedmail : Boolean= false
    var phoneverifyDone : Boolean= false
    var response1: Call<VerifyOtpPojo>? = null
    var responseResend: Call<ResendOtpPojo>? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val vBinding: VerifyCodeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.verify_code_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(VerifyCodeViewModel::class.java)
        vBinding.verifycode = viewModel
        vBinding.lifecycleOwner = this
        val behaviour = (dialog as BottomSheetDialog).behavior
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetFragmentEditNumber = EditNumberFragment()
        verifyEmailBottomsheet= VerifyEmailFragment()

        vBinding.rlMain.visibility = View.VISIBLE
        //  showview(vBinding)

        /*val params =
            (requireView()).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            (behavior as BottomSheetBehavior<*>).peekHeight = 700
        }*/

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



        }

        catch ( e : Exception){
            e.printStackTrace()
        }

        if (arguments?.getString("afterloginphoneverifyneed") != null) {

            afterloginphoneverifyneed = arguments?.getString("afterloginphoneverifyneed")!!

        }

        try {
            if (storePhone != null  && ! storePhone.equals("")) {

                vBinding.txtSubTonumberCode.isVisible=true
                vBinding.txtSubTonumberCode.setText(storeCode)
                vBinding.txtSubTonumber.setText(storePhone)


            }

            else if (iphoneUpdateSharedPref != null && ! iphoneUpdateSharedPref.equals("") && iphoneCodeUpdateSharedPref != null && ! iphoneCodeUpdateSharedPref.equals("")) {

                vBinding.txtSubTonumberCode.isVisible=true
                vBinding.txtSubTonumber.setText(iphoneCodeUpdateSharedPref)
                vBinding.txtSubTonumber.setText( iphoneUpdateSharedPref)

            }

        }
        catch (e : Exception){

            e.printStackTrace()
        }

        vBinding.imgClose.setOnClickListener {

            //  dismiss()

            if  (isLogInButtonClicked) {

                val myIntent = Intent(context, LogInActivity::class.java)
                requireActivity().startActivity(myIntent)
                requireActivity().finish()

            } else {
                val myIntent = Intent(context, RegisterActivity::class.java)
                requireActivity().startActivity(myIntent)
                requireActivity().finish()
            }


        }

        if (arguments?.getString("codeverify") != null) {

            codeverify = arguments?.getString("codeverify")!!

        }

        if (arguments?.getString("afterlogin") != null) {

            phonecode = arguments?.getString("afterlogin")!!
            codeverify =  arguments?.getString("codeverify")!!
            afterloginphone =  arguments?.getString("phone")!!
            afterloginid =  arguments?.getString("id")!!
            afterlogincountry_code =  arguments?.getString("country_code")!!
            afterloginemail =  arguments?.getString("email")!!

        }

        startCounter(vBinding)

        vBinding.txtResendTxt.setOnClickListener {

            vBinding.btnContinue.isEnabled=true
            vBinding.btnContinue.isClickable=true
            vBinding.btnContinue!!.setBackground(requireContext().getDrawable(R.drawable.round_background_2))


            reSendAPI(vBinding)
            startCounter(vBinding)


        }


        vBinding.btnContinue.setOnClickListener {


            if (vBinding.edtPassword1.text.toString().equals("") && vBinding.edtPassword2.text.toString().equals("") && vBinding.edtPassword3.text.toString().equals(
                    ""
                ) && vBinding.edtPassword4.text.toString().equals("")
            ) {

                Toast.makeText(requireContext(), "Enter 4 digit OTP ", Toast.LENGTH_LONG).show()
            } else {

                if (NetworkUtility.isNetworkAvailable(requireActivity())) {
                    verifyOTPForPhone(vBinding)
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


        if (arguments?.getString("withoutChangeCountrycodeEditPhone") != null) {

            withoutChangeCountrycodeEditPhone = arguments?.getString("withoutChangeCountrycodeEditPhone")!!

        }

        vBinding.imgEdit.setOnClickListener(View.OnClickListener {

            //

            // dismiss()

            //6th Jan,22

            /*  bottomSheetFragmentEditNumber = EditNumberFragment()
              val bundle = Bundle()
              bottomSheetFragmentEditNumber!!.setArguments(bundle)
              bottomSheetFragmentEditNumber!!.isCancelable=false

              //1st call in edit icon click

              bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)*/

            //  bottomSheetFragmentEditNumber!!.dismiss()

            /* if (phonecode.equals("phonecode")){


               //  BottomSheetBehavior.from(vBinding.rlMain).setPeekHeight(1)


                 withoutChangeCountrycodeEditPhone="default"



                 bottomSheetFragmentEditNumber = EditNumberFragment()
                 val bundle = Bundle()
                 bundle.putString("EditEmail", "phone")
                 bundle.putString("saveEmail", email)
                 bundle.putInt("custRegId", custRegId)
                 bundle.putString("mobilecode", mobilecode)
                 bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                 bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                 bundle.putBoolean("phoneverifyDone", true)
                 bottomSheetFragmentEditNumber!!.setArguments(bundle)
                 bottomSheetFragmentEditNumber!!.isCancelable=false

               //  edit icon click 2nd call this show

                 bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)
                 bottomSheetFragmentEditNumber!!.dismiss()

             }*/
            if (arguments?.getString("afterlogin").equals("afterlogin")){

                // BottomSheetBehavior.from(vBinding.rlMain).setPeekHeight(1)



                bottomSheetFragmentEditNumber = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", "phone")
                bundle.putString("afterlogin", "afterloginPhone")
                bundle.putString("saveEmail", email)
                bundle.putString("custRegId", custRegId)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putBoolean("phoneverifyDone", true)
                bottomSheetFragmentEditNumber!!.setArguments(bundle)
                bottomSheetFragmentEditNumber!!.isCancelable=false

                //after login 2nd call
                bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)


                //   bottomSheetFragmentEditNumber!!.dismiss()


            }
            else if (withoutChangeCountrycodeEditPhone.equals("")){


                bottomSheetFragmentEditNumber = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", /*"editemail"*/ "phone")
                bundle.putString("saveEmail", email)
                bundle.putString("custRegId", custRegId)
                bundle.putInt("savecustRegId_", arguments?.getInt("savecustRegId")!!)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putBoolean("phoneverifyDone", true)
                bottomSheetFragmentEditNumber!!.setArguments(bundle)
                bottomSheetFragmentEditNumber!!.isCancelable=false
                bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)

                bottomSheetFragmentEditNumber!!.dismiss()


            }
            else if( arguments?.getString("AfterSelectCodeEmail").equals("")){


                // dismiss()
                /* val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from<View>(rlMain)
                 behavior.setPeekHeight(1)
 */

                val bottomSheetFragment = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", /*"editemail"*/ "phone")
                bundle.putString("custRegId", custRegId)
                bundle.putInt("savecustRegId", savecustRegId)
                bundle.putBoolean("editemailchange", editemailchange)
                bundle.putBoolean("phoneverifyDone", true)
                bundle.putString("afterloginid", arguments?.getString("id")!!)
                bundle.putInt("afterloginphone", arguments?.getInt("phone")!!)
                bundle.putInt("afterlogincountry_code", arguments?.getInt("country_code")!!)
                bundle.putInt("afterloginemail", arguments?.getInt("email")!!)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putString("afterlogin", "afterlogin")
                bundle.putInt("afterEditEmailsavecustRegId", afterEditEmailsavecustRegId!!)
                bundle.putInt("AfterSelectCodecustRegId",arguments?.getInt("AfterSelectCodecustRegId")!!)
                bottomSheetFragment.setArguments(bundle)
                bottomSheetFragment.isCancelable=false

                //2nd call in edit icon click


            }
            else if (phonecode.equals("afterlogin")){

                //  dismiss()

                /* val behavior: BottomSheetBehavior<View> = BottomSheetBehavior.from<View>(rlMain)
                 behavior.setPeekHeight(1)*/

                bottomSheetFragmentEditNumber = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", "phone")
                bundle.putString("saveEmail", email)
                bundle.putString("custRegId", custRegId)
                bundle.putString("afterloginid", arguments?.getString("id")!!)
                bundle.putInt("afterloginphone", arguments?.getInt("phone")!!)
                bundle.putInt("afterlogincountry_code", arguments?.getInt("country_code")!!)
                bundle.putInt("afterloginemail", arguments?.getInt("email")!!)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putString("afterlogin", "afterlogin")
                bundle.putBoolean("phoneverifyDone", true)
                bottomSheetFragmentEditNumber!!.setArguments(bundle)
                bottomSheetFragmentEditNumber!!.isCancelable=false
                bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)


            }
            else if (arguments?.getString("afterloginphoneverifyneedemail") !=null){

                //  dismiss()

                val bottomSheetFragment = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", editemail)
                bundle.putString("afterloginphoneverifyneedemail", "afterloginphoneverifyneedemail")
                bundle.putString("custRegId", custRegId)
                bundle.putInt("savecustRegId", savecustRegId)
                bundle.putBoolean("editemailchange", editemailchange)
                bundle.putBoolean("phoneverifyDone", true)
                bundle.putString("afterloginid", arguments?.getString("id")!!)
                bundle.putInt("afterloginphone", arguments?.getInt("phone")!!)
                bundle.putInt("afterlogincountry_code", arguments?.getInt("country_code")!!)
                bundle.putInt("afterloginemail", arguments?.getInt("email")!!)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putString("afterlogin", "afterlogin")
                bundle.putInt("afterEditEmailsavecustRegId", afterEditEmailsavecustRegId!!)
                bundle.putInt("AfterSelectCodecustRegId",arguments?.getInt("AfterSelectCodecustRegId")!!)
                bottomSheetFragment.setArguments(bundle)
                bottomSheetFragment.isCancelable=false

                bottomSheetFragment.show(requireFragmentManager(),bottomSheetFragment!!.tag)

            }
            else{

                val bottomSheetFragment = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", editemail)
                bundle.putString("custRegId", custRegId)
                bundle.putInt("savecustRegId", savecustRegId)
                bundle.putBoolean("editemailchange", editemailchange)
                bundle.putBoolean("phoneverifyDone", true)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putInt("afterEditEmailsavecustRegId", afterEditEmailsavecustRegId!!)
                bundle.putInt("AfterSelectCodecustRegId",arguments?.getInt("AfterSelectCodecustRegId")!!)
                bottomSheetFragment.setArguments(bundle)
                bottomSheetFragment.isCancelable=false

                //3rd call in edit icon click

                bottomSheetFragment.show(requireFragmentManager(),bottomSheetFragment!!.tag)
                bottomSheetFragment.dismiss()

                //  dismiss()


            }


            if (phonecode.equals("phonecode")){


                // BottomSheetBehavior.from(vBinding.rlMain).setPeekHeight(1)


                val params =
                    (requireView().getParent() as View).layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior

                if (behavior != null && behavior is BottomSheetBehavior) {
                    // behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
                    (behavior as BottomSheetBehavior).setPeekHeight(30)

                    behavior.isHideable=true
                }

                withoutChangeCountrycodeEditPhone="default"

                bottomSheetFragmentEditNumber = EditNumberFragment()
                val bundle = Bundle()
                bundle.putString("EditEmail", "phone")
                bundle.putString("saveEmail", email)
                bundle.putString("custRegId", custRegId)
                bundle.putString("mobilecode", mobilecode)
                bundle.putString("mcode", vBinding.txtSubTonumberCode.text.toString())
                bundle.putString("mphone", vBinding.txtSubTonumber.text.toString())
                bundle.putBoolean("phoneverifyDone", true)
                bottomSheetFragmentEditNumber!!.setArguments(bundle)
                bottomSheetFragmentEditNumber!!.isCancelable=false

                //  edit icon click 2nd call this show

                bottomSheetFragmentEditNumber!!.show(requireFragmentManager(),bottomSheetFragmentEditNumber!!.tag)
                // bottomSheetFragmentEditNumber!!.dismiss()

            }

        })


        try {

            sharedPreferences = requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            val myEdit = sharedPreferences!!.edit()

            if (!sharedPreferences!!.getString("emailUpdateSharedPref","")!!.equals("")) {

                emailUpdateSharedPref = sharedPreferences!!.getString("emailUpdateSharedPref", "")!!

                vBinding.txtSubTonumberCode.isVisible=false

                vBinding.txtSubTonumber.setText(emailUpdateSharedPref)

                //  iphoneUpdateSharedPref =""
                //  iphoneCodeUpdateSharedPref=""
                myEdit.remove(iphoneUpdateSharedPref)
                myEdit.remove(iphoneCodeUpdateSharedPref)
                myEdit.commit()

            }else{
                vBinding.txtSubTonumberCode.isVisible=true
                vBinding.txtSubTonumberCode.setText(storeCode)

                vBinding.txtSubTonumber.setText(storePhone)
            }

            if (sharedPreferences!!.getString("emailUpdateSharedPref","")!!.equals("") || sharedPreferences!!.getString("emailUpdateSharedPref","")==null) {

                if (iphoneUpdateSharedPref != null && !iphoneUpdateSharedPref.equals("") && iphoneCodeUpdateSharedPref != null && !iphoneCodeUpdateSharedPref.equals(
                        ""
                    )
                ) {

                    vBinding.txtSubTonumberCode.isVisible=true
                    vBinding.txtSubTonumberCode.setText(iphoneCodeUpdateSharedPref)
                    vBinding.txtSubTonumber.setText( iphoneUpdateSharedPref)

                }
            }else{

                emailUpdateSharedPref = sharedPreferences!!.getString("emailUpdateSharedPref", "")!!
                vBinding.txtSubTonumberCode.isVisible=false
                vBinding.txtSubTonumber.setText(emailUpdateSharedPref)
            }


            if(arguments?.getString("afterlogin").equals("afterlogin")){

                if (!sharedPreferences!!.getString("phoneSharedPrefAfterLog","")!!.equals("")){


                    vBinding.txtSubTonumberCode.isVisible=true
                    vBinding.txtSubTonumberCode.setText(sharedPreferences!!.getString("codeSharedPrefAfterLog",""))
                    vBinding.txtSubTonumber.setText(
                        sharedPreferences!!.getString("phoneSharedPrefAfterLog",""))
                }

            }
            else if (arguments?.getString("afterloginphoneverifyneedemail").equals("afterloginphoneverifyneedemail")){

                if (!sharedPreferences!!.getString("emailSharedPrefAfterLog","")!!.equals("")){
                    vBinding.txtSubTonumberCode.isVisible=false
                    vBinding.txtSubTonumber.setText(sharedPreferences!!.getString("emailSharedPrefAfterLog",""))
                }
            }

        }

        catch ( e : Exception){
            e.printStackTrace()
        }



        vBinding.btnContinueOfSuccess.setOnClickListener {

            dismiss()


            fetchLocation()

        }


        clickOnPinView(vBinding)

        return vBinding.root
    }



    private fun fetchLocation() {


        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (sharedPreferences1!!.getBoolean("isshowpopup",false)){

                val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
                myIntent.putExtra("locationscreen", "Location")
                requireActivity().startActivity(myIntent)
                requireActivity().finish()

            }else{

                val myIntent = Intent(requireActivity(), PopupDiscloser::class.java)
                requireActivity().startActivity(myIntent)

            }



           /* val myIntent = Intent(requireActivity(), PermissionScreen::class.java)
            myIntent.putExtra("locationscreen","Location")
            startActivity(myIntent)*/

        }
        else if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        )  {
            // Permission has already been granted

            // Toast.makeText(this, "Location permission Grant..", Toast.LENGTH_LONG).show()

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
        else{

            val myIntent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(myIntent)
        }

    }


    private fun reSendAPI(vBinding: VerifyCodeFragmentBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()
            req["phone_email"] = vBinding.txtSubTonumber.text.toString()
            req["type"] = "phone"

            //   if (!codeverify.equals("") && codeverify!=null) {
            req["country_code"] = vBinding.txtSubTonumberCode.text.toString()
            //  }

            responseResend = apiservice.doingResendAPIOTP(req)
            responseResend!!.enqueue(object : Callback<ResendOtpPojo?> {
                override fun onResponse(
                    call: Call<ResendOtpPojo?>,
                    response: Response<ResendOtpPojo?>
                ) {
                    if (response.isSuccessful()) {
                        AppProgressBar.closeLoader()

                        if (response.body()!!.status==true) {


                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                        }else{

                            AppProgressBar.closeLoader();


                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            if (!codeverify.equals("") && codeverify!=null) {
                                vBinding.txtResend.isVisible=false
                                vBinding.txtResendTxt.isVisible=true
                            }
                            else{
                                vBinding.txtResendTxt.isVisible=false
                                vBinding.txtResend.isVisible=true

                            }
                            codeverify="email"

                            // vBinding.txtResendTxt.setText("")
                            //startCounter(vBinding)

                        }


                    } else {
                        try {

                            AppProgressBar.closeLoader();

                            //  val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                Muve.getInstance(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(Muve.getInstance(), e.message, Toast.LENGTH_LONG)
                                .show()
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



    private fun verifyOTPForPhone(vBinding: VerifyCodeFragmentBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            req["phone_email"] = vBinding.txtSubTonumber.text.toString()!!
            req["type"] = "phone"
            req["country_code"] = vBinding.txtSubTonumberCode.text.toString()!!!!

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

                            // After crash
                            // (activity as RegisterActivity).cancelVerifyCodePopUp()

                            if  (isLogInButtonClicked) {

                                (activity as LogInActivity).cancelVerifyCodePopUp()
                            } else {
                                (activity as RegisterActivity).cancelVerifyCodePopUp()
                            }

                            RegisterActivity.updatephonenumber = vBinding.txtSubTonumber.text.toString().trim()!!
                            RegisterActivity.updatephonecode = vBinding.txtSubTonumberCode.text.toString().trim()!!

                            sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE
                            )


                            println("verifycode"+sharedPreferences!!.getString("storeforprofileupdatepassword",""))


                            myEdit = sharedPreferences!!.edit()
                            myEdit!!.putString("storeforprofileupdatephone_number", vBinding.txtSubTonumber.text.toString()!!)
                            myEdit!!.putString("storeforprofileupdatephone_numbercode", vBinding.txtSubTonumberCode.text.toString().trim()!!)
                            myEdit!!.apply()


                            dismiss()

                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                            verifyEmailBottomsheet = VerifyEmailFragment()
                            verifyEmailBottomsheet!!.isCancelable=false
                            verifyEmailBottomsheet!!.show(requireFragmentManager(), verifyEmailBottomsheet!!.tag)

                            if (! sharedPreferences!!.getBoolean("isEmailVerify" ,false) == true){


                                verifyEmailBottomsheet = VerifyEmailFragment()
                                verifyEmailBottomsheet!!.isCancelable=false
                                verifyEmailBottomsheet!!.show(requireFragmentManager(), verifyEmailBottomsheet!!.tag)

                            }
                            else if (requireActivity().intent.hasExtra("fromeditupdateprofile") == true){

                                val myIntent = Intent(requireActivity(), HomeActivity::class.java)
                                startActivity(myIntent)
                                requireActivity().finish()

                            }
                            else{

                                fetchLocation()
                            }
                        }
                        else{

                            AppProgressBar.closeLoader()

                            vBinding.edtPassword1.text.clear()
                            vBinding.edtPassword2.text.clear()
                            vBinding.edtPassword3.text.clear()
                            vBinding.edtPassword4.text.clear()

                            vBinding.edtPassword1.requestFocus()


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
                        AppProgressBar.closeLoader();

                        Toast.makeText(
                            requireContext(),
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

    private fun startCounter(vBinding: VerifyCodeFragmentBinding) {

        object :  CountDownTimer(60000, 1000) {
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

                //  vBinding.btnContinue!!.setBackground(getDrawable(R.drawable.round_background_2_grey))


                //  tv_btnText.setText("Continue")
            }

            override fun onFinish() {
                // vBinding.txtResend.setVisibility(View.VISIBLE)
                vBinding.txtResendTxt.setVisibility(View.VISIBLE)
                visible_resentBtn = true
                vBinding.txtResendTxt.setText("Resend")
                vBinding.btnContinue.isEnabled=false
                vBinding.btnContinue.isClickable=false
                vBinding.btnContinue!!.setBackgroundResource(/*getDrawable(*/R.drawable.round_background_2_grey)


            }
        }.start()
    }

    /* private fun showEditNumberBottomSheetdialog() {

         bottomSheetFragmentEditNumber?.show(requireFragmentManager(), bottomSheetFragmentEditNumber!!.tag)

     }*/


    private fun showVerifyBottomSheetdialogforgamil() {
        //   verifyCodeBottomsheet?.show(supportFragmentManager, verifyCodeBottomsheet!!.tag)

    }

    fun cancelEditCodePopUp() {

        bottomSheetFragmentEditNumber?.dismiss()
    }




}

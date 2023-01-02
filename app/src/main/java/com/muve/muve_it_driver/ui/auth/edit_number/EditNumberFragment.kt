package com.muve.muve_it_driver.ui.auth.edit_number

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.FragmentEditNumberBinding
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.choose_country.SelectCountryCodeFragment
import com.muve.muve_it_driver.ui.auth.verifycode.VerifyCodeFragment
import com.muve.muve_it_driver.ui.auth.welcome.WelcomeActivity
import com.muve.muve_it_driver.util.AppProgressBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_edit_number.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditNumberFragment : BottomSheetDialogFragment()/*, editNumberListener*/ {


    // SharedPref Variable
    var sharedPreferenceManager: SharedPreferenceManager? = null

    private var myEdit: SharedPreferences.Editor?=null
    var sharedPreferences: SharedPreferences?=null
     var defaultId: String?=""
     var defaultIdLog: String?=""
    var storeFirstname :String?=""
    var storeFirstnameLog :String?=""
    var storeLastname :String?=""
    var storeLastnameLog :String?=""
    var storeEmail :String?=""
    var storeEmailLog :String?=""
    var storeEmailUpdate :String?=""
    var storePhone :String?=""
    var storePhoneLog :String?=""
    var storeCode :String?=""
    var storeCodeLog :String?=""
    var storePhoneUpdate :String?=""
    var storePhoneCodeUpdate :String?=""
    var token :String?=""

    //


    private lateinit var viewModel: EditNumberViewModel
    var verifyCodeBottomsheet: VerifyCodeFragment? = null
    var bottomSheetFragment: SelectCountryCodeFragment? = null
    var data :Int?=null
    var custRegId :String?=""
    var savecustRegId :Int?=null
    var AfterSelectCodecustRegId :Int?=null
    var afterEditEmailsavecustRegId :Int?=null
    var AfterSelectCodeEmail :String?=""

    var click :Boolean?=false
    var editemailchange :Boolean?=false
    var phoneverifyDone :Boolean?=false
    var editedmail :Boolean?=false
    var email :String?=""
    var mobilecode :String?=""
    var responseEditDetails: Call<ResendOtpPojo>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_edit_number, container, false)

        val binding: FragmentEditNumberBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_edit_number,container,false)
        viewModel = ViewModelProvider(this).get(EditNumberViewModel::class.java)
        binding. editnumber= viewModel
        binding.lifecycleOwner = this
        val behaviour = (dialog as BottomSheetDialog).behavior
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        verifyCodeBottomsheet = VerifyCodeFragment()
        bottomSheetFragment = SelectCountryCodeFragment()
        click = false

        sharedPreferenceManager = SharedPreferenceManager(requireActivity())


        try {

            sharedPreferences = requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultId = sharedPreferences!!.getString("idSharedPref","")!!
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")!!
            storeFirstname = sharedPreferences!!.getString("fnameSharedPref","")
            storeFirstnameLog = sharedPreferences!!.getString("fnameSharedPrefAfterLog","")!!

            storeLastname = sharedPreferences!!.getString("lnameSharedPref","")
            storeLastnameLog = sharedPreferences!!.getString("lnameSharedPrefAfterLog","")!!

            storeEmail = sharedPreferences!!.getString("emailSharedPref","")
            storeEmailLog = sharedPreferences!!.getString("emailSharedPrefAfterLog","")!!

            storePhone = sharedPreferences!!.getString("phoneSharedPref","")
            storePhoneLog = sharedPreferences!!.getString("phoneSharedPrefAfterLog","")!!

            storeCode = sharedPreferences!!.getString("codeSharedPref","")
            storeCodeLog = sharedPreferences!!.getString("codeSharedPrefAfterLog","")!!

            token = sharedPreferences!!.getString("Authorization","").toString()

        }

        catch ( e : Exception){
            e.printStackTrace()
        }

        binding.imgCloseCountry.setOnClickListener {


            dismiss()

            /* val params =
                 (requireView().getParent() as View).layoutParams as CoordinatorLayout.LayoutParams

             val behavior = params.behavior

             if (behavior != null && behavior is BottomSheetBehavior) {
                 // behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
                 (behavior).setPeekHeight(30)

             }
 */
            //(activity as RegisterActivity ).showVerifyBottomSheetdialogAgain()


//carsh
            //   (activity as RegisterActivity ).showVerifyBottomSheetdialog(defaultId!!)

            //after crash change code



            //  if  (isLogInButtonClicked) {



            // (activity as LogInActivity).showVerifyBottomSheetdialog(defaultIdLog!! , "afterlogin" , "phone" , storePhoneLog!!, storeCodeLog!! , storeEmailLog!!)

            //  dismiss()
            // verifyCodeBottomsheet!!.setpickupheight()

            //  } else {

            //    dismiss()
            //  verifyCodeBottomsheet!!.setpickupheight()

            // (activity as RegisterActivity).showVerifyBottomSheetdialog(defaultId!!)
            //  }

            /*  verifyCodeBottomsheet = VerifyCodeFragment()

              val bundle = Bundle()
              bundle.putString("editclick", "editclick")
              bottomSheetFragment!!.setArguments(bundle)
              verifyCodeBottomsheet!!.isCancelable = false
              verifyCodeBottomsheet?.show(requireFragmentManager(), verifyCodeBottomsheet!!.tag)*/

            // dismiss()


        }


        if (arguments?.getString("custRegId")!=null) {

            custRegId = arguments?.getString("custRegId")
        }

        if (arguments?.getBoolean("phoneverifyDone")!=null) {
            phoneverifyDone = arguments?.getBoolean("phoneverifyDone")
        }

        if (arguments?.getInt("AfterSelectCodecustRegId")!=null) {
            AfterSelectCodecustRegId = arguments?.getInt("AfterSelectCodecustRegId")
        }

        if (arguments?.getInt("afterEditEmailsavecustRegId")!=null) {
            afterEditEmailsavecustRegId = arguments?.getInt("afterEditEmailsavecustRegId")
        }

        if (arguments?.getInt("AfterSelectCodecustRegId")!=null) {
            AfterSelectCodecustRegId = arguments?.getInt("AfterSelectCodecustRegId")
        }

        if (arguments?.getString("AfterSelectCodeEmail")!=null) {
            AfterSelectCodeEmail = arguments?.getString("AfterSelectCodeEmail")
        }

        if (arguments?.getString("mobilecode")!=null) {
            mobilecode = arguments?.getString("mobilecode")
        }


        if (arguments?.getInt("savecustRegId")!=null) {
            savecustRegId = arguments?.getInt("savecustRegId")
        }

        if (arguments?.getString("saveEmail")!=null) {
            email = arguments?.getString("saveEmail")
        }

        if (arguments?.getBoolean("editemailchange")!=null) {
            editemailchange = arguments?.getBoolean("editemailchange")
        }



        if (arguments?.getInt("EditNumber")!=null && arguments?.getInt("EditNumber") !=0){

            data = arguments?.getInt("EditNumber")
            click=true
            binding.txtCountryCodeEditphonenumber.text = "+"+data.toString()
            binding.confirmPhoneNumber.setText(binding.txtCountryCodeEditphonenumber.text.toString())

        }

        else if (arguments?.getInt("EditNumber")==null /*|| arguments?.getInt("EditNumber") ==0*/){

            binding.txtCountryCodeEditphonenumber.text = "+1"
            /*  binding.confirmPhoneNumber.setText(binding.txtCountryCodeEditphonenumber.text.toString())
              binding.confirmPhoneNumberwithoutcode.setText(binding.txtCountryCodeEditphonenumber.text.toString())
  */

        }
        else{
            /*  binding.txtCountryCodeEditphonenumber.text = "+1"
              binding.confirmPhoneNumber.setText(binding.txtCountryCodeEditphonenumber.text.toString())*/

            binding.txtCountryCodeEditphonenumber.text = requireArguments().getString("mcode")
            binding.confirmPhoneNumber.setText(requireArguments().getString("mcode"))
            binding.confirmPhoneNumberwithoutcode.setText(requireArguments().getString("mphone"))

        }



        if (arguments?.getString("EditEmail")!=null){

            if (arguments?.getString("EditEmail").equals("editemail")){
                binding.txtChooseAccount.setText("Edit Email Address")
                binding.lytPhoneNumber.isVisible=false
                binding.edtEmailLay.isVisible=true
                binding.edtEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.confirmPhoneNumber.setText("")

            }
            else
            {

                binding.txtChooseAccount.setText("Edit Phone No.")
                binding.lytPhoneNumber.isVisible=true
                binding.edtEmailLay.isVisible=false

            }


        }else{

            binding.txtChooseAccount.setText("Edit Phone No.")
            binding.lytPhoneNumber.isVisible=true
            binding.edtEmailLay.isVisible=false

        }

/*
        binding.edtPhoneNumber.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->

                       // (activity as RegisterActivity).hideVerifyBottomSheetdialog()
                        //hideview(binding)
                //Do Something
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
*/


        binding.edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (click==true) {

                    //  binding.confirmPhoneNumber.setText( binding.txtCountryCodeEditphonenumber.text.toString()+" " /*data.toString()*/ + s.toString())
                    binding.confirmPhoneNumberwithoutcode.setText( s.toString())



                }
                else{

                    //  binding.confirmPhoneNumber.setText(  binding.txtCountryCodeEditphonenumber.text.toString() +" " + s.toString())
                    binding.confirmPhoneNumberwithoutcode.setText( s.toString())


                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        binding.clickCountryList.setOnClickListener {

            dismiss()

            click = true

            bottomSheetFragment = SelectCountryCodeFragment()
            val bundle = Bundle()
            bundle.putString("test", "EditFrag")
            bundle.putString("custRegId",custRegId!!)
            bundle.putString("email",email!!)
            bundle.putString("mcode",binding.confirmPhoneNumber.text.toString()!!)
            bundle.putString("mphone",binding.confirmPhoneNumberwithoutcode.text.toString()!!)
            bottomSheetFragment!!.setArguments(bundle)
            bottomSheetFragment!!.isCancelable = false
            bottomSheetFragment!!.show(requireFragmentManager(),bottomSheetFragment!!.tag)

            // showBottomSheetDialogFragment()
        }

        //    if  (isLogInButtonClicked) {

        /*  binding.confirmPhoneNumber.setText( sharedPreferences!!.getString("codeSharedPrefAfterLog","") )
          binding.confirmPhoneNumberwithoutcode.setText( sharedPreferences!!.getString("phoneSharedPrefAfterLog",""))
      }
      else{*/

        binding.confirmPhoneNumber.setText( binding.txtCountryCodeEditphonenumber.text.toString())
        binding.confirmPhoneNumberwithoutcode.setText(requireArguments().getString("mphone"))

        //   }


        binding.btnLogIn.setOnClickListener {

            if (binding.edtPhoneNumber.text.toString().equals("")){
                Toast.makeText(requireContext(), "Enter Phone to continue", Toast.LENGTH_LONG).show()


            }
            else if ( binding.edtPhoneNumber.text.toString().trim().length <9){

                Toast.makeText(requireActivity(), "Phone number should be minimum 9 digit", Toast.LENGTH_LONG).show()

            }
            else if (binding.edtPhoneNumber.text.toString().length>15){

                Toast.makeText(requireActivity(), "Phone number should be maximum 15 digit", Toast.LENGTH_LONG).show()

            }
            else{

                editAddess(binding)
            }


        }


        return binding.root
    }



    private fun editAddess(vBinding: FragmentEditNumberBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            req["type"] = "phone"
            req["phone_email"] = vBinding.edtPhoneNumber.text.toString()
            req["country_code"] =vBinding.txtCountryCodeEditphonenumber.text.toString()

            if ( ! requireArguments().getString("afterlogin").equals("afterloginPhone")) {

                req["id"] = defaultId!!
            }else{

                req["id"] = defaultIdLog!!
            }

            responseEditDetails = apiservice.doingEditDetails(req)
            responseEditDetails!!.enqueue(object : Callback<ResendOtpPojo?> {
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


                            editedmail=true



                            verifyCodeBottomsheet = VerifyCodeFragment()

                            storePhoneUpdate = vBinding.edtPhoneNumber.text.toString()
                            storePhoneCodeUpdate = vBinding.txtCountryCodeEditphonenumber.text.toString()

                            sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
                                AppCompatActivity.MODE_PRIVATE
                            )

                            myEdit = sharedPreferences!!.edit()

                            myEdit!!.putString("iphoneUpdateSharedPref", storePhoneUpdate!!)
                            myEdit!!.putString("iphoneCodeUpdateSharedPref", storePhoneCodeUpdate!!)
                            myEdit!!.apply()

                            val bundle = Bundle()
                            bundle.putString("afterEdit", "phone")
                            bundle.putString("afterEditphonewithoutchangecode", "afterEditphonewithoutchangecode")
                            bundle.putString("saveEmail", email)
                            bundle.putString("savecustRegId", arguments?.getString("custRegId")!!)
                            bundle.putBoolean("editemail", true)
                            bundle.putString("afterEditPhone",vBinding.edtPhoneNumber.text.toString() )
                            bundle.putString("afterEditPhoneCode",vBinding.txtCountryCodeEditphonenumber.text.toString() )
                            bundle.putString("AfterSelectCodeEmail",AfterSelectCodeEmail )
                            bundle.putInt("AfterSelectCodecustRegId",arguments?.getInt("AfterSelectCodecustRegId")!! )


                            verifyCodeBottomsheet!!.setArguments(bundle)
                            verifyCodeBottomsheet!!.isCancelable = false
                            verifyCodeBottomsheet!!.show(requireFragmentManager(), verifyCodeBottomsheet!!.tag)
                            /*
 */

                            dismiss()

                        }
                        else {

                            AppProgressBar.closeLoader()

                            if (response.body() != null) {

                                if (response.body()!!.message!!.equals(
                                        resources.getString(R.string.youhavebeenloggedinfromanotherdevice),
                                        ignoreCase = true)
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
                                        response.body()!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                            }
                        }


                    } else {

                        AppProgressBar.closeLoader()

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

    fun cancelCountryPopupEditNUmber(it: Int) {

        txt_country_code_editphonenumber.text = "+"+it.toString()
        confirmPhoneNumber.text = "+"+it.toString()
        // bottomSheetFragment?.dismiss()

    }

    fun cancelCountryPopup(countryCode: Int) {

        txt_country_code.text = "+"+countryCode.toString()
        bottomSheetFragment?.dismiss()
    }







}
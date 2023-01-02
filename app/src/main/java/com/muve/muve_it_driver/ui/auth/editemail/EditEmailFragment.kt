package com.muve.muve_it_driver.ui.auth.editemail

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.muve.muve_it_driver.NoConnectivityException
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.FragmentEditEmailBinding
import com.muve.muve_it_driver.model.resendotp.ResendOtpPojo
import com.muve.muve_it_driver.restapi.ApiClient
import com.muve.muve_it_driver.restapi.ApiInterface
import com.muve.muve_it_driver.ui.auth.verifycodeemail.VerifyEmailFragment
import com.muve.muve_it_driver.util.AppProgressBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class EditEmailFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: EditEmailViewModel
    private var defaultId: String?=""
    private var defaultIdLog: String?=""
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
    var responseEditDetails: Call<ResendOtpPojo>? = null
    private var myEdit: SharedPreferences.Editor?=null
    var sharedPreferences: SharedPreferences?=null
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vBinding: FragmentEditEmailBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_edit_email, container, false)
        viewModel = ViewModelProviders.of(this).get(EditEmailViewModel::class.java)
        vBinding.editemail = viewModel
        vBinding.lifecycleOwner = this

        vBinding.edtEmail.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)


        try {

            sharedPreferences = requireContext().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
            defaultId = sharedPreferences!!.getString("idSharedPref","")
            defaultIdLog = sharedPreferences!!.getString("idSharedPrefAfterLog","")
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

           /* if(defaultIdLog!=0){
                defaultId==0
            }*/
            if(!defaultIdLog.equals("")){
                defaultId==""
            }

        }

        catch ( e : Exception){
            e.printStackTrace()
        }

        vBinding.imgCloseCountry.setOnClickListener {

            dismiss()

            /* val bottomSheetFragmentV = VerifyEmailFragment()
             val bundle = Bundle()
             bottomSheetFragmentV.setArguments(bundle)
             bottomSheetFragmentV.isCancelable=false

             bottomSheetFragmentV.show(requireFragmentManager(),bottomSheetFragmentV!!.tag)*/
        }

        //   if  (isLogInButtonClicked) {

        /*    vBinding.confirmPhoneNumber.setText( sharedPreferences!!.getString("emailSharedPrefAfterLog",""))
        }
    else{
*/
        vBinding.confirmPhoneNumber.setText(requireArguments().getString("emailshow"))

        //  }


        vBinding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                vBinding.confirmPhoneNumber.setText(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }

            override fun afterTextChanged(s: Editable) {


            }
        })

        vBinding.btnLogIn.setOnClickListener {

            if (vBinding.edtEmail.text.toString().equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Enter email to continue",
                    Toast.LENGTH_LONG
                ).show()


            }
            else if (vBinding.edtEmail.text.toString().contains(" ")) {
                Toast.makeText(requireActivity(), "Enter valid Email id !", Toast.LENGTH_SHORT).show();
            }
            //  else if (vBinding.edtEmail.text.toString().isEmpty() || ! vBinding.edtEmail.text.toString().trim(). matches(Regex(emailPattern))) {
            else if (vBinding.edtEmail.text.toString().trim().isEmpty() || (Patterns.EMAIL_ADDRESS.matcher(vBinding.edtEmail.text.toString().trim()).matches()==false)) {
                Toast.makeText(requireActivity(), "Enter valid Email id !", Toast.LENGTH_SHORT).show();
            }
            else {

                editAddessEmail(vBinding)
            }
        }



        return vBinding.root

    }

    private fun editAddessEmail(vBinding: FragmentEditEmailBinding) {

        try {

            AppProgressBar.openLoader(
                requireActivity() as Activity,
                this.getResources().getString(R.string.pleasewait)
            )

            val apiservice: ApiInterface = ApiClient.getClient(requireActivity()).create(ApiInterface::class.java)
            val req = HashMap<String, Any>()

            req["type"] = "email"
            req["phone_email"] = vBinding.edtEmail.text.toString()
            //if (defaultId!=0 /* ! requireArguments().getString("afterlogin").equals("afterloginPhone")*/) {
            if (!defaultId.equals("") /* ! requireArguments().getString("afterlogin").equals("afterloginPhone")*/) {

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

                            dismiss()

                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()



                            val verifyCodeBottomsheet = VerifyEmailFragment()
                            val bundle = Bundle()
                            bundle.putString("afterEdit", "email")
                            bundle.putString("afterEditedmail", "email")
                            bundle.putBoolean("editedmail", true)
                            bundle.putString("afterEditEmail",vBinding.edtEmail.text.toString() )
                            bundle.putInt("afterEditEmailsavecustRegId",
                                arguments?.getInt("savecustRegId")!!
                            )
                            bundle.putInt("AfterSelectCodecustRegId",
                                arguments?.getInt("AfterSelectCodecustRegId")!!
                            )
                            bundle.putInt("savecustRegId__",
                                arguments?.getInt("savecustRegId_")!!
                            )

                            storeEmailUpdate=vBinding.edtEmail.text.toString()

                            sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",
                                AppCompatActivity.MODE_PRIVATE
                            )

                            myEdit = sharedPreferences!!.edit()

                            myEdit!!.putString("emailUpdateSharedPref", storeEmailUpdate!!)
                            myEdit!!.remove( sharedPreferences!!.getString("emailUpdateSharedPref","")!!).commit()
                            myEdit!!.apply()

                            verifyCodeBottomsheet.setArguments(bundle)
                            verifyCodeBottomsheet.isCancelable=false
                            verifyCodeBottomsheet?.show(requireFragmentManager(), verifyCodeBottomsheet!!.tag)


                        }else{

                            AppProgressBar.closeLoader();


                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()

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
}
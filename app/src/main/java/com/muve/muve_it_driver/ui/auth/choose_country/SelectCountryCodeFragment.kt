package com.muve.muve_it_driver.ui.auth.choose_country

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.CountriesAdapter
import com.muve.muve_it_driver.EditProfileUpdate
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.SelectCountryCodeFragmentBinding
import com.muve.muve_it_driver.model.countrymodel.DataItem
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.ui.auth.edit_number.EditNumberFragment
import com.muve.muve_it_driver.ui.auth.forgetpassword.ForgetPasswordFirstStep
import com.muve.muve_it_driver.ui.auth.login.LogInActivity
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.util.RecyclerViewItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList


class SelectCountryCodeFragment : BottomSheetDialogFragment(), RecyclerViewItemClickListener {

//

    var sharedPreferenceManager: SharedPreferenceManager? = null
    private lateinit var viewModel: SelectCountryCodeViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var data :String?=null
    var custRegId :String?=""
    var email :String?=""

    //  var arr_currency: ArrayList<CountryPojo> =  ArrayList()
    // var arr_currency: ArrayList<DataItem> = ArrayList<DataItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: SelectCountryCodeFragmentBinding =
            DataBindingUtil.inflate(inflater,R.layout.select_country_code_fragment,container,false)
        viewModel = ViewModelProvider(this).get(SelectCountryCodeViewModel::class.java)
        binding.selectcountry = viewModel
        sharedPreferenceManager = SharedPreferenceManager(this.requireActivity())
        binding.lifecycleOwner = this
        val behaviour = (dialog as BottomSheetDialog).behavior
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED

        try {
            if ( arguments?.getString("test")!=null) {
                data = arguments?.getString("test")
            }else{


            }

        }catch (e : Exception){
            e.printStackTrace()
        }



        val preferences = requireActivity().getSharedPreferences("COUNTRYLIST", Context.MODE_PRIVATE)
        val gson = Gson()
        val response: String = preferences.getString("COUNTRYLIST", "[]").toString()
        val msgList: ArrayList<DataItem> = gson.fromJson(
            response,
            object : TypeToken<List<DataItem?>?>() {}.getType()
        )


        val mSectionList: ArrayList<DataItem> = ArrayList()
        ///   getHeaderListLatter(msgList , mSectionList)
        getHeaderListLatter(sharedPreferenceManager!!.countryList as ArrayList<DataItem>, mSectionList)

        layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager
        binding.rcvCountry.layoutManager = layoutManager
        binding.rcvCountry.adapter = CountriesAdapter(mSectionList ,this@SelectCountryCodeFragment,this)

        if (arguments?.getString("custRegId")!=null ){

            custRegId =arguments?.getString("custRegId")
        }

        if (arguments?.getString("email")!=null ){

            email =arguments?.getString("email")
        }


        binding.crntLctnCode.setOnClickListener {

            if (data.equals("Register")) {

                (+1).let {
                    (activity as RegisterActivity).cancelCountryPopup(
                        it
                    )
                }


            }
            else  if (data.equals("Login")) {

                (+1).let {
                    (activity as LogInActivity).cancelCountryPopup(
                        it
                    )
                }


            }

            else  if (data.equals("ForgetPassword")) {

                (+1).let {
                    (activity as ForgetPasswordFirstStep).cancelCountryPopup(
                        it
                    )
                }


            }
            else  if (data.equals("EditProfile")) {

                (+1).let {
                    (activity as EditProfileUpdate).cancelCountryPopup(
                        it
                    )
                }


            }


            else{

                dismiss()

                val bottomSheetFragment = EditNumberFragment()
                val bundle = Bundle()
                bundle.putInt("EditNumber", +1)
                bundle.putString("mcode", requireArguments().getString("mcode"))
                bundle.putString("mphone", requireArguments().getString("mphone"))
                bottomSheetFragment.setArguments(bundle)
                bottomSheetFragment.isCancelable=false
                bottomSheetFragment.show(requireFragmentManager(),bottomSheetFragment!!.tag)

            }


        }


        binding.imgCloseCountry.setOnClickListener {

            dismiss()


        }

        return binding.root
    }



    private fun getHeaderListLatter(
        usersList: ArrayList<DataItem>,
        mSectionList: ArrayList<DataItem>
    ) {

        Collections.sort(
            usersList
        ) { user1, user2 ->
            java.lang.String.valueOf(user1.country!![0]).toUpperCase()
                .compareTo(java.lang.String.valueOf(user2.country!![0]).toUpperCase())
        }

        var lastHeader: String? = ""
        val size: Int = usersList.size
        for (i in 0 until size) {
            val user: DataItem = usersList[i]
            val header: String = java.lang.String.valueOf(user.country!![0]).toUpperCase()
            val code: Int = user.code!!
            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header
                mSectionList.add(DataItem(header,code, true))
            }
            mSectionList.add(user)
        }
    }

    override fun onItemClick(position: Int , countryList: ArrayList<DataItem>) {

        dismiss()

        if (data.equals("Register")) {
            countryList.get(position).code?.let {
                (activity as RegisterActivity).cancelCountryPopup(
                    it
                )
            }
        }
        else  if (data.equals("Login")) {
            countryList.get(position).code?.let {
                (activity as LogInActivity).cancelCountryPopup(
                    it
                )
            }
        }

        else  if (data.equals("ForgetPassword")) {
            countryList.get(position).code?.let {
                (activity as ForgetPasswordFirstStep).cancelCountryPopup(
                    it
                )
            }
        }

        else if (data.equals("EditFrag")){

            dismiss()

            val bottomSheetFragment = EditNumberFragment()
            val bundle = Bundle()
            bundle.putInt("EditNumber", countryList.get(position).code!!)
            if(custRegId!=null || custRegId!!.equals("")) {
                bundle.putString("AfterSelectCodecustRegId", custRegId!!)
            }
            bundle.putString("AfterSelectCodeEmail", email!!)
            bundle.putString("mcode", requireArguments().getString("mcode"))
            bundle.putString("mphone", requireArguments().getString("mphone"))
            bottomSheetFragment.setArguments(bundle)
            bottomSheetFragment.isCancelable=false

            bottomSheetFragment.show(requireFragmentManager(),bottomSheetFragment!!.tag)

        }

    }


}
package com.muve.muve_it_driver.ui.auth.prefered_city

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.CityAdapter
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.FragmentSelectPreferedCityBinding

import com.muve.muve_it_driver.model.aboutusmodel.cityDataItemAbout
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerPreferedCity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SelectPreferedCityFragment : BottomSheetDialogFragment(),
    RecyclerViewItemClickListenerPreferedCity {

    private lateinit var viewModel: SelectPreferedCityViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSelectPreferedCityBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_select_prefered_city,container,false)

        viewModel = ViewModelProvider(this).get(SelectPreferedCityViewModel::class.java)
        sharedPreferenceManager = SharedPreferenceManager(this.requireActivity())

        binding. selectprefercity= viewModel
        binding.lifecycleOwner = this
        val behaviour = (dialog as BottomSheetDialog).behavior
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED

        val preferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val gson = Gson()
        val response: String = preferences.getString("about_city", "[]").toString()
        val msgList: ArrayList<cityDataItemAbout> = gson.fromJson(
            response,
            object : TypeToken<List<cityDataItemAbout?>?>() {}.getType()
        )


        layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager
        binding.rcvCity.layoutManager = layoutManager
        binding.rcvCity.adapter = CityAdapter(msgList ,requireActivity(),this)
        binding.rcvCity.adapter = CityAdapter(sharedPreferenceManager!!.preferedCity as ArrayList<cityDataItemAbout> ,requireActivity(),this)

        binding.imgCloseCountry.setOnClickListener {

            dismiss()


        }

        return binding.root

    }

    override fun onItemClick(position: Int, countryList: ArrayList<cityDataItemAbout>) {

        countryList.get(position).city_name?.let { (activity as RegisterActivity).cancelPreferedCityPopup(it) }


    }


}
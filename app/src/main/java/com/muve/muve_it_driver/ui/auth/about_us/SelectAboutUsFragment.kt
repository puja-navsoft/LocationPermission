package com.muve.muve_it_driver.ui.auth.about_us

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muve.muve_it_driver.AboutUsAdapter
import com.muve.muve_it_driver.R
import com.muve.muve_it_driver.databinding.FragmentSelectAboutUsBinding
import com.muve.muve_it_driver.model.aboutusmodel.DataItemAbout
import com.muve.muve_it_driver.preferences.SharedPreferenceManager
import com.muve.muve_it_driver.ui.auth.register.RegisterActivity
import com.muve.muve_it_driver.util.RecyclerViewItemClickListenerAboutUS
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SelectAboutUsFragment : BottomSheetDialogFragment(), RecyclerViewItemClickListenerAboutUS {

    private lateinit var viewModel: SelectAboutUsViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var sharedPreferenceManager: SharedPreferenceManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_select_about_us, container, false)

        val binding: FragmentSelectAboutUsBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_select_about_us,container,false)
        viewModel = ViewModelProvider(this).get(SelectAboutUsViewModel::class.java)
        sharedPreferenceManager = SharedPreferenceManager(this.requireActivity())

        binding. selectaboutus= viewModel
        binding.lifecycleOwner = this
        val behaviour = (dialog as BottomSheetDialog).behavior
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED

        val preferences = requireActivity().getSharedPreferences("ABOUTLIST", Context.MODE_PRIVATE)
        val gson = Gson()
        val response: String = preferences.getString("ABOUTLIST", "[]").toString()
        val msgList: ArrayList<DataItemAbout> = gson.fromJson(
            response,
            object : TypeToken<List<DataItemAbout?>?>() {}.getType()
        )


        layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager
        binding.rcvAbout.layoutManager = layoutManager
       // binding.rcvAbout.adapter = AboutUsAdapter(msgList ,requireActivity(),this)
        binding.rcvAbout.adapter = AboutUsAdapter(sharedPreferenceManager!!.aboutList as ArrayList<DataItemAbout> ,requireActivity(),this)

        binding.imgCloseCountry.setOnClickListener {

            dismiss()


        }

        return binding.root

    }

    override fun onItemClick(position: Int, countryList: ArrayList<DataItemAbout>) {
        countryList.get(position).service_name?.let { (activity as RegisterActivity).cancelAboutusPopup(it) }

    }

}
package com.muve.muve_it_driver.model.aboutusmodel

import com.google.gson.annotations.SerializedName

data class AboutUsPojo(

	@field:SerializedName("detail")
	val data: MutableList<DataItemAbout>,

	@field:SerializedName("city")
	val city: MutableList<cityDataItemAbout>,


	@field:SerializedName("message")
	val details: String? = null,

	@field:SerializedName("customer_waiting_time")
	val customer_waiting_time: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItemAbout(

	@field:SerializedName("service_name")
	val service_name: String? = null,

)


data class cityDataItemAbout(

	@field:SerializedName("city_name")
	val city_name: String? = null,

	)

package com.muve.muve_it_driver.model.servicecall

import com.google.gson.annotations.SerializedName

data class FirstServiceCallResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("city")
	val city: List<CityItem?>? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItem(

	@field:SerializedName("service_name")
	val serviceName: String? = null
)

data class CityItem(

	@field:SerializedName("city_name")
	val cityName: String? = null
)


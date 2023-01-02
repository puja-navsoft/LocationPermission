package com.muve.muve_it_driver.model.countrymodel

import com.google.gson.annotations.SerializedName

data class CountryPojo(

	@field:SerializedName("detail")
	val data: MutableList<DataItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("country_code")
	val code: Int? = null,

	val isSection: Boolean?= false
)

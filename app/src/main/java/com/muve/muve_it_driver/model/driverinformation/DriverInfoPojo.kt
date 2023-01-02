package com.muve.muve_it_driver.model.driverinformation

import com.google.gson.annotations.SerializedName

data class DriverInfoPojo(


	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("detail")
	val detail: Detail? = null


	)
data class Detail(
	val any: Any? = null
)

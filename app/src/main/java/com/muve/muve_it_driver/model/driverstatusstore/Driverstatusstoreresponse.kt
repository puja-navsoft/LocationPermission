package com.muve.muve_it_driver

import com.google.gson.annotations.SerializedName

data class Driverstatusstoreresponse(

	@field:SerializedName("driver_id")
	val driverId: String? = null,

	@field:SerializedName("detail")
	val detail: Details? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("driver_status")
	val driverStatus: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Details(
	val any: Any? = null
)

package com.muve.muve_it_driver.model.servicecallAcceptByDriver

import com.google.gson.annotations.SerializedName

data class ServiceCallAcceptByDriverResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

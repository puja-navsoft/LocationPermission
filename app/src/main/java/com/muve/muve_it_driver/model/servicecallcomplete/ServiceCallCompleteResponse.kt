package com.muve.muve_it_driver.model.servicecallcomplete

import com.google.gson.annotations.SerializedName

data class ServiceCallCompleteResponse(

	@field:SerializedName("detail")
	val detail: Detail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Detail(

	@field:SerializedName("price")
	val price: String? = "0.0"
)

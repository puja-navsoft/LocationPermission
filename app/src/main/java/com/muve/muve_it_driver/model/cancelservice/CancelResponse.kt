package com.muve.muve_it

import com.google.gson.annotations.SerializedName

data class CancelResponse(

	@field:SerializedName("detail")
	val detail: Details? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Details(

	@field:SerializedName("cancel_charges")
	val cancelCharges: String? = null
)

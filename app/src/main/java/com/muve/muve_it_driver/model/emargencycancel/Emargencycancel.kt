package com.muve.muve_it

import com.google.gson.annotations.SerializedName

data class Emargencycancel(

	@field:SerializedName("detail")
	val detail: Detailsx? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Detailsx(

	@field:SerializedName("service_id")
	val newservice_id: String? = null
)

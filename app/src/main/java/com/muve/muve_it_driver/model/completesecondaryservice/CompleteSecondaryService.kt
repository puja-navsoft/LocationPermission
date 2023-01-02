package com.muve.muve_it

import com.google.gson.annotations.SerializedName

data class CompleteSecondaryService(

	@field:SerializedName("detail")
	val detail: Details1? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Details1(

	@field:SerializedName("price")
	val price: String? = null
)

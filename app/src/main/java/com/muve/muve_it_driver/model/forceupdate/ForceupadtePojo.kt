package com.muve.muve_it_driver.model.forceupdate

import com.google.gson.annotations.SerializedName

data class ForceupdatePojo(

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null

)

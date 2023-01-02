package com.muve.muve_it_driver

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse(

	@field:SerializedName("detail")
	val detail: Detail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Detail(

	@field:SerializedName("images")
	val images: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

package com.muve.muve_it_driver.model.editprofile

import com.google.gson.annotations.SerializedName

data class EditProfilePojo(

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val detail: String? = null,

	@field:SerializedName("detail")
	val customerImage: Detail? = null,


	)

data class Detail(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("driver_id")
	val driver_id: String? = null,

	@field:SerializedName("driver_image")
	val driver_image: String? = null,

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null ,

	@field:SerializedName("email")
	val email: String? = null ,

	@field:SerializedName("phone_number")
	val phone_number: String? = null ,

	@field:SerializedName("country_code")
	val country_code: String? = null ,

	@field:SerializedName("device_fcm_token")
	val device_fcm_token: String? = null ,

	@field:SerializedName("mobile_verify_status")
	val mobile_verify_status: Boolean? = null ,

	@field:SerializedName("email_verify_status")
	val email_verify_status: Boolean? = null

)


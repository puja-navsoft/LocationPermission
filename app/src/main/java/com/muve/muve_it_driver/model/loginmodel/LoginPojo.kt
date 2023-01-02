package com.muve.muve_it_driver.model.loginmodel
import com.google.gson.annotations.SerializedName

data class LoginPojo(

	@field:SerializedName("detail")
	val detail: Detail? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Detail(

	@field:SerializedName("driver_id")
	val driverId: String? = null,

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("mobile_verify_status")
	val mobileVerifyStatus: Boolean? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("count")
	val count: String? = null,

	@field:SerializedName("availability")
	val availability: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("hear_about_Us")
	val hearAboutUs: String? = null,

	@field:SerializedName("fixed_pin")
	val fixedPin: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("preferred_city")
	val preferredCity: String? = null,

	@field:SerializedName("alternate_city")
	val alternateCity: String? = null,

	@field:SerializedName("email_verify_status")
	val emailVerifyStatus: Boolean? = null,

	@field:SerializedName("driver_image")
	val driverImage: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("vehicle_type")
	val vehicle_type: String? = null,


	@field:SerializedName("account_status")
	val account_status: String? = null,

	@field:SerializedName("is_driverinformation_status")
	val is_driverinformation_status: Boolean? = null,
	@field:SerializedName("is_driver_vehicle_information_status")
	val is_driver_vehicle_information_status: Boolean? = null ,

	@field:SerializedName("is_bank_status")
	val is_bank_status: Boolean? = null,

	@field:SerializedName("attempt_status")
	val attempt_status: Boolean? = null ,


	@field:SerializedName("is_training")
	val is_training: String? = null ,

	@field:SerializedName("document_verify_status")
	val document_verify_status: Boolean? = null



	)


package com.muve.muve_it_driver.UserDetailPojo
import com.google.gson.annotations.SerializedName


data class FavouritePlacesItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("is_deleted")
	val isDeleted: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("customer")
	val customer: Int? = null
)


data class UserDetailPojo(
	@field:SerializedName("favourite_places")
	val favouritePlaces: MutableList<FavouritePlacesItem>,

	@field:SerializedName("detail")
	val detail: Detail? = null,

	@field:SerializedName("status")
	val status: Boolean? = null ,

	@field:SerializedName("message")
	val message: String? = null
)

data class Detail(

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("mobile_verify_status")
	val mobileVerifyStatus: Boolean? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("count")
	val count: String? = null,

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

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email_verify_status")
	val emailVerifyStatus: Boolean? = null,

	@field:SerializedName("promotional_code")
	val promotionalCode: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("driver_image")
	val driver_image: String? = null ,

	@field:SerializedName("service_id")
	val service_id: String? = null ,

	@field:SerializedName("attempt_status")
	val attempt_status: Boolean? = null ,


	@field:SerializedName("is_training")
	val is_training: String? = null ,

	@field:SerializedName("document_verify_status")
	val document_verify_status: Boolean? = null ,

	@field:SerializedName("account_status")
	val account_status: String? = null
)


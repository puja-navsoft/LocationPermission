package com.muve.muve_it_driver.model.addaddress

import com.google.gson.annotations.SerializedName

data class AddaddressResponse(

	@field:SerializedName("favourite_places")
	val favouritePlaces: List<FavouritePlacesItem?>? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("particular_fav")
	val particularFav: ParticularFav? = null,

)

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

data class ParticularFav(

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


package com.muve.muve_it_driver

import com.google.gson.annotations.SerializedName

data class DriverInformationResponse(

	@field:SerializedName("driver_information")
	val driverInformation: DriverInformation? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("driver_image")
	val driverImage: DriverImage? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DriverInformation(

	@field:SerializedName("driver_information_id")
	val driverInformationId: Int? = null,

	@field:SerializedName("driver_licence_front")
	val driverLicenceFront: String? = null,

	@field:SerializedName("driver_licence_back")
	val driverLicenceBack: String? = null
)

data class DriverImage(

	@field:SerializedName("driver_id")
	val driverId: Int? = null,

	@field:SerializedName("driver_image")
	val driverImage: String? = null
)

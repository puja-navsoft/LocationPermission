package com.muve.muve_it_driver.model.servicecalllisting

import com.google.gson.annotations.SerializedName

data class ServiceCallListingResponse(

	@field:SerializedName("detail")
	val detail: ArrayList<DetailItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DetailItem(

	@field:SerializedName("unattend_drop")
	val unattendDrop: Boolean? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("cancelation_reason")
	val cancelationReason: String? = null,

	@field:SerializedName("creditcard_id")
	val creditcardId: String? = null,

	@field:SerializedName("pick_long")
	val pickLong: String? = null,

	@field:SerializedName("driver_id")
	val driverId: String? = null,

	@field:SerializedName("other")
	val other: Boolean? = null,

	@field:SerializedName("upload_pic")
	val uploadPic: String? = null,

	@field:SerializedName("paid_on")
	val paidOn: String? = null,

	@field:SerializedName("pickup_adrs_instruction")
	val pickupAdrsInstruction: String? = null,

	@field:SerializedName("drop_place")
	val dropPlace: String? = null,

	@field:SerializedName("pick_place")
	val pickPlace: String? = null,

	@field:SerializedName("complete_time")
	val completeTime: String? = null,

	@field:SerializedName("drop_image")
	val dropImage: String? = null,

	@field:SerializedName("service_id")
	val serviceId: String? = null,

	@field:SerializedName("arrived_destination_time")
	val arrivedDestinationTime: String? = null,

	@field:SerializedName("total_distance")
	val totalDistance: String? = null,

	@field:SerializedName("dropof_adrs_instruction")
	val dropofAdrsInstruction: String? = null,

	@field:SerializedName("verify_code_time")
	val verifyCodeTime: String? = null,

	@field:SerializedName("dimension")
	val dimension: String? = null,

	@field:SerializedName("no_help_toload")
	val noHelpToload: Boolean? = null,

	@field:SerializedName("cancel_time")
	val cancelTime: String? = null,

	@field:SerializedName("vehicle_image2")
	val vehicleImage2: String? = null,

	@field:SerializedName("vehicle_image1")
	val vehicleImage1: String? = null,

	@field:SerializedName("total_fare")
	val totalFare: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("vehicle_type")
	val vehicleType: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("no_of_items")
	val noOfItems: String? = null,

	@field:SerializedName("drop_long")
	val dropLong: String? = null,

	@field:SerializedName("pick_lat")
	val pickLat: String? = null,

	@field:SerializedName("drop_lat")
	val dropLat: String? = null,

	@field:SerializedName("restricted_image2")
	val restrictedImage2: String? = null,

	@field:SerializedName("restricted_image1")
	val restrictedImage1: String? = null,

	@field:SerializedName("load_unload")
	val loadUnload: Boolean? = null,

	@field:SerializedName("customer_behaviour")
	val customerBehaviour: Boolean? = null,

	@field:SerializedName("service_status")
	val serviceStatus: Boolean? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("start_driving_time")
	val startDrivingTime: String? = null,

	@field:SerializedName("arrived_time")
	val arrivedTime: String? = null,

	@field:SerializedName("customer_no_longer")
	val customerNoLonger: Boolean? = null,

	@field:SerializedName("status")
	val status: String? = null ,

	@field:SerializedName("second_driver_id")
	val second_driver_id: String? = null ,

	@field:SerializedName("driver_charge")
	val driver_charge: String? = null ,

	@field:SerializedName("customer_images")
	val customer_images: String? = null
)

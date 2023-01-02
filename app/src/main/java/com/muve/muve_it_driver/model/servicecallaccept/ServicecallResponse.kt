package com.muve.muve_it_driver.model.servicecallaccept
import com.google.gson.annotations.SerializedName


data class ServicecallResponse(

	@field:SerializedName("detail")
	val detail: Detail? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Detail(

	@field:SerializedName("unattend_drop")
	val unattendDrop: Boolean? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("cancelation_reason")
	val cancelationReason: Any? = null,

	@field:SerializedName("creditcard_id")
	val creditcardId: String? = null,

	@field:SerializedName("pick_long")
	val pickLong: String? = null,

	@field:SerializedName("driver_id")
	val driverId: String? = null,

	@field:SerializedName("upload_pic")
	val uploadPic: String? = null,

	@field:SerializedName("paid_on")
	val paidOn: Any? = null,

	@field:SerializedName("pickup_adrs_instruction")
	val pickupAdrsInstruction: String? = null,

	@field:SerializedName("drop_place")
	val dropPlace: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("pick_place")
	val pickPlace: String? = null,

	@field:SerializedName("service_id")
	val serviceId: String? = null,

	@field:SerializedName("total_distance")
	val totalDistance: String? = null,

	@field:SerializedName("dropof_adrs_instruction")
	val dropofAdrsInstruction: Any? = null,

	@field:SerializedName("dimension")
	val dimension: String? = null,

	@field:SerializedName("total_fare")
	val totalFare: String? = null,

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

	@field:SerializedName("load_unload")
	val loadUnload: Boolean? = null,

	@field:SerializedName("service_status")
	val serviceStatus: Boolean? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("customer_id")
	val customerId: String? = null,

	@field:SerializedName("status")
	val status: String? = null ,

	@field:SerializedName("service_type")
	val service_type: String? = null
)


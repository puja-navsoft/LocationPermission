package com.muve.muve_it_driver.model.notificationlist


import com.google.gson.annotations.SerializedName

data class NotificationListResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("status")
    val status: Boolean
)

data class Result(
    @SerializedName("driver_id")
    val customerId: String,
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_deleted")
    val isDeleted: Boolean,
    @SerializedName("message")
    val message: String
)
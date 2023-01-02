package com.muve.muve_it_driver.model.earninglist
import com.google.gson.annotations.SerializedName

data class EarningListResponse(
    @SerializedName("detail")
    val detail: List<Detail>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("today_earning")
    val todayEarning: Double,
    @SerializedName("today_servicecall")
    val todayServicecall: Double
)

data class Detail(
    @SerializedName("charge")
    val charge: Double,
    @SerializedName("date")
    val date: String
)
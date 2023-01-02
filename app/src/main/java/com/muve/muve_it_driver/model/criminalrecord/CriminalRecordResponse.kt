package com.muve.muve_it_driver.model.criminalrecord


import com.google.gson.annotations.SerializedName

data class CriminalRecordResponse(
    @SerializedName("detail")
    val detail: List<Detail>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class Detail(
    @SerializedName("account_holder_name")
    val accountHolderName: String,
    @SerializedName("account_holder_type")
    val accountHolderType: String,
    @SerializedName("account_type")
    val accountType: Any,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("customer")
    val customer: String,
    @SerializedName("fingerprint")
    val fingerprint: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("last4")
    val last4: String,
   /* @SerializedName("metadata")
    val metadata: Metadata,*/
    @SerializedName("object")
    val objectX: String,
    @SerializedName("routing_number")
    val routingNumber: String,
    @SerializedName("status")
    val status: String
)
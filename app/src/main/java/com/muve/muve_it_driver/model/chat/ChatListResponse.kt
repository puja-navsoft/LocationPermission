package com.muve.muve_it_driver.model.chat


import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    @SerializedName("detail")
    val `data`: List<IndividualChatResponse>?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class IndividualChatResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("receiver_id")
    val receiverId: String,
    @SerializedName("sender_id")
    val senderId: String,
    @SerializedName("service_id")
    val serviceId: String,
    @SerializedName("time_stamp")
    val timeStamp: String
)
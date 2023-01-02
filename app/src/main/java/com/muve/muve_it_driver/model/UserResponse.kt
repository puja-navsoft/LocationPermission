package com.muve.muve_it_driver.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
@field:SerializedName("c_code")
var cCode: String? = null,
@field:SerializedName("mobile_number")
var mobileNumber: String? = null,
@field:SerializedName("is_remember")
var isRemember: Boolean? = null ,

@field:SerializedName("password")
var password: String? = null
)
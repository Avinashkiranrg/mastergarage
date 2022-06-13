package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class DriverDrop(
    val dob: String,
    val driverId: Int,
    @SerializedName("drop_up_date")
    val dropUpDate: String,
    @SerializedName("drop_up_time")
    val dropUpTime: String,
    @SerializedName("first_name")
    val firstName: String,
    val imageUri: String,
    @SerializedName("last_name")
    val lastName: String,
    val leadId: Int,
    @SerializedName("mobile_no")
    val mobileNo: String,
    val outletId: Int,
    val status: String
)
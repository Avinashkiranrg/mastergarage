package com.vendor.mastergarage.model

import com.google.gson.annotations.SerializedName

data class VehicleOwner(
    val address: String?,
    val email: String?,
    val imageUri: Any?,
    val name: String?,
    val ownerId: Int?,
    val pin_code: Int?,
    @SerializedName("phone_no")
    val phoneNo: String?,
    val city: String?
)
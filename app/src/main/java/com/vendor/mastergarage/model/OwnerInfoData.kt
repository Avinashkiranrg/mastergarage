package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class OwnerInfoData(
    @SerializedName("alternate_phone")
    val alternatePhone: String?,
    val dob: String?,
    val email: String?,
    @SerializedName("first_name")
    val firstName: String?,
    val gender: String?,
    val imageUri: String?,
    @SerializedName("last_name")
    val lastName: String?,
    val ownerId: Int?,
    @SerializedName("phone_no")
    val phoneNo: String?
)
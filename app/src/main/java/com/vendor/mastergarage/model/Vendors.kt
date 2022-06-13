package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class Vendors(
    @SerializedName("alternate_phone")
    val alternatePhone: String?,
    val city: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    val email: String?,
    val name: String?,
    val phone: String?,
    val status: String?,
    val username: String?,
    val vendorId: Int?
)
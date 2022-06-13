package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class OutletsItem(
    val address: String?,
    @SerializedName("alternate_phone")
    val alternatePhone: String?,
    val city: String?,
    var isOperating: String?,
    val email: String?,
    val name: String?,
    val outletId: Int?,
    val gst_number: String?,
    val phone: String?,
    val status: String?,
    val vendorId: Int?,
    val system_id: String?,
    val imageUri: String?
)
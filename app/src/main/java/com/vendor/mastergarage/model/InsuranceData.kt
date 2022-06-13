package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class InsuranceData(
    val company: String?,
    val insuranceId: Int?,
    @SerializedName("insurance_number")
    val insuranceNumber: String?,
    @SerializedName("Insurance_type")
    val insuranceType: String?,
    val status: String?,
    val vehicleId: Int?
)
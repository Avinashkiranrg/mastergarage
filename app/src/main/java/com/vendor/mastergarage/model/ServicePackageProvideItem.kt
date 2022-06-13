package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class ServicePackageProvideItem(
    val costs: Int?,
    @SerializedName("months_or_kms")
    val monthsOrKms: String?,
    val packageId: Int?,
    @SerializedName("package_name")
    val packageName: String?,
    @SerializedName("package_notation")
    val packageNotation: String?,
    @SerializedName("package_type")
    val packageType: String?,
    val points: String?,
    val status: String?,
    val vpId: Int?
)
package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceRequestedItem(
    @SerializedName("months_or_kms")
    val monthsOrKms: String?,
    val packageId: Int?,
    @SerializedName("package_name")
    val packageName: String?,
    @SerializedName("package_notation")
    val packageNotation: String?,
    @SerializedName("package_type")
    val packageType: String?,
    val points: Int?,
    val costs: Int?,
    val requestedId: Int?,
    val status: String?,
    val leadId: Int?
) : Parcelable
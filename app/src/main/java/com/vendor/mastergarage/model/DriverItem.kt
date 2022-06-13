package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DriverItem(
    val driverId: Int?,
    val dob: String?,
    @SerializedName("first_name")
    val firstName: String?,
    val designation: String?,
    val imageUri: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("mobile_no")
    val mobileNo: String?,
    val outletId: Int?,
    val status: String?
) : Parcelable




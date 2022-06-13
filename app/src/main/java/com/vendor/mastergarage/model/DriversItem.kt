package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DriversItem(
    val dob: String?,
    val driverId: Int?,
    @SerializedName("first_name")
    val firstName: String?,
    val imageUri: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("licence_no")
    val licenceNo: String?,
    val licenceUri: String?,
    @SerializedName("mobile_no")
    val mobileNo: String?,
    val outletId: Int?,
    val pick_up_date: String?,
    val pick_up_time: String?,
    val status: String?
) : Parcelable
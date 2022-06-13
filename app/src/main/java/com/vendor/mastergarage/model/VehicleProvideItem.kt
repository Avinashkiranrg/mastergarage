package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleProvideItem(
    val imageUri: String?,
    val name: String?,
    val status: String?,
    val vcId: Int?,
    val vehicleId: Int?
) : Parcelable
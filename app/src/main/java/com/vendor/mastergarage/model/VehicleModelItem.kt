package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleModelItem(
    val imageUri: String?,
    val modelId: Int?,
    val vmId: Int?,
    val name: String?,
    val status: String?,
    val variant: List<VehicleVariantsItem>?,
    val vehicleId: Int?
) : Parcelable

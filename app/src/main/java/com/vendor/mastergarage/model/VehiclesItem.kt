package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class VehiclesItem(
    val imageUri: String?,
    val model: List<VehicleModel>?,
    val name: String?,
    val status: String?,
    val vehicleId: Int?
) : Parcelable
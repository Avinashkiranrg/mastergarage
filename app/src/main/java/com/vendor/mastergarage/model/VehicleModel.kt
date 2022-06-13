package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//class VehicleModel : ArrayList<VehicleModelItem>()
@Parcelize
data class VehicleModel(
    val message: String?,
    val result: List<VehicleModelItem>?,
    val success: Int?
) : Parcelable
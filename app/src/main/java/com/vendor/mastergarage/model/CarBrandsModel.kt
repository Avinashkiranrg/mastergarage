package com.vendor.mastergarage.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarBrandsModel(
    val fuelType: String,
    val brandId: Int?,
    val model_name: String?,
    val imageUri: String?,
) : Parcelable
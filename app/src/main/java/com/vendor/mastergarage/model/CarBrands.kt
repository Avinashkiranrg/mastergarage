package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarBrands(
    val brandId: Int?,
    val brand_name: String?,
    val imageUri: String?,
) : Parcelable
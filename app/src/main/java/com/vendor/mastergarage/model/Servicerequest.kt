package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Servicerequest(
    val costs: Int?,
    val imageUri: String?,
    val months_or_kms: String?,
    val packageId: Int?,
    val package_name: String?,
    val package_notation: String?,
    val package_type: String?,
    val points: String?,
    val serviceId: Int?,
    val status: String?
) : Parcelable
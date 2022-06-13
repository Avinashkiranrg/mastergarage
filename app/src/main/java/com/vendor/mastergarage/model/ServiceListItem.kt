package com.vendor.mastergarage.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceListItem(
    val imageUri: String?,
    var vsId: Int?,
    val name: String?,
    val serviceId: Int?,
    val status: String?
) : Parcelable
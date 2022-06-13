package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    val imageId: Int?,
    val imageUri: String?,
    val status: String?,
    val leadId: Int?,
    val position: String?
) : Parcelable
package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleVariantsItem(
    val imageUri: String?,
    val variantId: Int?,
    val name: String?,
    var status: String?,
    val fuel: String?,
    val capacity: String?,
    val modelId: Int?,
    val vvId: Int?
) : Parcelable


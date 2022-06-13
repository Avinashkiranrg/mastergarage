package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BodyDamagesItem(
    val bdvId: Int?,
    val damagedId: Int?,
    val leadId: Int?,
    val name: String?,
    val status: String?
) : Parcelable
package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryChecklistItem(
    val chvId: Int?,
    val inventoryId: Int?,
    val no_of_inventory: Int?,
    val leadId: Int?,
    val name: String?,
    val status: String?
) : Parcelable
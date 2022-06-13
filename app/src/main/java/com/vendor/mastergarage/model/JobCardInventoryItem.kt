package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class JobCardInventoryItem(
    val chvId: Int,
    val inventoryId: Int,
    val name: String,
    @SerializedName("no_of_inventory")
    val noOfInventory: Int,
    val status: String
)
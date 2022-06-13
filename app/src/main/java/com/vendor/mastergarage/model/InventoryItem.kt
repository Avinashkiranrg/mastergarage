package com.vendor.mastergarage.model


data class InventoryItem(
    val inventoryId: Int?,
    val name: String?,
    val status: String?,
    var counter: Int = 1,
    var boolean: Boolean = false,
)
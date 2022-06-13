package com.vendor.mastergarage.model


//class OnDelivered : ArrayList<OnDeliveredItem>()
data class OnDelivered(
    val message: String?,
    val result: List<OnDeliveredItem>?,
    val success: Int?
)

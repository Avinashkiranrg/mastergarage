package com.vendor.mastergarage.model


//class OnGoingData : ArrayList<OnGoingDataItem>()
data class OnGoingData(
    val message: String?,
    val result: List<OnGoingDataItem>?,
    val success: Int?
)

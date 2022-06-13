package com.vendor.mastergarage.model


//class VehicleProvide : ArrayList<VehicleProvideItem>()

data class VehicleProvide(
    val message: String?,
    val result: List<VehicleProvideItem>?,
    val success: Int?
)
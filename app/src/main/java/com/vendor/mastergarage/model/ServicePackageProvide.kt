package com.vendor.mastergarage.model


//class ServicePackageProvide : ArrayList<ServicePackageProvideItem>()
data class ServicePackageProvide(
    val success: Int?,
    val message: String?,
    val result: List<ServicePackageProvideItem>?
)
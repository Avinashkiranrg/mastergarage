package com.vendor.mastergarage.model


data class ServicePackage(
    val success: Int?,
    val message: String?,
    val result: List<ServicePackageItem>?
)
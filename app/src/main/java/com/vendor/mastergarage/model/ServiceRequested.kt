package com.vendor.mastergarage.model



data class ServiceRequested(
    val success: Int?,
    val message: String?,
    val result: List<ServiceRequestedItem>?
)
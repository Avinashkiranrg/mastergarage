package com.vendor.mastergarage.model


//class ServiceList : ArrayList<ServiceListItem>()
data class ServiceList(
    val success: Int?,
    val message: String?,
    val result: List<ServiceListItem>?
)
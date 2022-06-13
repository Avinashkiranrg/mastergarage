package com.vendor.mastergarage.model


//class ServiceAdvisor : ArrayList<ServiceAdvisorItem>()
data class ServiceAdvisor(
    val success: Int?,
    val message: String?,
    val result: List<ServiceAdvisorItem>?
)
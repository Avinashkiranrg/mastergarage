package com.vendor.mastergarage.model


//class Leads : ArrayList<LeadsItem>()
data class Leads(
    val message: String?,
    val result: List<LeadsItem>?,
    val success: Int?
)

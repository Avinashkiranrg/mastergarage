package com.vendor.mastergarage.model

data class BookingIdsData(
    val bookingId: String?,
    val leadId: Int?,
    val ownerId: Int?,
    val payment_status: String?,
    val pick_up_date: String?,
    val pick_up_time: String?,
    val status: String?
)
package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class OnGoingItem(
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("booking_time")
    val bookingTime: String,
    @SerializedName("last_up_date")
    val lastUpDate: String,
    @SerializedName("last_up_time")
    val lastUpTime: String,
    val leadId: Int,
    val outletId: Int,
    @SerializedName("pickup_date")
    val pickupDate: String,
    @SerializedName("pickup_time")
    val pickupTime: String,
    val vehicleId: Int
)
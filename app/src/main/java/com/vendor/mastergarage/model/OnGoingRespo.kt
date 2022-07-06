package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class OnGoingRespo(
    val message: String?=null,
    val result: List<ResultOnGoing>,
    val success: Int?=null
)
@Parcelize
data class ResultOnGoing(
    val address: String?=null,
    val addressId: Int?=null,
    val appointment_date: String?=null,
    val appointment_time: String?=null,
    val backup_timer: Int?=null,
    val booking_date: String?=null,
    val booking_time: String?=null,
    val city: String?=null,
    val city_owner: String?=null,
    val classic_no: String?=null,
    val color: String?=null,
    val company: String?=null,
    val email: String?=null,
    val engine_no: String?=null,
    val fuelType: String?=null,
    val imageUri: String?=null,
    val insuranceId: Int?=null,
    val insurance_number: String?=null,
    val insurance_type: String?=null,
    val leadId: Int?=null,
    val manufacturer_name: String?=null,
    val outletId: Int?=null,
    val ownerId: Int?=null,
    val owner_name: String?=null,
    val phone_no: String?=null,
    val pin_code: Int?=null,
    val registration_no: String?=null,
    val status: String?=null,
    val v_imageUri: String?=null,
    val variants: String?=null,
    val vehicleId: Int?=null,
    val year_of_purchase: String?=null,
    val bookingId: String?=null,
    val totalCost: String?=null
):Parcelable
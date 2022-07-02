package com.vendor.mastergarage.model

data class OnGoingRespo(
    val message: String,
    val result: List<ResultOnGoing>,
    val success: Int
)
data class ResultOnGoing(
    val address: String,
    val addressId: Int,
    val appointment_date: String,
    val appointment_time: String,
    val backup_timer: Int,
    val booking_date: String,
    val booking_time: String,
    val city: String,
    val city_owner: String,
    val classic_no: String,
    val color: String,
    val company: Any,
    val email: String,
    val engine_no: String,
    val fuelType: String,
    val imageUri: Any,
    val insuranceId: Int,
    val insurance_number: Any,
    val insurance_type: Any,
    val leadId: Int,
    val manufacturer_name: String,
    val outletId: Int,
    val ownerId: Int,
    val owner_name: String,
    val phone_no: String,
    val pin_code: Int,
    val registration_no: Any,
    val status: String,
    val v_imageUri: String,
    val variants: String,
    val vehicleId: Int,
    val year_of_purchase: String
)
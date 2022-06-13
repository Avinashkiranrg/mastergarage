package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LeadsItem(
    @SerializedName("appointment_date")
    val appointmentDate: String?,
    val status: String?,
    @SerializedName("appointment_time")
    val appointmentTime: String?,
    @SerializedName("booking_date")
    val bookingDate: String?,
    @SerializedName("booking_time")
    val bookingTime: String?,
    @SerializedName("backup_timer")
    val backupTimer: Int?,
    val city: String?,
    @SerializedName("classic_no")
    val classicNo: String?,
    val color: String?,
    @SerializedName("engine_no")
    val engineNo: String?,
    val fuelType: String?,
    val leadId: Int?,
    @SerializedName("manufacturer_name")
    val manufacturerName: String?,
    val model: String?,
    val variants: String?,
    val outletId: Int?,
    @SerializedName("registration_no")
    val registrationNo: String?,
    val vehicleId: Int?,
    val addressId: Int?,
    @SerializedName("year_of_purchase")
    val yearOfPurchase: String?,
    val v_imageUri: String?,
    val ownerId: Int?,
    val owner_name: String?,
    val imageUri: String?,
    val address: String?,
    val pin_code: Int?,
    val phone_no: String?,
    val email: String?,
    val city_owner: String?,
    val insuranceId: Int?,
    val company: String?,
    val insurance_number: String?,
    val insurance_type: String?,
    val servicerequest: List<Servicerequest>?,
    val paymentInfo: Payment?,

    ) : Parcelable
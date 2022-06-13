package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnGoingDataItem(
    val address: String?,
    @SerializedName("booking_date")
    val bookingDate: String?,
    @SerializedName("booking_time")
    val bookingTime: String?,
    val city: String?,
    @SerializedName("city_owner")
    val cityOwner: String?,
    @SerializedName("classic_no")
    val classicNo: String?,
    val color: String?,
    val email: String?,
    @SerializedName("engine_no")
    val engineNo: String?,
    val fuelType: String?,
    val variants: String?,
    val imageUri: String?,
    @SerializedName("last_up_date")
    val lastUpDate: String?,
    @SerializedName("last_up_time")
    val lastUpTime: String?,
    val leadId: Int?,
    @SerializedName("manufacturer_name")
    val manufacturerName: String?,
    val model: String?,
    val ongoingId: Int?,
    val outletId: Int?,
    val ownerId: Int?,
    @SerializedName("owner_name")
    val ownerName: String?,
    @SerializedName("phone_no")
    val phoneNo: String?,
    @SerializedName("pickup_date")
    val pickupDate: String?,
    @SerializedName("pickup_time")
    val pickupTime: String?,
    @SerializedName("pin_code")
    val pinCode: Int?,
    @SerializedName("registration_no")
    val registrationNo: String?,
    @SerializedName("v_imageUri")
    val vImageUri: String?,
    val vehicleId: Int?,
    val addressId: Int?,
    @SerializedName("year_of_purchase")
    val yearOfPurchase: String?,
    val update_status: Int?,
    val update_remarks: String?,
    val paymentInfo: Payment?,
    ) : Parcelable
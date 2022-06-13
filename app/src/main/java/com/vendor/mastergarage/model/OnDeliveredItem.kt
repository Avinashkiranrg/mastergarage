package com.vendor.mastergarage.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnDeliveredItem(
    val address: String?,
    val city: String?,
    @SerializedName("city_owner")
    val cityOwner: String?,
    @SerializedName("classic_no")
    val classicNo: String?,
    val color: String?,
    val deliveredId: Int?,
    @SerializedName("delivery_date")
    val deliveryDate: String?,
    @SerializedName("delivery_time")
    val deliveryTime: String?,
    val email: String?,
    @SerializedName("engine_no")
    val engineNo: String?,
    val fuelType: String?,
    val imageUri: String?,
    @SerializedName("last_up_date")
    val lastUpDate: String?,
    @SerializedName("last_up_time")
    val lastUpTime: String?,
    val leadId: Int?,
    @SerializedName("manufacturer_name")
    val manufacturerName: String?,
    val model: String?,
    val outletId: Int?,
    val ownerId: Int?,
    @SerializedName("owner_name")
    val ownerName: String?,
    @SerializedName("phone_no")
    val phoneNo: String?,
    @SerializedName("pin_code")
    val pinCode: Int?,
    @SerializedName("registration_no")
    val registrationNo: String?,
    @SerializedName("update_remarks")
    val updateRemarks: String?,
    @SerializedName("update_status")
    val updateStatus: Int?,
    @SerializedName("v_imageUri")
    val vImageUri: String?,
    val variants: String?,
    val vehicleId: Int?,
    val addressId: Int?,
    @SerializedName("year_of_purchase")
    val yearOfPurchase: String?,
    val rate: Float?,
    val paymentInfo: Payment?,

    ) : Parcelable
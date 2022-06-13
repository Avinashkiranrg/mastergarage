package com.vendor.mastergarage.model


import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
//    @SerializedName("card_number")
//    val cardNumber: String?,
//    val cost: Double?,
//    val leadId: Int?,
//    @SerializedName("other_charges")
//    val otherCharges: Double?,
//    val outletId: Int?,
//    @SerializedName("payment_date")
//    val paymentDate: String?,
//    val paymentId: Int?,
//    @SerializedName("payment_method")
//    val paymentMethod: String?,
//    @SerializedName("payment_status")
//    val paymentStatus: String?,
//    @SerializedName("payment_time")
//    val paymentTime: String?,
//    @SerializedName("payment_transaction_id")
//    val paymentTransactionId: String?,
//    val promocode: String?,
//    @SerializedName("promocode_cost")
//    val promocodeCost: Double?,
//    val status: String?,
//    val taxes: Double?,
//    val vendorId: Int?

    val paymentId: Int?,
    val costs: Int?,
    val other_charges: Int?,
    val taxes: Int?,
    val mg_coins: Long?,
    val wallet_amount: Long?,
    val purpose: String?,
    val payment_transaction_id: String?,
    val card_number: String?,
    val payment_category: String?,
    val payment_method: String?,
    val payment_status: Int?,
    val payment_remarks: String?,
    val payment_date: String?,
    val payment_time: String?,
    val coupon_code: String?,
    val coupon_rupee: Int?,
    @PrimaryKey
    var bookingId: Long?,
    val isClick: Int?
) : Parcelable
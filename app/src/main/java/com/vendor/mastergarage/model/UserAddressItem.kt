package com.vendor.mastergarage.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "UserAddressItem",
    indices = [Index(value = ["addressId"], unique = true)],
)
data class UserAddressItem(
    val address: String?,
    @PrimaryKey
    val addressId: Int?,
    @SerializedName("address_type")
    val addressType: String?,
    val city: String?,
    val country: String?,
    @SerializedName("house_no")
    val houseNo: String?,
    val landmark: String?,
    val ownerId: Int?,
    @SerializedName("phone_no")
    val phoneNo: String?,
    val pincode: Int?,
    val state: String?,
    val status: String?,
    var bookingId: Long?
) : Parcelable
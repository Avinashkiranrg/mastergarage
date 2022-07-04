package com.vendor.mastergarage.model

import com.google.gson.annotations.SerializedName

data class AcceptLeadsReq(
    @SerializedName("leadId")
    var leadId: Int,

    @SerializedName("last_up_date")
    var last_up_date: String,

    @SerializedName("last_up_time")
    var last_up_time: String,

    @SerializedName("booking_date")
    var booking_date: String,

    @SerializedName("booking_time")
    var booking_time: String,

    @SerializedName("outletId")
    var outletId: String,

     @SerializedName("vehicleId")
    var vehicleId: String,

    @SerializedName("addressId")
    var addressId: String
)

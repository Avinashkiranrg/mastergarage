package com.vendor.mastergarage.model

import com.google.gson.annotations.SerializedName

data class SignUpRespon(

    @SerializedName("vendorId")
    val vendorId: String,

    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String

)

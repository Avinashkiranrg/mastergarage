package com.vendor.mastergarage.model

import com.google.gson.annotations.SerializedName

data class VerifyOtpRespon(

    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String

)

package com.vendor.mastergarage.model

import com.google.gson.annotations.SerializedName

data class DashBoardRespon(

    @SerializedName("success")
    val success: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("Ongoing")
    val Ongoing: String,

    @SerializedName("Pending")
    val Pending: String,

    @SerializedName("Develivered")
    val Develivered: String

)

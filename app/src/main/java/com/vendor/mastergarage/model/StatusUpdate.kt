package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class StatusUpdate(
    @SerializedName("last_up_date")
    val lastUpDate: String,
    @SerializedName("last_up_time")
    val lastUpTime: String,
    val leadId: Int,
    val updateId: Int,
    @SerializedName("update_remarks")
    val updateRemarks: String,
    @SerializedName("update_status")
    val updateStatus: Int
)
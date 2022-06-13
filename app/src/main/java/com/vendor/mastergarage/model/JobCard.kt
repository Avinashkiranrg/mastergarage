package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class JobCard(
    val cardId: Int,
    val fuel: Int,
    val instruction: String,
    @SerializedName("kms_driven")
    val kmsDriven: Int,
    val leadId: Int,
    val status: String
)
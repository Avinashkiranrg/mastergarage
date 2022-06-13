package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class FaqItem(
    val body: String?,
    val faqid: Int?,
    val title: String?
)
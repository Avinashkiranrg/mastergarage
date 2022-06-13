package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class CountryCodeItem(
    val id: Int?,
    val code: String?,
    @SerializedName("dial_code")
    val dialCode: String?,
    val flag: String?,
    val name: String?
)
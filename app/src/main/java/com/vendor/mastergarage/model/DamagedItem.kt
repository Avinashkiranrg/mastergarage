package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class DamagedItem(
    val damagedId: Int,
    val name: String,
    val status: String
)
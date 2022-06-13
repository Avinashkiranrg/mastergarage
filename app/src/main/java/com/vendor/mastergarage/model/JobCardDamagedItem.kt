package com.vendor.mastergarage.model


data class JobCardDamagedItem(
    val bdvId: Int,
    val damagedId: Int,
    val leadId: Int,
    val name: String,
    val status: String
)
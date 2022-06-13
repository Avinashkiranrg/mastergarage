package com.vendor.mastergarage.model


import com.google.gson.annotations.SerializedName

data class ContactusItem(
    val address: String?,
    @SerializedName("alternate_emailId")
    val alternateEmailId: String?,
    @SerializedName("alternate_phone")
    val alternatePhone: String?,
    val city: String?,
    val contactId: Int?,
    val emailId: String?,
    @SerializedName("facebook_link")
    val facebookLink: String?,
    @SerializedName("instagram_link")
    val instagramLink: String?,
    @SerializedName("linkedin_link")
    val linkedinLink: String?,
    val phone: String?,
    @SerializedName("pin_code")
    val pinCode: Int?,
    @SerializedName("twitter_link")
    val twitterLink: String?,
    val share_text: String?
)
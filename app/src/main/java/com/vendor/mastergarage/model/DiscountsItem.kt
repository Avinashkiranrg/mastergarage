package com.example.uidesign.model


import com.google.gson.annotations.SerializedName

data class DiscountsItem(
    @SerializedName("applicable_on")
    val applicableOn: List<ApplicableOn>,
    @SerializedName("apply_users")
    val applyUsers: String,
    @SerializedName("discount_code")
    val discountCode: String,
    val discountId: Int,
    @SerializedName("discount_value")
    val discountValue: Int,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("max_discount")
    val maxDiscount: Int,
    @SerializedName("min_order_value")
    val minOrderValue: Int,
    val outletId: Int,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("type_discount")
    val typeDiscount: String
)
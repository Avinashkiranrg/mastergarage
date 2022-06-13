package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceDetails(
    val serviceId: Int?,
    val name: String?,
    val cost: Float?,
    val other_charges: Float?,
    val additional_details: String?,
    val attachedUri: String?,
    val vendorId: Int?,
    val leadId: Int?,
    val outletId: Int?,
    val approval_status: Int?,
    val spares: List<SparesDetails>
) : Parcelable
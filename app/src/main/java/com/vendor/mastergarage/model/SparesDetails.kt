package com.vendor.mastergarage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SparesDetails(
    var spareId: Int?,
    var manufacturer_name: String?,
    var part_name: String?,
    var warranty: String?,
    var year: Int?,
    var part_type: Int?,
    var serviceId: Int?,
) : Parcelable
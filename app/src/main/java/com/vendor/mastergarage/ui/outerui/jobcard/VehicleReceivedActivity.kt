package com.vendor.mastergarage.ui.outerui.jobcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityVehicleReceived2Binding
import com.vendor.mastergarage.databinding.ActivityVehicleReceivedBinding
import com.vendor.mastergarage.utlis.toFormattedString

class VehicleReceivedActivity : AppCompatActivity() {
    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var manufacturerName: String? = null
    private var fuelType: String? = null
    private var registrationNo: String? = null
    private var variants: String? = null

    lateinit var binding: ActivityVehicleReceived2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleReceived2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            leadId = intent.getIntExtra("leadId", -1)
            vehicleId = intent.getIntExtra("vehicleId", -1)
            manufacturerName = intent.getStringExtra("manufacturerName")
            fuelType = intent.getStringExtra("fuelType")
            registrationNo = intent.getStringExtra("registrationNo")
            variants = intent.getStringExtra("variants")

            binding.carName.text = "$manufacturerName $variants"
            val p = "## ## ## ####"
            binding.registrationNumber.text =
                registrationNo?.toFormattedString(p)

        } catch (n: NullPointerException) {

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}


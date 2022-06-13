package com.vendor.mastergarage.ui.outerui.jobcard

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vendor.mastergarage.R
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityVehicleDeliveredBinding
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.outerui.VehicleViewModel
import com.vendor.mastergarage.utlis.toFormattedString
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class VehicleDeliveredActivity : AppCompatActivity() {
    lateinit var binding: ActivityVehicleDeliveredBinding

    private val viewModel: VehicleViewModel by viewModels()

    private var leadId: Int? = null
    private var vehicleId: Int? = null
    private var manufacturerName: String? = null
    private var fuelType: String? = null
    private var registrationNo: String? = null
    private var variants: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_delivered)
        binding = ActivityVehicleDeliveredBinding.inflate(layoutInflater)
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

        val last_up_date =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date()
            )
        val last_up_time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
            Date()
        )
        try {
            leadId?.let { it1 ->
                viewModel.updateStatus(
                    last_up_date,
                    last_up_time,
                    Constraints.LEAD_CLOSED,
                    "Vehicle Delivered",
                    it1
                )
            }
        } catch (n: java.lang.NullPointerException) {

        }


        viewModel.updateStatus.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Response.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
                is Response.Success -> {

                }
                is Response.Failure -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }
}
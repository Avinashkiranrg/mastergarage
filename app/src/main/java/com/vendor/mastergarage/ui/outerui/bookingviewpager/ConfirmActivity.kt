package com.vendor.mastergarage.ui.outerui.bookingviewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.ActivityAwaitingConfirmationBinding
import com.vendor.mastergarage.databinding.FragmentConfirmBinding
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.ui.outerui.VehicleDetailsActivity

class ConfirmActivity : AppCompatActivity() {
    lateinit var binding: FragmentConfirmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {


            val leadsItem = intent.getParcelableExtra<ResultOnGoing>("leadItem")

            binding.bookingId.text="Booking ID: ${leadsItem?.bookingId}"
            binding.amount.text="₹ ${leadsItem?.totalCost}"


            binding.cardviewcontinue.setOnClickListener {
                val intent = Intent(this, VehicleDetailsActivity::class.java)
                intent.putExtra("leadItem", leadsItem)
                startActivity(intent)
            }

        }catch (e:Exception){

        }
    }
}
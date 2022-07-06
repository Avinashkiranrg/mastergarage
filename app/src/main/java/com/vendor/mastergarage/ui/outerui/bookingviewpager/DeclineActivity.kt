package com.vendor.mastergarage.ui.outerui.bookingviewpager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.ActivityDeclineBinding
import com.vendor.mastergarage.databinding.FragmentConfirmBinding
import com.vendor.mastergarage.model.ResultOnGoing
import com.vendor.mastergarage.ui.mainactivity.MainActivity
import com.vendor.mastergarage.ui.outerui.VehicleDetailsActivity
import com.vendor.mastergarage.utlis.goToActivitiesFinish

class DeclineActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeclineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decline)

        binding = ActivityDeclineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {


            val leadsItem = intent.getParcelableExtra<ResultOnGoing>("leadItem")

            binding.bookingId.text="Booking ID: ${leadsItem?.bookingId}"
            binding.amount.text="₹ ${leadsItem?.totalCost}"


            binding.cardviewcontinue.setOnClickListener {
                goToActivitiesFinish(this@DeclineActivity, MainActivity::class.java)
            }

        }catch (e:Exception){

        }
    }
}
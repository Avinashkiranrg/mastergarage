package com.vendor.mastergarage.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.vendor.mastergarage.constraints.Constraints
import com.vendor.mastergarage.databinding.ActivityOtpBinding
import com.vendor.mastergarage.datastore.VendorPreference
import com.vendor.mastergarage.networkcall.Response
import com.vendor.mastergarage.ui.mainactivity.MainActivity
import com.vendor.mastergarage.utlis.goToActivitiesFinish
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OtpActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpBinding
    private val viewModel: LoginViewModel by viewModels()

    var phoneNo: String? = null
    var venderId: String? = null

    @Inject
    lateinit var vendorPreference: VendorPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNo = intent.getStringExtra("phone")
        venderId = intent.getStringExtra("venderId")

        binding.submitBtn.setOnClickListener {
            if (binding.enterOtpEt.text.toString().equals("")) {
                Toast.makeText(this, "Please Enter The Otp", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.verifyOtp(phoneNo!!, binding.enterOtpEt.text.toString())
            }
        }

        viewModel.verifyOtpData.observe(this, Observer {
            when (it) {
                is Response.Success -> {
                    val userItem = it.data
                    if (userItem != null) {
                        if (userItem.success == Constraints.TRUE_STRING) {

                            Toast.makeText(this, it.data.success, Toast.LENGTH_SHORT).show()

                            saveData()

                            //
                            /* val intent = Intent(this, MainActivity::class.java)
                             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                             startActivity(intent)
                             finish()*/

                        } else {
                            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun saveData() {

        lifecycleScope.launch {

            phoneNo?.let { it ->
                vendorPreference.setVendorPhone(it)
            }

            venderId?.let { it ->
                vendorPreference.setVendorId(
                    it)
            }
            true.let { it ->
                vendorPreference.setVendorLogin(it)
            }

            goToActivitiesFinish(this@OtpActivity, MainActivity::class.java)
        }
    }
}